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

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.BinaryOperator;

/**
 * The DecisionDnnfEvaluator is a {@link DecisionDnnfVisitor} which evaluates the truth
 * value of a decision-DNNF, given an assignment of its variables.
 *
 * Visiting must be performed in a depth-first manner.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class DecisionDnnfEvaluator implements DecisionDnnfVisitor {

    /**
     * The Boolean operator used to compute conjunctions.
     */
    private static final BinaryOperator<Boolean> AND = (x, y) -> x && y;

    /**
     * The Boolean operator used to compute disjunctions.
     */
    private static final BinaryOperator<Boolean> OR = (x, y) -> x || y;

    /**
     * The assignment of the variables.
     */
    private final Set<Integer> assignment;

    /**
     * The stack of partial evaluations that have been computed.
     */
    private final Deque<Boolean> partialEvaluations;

    /**
     * The stack of the operation to perform.
     */
    private final Deque<BinaryOperator<Boolean>> operations;

    /**
     * Creates a new DecisionDnnfEvaluator.
     *
     * @param assignment The assignment of the variables.
     */
    public DecisionDnnfEvaluator(Set<Integer> assignment) {
        this.assignment = assignment;
        this.partialEvaluations = new LinkedList<>();
        this.operations = new LinkedList<>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#enter(fr.univartois.cril.pbd4.
     * ddnnf.ConjunctionNode)
     */
    @Override
    public void enter(ConjunctionNode node) {
        partialEvaluations.push(true);
        operations.push(AND);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.
     * ddnnf.ConjunctionNode)
     */
    @Override
    public void visit(ConjunctionNode node) {
        // Nothing to do when visiting this node.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#exit(fr.univartois.cril.pbd4.
     * ddnnf.ConjunctionNode)
     */
    @Override
    public void exit(ConjunctionNode node) {
        exitInternal();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#enter(fr.univartois.cril.pbd4.
     * ddnnf.DecisionNode)
     */
    @Override
    public void enter(DecisionNode node) {
        partialEvaluations.push(false);
        operations.push(OR);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.
     * ddnnf.DecisionNode)
     */
    @Override
    public void visit(DecisionNode node) {
        // Nothing to do when visiting this node.
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#exit(fr.univartois.cril.pbd4.
     * ddnnf.DecisionNode)
     */
    @Override
    public void exit(DecisionNode node) {
        exitInternal();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.
     * ddnnf.LiteralNode)
     */
    @Override
    public void visit(LiteralNode node) {
        pushPartialEvaluation(assignment.contains(node.getLiteral()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.
     * ddnnf.ConstantNode)
     */
    @Override
    public void visit(ConstantNode node) {
        pushPartialEvaluation(node == ConstantNode.TRUE);
    }

    /**
     * Pushes a partial evaluation on {@link #partialEvaluations}.
     * The last operation is applied between {@code value} and the last value on the
     * stack.
     * 
     * @param value The value to push.
     */
    private void pushPartialEvaluation(boolean value) {
        boolean previous = partialEvaluations.pop();
        partialEvaluations.push(operations.peek().apply(previous, value));
    }

    /**
     * Exits an internal node.
     * Unless if the node is the root node, the last pushed value is consumed.
     */
    private void exitInternal() {
        if (operations.size() > 1) {
            // The value of the exited node must be consummed.
            operations.pop();
            pushPartialEvaluation(partialEvaluations.pop());
        }
    }

    /**
     * Gives the value of the decision-DNNF evaluated on the associated assignment.
     * 
     * @return The result of the evaluation.
     */
    public boolean evaluate() {
        return partialEvaluations.peek();
    }

}
