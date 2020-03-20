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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sat4j.core.LiteralsUtils;

/**
 * The TestReadOpbFormula is a JUnit test case testing the reading of input pseudo-Boolean
 * formulae in the OPB format using {@link PseudoBooleanFormulaReader}.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("Pseudo-Boolean formulae are properly read from an OPB input stream.")
public final class TestReadOpbFormula extends AbstractTestPseudoBooleanSolving {

    /**
     * Tests that the OPB formula is properly read from the file {@code example-1.opb}.
     */
    @Test
    @DisplayName("The OPB formula 'example-1.opb' is properly read.")
    public void testExample1() {
        var formula = readOpb("example-1.opb");
        
        // Checking the variables of the formula.
        assertEquals(7, formula.numberOfVariables());

        // The selectors must not appear in the variables.
        var variables = formula.variables();
        assertEquals(7, variables.size());
        assertTrue(variables.contains(1));
        assertTrue(variables.contains(2));
        assertTrue(variables.contains(3));
        assertTrue(variables.contains(4));
        assertTrue(variables.contains(5));
        assertTrue(variables.contains(6));
        assertTrue(variables.contains(7));
        assertFalse(variables.contains(8));
        assertFalse(variables.contains(9));
        assertFalse(variables.contains(10));
        assertFalse(variables.contains(11));

        // Checking the constraints containing the variable 1.
        var constraintsContaining1 = formula.getConstraintsContaining(1);
        assertEquals(2, constraintsContaining1.size());
        assertTrue(constraintsContaining1.contains(0));
        assertTrue(constraintsContaining1.contains(1));
        assertFalse(constraintsContaining1.contains(2));
        assertFalse(constraintsContaining1.contains(3));

        // Checking the constraints containing the variable 2.
        var constraintsContaining2 = formula.getConstraintsContaining(2);
        assertEquals(2, constraintsContaining2.size());
        assertTrue(constraintsContaining2.contains(0));
        assertFalse(constraintsContaining2.contains(1));
        assertTrue(constraintsContaining2.contains(2));
        assertFalse(constraintsContaining2.contains(3));

        // Checking the constraints containing the variable 3.
        var constraintsContaining3 = formula.getConstraintsContaining(3);
        assertEquals(1, constraintsContaining3.size());
        assertTrue(constraintsContaining3.contains(0));
        assertFalse(constraintsContaining3.contains(1));
        assertFalse(constraintsContaining3.contains(2));
        assertFalse(constraintsContaining3.contains(3));

        // Checking the constraints containing the variable 4.
        var constraintsContaining4 = formula.getConstraintsContaining(4);
        assertEquals(1, constraintsContaining4.size());
        assertFalse(constraintsContaining4.contains(0));
        assertTrue(constraintsContaining4.contains(1));
        assertFalse(constraintsContaining4.contains(2));
        assertFalse(constraintsContaining4.contains(3));

        // Checking the constraints containing the variable 5.
        var constraintsContaining5 = formula.getConstraintsContaining(5);
        assertEquals(2, constraintsContaining5.size());
        assertFalse(constraintsContaining5.contains(0));
        assertFalse(constraintsContaining5.contains(1));
        assertTrue(constraintsContaining5.contains(2));
        assertTrue(constraintsContaining5.contains(3));

        // Checking the constraints containing the variable 6.
        var constraintsContaining6 = formula.getConstraintsContaining(6);
        assertEquals(1, constraintsContaining6.size());
        assertFalse(constraintsContaining6.contains(0));
        assertFalse(constraintsContaining6.contains(1));
        assertTrue(constraintsContaining6.contains(2));
        assertFalse(constraintsContaining6.contains(3));

        // Checking the constraints containing the variable 7.
        var constraintsContaining7 = formula.getConstraintsContaining(7);
        assertEquals(2, constraintsContaining7.size());
        assertFalse(constraintsContaining7.contains(0));
        assertFalse(constraintsContaining7.contains(1));
        assertTrue(constraintsContaining7.contains(2));
        assertTrue(constraintsContaining7.contains(3));
        
        // Checking the constraints.
        assertEquals(4, formula.numberOfConstraints());

        // Checking the first constraint.
        var constraint0 = formula.getConstraint(0);
        assertEquals(4, constraint0.size());
        assertEquals(1, LiteralsUtils.toDimacs(constraint0.get(1)));
        assertEquals(BigInteger.valueOf(2), constraint0.getCoef(1));
        assertEquals(2, LiteralsUtils.toDimacs(constraint0.get(2)));
        assertEquals(BigInteger.valueOf(1), constraint0.getCoef(2));
        assertEquals(3, LiteralsUtils.toDimacs(constraint0.get(3)));
        assertEquals(BigInteger.valueOf(1), constraint0.getCoef(3));
        // The first literal is a fresh variable, used as a selector.
        assertEquals(8, LiteralsUtils.toDimacs(constraint0.get(0)));
        assertEquals(BigInteger.valueOf(3), constraint0.getCoef(0));
        assertEquals(BigInteger.valueOf(3), constraint0.getDegree());

        // Checking the second constraint.
        var constraint1 = formula.getConstraint(1);
        assertEquals(3, constraint1.size());
        assertEquals(1, LiteralsUtils.toDimacs(constraint1.get(0)));
        assertEquals(BigInteger.valueOf(1), constraint1.getCoef(0));
        assertEquals(4, LiteralsUtils.toDimacs(constraint1.get(1)));
        assertEquals(BigInteger.valueOf(1), constraint1.getCoef(1));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(9, LiteralsUtils.toDimacs(constraint1.get(2)));
        assertEquals(BigInteger.valueOf(1), constraint1.getCoef(2));
        assertEquals(BigInteger.valueOf(1), constraint1.getDegree());

        // Checking the third constraint.
        var constraint2 = formula.getConstraint(2);
        assertEquals(5, constraint2.size());
        assertEquals(-2, LiteralsUtils.toDimacs(constraint2.get(1)));
        assertEquals(BigInteger.valueOf(2), constraint2.getCoef(1));
        assertEquals(5, LiteralsUtils.toDimacs(constraint2.get(2)));
        assertEquals(BigInteger.valueOf(2), constraint2.getCoef(2));
        assertEquals(6, LiteralsUtils.toDimacs(constraint2.get(3)));
        assertEquals(BigInteger.valueOf(2), constraint2.getCoef(3));
        assertEquals(7, LiteralsUtils.toDimacs(constraint2.get(4)));
        assertEquals(BigInteger.valueOf(1), constraint2.getCoef(4));
        // The first literal is a fresh variable, used as a selector.
        assertEquals(10, LiteralsUtils.toDimacs(constraint2.get(0)));
        assertEquals(BigInteger.valueOf(3), constraint2.getCoef(0));
        assertEquals(BigInteger.valueOf(3), constraint2.getDegree());

        // Checking the fourth constraint.
        var constraint3 = formula.getConstraint(3);
        assertEquals(3, constraint3.size());
        assertEquals(-5, LiteralsUtils.toDimacs(constraint3.get(0)));
        assertEquals(BigInteger.valueOf(1), constraint3.getCoef(0));
        assertEquals(7, LiteralsUtils.toDimacs(constraint3.get(1)));
        assertEquals(BigInteger.valueOf(1), constraint3.getCoef(1));
        // The last literal is a fresh variable, used as a selector.
        assertEquals(11, LiteralsUtils.toDimacs(constraint3.get(2)));
        assertEquals(BigInteger.valueOf(1), constraint3.getCoef(2));
        assertEquals(BigInteger.valueOf(1), constraint3.getDegree());
    }

    /**
     * Tests that the OPB formula is properly read from the file {@code example-2.opb}.
     */
    @Test
    @DisplayName("The OPB formula 'example-2.opb' is properly read.")
    public void testExample2() {
        var formula = readOpb("example-2.opb");
        
        // Checking the variables of the formula.
        assertEquals(10, formula.numberOfVariables());

        // The selectors must not appear in the variables.
        var variables = formula.variables();
        assertEquals(10, variables.size());
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
        assertFalse(variables.contains(11));
        assertFalse(variables.contains(12));
        assertFalse(variables.contains(13));
        assertFalse(variables.contains(14));
        assertFalse(variables.contains(15));

        // Checking the constraints containing the variable 1.
        var constraintsContaining1 = formula.getConstraintsContaining(1);
        assertEquals(2, constraintsContaining1.size());
        assertFalse(constraintsContaining1.contains(0));
        assertTrue(constraintsContaining1.contains(1));
        assertTrue(constraintsContaining1.contains(2));
        assertFalse(constraintsContaining1.contains(3));
        assertFalse(constraintsContaining1.contains(4));

        // Checking the constraints containing the variable 2.
        var constraintsContaining2 = formula.getConstraintsContaining(2);
        assertEquals(4, constraintsContaining2.size());
        assertTrue(constraintsContaining2.contains(0));
        assertTrue(constraintsContaining2.contains(1));
        assertTrue(constraintsContaining2.contains(2));
        assertTrue(constraintsContaining2.contains(3));
        assertFalse(constraintsContaining2.contains(4));

        // Checking the constraints containing the variable 3.
        var constraintsContaining3 = formula.getConstraintsContaining(3);
        assertEquals(4, constraintsContaining3.size());
        assertTrue(constraintsContaining3.contains(0));
        assertTrue(constraintsContaining3.contains(1));
        assertTrue(constraintsContaining3.contains(2));
        assertFalse(constraintsContaining3.contains(3));
        assertTrue(constraintsContaining3.contains(4));

        // Checking the constraints containing the variable 4.
        var constraintsContaining4 = formula.getConstraintsContaining(4);
        assertEquals(1, constraintsContaining4.size());
        assertTrue(constraintsContaining4.contains(0));
        assertFalse(constraintsContaining4.contains(1));
        assertFalse(constraintsContaining4.contains(2));
        assertFalse(constraintsContaining4.contains(3));
        assertFalse(constraintsContaining4.contains(4));

        // Checking the constraints containing the variable 5.
        var constraintsContaining5 = formula.getConstraintsContaining(5);
        assertEquals(1, constraintsContaining5.size());
        assertFalse(constraintsContaining5.contains(0));
        assertFalse(constraintsContaining5.contains(1));
        assertFalse(constraintsContaining5.contains(2));
        assertTrue(constraintsContaining5.contains(3));
        assertFalse(constraintsContaining5.contains(4));

        // Checking the constraints containing the variable 6.
        var constraintsContaining6 = formula.getConstraintsContaining(6);
        assertEquals(1, constraintsContaining6.size());
        assertFalse(constraintsContaining6.contains(0));
        assertFalse(constraintsContaining6.contains(1));
        assertFalse(constraintsContaining6.contains(2));
        assertTrue(constraintsContaining6.contains(3));
        assertFalse(constraintsContaining6.contains(4));

        // Checking the constraints containing the variable 7.
        var constraintsContaining7 = formula.getConstraintsContaining(7);
        assertEquals(1, constraintsContaining7.size());
        assertFalse(constraintsContaining7.contains(0));
        assertFalse(constraintsContaining7.contains(1));
        assertFalse(constraintsContaining7.contains(2));
        assertTrue(constraintsContaining7.contains(3));
        assertFalse(constraintsContaining7.contains(4));

        // Checking the constraints containing the variable 8.
        var constraintsContaining8 = formula.getConstraintsContaining(8);
        assertEquals(1, constraintsContaining8.size());
        assertFalse(constraintsContaining8.contains(0));
        assertFalse(constraintsContaining8.contains(1));
        assertFalse(constraintsContaining8.contains(2));
        assertFalse(constraintsContaining8.contains(3));
        assertTrue(constraintsContaining8.contains(4));

        // Checking the constraints containing the variable 9.
        var constraintsContaining9 = formula.getConstraintsContaining(9);
        assertEquals(1, constraintsContaining9.size());
        assertFalse(constraintsContaining9.contains(0));
        assertFalse(constraintsContaining9.contains(1));
        assertFalse(constraintsContaining9.contains(2));
        assertFalse(constraintsContaining9.contains(3));
        assertTrue(constraintsContaining9.contains(4));

        // Checking the constraints containing the variable 10.
        var constraintsContaining10 = formula.getConstraintsContaining(10);
        assertEquals(1, constraintsContaining10.size());
        assertFalse(constraintsContaining10.contains(0));
        assertFalse(constraintsContaining10.contains(1));
        assertFalse(constraintsContaining10.contains(2));
        assertFalse(constraintsContaining10.contains(3));
        assertTrue(constraintsContaining10.contains(4));
        
        // Checking the constraints.
        assertEquals(5, formula.numberOfConstraints());

        // Checking the first constraint.
        var constraint0 = formula.getConstraint(0);
        assertEquals(4, constraint0.size());
        assertEquals(3, LiteralsUtils.toDimacs(constraint0.get(1)));
        assertEquals(BigInteger.valueOf(2), constraint0.getCoef(1));
        assertEquals(4, LiteralsUtils.toDimacs(constraint0.get(2)));
        assertEquals(BigInteger.valueOf(2), constraint0.getCoef(2));
        assertEquals(2, LiteralsUtils.toDimacs(constraint0.get(3)));
        assertEquals(BigInteger.valueOf(1), constraint0.getCoef(3));
        // The remaining literal is a fresh variable, used as a selector.
        assertEquals(11, LiteralsUtils.toDimacs(constraint0.get(0)));
        assertEquals(BigInteger.valueOf(3), constraint0.getCoef(0));
        assertEquals(BigInteger.valueOf(3), constraint0.getDegree());

        // Checking the second constraint (representing the equality constraint).
        var constraint1 = formula.getConstraint(1);
        assertEquals(4, constraint1.size());
        assertEquals(1, LiteralsUtils.toDimacs(constraint1.get(0)));
        assertEquals(BigInteger.valueOf(2), constraint1.getCoef(0));
        assertEquals(2, LiteralsUtils.toDimacs(constraint1.get(2)));
        assertEquals(BigInteger.valueOf(1), constraint1.getCoef(2));
        assertEquals(3, LiteralsUtils.toDimacs(constraint1.get(3)));
        assertEquals(BigInteger.valueOf(1), constraint1.getCoef(3));
        // The remaining literal is a fresh variable, used as a selector.
        assertEquals(12, LiteralsUtils.toDimacs(constraint1.get(1)));
        assertEquals(BigInteger.valueOf(2), constraint1.getCoef(1));
        assertEquals(BigInteger.valueOf(2), constraint1.getDegree());

        // Checking the third constraint (representing the equality constraint).
        var constraint2 = formula.getConstraint(2);
        assertEquals(4, constraint2.size());
        assertEquals(-1, LiteralsUtils.toDimacs(constraint2.get(0)));
        assertEquals(BigInteger.valueOf(2), constraint2.getCoef(0));
        assertEquals(-2, LiteralsUtils.toDimacs(constraint2.get(2)));
        assertEquals(BigInteger.valueOf(1), constraint2.getCoef(2));
        assertEquals(-3, LiteralsUtils.toDimacs(constraint2.get(3)));
        assertEquals(BigInteger.valueOf(1), constraint2.getCoef(3));
        // The remaining literal is a fresh variable, used as a selector.
        assertEquals(13, LiteralsUtils.toDimacs(constraint2.get(1)));
        assertEquals(BigInteger.valueOf(2), constraint2.getCoef(1));
        assertEquals(BigInteger.valueOf(2), constraint2.getDegree());

        // Checking the fourth constraint.
        var constraint3 = formula.getConstraint(3);
        assertEquals(5, constraint3.size());
        assertEquals(-2, LiteralsUtils.toDimacs(constraint3.get(0)));
        assertEquals(BigInteger.valueOf(3), constraint3.getCoef(0));
        assertEquals(5, LiteralsUtils.toDimacs(constraint3.get(2)));
        assertEquals(BigInteger.valueOf(2), constraint3.getCoef(2));
        assertEquals(6, LiteralsUtils.toDimacs(constraint3.get(3)));
        assertEquals(BigInteger.valueOf(2), constraint3.getCoef(3));
        assertEquals(7, LiteralsUtils.toDimacs(constraint3.get(4)));
        assertEquals(BigInteger.valueOf(1), constraint3.getCoef(4));
        // The remaining literal is a fresh variable, used as a selector.
        assertEquals(14, LiteralsUtils.toDimacs(constraint3.get(1)));
        assertEquals(BigInteger.valueOf(3), constraint3.getCoef(1));
        assertEquals(BigInteger.valueOf(3), constraint3.getDegree());

        // Checking the fifth constraint.
        var constraint4 = formula.getConstraint(4);
        assertEquals(5, constraint4.size());
        assertEquals(3, LiteralsUtils.toDimacs(constraint4.get(0)));
        assertEquals(BigInteger.valueOf(3), constraint4.getCoef(0));
        assertEquals(8, LiteralsUtils.toDimacs(constraint4.get(2)));
        assertEquals(BigInteger.valueOf(2), constraint4.getCoef(2));
        assertEquals(9, LiteralsUtils.toDimacs(constraint4.get(3)));
        assertEquals(BigInteger.valueOf(2), constraint4.getCoef(3));
        assertEquals(10, LiteralsUtils.toDimacs(constraint4.get(4)));
        assertEquals(BigInteger.valueOf(1), constraint4.getCoef(4));
        // The remaining literal is a fresh variable, used as a selector.
        assertEquals(15, LiteralsUtils.toDimacs(constraint4.get(1)));
        assertEquals(BigInteger.valueOf(3), constraint4.getCoef(1));
        assertEquals(BigInteger.valueOf(3), constraint4.getDegree());
    }

}
