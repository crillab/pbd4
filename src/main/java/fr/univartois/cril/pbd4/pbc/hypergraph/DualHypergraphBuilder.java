/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Univ Artois & CNRS.
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

package fr.univartois.cril.pbd4.pbc.hypergraph;

import static fr.univartois.cril.jkahypar.hypergraph.HypergraphBuilder.createHypergraph;
import static fr.univartois.cril.jkahypar.hypergraph.UnweightedHyperedge.joining;

import java.util.HashMap;
import java.util.Map;

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.hypergraph.HypergraphBuilder;

/**
 * The DualHypergraphBuilder allows to build the dual hypergraph associated to a
 * {@link Hypergraphable}.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class DualHypergraphBuilder {

    /**
     * The {@link Hypergraphable} to build the dual hypergraph of.
     */
    private final Hypergraphable hypergraphable;

    /**
     * The current hyperedge identifier.
     */
    private int currentHyperedgeIdentifier = 0;

    /**
     * The map associating a hyperedge identifier to the variable it represents.
     */
    private final Map<Integer, Integer> identifierToVariable;

    /**
     * The current vertex identifier.
     */
    private int currentVertexIdentifier;

    /**
     * The map associating a constraint to the vertex identifier which represents it.
     */
    private final Map<Integer, Integer> constraintToIdentifier;

    /**
     * The map associating a vertex identifier to the constraint it represents.
     */
    private final Map<Integer, Integer> identifierToConstraint;

    /**
     * The identifiers of the variables appearing in each constraint.
     */
    private final IVecInt[] variablesAppearingInConstraint;

    /**
     * The builder used to build the hypergraph.
     */
    private final HypergraphBuilder builder;

    /**
     * Creates a new DualHypergraphBuilder.
     *
     * @param hypergraphable The {@link Hypergraphable} to build the dual hypergraph of.
     */
    DualHypergraphBuilder(Hypergraphable hypergraphable) {
        this.hypergraphable = hypergraphable;
        this.identifierToVariable = new HashMap<>();
        this.constraintToIdentifier = new HashMap<>();
        this.identifierToConstraint = new HashMap<>();
        this.variablesAppearingInConstraint = new IVecInt[hypergraphable.numberOfConstraints() + 1];
        this.builder = createHypergraph(
                hypergraphable.numberOfConstraints(),
                hypergraphable.numberOfVariables());
    }

    /**
     * Builds the dual hypergraph representing the associate {@link Hypergraphable}.
     *
     * @return The built hypergraph.
     */
    DualHypergraph build() {
        for (var it = hypergraphable.variables().iterator(); it.hasNext();) {
            var variable = it.next();
            identifierToVariable.put(++currentHyperedgeIdentifier, variable);
            addHyperedge(variable);
        }
        
        return new DualHypergraph(builder.build(), variablesAppearingInConstraint, 
                identifierToVariable, identifierToConstraint);
    }

    /**
     * Adds a hyperedge representing the given variable to the hypergraph.
     *
     * @param variable The variable for which a hyperedge must be added.
     */
    private void addHyperedge(int variable) {
        int index = 0;
        int[] constraints = new int[hypergraphable.numberOfConstraintsContaining(variable)];

        // Adding each active constraint to the hyperedge.
        for (var it = hypergraphable.constraintsContaining(variable).iterator(); it.hasNext();) {
            var constr = it.next();

            if (hypergraphable.isActive(constr)) {
                int constraintId = getVertexIdentifier(constr);
                constraints[index] = constraintId;
                variablesAppearingInConstraint[constraintId].push(currentHyperedgeIdentifier);
                index++;
            }
        }

        // Actually adding the hyperedge.
        builder.withHyperedge(joining(constraints));
    }

    /**
     * Gives the vertex identifier which represents a given constraint.
     * 
     * @param constraint The constraint to get the identifier of.
     *
     * @return The vertex identifier representing the constraint.
     */
    private int getVertexIdentifier(int constraint) {
        var identifier = constraintToIdentifier.get(constraint);

        if (identifier == null) {
            // This constraint does not have an identifier yet.
            identifier = ++currentVertexIdentifier;
            constraintToIdentifier.put(constraint, identifier);
            identifierToConstraint.put(identifier, constraint);
            variablesAppearingInConstraint[identifier] = new VecInt();
        }

        return identifier;
    }

}
