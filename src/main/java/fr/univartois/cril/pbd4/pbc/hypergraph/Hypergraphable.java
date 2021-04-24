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

import org.sat4j.specs.IVecInt;

/**
 * The Hypergraphable defines the interface for classes which can be represented with
 * a hypergraph.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface Hypergraphable {

    /**
     * Gives the number of variables in this Hypergraphable.
     *
     * @return The number of variables in this Hypergraphable.
     */
    int numberOfVariables();

    /**
     * Gives the variables appearing in this Hypergraphable.
     *
     * @return The variables appearing in this Hypergraphable.
     */
    IVecInt variables();

    /**
     * Gives the number of constraints in this Hypergraphable.
     *
     * @return The number of constraints in this Hypergraphable.
     */
    int numberOfConstraints();

    /**
     * Checks whether a given constraint is active.
     *
     * @param constraint The constraint to check.
     *
     * @return Whether the constraint is active.
     */
    boolean isActive(int constraint);

    /**
     * Gives the number of constraints containing a given variable.
     * Only active constraints are counted.
     *
     * @param variable The variable to consider.
     *
     * @return The number constraints containing the variable.
     */
    int numberOfConstraintsContaining(int variable);

    /**
     * Gives the indices of the constraints containing a given variable.
     * Any constraint (including inactive constraints) may be present.
     *
     * @param variable The variable to consider.
     *
     * @return The indices of the constraints containing {@code variable}.
     */
    IVecInt constraintsContaining(int variable);

}
