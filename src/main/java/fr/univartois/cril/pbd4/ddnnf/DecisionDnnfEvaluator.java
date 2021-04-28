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

import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.BinaryOperator;

/**
 * The DecisionDnnfEvaluator is a {@link DecisionDnnfVisitor} which evaluates
 * the truth value of a decision-DNNF, given an assignment of its variables.
 *
 * Visiting must be performed in a depth-first manner.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
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
     * The stack of the operations to perform.
     */
    private final Deque<BinaryOperator<Boolean>> operations;

    /**
     * The time stamp at which visiting has started (in nano-seconds since the epoch).
     */
    private long startTime;

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
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#enter(fr.univartois.cril.
     * pbd4.ddnnf.DecisionDnnf)
     */
    @Override
    public void enter(DecisionDnnf ddnnf) {
        this.startTime = System.nanoTime();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#enter(fr.univartois.cril.
     * pbd4.ddnnf.ConjunctionNode)
     */
    @Override
    public boolean enter(ConjunctionNode node) {
        if (pushCachedValue(node)) {
            // No need to evaluate the conjuncts.
            return false;
        }

        // The conjuncts must be evaluated.
        partialEvaluations.push(true);
        operations.push(AND);
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.
     * pbd4.ddnnf.ConjunctionNode)
     */
    @Override
    public void visit(ConjunctionNode node) {
        // Nothing to do when visiting this node.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#exit(fr.univartois.cril.
     * pbd4.ddnnf.ConjunctionNode)
     */
    @Override
    public void exit(ConjunctionNode node) {
        exitInternal(node);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#enter(fr.univartois.cril.
     * pbd4.ddnnf.DecisionNode)
     */
    @Override
    public boolean enter(DecisionNode node) {
        if (pushCachedValue(node)) {
            // No need to evaluate the disjuncts.
            return false;
        }

        // The disjuncts must be evaluated.
        partialEvaluations.push(false);
        operations.push(OR);
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.
     * pbd4.ddnnf.DecisionNode)
     */
    @Override
    public void visit(DecisionNode node) {
        // Nothing to do when visiting this node.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#exit(fr.univartois.cril.
     * pbd4.ddnnf.DecisionNode)
     */
    @Override
    public void exit(DecisionNode node) {
        exitInternal(node);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.
     * pbd4.ddnnf.LiteralNode)
     */
    @Override
    public void visit(LiteralNode node) {
        pushPartialEvaluation(assignment.contains(node.getLiteral()));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.
     * pbd4.ddnnf.ConstantNode)
     */
    @Override
    public void visit(ConstantNode node) {
        pushPartialEvaluation(node == ConstantNode.TRUE);
    }

    /**
     * Checks whether the value for the given node has already been computed, and pushes
     * the cached value on the stack of partial evaluations if this is the case.
     *
     * @param node The node to check the cached value of.
     *
     * @return Whether there is a cached value for the node.
     */
    private boolean pushCachedValue(NonConstantDecisionDnnfNode node) {
        if (node.getVisitStamp() == this.startTime) {
            pushPartialEvaluation(node.getCached());
            return true;
        }
        return false;
    }

    /**
     * Pushes a partial evaluation on {@link #partialEvaluations}.
     * The last operation is applied between {@code value} and the last value on the
     * stack.
     * 
     * @param value The value to push.
     */
    private boolean pushPartialEvaluation(boolean value) {
        boolean previous = partialEvaluations.pop();
        boolean result = operations.peek().apply(previous, value);
        partialEvaluations.push(result);
        return result;
    }

    /**
     * Exits an internal node.
     * Unless the node is the root node, the last pushed value is consumed.
     */
    private void exitInternal(NonConstantDecisionDnnfNode node) {
        boolean value;

        if (operations.size() > 1) {
            // The value of the exited node must be consumed.
            operations.pop();
            value = pushPartialEvaluation(partialEvaluations.pop());

        } else {
            // The value is that on the top of the stack.
            value = partialEvaluations.peek();
        }

        // Caching the value computed for the node.
        node.setVisitStamp(startTime);
        node.cacheValue(value);
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
