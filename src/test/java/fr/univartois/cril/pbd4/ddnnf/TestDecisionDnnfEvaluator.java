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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The TestDecisionDnnfEvaluator is a JUnit test case testing the implementation of the
 * {@link DecisionDnnfEvaluator}.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class TestDecisionDnnfEvaluator extends AbstractTestDecisionDnnf {

    /**
     * Tests that the decision-DNNF stored in the file {@code example-1.nnf} is properly
     * evaluated.
     */
    @Test
    public void testEvaluateExample1() {
        var ddnnf = read("example-1.nnf");

        assertTrue(ddnnf.evaluate(1, 2, 3, 4));
        assertTrue(ddnnf.evaluate(1, 2, 3, -4));
        assertTrue(ddnnf.evaluate(1, 2, -3, 4));
        assertFalse(ddnnf.evaluate(1, 2, -3, -4));
        assertTrue(ddnnf.evaluate(1, -2, 3, 4));
        assertTrue(ddnnf.evaluate(1, -2, 3, -4));
        assertFalse(ddnnf.evaluate(1, -2, -3, 4));
        assertTrue(ddnnf.evaluate(1, -2, -3, -4));
        assertFalse(ddnnf.evaluate(-1, 2, 3, 4));
        assertTrue(ddnnf.evaluate(-1, 2, 3, -4));
        assertFalse(ddnnf.evaluate(-1, 2, -3, 4));
        assertFalse(ddnnf.evaluate(-1, 2, -3, -4));
        assertFalse(ddnnf.evaluate(-1, -2, 3, 4));
        assertTrue(ddnnf.evaluate(-1, -2, 3, -4));
        assertFalse(ddnnf.evaluate(-1, -2, -3, 4));
        assertFalse(ddnnf.evaluate(-1, -2, -3, -4));
    }

    /**
     * Tests that the decision-DNNF stored in the file {@code example-2.nnf} is properly
     * evaluated.
     */
    @Test
    public void testEvaluateExample2() {
        var ddnnf = read("example-2.nnf");

        assertFalse(ddnnf.evaluate(1, 2, 3));
        assertFalse(ddnnf.evaluate(1, 2, -3));
        assertTrue(ddnnf.evaluate(1, -2, 3));
        assertTrue(ddnnf.evaluate(1, -2, -3));
        assertFalse(ddnnf.evaluate(-1, 2, 3));
        assertTrue(ddnnf.evaluate(-1, 2, -3));
        assertTrue(ddnnf.evaluate(-1, -2, 3));
        assertFalse(ddnnf.evaluate(-1, -2, -3));
    }

}
