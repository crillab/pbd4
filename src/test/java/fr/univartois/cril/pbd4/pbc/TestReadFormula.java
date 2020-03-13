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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sat4j.core.LiteralsUtils;

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
    @DisplayName("The CNF formula 'example-1.cnf' is properly read.")
    public void testExample1() {
        var formula = readCnf("example-1.cnf");
        
        // Checking the variables of the formula.
        assertEquals(4, formula.numberOfVariables());

        // The selectors must not appear.
        var variables = formula.variables();
        assertEquals(4, variables.size());
        assertTrue(variables.contains(1));
        assertTrue(variables.contains(2));
        assertTrue(variables.contains(3));
        assertTrue(variables.contains(4));
        assertFalse(variables.contains(5));
        assertFalse(variables.contains(6));
        assertFalse(variables.contains(7));
        assertFalse(variables.contains(8));

        // Checking the constraints containing the variable 1.
        var constraintsContaining1 = formula.getConstraintsContaining(1);
        assertEquals(2, constraintsContaining1.size());
        assertTrue(constraintsContaining1.contains(0));
        assertFalse(constraintsContaining1.contains(1));
        assertTrue(constraintsContaining1.contains(2));
        assertFalse(constraintsContaining1.contains(3));

        // Checking the constraints containing the variable 2.
        var constraintsContaining2 = formula.getConstraintsContaining(2);
        assertEquals(3, constraintsContaining2.size());
        assertTrue(constraintsContaining2.contains(0));
        assertTrue(constraintsContaining2.contains(1));
        assertFalse(constraintsContaining2.contains(2));
        assertTrue(constraintsContaining2.contains(3));

        // Checking the constraints containing the variable 3.
        var constraintsContaining3 = formula.getConstraintsContaining(3);
        assertEquals(3, constraintsContaining3.size());
        assertTrue(constraintsContaining3.contains(0));
        assertTrue(constraintsContaining3.contains(1));
        assertFalse(constraintsContaining3.contains(2));
        assertTrue(constraintsContaining3.contains(3));

        // Checking the constraints containing the variable 4.
        var constraintsContaining4 = formula.getConstraintsContaining(4);
        assertEquals(3, constraintsContaining4.size());
        assertFalse(constraintsContaining4.contains(0));
        assertTrue(constraintsContaining4.contains(1));
        assertTrue(constraintsContaining4.contains(2));
        assertTrue(constraintsContaining4.contains(3));
        
        // Checking the constraints.
        assertEquals(4, formula.numberOfConstraints());

        // Checking the first clause.
        var clause0 = formula.getConstraint(0);
        assertEquals(4, clause0.size());
        assertEquals(1, LiteralsUtils.toDimacs(clause0.get(0)));
        assertEquals(2, LiteralsUtils.toDimacs(clause0.get(1)));
        assertEquals(3, LiteralsUtils.toDimacs(clause0.get(2)));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(5, LiteralsUtils.toDimacs(clause0.get(3)));

        // Checking the second clause.
        var clause1 = formula.getConstraint(1);
        assertEquals(4, clause1.size());
        assertEquals(-2, LiteralsUtils.toDimacs(clause1.get(0)));
        assertEquals(3, LiteralsUtils.toDimacs(clause1.get(1)));
        assertEquals(4, LiteralsUtils.toDimacs(clause1.get(2)));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(6, LiteralsUtils.toDimacs(clause1.get(3)));

        // Checking the third clause.
        var clause2 = formula.getConstraint(2);
        assertEquals(3, clause2.size());
        assertEquals(1, LiteralsUtils.toDimacs(clause2.get(0)));
        assertEquals(-4, LiteralsUtils.toDimacs(clause2.get(1)));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(7, LiteralsUtils.toDimacs(clause2.get(2)));

        // Checking the fourth clause.
        var clause3 = formula.getConstraint(3);
        assertEquals(4, clause3.size());
        assertEquals(2, LiteralsUtils.toDimacs(clause3.get(0)));
        assertEquals(3, LiteralsUtils.toDimacs(clause3.get(1)));
        assertEquals(-4, LiteralsUtils.toDimacs(clause3.get(2)));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(8, LiteralsUtils.toDimacs(clause3.get(3)));
    }

    /**
     * Tests that the CNF formula is properly read from the file {@code example-2.cnf}.
     */
    @Test
    @DisplayName("The CNF formula 'example-2.cnf' is properly read.")
    public void testExample2() {
        var formula = readCnf("example-2.cnf");

        // Checking the variables of the formula.
        assertEquals(3, formula.numberOfVariables());
        
        // The selectors must not appear.
        var variables = formula.variables();
        assertEquals(3, variables.size());
        assertTrue(variables.contains(1));
        assertTrue(variables.contains(2));
        assertTrue(variables.contains(3));
        assertFalse(variables.contains(4));
        assertFalse(variables.contains(5));
        assertFalse(variables.contains(6));

        // Checking the constraints containing the variable 1.
        var constraintsContaining1 = formula.getConstraintsContaining(1);
        assertEquals(2, constraintsContaining1.size());
        assertTrue(constraintsContaining1.contains(0));
        assertTrue(constraintsContaining1.contains(1));
        assertFalse(constraintsContaining1.contains(2));

        // Checking the constraints containing the variable 2.
        var constraintsContaining2 = formula.getConstraintsContaining(2);
        assertEquals(3, constraintsContaining2.size());
        assertTrue(constraintsContaining2.contains(0));
        assertTrue(constraintsContaining2.contains(1));
        assertTrue(constraintsContaining2.contains(2));

        // Checking the constraints containing the variable 3.
        var constraintsContaining3 = formula.getConstraintsContaining(3);
        assertEquals(2, constraintsContaining3.size());
        assertTrue(constraintsContaining3.contains(0));
        assertFalse(constraintsContaining3.contains(1));
        assertTrue(constraintsContaining3.contains(2));
        
        // Checking the constraints.
        assertEquals(3, formula.numberOfConstraints());

        // Checking the first clause.
        var clause0 = formula.getConstraint(0);
        assertEquals(4, clause0.size());
        assertEquals(1, LiteralsUtils.toDimacs(clause0.get(0)));
        assertEquals(2, LiteralsUtils.toDimacs(clause0.get(1)));
        assertEquals(3, LiteralsUtils.toDimacs(clause0.get(2)));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(4, LiteralsUtils.toDimacs(clause0.get(3)));

        // Checking the second clause.
        var clause1 = formula.getConstraint(1);
        assertEquals(3, clause1.size());
        assertEquals(-1, LiteralsUtils.toDimacs(clause1.get(0)));
        assertEquals(-2, LiteralsUtils.toDimacs(clause1.get(1)));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(5, LiteralsUtils.toDimacs(clause1.get(2)));

        // Checking the third clause.
        var clause2 = formula.getConstraint(2);
        assertEquals(3, clause2.size());
        assertEquals(-2, LiteralsUtils.toDimacs(clause2.get(0)));
        assertEquals(-3, LiteralsUtils.toDimacs(clause2.get(1)));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(6, LiteralsUtils.toDimacs(clause2.get(2)));
    }

}
