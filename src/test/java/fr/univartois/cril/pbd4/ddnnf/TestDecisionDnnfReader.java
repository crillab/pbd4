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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * The TestDecisionDnnfReader is a JUnit test case testing the implementation of the
 * {@link DecisionDnnfReader}.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class TestDecisionDnnfReader extends AbstractTestDecisionDnnf {

    /**
     * Tests that the decision-DNNF stored in the file {@code example-1.nnf} is properly
     * read.
     */
    @Test
    public void testEvaluateExample1() {
        var ddnnf = read("example-1.nnf");
        assertEquals(nnfOfExample1(), ddnnf.toString());
    }

    /**
     * Gives the String representation of the decision-DNNF stored in the file
     * {@code example-1.nnf}.
     * 
     * @return The String representation of the decision-DNNF.
     */
    static String nnfOfExample1() {
        return "nnf 15 17 4" + System.lineSeparator() +
                "L -3" + System.lineSeparator() +
                "L -2" + System.lineSeparator() +
                "L 1" + System.lineSeparator() +
                "A 3 2 1 0" + System.lineSeparator() +
                "L 3" + System.lineSeparator() +
                "O 3 2 4 3" + System.lineSeparator() +
                "L -4" + System.lineSeparator() +
                "A 2 6 5" + System.lineSeparator() +
                "L 4" + System.lineSeparator() +
                "A 2 2 8" + System.lineSeparator() +
                "A 2 1 4" + System.lineSeparator() +
                "L 2" + System.lineSeparator() +
                "O 2 2 11 10" + System.lineSeparator() +
                "A 2 12 9" + System.lineSeparator() +
                "O 4 2 13 7";
    }

    /**
     * Tests that the decision-DNNF stored in the file {@code example-2.nnf} is properly
     * read.
     */
    @Test
    public void testEvaluateExample2() {
        var ddnnf = read("example-2.nnf");
        assertEquals(nnfOfExample2(), ddnnf.toString());
    }

    /**
     * Gives the String representation of the decision-DNNF stored in the file
     * {@code example-2.nnf}.
     * 
     * @return The String representation of the decision-DNNF.
     */
    static String nnfOfExample2() {
        return "nnf 15 14 3" + System.lineSeparator() +
                "O 0 0" + System.lineSeparator() +
                "A 0" + System.lineSeparator() +
                "L -1" + System.lineSeparator() +
                "L 3" + System.lineSeparator() +
                "A 3 3 2 1" + System.lineSeparator() +
                "L 1" + System.lineSeparator() +
                "A 2 5 1" + System.lineSeparator() +
                "O 1 2 6 4" + System.lineSeparator() +
                "L -2" + System.lineSeparator() +
                "A 2 8 7" + System.lineSeparator() +
                "L -1" + System.lineSeparator() +
                "L 2" + System.lineSeparator() +
                "L -3" + System.lineSeparator() +
                "A 4 12 11 10 1" + System.lineSeparator() +
                "O 2 2 13 9";
    }

}
