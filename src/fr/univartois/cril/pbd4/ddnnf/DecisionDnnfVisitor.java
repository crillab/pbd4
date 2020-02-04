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

package fr.univartois.cril.pbd4.ddnnf;

/**
 * The DecisionDnnfVisitor allows to visit a d-DNNF in a depth-first manner.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface DecisionDnnfVisitor {

    /***
     * Notifies this visitor that it is going to visit a d-DNNF.
     * This method may be used to perform some initialization before actually visiting the
     * d-DNNF.
     *
     * @param root The root node of the d-DNNF to visit.
     *
     * @implSpec The default implementation does nothing.
     */
    default void enter(DecisionDnnf root) {
        // Nothing to do by default.
    }

    /**
     * Visits a {@link ConjunctionNode}.
     *
     * @param node The node to visit.
     */
    void visit(ConjunctionNode node);

    /**
     * Visits a {@link DecisionNode}.
     *
     * @param node The node to visit.
     */
    void visit(DecisionNode node);

    /**
     * Visits a {@link LiteralNode}.
     *
     * @param node The node to visit.
     */
    void visit(LiteralNode node);

    /**
     * Visits a {@link LeafNode}.
     *
     * @param node The node to visit.
     */
    void visit(LeafNode node);

    /**
     * Notifies this visitor that it has visited a whole d-DNNF.
     * This method may be used to perform some last computations after having visited the
     * d-DNNF.
     *
     * @param root The root node of the d-DNNF that has been visited.
     *
     * @implSpec The default implementation does nothing.
     */
    default void exit(DecisionDnnf root) {
        // Nothing to do by default.
    }

}
