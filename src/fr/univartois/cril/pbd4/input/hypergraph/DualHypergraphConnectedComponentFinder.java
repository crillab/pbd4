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

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

/**
 * The DualHypergraphConnectedComponentFinder finds the connected components of a hypergraph.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class DualHypergraphConnectedComponentFinder {

    /**
     * The constraints of the formula.
     */
    private final IConstr[] constraints;

    /**
     * The array storing, for each constraint, the identifiers of the hyperedges in which
     * it appears.
     */
    private final IVecInt[] hyperedgesContainingConstraint;
    
    /**
     * The array storing, for each constraint, whether it has already been visited.
     */
    private final boolean[] visited;

    /**
     * Creates a new DualHypergraphConnectedComponentFinder.
     * 
     * @param constraints The constraints of the formula.
     * @param hyperedgesContainingConstraint The array storing, for each constraint, the
     *        identifiers of the hyperedges in which it appears.
     */
    public DualHypergraphConnectedComponentFinder(IConstr[] constraints,
            IVecInt[] hyperedgesContainingConstraint) {
        this.constraints = constraints;
        this.hyperedgesContainingConstraint = hyperedgesContainingConstraint;
        this.visited = new boolean[hyperedgesContainingConstraint.length];
    }

    /**
     * Computes the connected components of the hypergraph.
     *
     * @return The vector of connected components.
     */
    public IVec<IVec<IConstr>> connectedComponents() {
        var components = new Vec<IVec<IConstr>>();

        for (int constraint = 0; constraint < hyperedgesContainingConstraint.length; constraint++) {
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
    private IVec<IConstr> componentOf(int constraint) {
        // Initializing the exploration.
        var toExplore = new VecInt();
        toExplore.push(constraint);
        var explored = new Vec<IConstr>();

        while (!toExplore.isEmpty()) {
            // Visiting the next constraint.
            int c = toExplore.last();
            explored.push(constraints[c]);
            toExplore.pop();
            visited[c] = true;

            // Adding the neighbors of the constraint to the ones to explore.
            for (var it = hyperedgesContainingConstraint[c].iterator(); it.hasNext();) {
                var n = it.next();
                if (!visited[n]) {
                    toExplore.push(n);
                }
            }
        }

        return explored;
    }
    
}

