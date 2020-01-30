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
 * The LiteralNode is a node representing a single literal.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class LiteralNode extends InternalNode {

    /**
     * The DIMACS identifier of the literal represented by this node.
     */
    private final int literal;

    /**
     * Creates a new LiteralNode.
     *
     * @param literal The DIMACS identifier of the literal represented by the node.
     */
    private LiteralNode(int literal) {
        this.literal = literal;
    }

    /**
     * Creates a new LiteralNode.
     *
     * @param literal The DIMACS identifier of the literal represented by the node.
     *
     * @return The created node.
     */
    public static DecisionDnnf literal(int literal) {
        return new LiteralNode(literal);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnf#accept(fr.univartois.cril.pbd4.ddnnf.
     * DecisionDnnfVisitor)
     */
    @Override
    public void accept(DecisionDnnfVisitor visitor) {
        visitor.visit(this);
    }
    
    /**
     * Gives the DIMACS identifier of the literal represented by the node.
     *
     * @return The literal represented by the node.
     */
    public int getLiteral() {
        return literal;
    }

}
