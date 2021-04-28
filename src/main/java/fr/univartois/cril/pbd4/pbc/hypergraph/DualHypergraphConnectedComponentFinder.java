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
 * @version 0.2.0
 */
public final class DualHypergraphConnectedComponentFinder {

    /**
     * The representation of the hypergraph to compute the connected components of.
     */
    private final Hypergraph hypergraph;

    /**
     * The identifiers of the variables appearing in each constraint.
     */
    private final IVecInt[] variablesAppearingInConstraint;

    /**
     * The array storing which constraints have already been visited and which have not.
     */
    private final boolean[] visited;

    /**
     * Creates a new DualHypergraphConnectedComponentFinder.
     *
     * @param hypergraph The representation of the hypergraph to compute the connected
     *        components of.
     * @param variablesAppearingInConstraint The identifiers of the variables
     *        appearing in each constraint.
     */
    DualHypergraphConnectedComponentFinder(Hypergraph hypergraph,
            IVecInt[] variablesAppearingInConstraint) {
        this.hypergraph = hypergraph;
        this.variablesAppearingInConstraint = variablesAppearingInConstraint;
        this.visited = new boolean[variablesAppearingInConstraint.length];
    }

    /**
     * Computes the connected components of the hypergraph.
     *
     * @return The vector of connected components, where each component is represented
     *         by the vector of (the identifiers of) the constraints appearing
     *         in the component.
     */
    IVec<IVecInt> connectedComponents() {
        var components = new Vec<IVecInt>();

        for (int constraint = 1; constraint < variablesAppearingInConstraint.length; constraint++) {
            if (!visited[constraint]) {
                components.push(componentOf(constraint));
            }
        }

        return components;
    }

    /**
     * Finds the connected component that contains the given constraint.
     * It is assumed that this constraint has never been visited before.
     *
     * @param constraint The identifier of the constraint to start the
     *        exploration from.
     *
     * @return The connected component containing the given constraint.
     */
    private IVecInt componentOf(int constraint) {
        // Initializing the exploration.
        var toExplore = new VecInt();
        toExplore.push(constraint);
        var explored = new VecInt();

        while (!toExplore.isEmpty()) {
            // Visiting the next constraint, only if it has not been visited yet.
            int constr = toExplore.last();
            toExplore.pop();
            if (visited[constr]) {
                continue;
            }

            // Marking the constraint as visited.
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
            toExplore.push(constraints[i] + 1);
        }
    }

}
