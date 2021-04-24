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
 * The ConstantNode represents a Boolean constant.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public enum ConstantNode implements DecisionDnnfNode {

    /**
     * The FALSE Boolean constant.
     */
    FALSE("O 0 0"),

    /**
     * The TRUE Boolean constant.
     */
    TRUE("A 0");

    /**
     * The NNF representation of this constant.
     */
    private final String nnfString;

    /**
     * Creates a new ConstantNode.
     *
     * @param nnfString The NNF representation of the constant.
     */
    private ConstantNode(String nnfString) {
        this.nnfString = nnfString;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfNode#depthFirstAccept(fr.univartois.cril.
     * pbd4.ddnnf.DecisionDnnfVisitor)
     */
    @Override
    public void depthFirstAccept(DecisionDnnfVisitor visitor) {
        accept(visitor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfNode#breadthFirstAccept(fr.univartois.
     * cril.pbd4.ddnnf.DecisionDnnfVisitor)
     */
    @Override
    public void breadthFirstAccept(DecisionDnnfVisitor visitor) {
        accept(visitor);
    }

    /**
     * Accepts a visitor, in either depth-first or breadth-first manner (there is no
     * difference for a constant node).
     * 
     * @param visitor The visitor to accept.
     */
    private void accept(DecisionDnnfVisitor visitor) {
        visitor.enter(this);
        visitor.visit(this);
        visitor.exit(this);
    }

    /**
     * Gives The NNF representation of this constant.
     *
     * @return The NNF representation of this constant.
     */
    public String toNNF() {
        return nnfString;
    }

}
