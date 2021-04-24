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

package fr.univartois.cril.pbd4.pbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sat4j.core.LiteralsUtils;

/**
 * The TestReadCnfFormula is a JUnit test case testing the reading of input pseudo-Boolean
 * formulae in Conjunctive Normal Form (CNF) using {@link PseudoBooleanFormulaReader}.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("Pseudo-Boolean formulae are properly read from a CNF input stream.")
public final class TestReadCnfFormula extends AbstractTestPseudoBooleanSolving {

    /**
     * Tests that the CNF formula is properly read from the file {@code example-1.cnf}.
     */
    @Test
    @DisplayName("The CNF formula 'example-1.cnf' is properly read.")
    public void testExample1() {
        var formula = readCnf("example-1.cnf");
        
        // Checking the variables of the formula.
        assertEquals(4, formula.numberOfVariables());

        // The selectors must not appear in the variables.
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
        assertEquals(BigInteger.ONE, clause0.getCoef(0));
        assertEquals(2, LiteralsUtils.toDimacs(clause0.get(1)));
        assertEquals(BigInteger.ONE, clause0.getCoef(1));
        assertEquals(3, LiteralsUtils.toDimacs(clause0.get(2)));
        assertEquals(BigInteger.ONE, clause0.getCoef(2));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(5, LiteralsUtils.toDimacs(clause0.get(3)));
        assertEquals(BigInteger.ONE, clause0.getCoef(3));
        assertEquals(BigInteger.ONE, clause0.getDegree());

        // Checking the second clause.
        var clause1 = formula.getConstraint(1);
        assertEquals(4, clause1.size());
        assertEquals(-2, LiteralsUtils.toDimacs(clause1.get(0)));
        assertEquals(BigInteger.ONE, clause1.getCoef(0));
        assertEquals(3, LiteralsUtils.toDimacs(clause1.get(1)));
        assertEquals(BigInteger.ONE, clause1.getCoef(1));
        assertEquals(4, LiteralsUtils.toDimacs(clause1.get(2)));
        assertEquals(BigInteger.ONE, clause1.getCoef(2));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(6, LiteralsUtils.toDimacs(clause1.get(3)));
        assertEquals(BigInteger.ONE, clause1.getCoef(3));
        assertEquals(BigInteger.ONE, clause1.getDegree());

        // Checking the third clause.
        var clause2 = formula.getConstraint(2);
        assertEquals(3, clause2.size());
        assertEquals(1, LiteralsUtils.toDimacs(clause2.get(0)));
        assertEquals(BigInteger.ONE, clause2.getCoef(0));
        assertEquals(-4, LiteralsUtils.toDimacs(clause2.get(1)));
        assertEquals(BigInteger.ONE, clause2.getCoef(1));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(7, LiteralsUtils.toDimacs(clause2.get(2)));
        assertEquals(BigInteger.ONE, clause2.getCoef(2));
        assertEquals(BigInteger.ONE, clause2.getDegree());

        // Checking the fourth clause.
        var clause3 = formula.getConstraint(3);
        assertEquals(4, clause3.size());
        assertEquals(2, LiteralsUtils.toDimacs(clause3.get(0)));
        assertEquals(BigInteger.ONE, clause3.getCoef(0));
        assertEquals(3, LiteralsUtils.toDimacs(clause3.get(1)));
        assertEquals(BigInteger.ONE, clause3.getCoef(1));
        assertEquals(-4, LiteralsUtils.toDimacs(clause3.get(2)));
        assertEquals(BigInteger.ONE, clause3.getCoef(2));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(8, LiteralsUtils.toDimacs(clause3.get(3)));
        assertEquals(BigInteger.ONE, clause3.getCoef(3));
        assertEquals(BigInteger.ONE, clause3.getDegree());
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
        assertEquals(BigInteger.ONE, clause0.getCoef(0));
        assertEquals(2, LiteralsUtils.toDimacs(clause0.get(1)));
        assertEquals(BigInteger.ONE, clause0.getCoef(1));
        assertEquals(3, LiteralsUtils.toDimacs(clause0.get(2)));
        assertEquals(BigInteger.ONE, clause0.getCoef(2));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(4, LiteralsUtils.toDimacs(clause0.get(3)));
        assertEquals(BigInteger.ONE, clause0.getCoef(3));
        assertEquals(BigInteger.ONE, clause0.getDegree());

        // Checking the second clause.
        var clause1 = formula.getConstraint(1);
        assertEquals(3, clause1.size());
        assertEquals(-1, LiteralsUtils.toDimacs(clause1.get(0)));
        assertEquals(BigInteger.ONE, clause1.getCoef(0));
        assertEquals(-2, LiteralsUtils.toDimacs(clause1.get(1)));
        assertEquals(BigInteger.ONE, clause1.getCoef(1));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(5, LiteralsUtils.toDimacs(clause1.get(2)));
        assertEquals(BigInteger.ONE, clause1.getCoef(2));
        assertEquals(BigInteger.ONE, clause1.getDegree());

        // Checking the third clause.
        var clause2 = formula.getConstraint(2);
        assertEquals(3, clause2.size());
        assertEquals(-2, LiteralsUtils.toDimacs(clause2.get(0)));
        assertEquals(BigInteger.ONE, clause2.getCoef(0));
        assertEquals(-3, LiteralsUtils.toDimacs(clause2.get(1)));
        assertEquals(BigInteger.ONE, clause2.getCoef(1));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(6, LiteralsUtils.toDimacs(clause2.get(2)));
        assertEquals(BigInteger.ONE, clause2.getCoef(2));
        assertEquals(BigInteger.ONE, clause2.getDegree());
    }

    /**
     * Tests that the CNF formula is properly read from the file {@code example-3.cnf}.
     */
    @Test
    @DisplayName("The CNF formula 'example-3.cnf' is properly read.")
    public void testExample3() {
        var formula = readCnf("example-3.cnf");
        
        // Checking the variables of the formula.
        assertEquals(11, formula.numberOfVariables());

        // The selectors must not appear in the variables.
        var variables = formula.variables();
        assertEquals(11, variables.size());
        assertTrue(variables.contains(1));
        assertTrue(variables.contains(2));
        assertTrue(variables.contains(3));
        assertTrue(variables.contains(4));
        assertTrue(variables.contains(5));
        assertTrue(variables.contains(6));
        assertTrue(variables.contains(7));
        assertTrue(variables.contains(8));
        assertTrue(variables.contains(9));
        assertTrue(variables.contains(10));
        assertTrue(variables.contains(11));

        // Checking the constraints containing the variable 1.
        var constraintsContaining1 = formula.getConstraintsContaining(1);
        assertEquals(2, constraintsContaining1.size());
        assertTrue(constraintsContaining1.contains(0));
        assertTrue(constraintsContaining1.contains(1));

        // Checking the constraints containing the variable 2.
        var constraintsContaining2 = formula.getConstraintsContaining(2);
        assertEquals(3, constraintsContaining2.size());
        assertTrue(constraintsContaining2.contains(0));
        assertTrue(constraintsContaining2.contains(2));
        assertTrue(constraintsContaining2.contains(3));

        // Checking the constraints containing the variable 3.
        var constraintsContaining3 = formula.getConstraintsContaining(3);
        assertEquals(3, constraintsContaining3.size());
        assertTrue(constraintsContaining3.contains(1));
        assertTrue(constraintsContaining3.contains(4));
        assertTrue(constraintsContaining3.contains(5));

        // Checking the constraints containing the variable 4.
        var constraintsContaining4 = formula.getConstraintsContaining(4);
        assertEquals(1, constraintsContaining4.size());
        assertTrue(constraintsContaining4.contains(2));

        // Checking the constraints containing the variable 5.
        var constraintsContaining5 = formula.getConstraintsContaining(5);
        assertEquals(1, constraintsContaining5.size());
        assertTrue(constraintsContaining5.contains(2));

        // Checking the constraints containing the variable 6.
        var constraintsContaining6 = formula.getConstraintsContaining(6);
        assertEquals(1, constraintsContaining6.size());
        assertTrue(constraintsContaining6.contains(3));

        // Checking the constraints containing the variable 7.
        var constraintsContaining7 = formula.getConstraintsContaining(7);
        assertEquals(1, constraintsContaining7.size());
        assertTrue(constraintsContaining7.contains(3));

        // Checking the constraints containing the variable 8.
        var constraintsContaining8 = formula.getConstraintsContaining(8);
        assertEquals(1, constraintsContaining8.size());
        assertTrue(constraintsContaining8.contains(4));

        // Checking the constraints containing the variable 9.
        var constraintsContaining9 = formula.getConstraintsContaining(9);
        assertEquals(1, constraintsContaining9.size());
        assertTrue(constraintsContaining9.contains(4));

        // Checking the constraints containing the variable 10.
        var constraintsContaining10 = formula.getConstraintsContaining(10);
        assertEquals(1, constraintsContaining10.size());
        assertTrue(constraintsContaining10.contains(5));

        // Checking the constraints containing the variable 11.
        var constraintsContaining11 = formula.getConstraintsContaining(11);
        assertEquals(1, constraintsContaining11.size());
        assertTrue(constraintsContaining11.contains(5));
        
        // Checking the constraints.
        assertEquals(6, formula.numberOfConstraints());

        // Checking the first clause.
        var clause0 = formula.getConstraint(0);
        assertEquals(3, clause0.size());
        assertEquals(1, LiteralsUtils.toDimacs(clause0.get(0)));
        assertEquals(BigInteger.ONE, clause0.getCoef(0));
        assertEquals(-2, LiteralsUtils.toDimacs(clause0.get(1)));
        assertEquals(BigInteger.ONE, clause0.getCoef(1));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(12, LiteralsUtils.toDimacs(clause0.get(2)));
        assertEquals(BigInteger.ONE, clause0.getCoef(2));
        assertEquals(BigInteger.ONE, clause0.getDegree());

        // Checking the second clause.
        var clause1 = formula.getConstraint(1);
        assertEquals(3, clause1.size());
        assertEquals(-1, LiteralsUtils.toDimacs(clause1.get(0)));
        assertEquals(BigInteger.ONE, clause1.getCoef(0));
        assertEquals(3, LiteralsUtils.toDimacs(clause1.get(1)));
        assertEquals(BigInteger.ONE, clause1.getCoef(1));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(13, LiteralsUtils.toDimacs(clause1.get(2)));
        assertEquals(BigInteger.ONE, clause1.getCoef(2));
        assertEquals(BigInteger.ONE, clause1.getDegree());

        // Checking the third clause.
        var clause2 = formula.getConstraint(2);
        assertEquals(4, clause2.size());
        assertEquals(2, LiteralsUtils.toDimacs(clause2.get(0)));
        assertEquals(BigInteger.ONE, clause2.getCoef(0));
        assertEquals(4, LiteralsUtils.toDimacs(clause2.get(1)));
        assertEquals(BigInteger.ONE, clause2.getCoef(1));
        assertEquals(-5, LiteralsUtils.toDimacs(clause2.get(2)));
        assertEquals(BigInteger.ONE, clause2.getCoef(2));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(14, LiteralsUtils.toDimacs(clause2.get(3)));
        assertEquals(BigInteger.ONE, clause2.getCoef(3));
        assertEquals(BigInteger.ONE, clause2.getDegree());

        // Checking the fourth clause.
        var clause3 = formula.getConstraint(3);
        assertEquals(4, clause3.size());
        assertEquals(-2, LiteralsUtils.toDimacs(clause3.get(0)));
        assertEquals(BigInteger.ONE, clause3.getCoef(0));
        assertEquals(-6, LiteralsUtils.toDimacs(clause3.get(1)));
        assertEquals(BigInteger.ONE, clause3.getCoef(1));
        assertEquals(7, LiteralsUtils.toDimacs(clause3.get(2)));
        assertEquals(BigInteger.ONE, clause3.getCoef(2));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(15, LiteralsUtils.toDimacs(clause3.get(3)));
        assertEquals(BigInteger.ONE, clause3.getCoef(3));
        assertEquals(BigInteger.ONE, clause3.getDegree());

        // Checking the fifth clause.
        var clause4 = formula.getConstraint(4);
        assertEquals(4, clause4.size());
        assertEquals(3, LiteralsUtils.toDimacs(clause4.get(0)));
        assertEquals(BigInteger.ONE, clause4.getCoef(0));
        assertEquals(8, LiteralsUtils.toDimacs(clause4.get(1)));
        assertEquals(BigInteger.ONE, clause4.getCoef(1));
        assertEquals(9, LiteralsUtils.toDimacs(clause4.get(2)));
        assertEquals(BigInteger.ONE, clause4.getCoef(2));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(16, LiteralsUtils.toDimacs(clause4.get(3)));
        assertEquals(BigInteger.ONE, clause4.getCoef(3));
        assertEquals(BigInteger.ONE, clause4.getDegree());

        // Checking the sixth clause.
        var clause5 = formula.getConstraint(5);
        assertEquals(4, clause5.size());
        assertEquals(-3, LiteralsUtils.toDimacs(clause5.get(0)));
        assertEquals(BigInteger.ONE, clause5.getCoef(0));
        assertEquals(10, LiteralsUtils.toDimacs(clause5.get(1)));
        assertEquals(BigInteger.ONE, clause5.getCoef(1));
        assertEquals(-11, LiteralsUtils.toDimacs(clause5.get(2)));
        assertEquals(BigInteger.ONE, clause5.getCoef(2));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(17, LiteralsUtils.toDimacs(clause5.get(3)));
        assertEquals(BigInteger.ONE, clause5.getCoef(3));
        assertEquals(BigInteger.ONE, clause5.getDegree());
    }

}
