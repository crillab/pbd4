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

package fr.univartois.cril.pbd4.pbc.hypergraph;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.hypergraph.Hypergraph;

/**
 * The DualHypergraphConnectedComponentFinder allows to find the connected components of a
 * dual hypergraph.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class DualHypergraphConnectedComponentFinder {
    
    /**
     * The representation of the hypergraph to compute the connected components of.
     */
    private final Hypergraph hypergraph;

    /**
     * The identifiers of the variables appearing in each constraint.
     */
    private final IVecInt[] variablesAppearingInConstraint;

    /**
     * Whether each constraint has already been visited.
     */
    private final boolean[] visited;

    /**
     * Creates a new DualHypergraphConnectedComponentFinder.
     * 
     * @param constraints The constraints of the formula.
     * @param variablesAppearingInConstraint The identifiers of the variables appearing in
     *        each constraint.
     */
    public DualHypergraphConnectedComponentFinder(Hypergraph hypergraph, 
            IVecInt[] variablesAppearingInConstraint) {
        this.hypergraph = hypergraph;
        this.variablesAppearingInConstraint = variablesAppearingInConstraint;
        this.visited = new boolean[variablesAppearingInConstraint.length];
    }

    /**
     * Computes the connected components of the hypergraph.
     *
     * @return The vector of connected components.
     */
    public IVec<IVecInt> connectedComponents() {
        var components = new Vec<IVecInt>();

        for (int constraint = 1; constraint < variablesAppearingInConstraint.length; constraint++) {
            if (!visited[constraint]) {
                components.push(componentOf(constraint));
            }
        }

        return components;
    }

    /**
     * Finds the connected component of the graph containing the given constraint.
     * It is assumed that this constraint has never been visited before.
     *
     * @param constraint The identifier of the constraint to start the exploration from.
     *
     * @return The connected component of the graph containing the given constraint.
     */
    private IVecInt componentOf(int constraint) {
        // Initializing the exploration.
        var toExplore = new VecInt();
        toExplore.push(constraint);
        var explored = new VecInt();

        while (!toExplore.isEmpty()) {
            // Visiting the next constraint.
            int constr = toExplore.last();
            toExplore.pop();
            explored.push(constr);
            visited[constr] = true;

            // Adding the neighbors of the constraint to the ones to explore.
            for (var it = variablesAppearingInConstraint[constr].iterator(); it.hasNext();) {
                addConstraintsContaining(toExplore, it.next());
            }
        }

        return explored;
    }

    /**
     * Adds the constraints containing the given variable to the given vector.
     *
     * @param toExplore The vector of constraints to explore.
     * @param variable The variable shared between the constraints.
     */
    private void addConstraintsContaining(IVecInt toExplore, int variable) {
        var limits = hypergraph.getHyperedgeIndices();
        var constraints = hypergraph.getHyperedgeVertices();
        
        for (int i = (int) limits[variable - 1]; i < limits[variable]; i++) {
            int constraint = constraints[i] + 1;
            if (!visited[constraint]) {
                toExplore.push(constraint);
            }
        }
    }

}
