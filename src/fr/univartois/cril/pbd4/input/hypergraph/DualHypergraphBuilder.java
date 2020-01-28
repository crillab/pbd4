/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Romain WALLON.
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 * If not, see {@link http://www.gnu.org/licenses}.
 */

package fr.univartois.cril.pbd4.input.hypergraph;

import org.sat4j.core.LiteralsUtils;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.hypergraph.HypergraphBuilder;

import static fr.univartois.cril.jkahypar.hypergraph.HypergraphBuilder.createHypergraph;
import static fr.univartois.cril.jkahypar.hypergraph.UnweightedHyperedge.joining;

/**
 * The DualHypergraphBuilder allows to build the dual hypergraph of a pseudo-Boolean
 * formula.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class DualHypergraphBuilder implements PseudoBooleanFormulaHypergraphBuilder {

    /**
     * The constraints of the formula.
     */
    private IConstr[] constraints;

    /**
     * The identifier of the next constraint.
     */
    private int nextIdentifier;

    /**
     * The array storing, for each variable, the identifiers of the constraints in which
     * it appears.
     */
    private IVecInt[] constraintsContainingVariable;

    /**
     * The array storing, for each constraint, the identifiers of the hyperedges in which
     * it appears.
     */
    private IVecInt[] hyperedgesContainingConstraint;

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.input.hypergraph.PseudoBooleanFormulaHypergraphBuilder#
     * setNumberOfVariables(int)
     */
    @Override
    public void setNumberOfVariables(int numberOfVariables) {
        this.constraintsContainingVariable = new IVecInt[numberOfVariables + 1];
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.input.hypergraph.PseudoBooleanFormulaHypergraphBuilder#
     * setNumberOfConstraints(int)
     */
    @Override
    public void setNumberOfConstraints(int numberOfConstraints) {
        this.constraints = new IConstr[numberOfConstraints];
        this.hyperedgesContainingConstraint = new IVecInt[numberOfConstraints];
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.input.hypergraph.PseudoBooleanFormulaHypergraphBuilder#
     * addConstraint(org.sat4j.specs.IConstr, org.sat4j.specs.IVecInt)
     */
    @Override
    public void addConstraint(IConstr constraint, IVecInt literals) {
        // Storing the constraint.
        constraints[nextIdentifier] = constraint;

        // Updating the pre-hypergraph.
        for (var it = literals.iterator(); it.hasNext();) {
            int variable = LiteralsUtils.var(it.next());
            var vec = constraintsContainingVariable[variable];

            if (vec == null) {
                vec = new VecInt();
                constraintsContainingVariable[variable] = vec;
            }

            vec.push(nextIdentifier);
        }

        // Moving to the next identifier.
        nextIdentifier++;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.input.hypergraph.PseudoBooleanFormulaHypergraphBuilder#
     * build()
     */
    @Override
    public PseudoBooleanFormulaHypergraph build() {
        var builder = createHypergraph(constraints.length, constraintsContainingVariable.length - 1);

        // Adding the hyperedges.
        for (int v = 1; v < constraintsContainingVariable.length; v++) {
            var constraintsWithV = constraintsContainingVariable[v];
            addHyperedge(builder, constraintsWithV);
            associate(v - 1, constraintsWithV);
        }

        return new DualHypergraph(constraints,
                builder.build(), hyperedgesContainingConstraint);
    }

    /**
     * Adds a hyperedge joining the given vertices to the hypergraph built by the given
     * builder.
     * 
     * @param builder The builder used to build the hypergraph.
     * @param vertices The vertices joined by the hyperedge to add.
     */
    private void addHyperedge(HypergraphBuilder builder, IVecInt vertices) {
        if (vertices == null) {
            builder.withHyperedge(joining());
            return;
        }

        int[] asArray = new int[vertices.size()];
        vertices.copyTo(asArray);
        builder.withHyperedge(joining(asArray));
    }

    /**
     * Associates the given constraints to a hyperedge joining them in
     * {@link #hyperedgesContainingConstraint}.
     *
     * @param hyperedgeId The identifier of the hyperedge joining the constraints.
     * @param joinedConstraints The constraints joined by the hyperedge.
     */
    private void associate(int hyperedgeId, IVecInt joinedConstraints) {
        for (var it = joinedConstraints.iterator(); it.hasNext();) {
            int constraintId = it.next();
            var vec = hyperedgesContainingConstraint[constraintId];

            if (vec == null) {
                vec = new VecInt();
                hyperedgesContainingConstraint[constraintId] = vec;
            }

            vec.push(hyperedgeId);
        }
    }

}
