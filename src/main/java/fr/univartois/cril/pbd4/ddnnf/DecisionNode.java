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
 * The DecisionNode is a node in a decision-DNNF representing a decision on a variable.
 * Such a node is also known as an {@code if-then-else} node.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class DecisionNode implements DecisionDnnfNode {

    /**
     * The variable for which this node represents a decision on.
     */
    private final int variable;

    /**
     * The decision-DNNF node that is the left child of this node.
     */
    private final DecisionDnnfNode leftChild;

    /**
     * The decision-DNNF node that is the right child of this node.
     */
    private final DecisionDnnfNode rightChild;

    /**
     * Creates a new DecisionNode.
     *
     * @param variable The variable for which the node represents a decision on.
     * @param leftChild The decision-DNNF node that is the left child of the node.
     * @param rightChild The decision-DNNF node that is the right child of the node.
     */
    private DecisionNode(int variable, DecisionDnnfNode leftChild, DecisionDnnfNode rightChild) {
        this.variable = variable;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * Creates a new DecisionNode.
     *
     * @param variable The variable for which the node represents a decision on.
     * @param leftChild The decision-DNNF node that is the left child of the node.
     * @param rightChild The decision-DNNF node that is the right child of the node.
     *
     * @return The created node.
     */
    public static DecisionDnnfNode or(int variable, DecisionDnnfNode leftChild,
            DecisionDnnfNode rightChild) {
        return new DecisionNode(variable, leftChild, rightChild);
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
        visitor.enter(this);
        leftChild.depthFirstAccept(visitor);
        rightChild.depthFirstAccept(visitor);
        visitor.visit(this);
        visitor.exit(this);
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
        visitor.enter(this);
        visitor.visit(this);
        leftChild.breadthFirstAccept(visitor);
        rightChild.breadthFirstAccept(visitor);
        visitor.exit(this);
    }

    /**
     * Gives the variable for which this node represents a decision on.
     *
     * @return The variable on which a decision has been taken.
     */
    public int getVariable() {
        return variable;
    }

}
