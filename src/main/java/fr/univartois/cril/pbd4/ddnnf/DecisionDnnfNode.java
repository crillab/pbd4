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

package fr.univartois.cril.pbd4.ddnnf;

/**
 * The DecisionDnnfNode defines the interface for manipulating the nodes of a
 * decision-DNNF.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public interface DecisionDnnfNode {

    /**
     * Accepts a {@link DecisionDnnfVisitor} in a depth-first manner.
     *
     * @param visitor The visitor to accept.
     */
    void depthFirstAccept(DecisionDnnfVisitor visitor);

    /**
     * Accepts a {@link DecisionDnnfVisitor} in a breadth-first manner.
     *
     * @param visitor The visitor to accept.
     */
    void breadthFirstAccept(DecisionDnnfVisitor visitor);

}
