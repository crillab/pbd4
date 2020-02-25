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

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * The DecisionDnnf represents the decision-DNNFs computed by the compiler.
 * It encapsulates the root node of the decision-DNNF, and provides convenient
 * methods for manipulating it.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class DecisionDnnf {

    /**
     * The number of variables in this decision-DNNF.
     */
    private final int numberOfVariables;

    /**
     * The number of nodes in this decision-DNNF.
     */
    private final int numberOfNodes;

    /**
     * The number of edges in this decision-DNNF.
     */
    private final int numberOfEdges;

    /**
     * The root node of this decision-DNNF.
     */
    private final DecisionDnnfNode rootNode;

    /**
     * Creates a new DecisionDnnf.
     *
     * @param numberOfVariables The number of variables in the decision-DNNF.
     * @param numberOfNodes The number of nodes in the decision-DNNF.
     * @param numberOfEdges The number of edges in the decision-DNNF.
     * @param rootNode The root node of the decision-DNNF.
     */
    public DecisionDnnf(int numberOfVariables, int numberOfNodes, int numberOfEdges,
            DecisionDnnfNode rootNode) {
        this.numberOfVariables = numberOfVariables;
        this.numberOfNodes = numberOfNodes;
        this.numberOfEdges = numberOfEdges;
        this.rootNode = rootNode;
    }

    /**
     * Accepts a {@link DecisionDnnfVisitor} in a depth-first manner.
     *
     * @param visitor The visitor to accept.
     */
    public void depthFirstAccept(DecisionDnnfVisitor visitor) {
        visitor.enter(this);
        rootNode.depthFirstAccept(visitor);
        visitor.visit(this);
        visitor.exit(this);
    }

    /**
     * Accepts a {@link DecisionDnnfVisitor} in a breadth-first manner.
     *
     * @param visitor The visitor to accept.
     */
    public void breadthFirstAccept(DecisionDnnfVisitor visitor) {
        visitor.enter(this);
        visitor.visit(this);
        rootNode.breadthFirstAccept(visitor);
        visitor.exit(this);
    }

    /**
     * Evaluates this decision-DNNF on the given assignment.
     * An assignment is given by the set of literals it satisfies, given in DIMACS format.
     *
     * @param assignment The assignment on which to evaluate this decision-DNNF.
     *
     * @return Whether this decision-DNNF is satisfied by the given assignment.
     */
    public boolean evaluate(Set<Integer> assignment) {
        var evaluator = new DecisionDnnfEvaluator(assignment);
        depthFirstAccept(evaluator);
        return evaluator.evaluate();
    }

    /**
     * Evaluates this decision-DNNF on the given assignment.
     * An assignment is given by the array of literals it satisfies, given in DIMACS
     * format.
     *
     * @param assignment The assignment on which to evaluate this decision-DNNF.
     *
     * @return Whether this decision-DNNF is satisfied by the given assignment.
     */
    public boolean evaluate(int... assignment) {
        return evaluate(stream(assignment).boxed().collect(toSet()));
    }

    /**
     * Evaluates this decision-DNNF on the given assignment.
     * An assignment is given by the array of Boolean values it assigns to the variables.
     * In other words, {@code assignment[i]} is {@code true} if, and only if, variable
     * {@code i + 1} is satisfied.
     *
     * @param assignment The assignment on which to evaluate this decision-DNNF.
     *
     * @return Whether this decision-DNNF is satisfied by the given assignment.
     */
    public boolean evaluate(boolean[] assignment) {
        var set = new HashSet<Integer>();
        for (int i = 0; i < assignment.length; i++) {
            set.add(assignment[i] ? (i + 1) : (-i - 1));
        }
        return evaluate(set);
    }

    /**
     * Gives the number of variables in this decision-DNNF.
     *
     * @return The number of variables in this decision-DNNF.
     */
    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    /**
     * Gives the number of nodes in this decision-DNNF.
     *
     * @return The number of nodes in this decision-DNNF.
     */
    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    /**
     * Gives the number of edges in this decision-DNNF.
     *
     * @return The number of edges in this decision-DNNF.
     */
    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    /**
     * Gives the root node of this decision-DNNF.
     *
     * @return The root node of this decision-DNNF.
     */
    public DecisionDnnfNode getRootNode() {
        return rootNode;
    }

    /**
     * Writes this decision-DNNF node to the given output stream, using the NNF format.
     *
     * @param outputStream The output stream in which to write this decision-DNNF node.
     */
    public void writeTo(OutputStream outputStream) {
        depthFirstAccept(new DecisionDnnfWriter(outputStream));
    }

    /**
     * Computes a String representation of this decision-DNNF node, using the NNF format.
     *
     * @return A String representation of this decision-DNNF node.
     */
    @Override
    public String toString() {
        var output = new ByteArrayOutputStream();
        writeTo(output);
        return output.toString();
    }

}
