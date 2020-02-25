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

package fr.univartois.cril.pbd4.ddnnf;

/**
 * The DecisionDnnfVisitor allows to visit a decision-DNNF.
 * Visiting may be performed in either a depth-first or breadth-first manner.
 * Visitor implementations are not required to support both traversals, but should
 * document whether they do.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface DecisionDnnfVisitor {

    /**
     * Enters a {@link DecisionDnnf}.
     *
     * @param ddnnf The decision-DNNF to enter.
     *
     * @implSpec The default implementation does nothing.
     */
    default void enter(DecisionDnnf ddnnf) {
        // Nothing to do by default.
    }

    /**
     * Visits a {@link DecisionDnnf}.
     *
     * @param ddnnf The decision-DNNF to visit.
     *
     * @implSpec The default implementation does nothing.
     */
    default void visit(DecisionDnnf ddnnf) {
        // Nothing to do by default.
    }

    /**
     * Exits a {@link DecisionDnnf}.
     *
     * @param ddnnf The decision-DNNF to exit.
     *
     * @implSpec The default implementation does nothing.
     */
    default void exit(DecisionDnnf ddnnf) {
        // Nothing to do by default.
    }

    /**
     * Enters a {@link ConjunctionNode}.
     *
     * @param node The node to enter.
     *
     * @implSpec The default implementation does nothing.
     */
    default void enter(ConjunctionNode node) {
        // Nothing to do by default.
    }

    /**
     * Visits a {@link ConjunctionNode}.
     *
     * @param node The node to visit.
     */
    void visit(ConjunctionNode node);

    /**
     * Exits a {@link ConjunctionNode}.
     *
     * @param node The node to exit.
     *
     * @implSpec The default implementation does nothing.
     */
    default void exit(ConjunctionNode node) {
        // Nothing to do by default.
    }

    /**
     * Enters a {@link DecisionNode}.
     *
     * @param node The node to enter.
     *
     * @implSpec The default implementation does nothing.
     */
    default void enter(DecisionNode node) {
        // Nothing to do by default.
    }

    /**
     * Visits a {@link DecisionNode}.
     *
     * @param node The node to visit.
     */
    void visit(DecisionNode node);

    /**
     * Exits a {@link DecisionNode}.
     *
     * @param node The node to exit.
     *
     * @implSpec The default implementation does nothing.
     */
    default void exit(DecisionNode node) {
        // Nothing to do by default.
    }

    /**
     * Enters a {@link LiteralNode}.
     *
     * @param node The node to enter.
     *
     * @implSpec The default implementation does nothing.
     */
    default void enter(LiteralNode node) {
        // Nothing to do by default.
    }

    /**
     * Visits a {@link LiteralNode}.
     *
     * @param node The node to visit.
     */
    void visit(LiteralNode node);

    /**
     * Exits a {@link LiteralNode}.
     *
     * @param node The node to exit.
     *
     * @implSpec The default implementation does nothing.
     */
    default void exit(LiteralNode node) {
        // Nothing to do by default.
    }

    /**
     * Enters a {@link LeafNode}.
     *
     * @param node The node to enter.
     *
     * @implSpec The default implementation does nothing.
     */
    default void enter(LeafNode node) {
        // Nothing to do by default.
    }

    /**
     * Visits a {@link LeafNode}.
     *
     * @param node The node to visit.
     */
    void visit(LeafNode node);

    /**
     * Exits a {@link LeafNode}.
     *
     * @param node The node to exit.
     *
     * @implSpec The default implementation does nothing.
     */
    default void exit(LeafNode node) {
        // Nothing to do by default.
    }

}
