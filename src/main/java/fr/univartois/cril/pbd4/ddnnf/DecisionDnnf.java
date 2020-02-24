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

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * The DecisionDnnf defines the interface for manipulating decision-DNNFs.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface DecisionDnnf {

    /**
     * Accepts a {@link DecisionDnnfVisitor} in a depth-first manner.
     *
     * @param visitor The visitor to accept.
     */
    void depthFirstAccept(DecisionDnnfVisitor visitor);

    /**
     * Accepts a {@link DecisionDnnfVisitor} in a breadth-first manner.
     *
     * @param visitor The visitor to accept.
     */
    void breadthFirstAccept(DecisionDnnfVisitor visitor);

    /**
     * Evaluates this decision-DNNF on the given assignment.
     * An assignment is given by the set of literals it satisfies, given in DIMACS format.
     *
     * @param assignment The assignment on which to evaluate this decision-DNNF.
     *
     * @return Whether this decision-DNNF is satisfied by the given assignment.
     */
    default boolean evaluate(Set<Integer> assignment) {
        var evaluator = new DecisionDnnfEvaluator(assignment);
        breadthFirstAccept(evaluator);
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
    default boolean evaluate(int... assignment) {
        return evaluate(stream(assignment).boxed().collect(toSet()));
    }

    /**
     * Evaluates this decision-DNNF on the given assignment.
     * An assignment is given by the array of Boolean values it assigns to the variables
     * of the decision-DNNF.
     * In other words, {@code assignment[i]} is {@code true} if, and only if, variable
     * {@code i + 1} is satisfied.
     *
     * @param assignment The assignment on which to evaluate this decision-DNNF.
     *
     * @return Whether this decision-DNNF is satisfied by the given assignment.
     */
    default boolean evaluate(boolean[] assignment) {
        var set = new HashSet<Integer>();
        for (int i = 0; i < assignment.length; i++) {
            set.add(assignment[i] ? (i + 1) : (-i - 1));
        }
        return evaluate(set);
    }

    /**
     * Writes this decision-DNNF to the given output stream, using the NNF format.
     *
     * @param outputStream The output stream in which to write this decision-DNNF.
     */
    default void writeTo(OutputStream outputStream) {
        try (var visitor = new DecisionDnnfWriter(outputStream)) {
            depthFirstAccept(visitor);
        }
    }

    /**
     * Computes a String representation of this decision-DNNF, using the NNF format.
     *
     * @return A String representation of this decision-DNNF.
     */
    String toString();

}
