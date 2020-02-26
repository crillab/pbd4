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

package fr.univartois.cril.pbd4.pbc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * The TestReadFormula is a JUnit test case testing the reading of input pseudo-Boolean
 * formulae with {@link PseudoBooleanFormulaReader}.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class TestReadFormula extends AbstractTestPseudoBooleanSolving {

    /**
     * Tests that the CNF formula is properly read from the file {@code example-1.cnf}.
     */
    @Test
    public void testExample1() {
        var formula = readCnf("example-1.cnf");
        assertEquals(4, formula.numberOfVariables());
        assertEquals(4, formula.numberOfConstraints());

        var variables = formula.variables();
        assertEquals(4, variables.size());
        assertTrue(variables.contains(1));
        assertTrue(variables.contains(2));
        assertTrue(variables.contains(3));
        assertTrue(variables.contains(4));
    }

    /**
     * Tests that the CNF formula is properly read from the file {@code example-2.cnf}.
     */
    @Test
    public void testExample2() {
        var formula = readCnf("example-2.cnf");
        assertEquals(3, formula.numberOfVariables());
        assertEquals(3, formula.numberOfConstraints());

        var variables = formula.variables();
        assertEquals(3, variables.size());
        assertTrue(variables.contains(1));
        assertTrue(variables.contains(2));
        assertTrue(variables.contains(3));
    }

}
