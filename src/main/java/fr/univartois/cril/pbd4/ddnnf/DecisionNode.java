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
public final class DecisionNode extends InternalNode {

    /**
     * The variable for which this node represents a decision on.
     */
    private final int variable;

    /**
     * The decision-DNNF representing the case in which the variable is satisfied.
     */
    private final DecisionDnnf ifTrue;

    /**
     * The decision-DNNF representing the case in which the variable is falsified.
     */
    private final DecisionDnnf ifFalse;

    /**
     * Creates a new DecisionNode.
     *
     * @param variable The variable for which the node represents a decision on.
     * @param ifTrue The decision-DNNF representing the case in which the variable is
     *        satisfied.
     * @param ifFalse The decision-DNNF representing the case in which the variable is
     *        falsified.
     */
    private DecisionNode(int variable, DecisionDnnf ifTrue, DecisionDnnf ifFalse) {
        this.variable = variable;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    /**
     * Creates a new DecisionNode.
     *
     * @param variable The variable for which the node represents a decision on.
     * @param ifTrue The decision-DNNF representing the case in which the variable is
     *        satisfied.
     * @param ifFalse The decision-DNNF representing the case in which the variable is
     *        falsified.
     *
     * @return The created node.
     */
    public static DecisionDnnf ifThenElse(int variable, DecisionDnnf ifTrue, DecisionDnnf ifFalse) {
        return new DecisionNode(variable, ifTrue, ifFalse);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnf#depthFirstAccept(fr.univartois.cril.pbd4
     * .ddnnf.DecisionDnnfVisitor)
     */
    @Override
    public void depthFirstAccept(DecisionDnnfVisitor visitor) {
        ifTrue.depthFirstAccept(visitor);
        ifFalse.depthFirstAccept(visitor);
        visitor.visit(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnf#breadthFirstAccept(fr.univartois.cril.
     * pbd4.ddnnf.DecisionDnnfVisitor)
     */
    @Override
    public void breadthFirstAccept(DecisionDnnfVisitor visitor) {
        visitor.visit(this);
        ifTrue.breadthFirstAccept(visitor);
        ifFalse.breadthFirstAccept(visitor);
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
