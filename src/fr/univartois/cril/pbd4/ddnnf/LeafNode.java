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
 * The LeafNode represents a leaf node of a d-DNNF, i.e., a Boolean constant.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public enum LeafNode implements DecisionDnnf {

    /**
     * The FALSE Boolean constant.
     */
    FALSE("O 0 0"),

    /**
     * The TRUE Boolean constant.
     */
    TRUE("A 0");

    /**
     * The NNF representation of this leaf.
     */
    private final String nnfString;

    /**
     * Creates a new LeafNode.
     *
     * @param nnfString The NNF representation of the leaf.
     */
    private LeafNode(String nnfString) {
        this.nnfString = nnfString;
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
     * Gives The NNF representation of this leaf.
     *
     * @return The NNF representation of this leaf.
     */
    public String toNNF() {
        return nnfString;
    }

}
