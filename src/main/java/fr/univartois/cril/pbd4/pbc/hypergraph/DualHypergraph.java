/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Univ Artois & CNRS.
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package fr.univartois.cril.pbd4.pbc.hypergraph;

import java.util.Map;

import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.hypergraph.Hypergraph;

/**
 * The DualHypergraph represents the dual hypergraph of a {@link Hypergraphable}, i.e.,
 * the hypergraph in which the vertices are constraints and a hyperedge represents a
 * variable shared between the constraints joined by this hyperedge.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public final class DualHypergraph {

    /**
     * The KaHyPar representation of the hypergraph.
     */
    private final Hypergraph hypergraph;

    /**
     * The identifiers of the variables appearing in each constraint.
     */
    private final IVecInt[] variablesAppearingInConstraint;

    /**
     * The map associating a hyperedge identifier to the variable it represents.
     */
    private final Map<Integer, Integer> identifierToVariable;

    /**
     * The map associating a vertex identifier to the constraint it represents.
     */
    private final Map<Integer, Integer> identifierToConstraint;

    /**
     * Creates a new DualHypergraph.
     *
     * @param hypergraph The KaHyPar representation of the hypergraph.
     * @param variablesAppearingInConstraint The identifiers of the variables appearing in
     *        each constraint.
     * @param identifierToVariable The map associating a hyperedge identifier to the
     *        variable it represents.
     * @param identifierToConstraint The map associating a vertex identifier to the
     *        constraint it represents.
     */
    DualHypergraph(Hypergraph hypergraph, IVecInt[] variablesAppearingInConstraint,
            Map<Integer, Integer> identifierToVariable, Map<Integer, Integer> identifierToConstraint) {
        this.hypergraph = hypergraph;
        this.variablesAppearingInConstraint = variablesAppearingInConstraint;
        this.identifierToVariable = identifierToVariable;
        this.identifierToConstraint = identifierToConstraint;
    }

    /**
     * Creates a new DualHypergraph.
     *
     * @param hypergraphable The {@link Hypergraphable} to create the dual hypergraph of.
     *
     * @return The created dual hypergraph.
     */
    public static DualHypergraph of(Hypergraphable hypergraphable) {
        var builder = new DualHypergraphBuilder(hypergraphable);
        return builder.build();
    }

    /**
     * Computes the connected components of this hypergraph.
     * The components are expressed in terms of constraint indices.
     *
     * @return The computed components.
     */
    public IVec<IVecInt> connectedComponents() {
        var finder = new DualHypergraphConnectedComponentFinder(hypergraph, variablesAppearingInConstraint);
        var components = finder.connectedComponents();

        for (var it = components.iterator(); it.hasNext();) {
            var component = it.next();
            translateAsConstraints(component);
        }

        return components;
    }

    /**
     * Gives the KaHyPar representation of this dual hypergraph.
     *
     * @return The KaHyPar representation of this dual hypergraph.
     */
    public Hypergraph asKahyparHypergraph() {
        return hypergraph;
    }

    /**
     * Translates the hyperedge identifiers in the given vector into the identifiers
     * of the corresponding variables.
     *
     * @param vec The vector to translate.
     */
    public void translateAsVariables(IVecInt vec) {
        translate(vec, identifierToVariable);
    }

    /**
     * Translates the hypervertex identifiers in the given vector into the identifiers
     * of the corresponding constraints.
     *
     * @param vec The vector to translate.
     */
    public void translateAsConstraints(IVecInt vec) {
        translate(vec, identifierToConstraint);
    }

    /**
     * Translates the identifiers in the given vector into the corresponding identifiers
     * in the original {@link Hypergraphable}.
     *
     * @param vec The vector to translate.
     * @param identifiers The map providing the correspondence between identifiers.
     */
    private void translate(IVecInt vec, Map<Integer, Integer> identifiers) {
        for (int i = 0; i < vec.size(); i++) {
            int identifier = vec.get(i);
            int realValue = identifiers.get(identifier);
            vec.set(i, realValue);
        }
    }

}
