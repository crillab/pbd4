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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The TestPropagateCnf is a JUnit test case testing Boolean Constraint Propagation
 * (BCP) on formula in Conjunctive Normal Form (CNF).
 * 
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("BCP on CNF formulae behaves as expected.")
public final class TestPropagateCnf extends AbstractTestPseudoBooleanSolving {

    /**
     * Tests that, when variables are assumed in such a way that BCP identifies a model
     * of the formula, the partial assignment is detected as satisfying.
     */
    @Test
    @DisplayName("Partial satisfying assignments are detected as such.")
    public void testDetectSatisfyingAssignment() {
        var formula = readCnf("example-2.cnf");

        // At first, there is nothing to propagate.
        var propagation = formula.propagate();
        assertTrue(propagation.isUnknown());
        assertTrue(propagation.getPropagatedLiterals().isEmpty());
        var simplifiedFormula = propagation.getSimplifiedFormula();
        assertEquals(3, simplifiedFormula.numberOfVariables());
        assertEquals(3, simplifiedFormula.numberOfConstraints());

        // When literal 1 is satisfied, propagating satisfies the formula.
        var propagationAssuming1 = simplifiedFormula.assume(1).propagate();
        assertTrue(propagationAssuming1.isSatisfiable());
        var propagatedLiterals1 = propagationAssuming1.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals1.size());
        assertTrue(propagatedLiterals1.contains(1));
        assertTrue(propagatedLiterals1.contains(-2));

        // When literal 2 is satisfied, propagating satisfies the formula.
        var propagationAssuming2 = simplifiedFormula.assume(2).propagate();
        assertTrue(propagationAssuming2.isSatisfiable());
        var propagatedLiterals2 = propagationAssuming2.getPropagatedLiterals();
        assertEquals(3, propagatedLiterals2.size());
        assertTrue(propagatedLiterals2.contains(-1));
        assertTrue(propagatedLiterals2.contains(2));
        assertTrue(propagatedLiterals2.contains(-3));

        // When literal 3 is satisfied, propagating satisfies the formula.
        var propagationAssuming3 = simplifiedFormula.assume(3).propagate();
        assertTrue(propagationAssuming3.isSatisfiable());
        var propagatedLiterals3 = propagationAssuming3.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals3.size());
        assertTrue(propagatedLiterals3.contains(-2));
        assertTrue(propagatedLiterals3.contains(3));
    }

    /**
     * Tests that, when variables are assumed in such a way that BCP identifies a conflict,
     * the partial assignment is detected as falsifying.
     */
    @Test
    @DisplayName("Partial falsifying assignments are detected as such.")
    public void testDetectFalsifyingAssignment() {
        var formula = readCnf("example-1.cnf");

        // At first, there is nothing to propagate.
        var propagation = formula.propagate();
        assertTrue(propagation.isUnknown());
        assertTrue(propagation.getPropagatedLiterals().isEmpty());
        var simplifiedFormula = propagation.getSimplifiedFormula();
        assertEquals(4, simplifiedFormula.numberOfVariables());
        assertEquals(4, simplifiedFormula.numberOfConstraints());

        // When literal 3 is falsified, the status of the formula remains unknown.
        var propagationAssumingNot3 = simplifiedFormula.assume(-3).propagate();
        assertTrue(propagationAssumingNot3.isUnknown());
        var propagatedLiteralsNot3 = propagationAssumingNot3.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot3.size());
        assertTrue(propagatedLiteralsNot3.contains(-3));
        var simplifiedFormulaNot3 = propagation.getSimplifiedFormula();
        assertEquals(3, simplifiedFormulaNot3.numberOfVariables());
        assertEquals(4, simplifiedFormulaNot3.numberOfConstraints());
        
        // Now, when literal 1 is falsified, the formula becomes unsatisfiable.
        var propagationAssumingNot1 = simplifiedFormulaNot3.assume(-1).propagate();
        assertTrue(propagationAssumingNot1.isUnsatisfiable());
    }

    /**
     * Tests that, when the whole search space is explored, satisfying and falsifying
     * assignments are detected as such on the formula {@code example-1.cnf}.
     */
    @Test
    @DisplayName("Exhaustive exploration on 'example-1.cnf' detects satsifying and falsifying assignments.")
    public void testExploreAllAssignments1() {
        var formula = readCnf("example-1.cnf");

        // At first, there is nothing to propagate.
        var propagate0 = formula.propagate();
        assertTrue(propagate0.isUnknown());
        assertTrue(propagate0.getPropagatedLiterals().isEmpty());
        var formula0 = propagate0.getSimplifiedFormula();
        assertEquals(4, formula0.numberOfVariables());
        assertEquals(4, formula0.numberOfConstraints());
        
        // Assuming the literal -1.
        var propagateNot1 = formula0.assume(-1).propagate();
        assertTrue(propagateNot1.isUnknown());
        var propagatedLiteralsNot1 = propagateNot1.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot1.size());
        assertTrue(propagatedLiteralsNot1.contains(-1));
        assertTrue(propagatedLiteralsNot1.contains(-4));
        var formulaNot1 = propagateNot1.getSimplifiedFormula();
        assertEquals(2, formulaNot1.numberOfVariables());
        assertTrue(formulaNot1.variables().contains(2));
        assertTrue(formulaNot1.variables().contains(3));
        assertEquals(2, formulaNot1.numberOfConstraints());

        // Assuming the literal -2 on the obtained formula.
        var propagateNot2 = formulaNot1.assume(-2).propagate();
        assertTrue(propagateNot2.isSatisfiable());
        var propagatedLiteralsNot2 = propagateNot2.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot2.size());
        assertTrue(propagatedLiteralsNot2.contains(-2));
        assertTrue(propagatedLiteralsNot2.contains(3));

        // Assuming now the literal 2.
        var propagate2 = formulaNot1.assume(2).propagate();
        assertTrue(propagate2.isSatisfiable());
        var propagatedLiterals2 = propagate2.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals2.size());
        assertTrue(propagatedLiterals2.contains(2));
        assertTrue(propagatedLiterals2.contains(3));

        // Assuming now the literal 1 on the original formula.
        var propagate1 = formula0.assume(1).propagate();
        assertTrue(propagate1.isUnknown());
        var propagatedLiterals1 = propagate1.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals1.size());
        assertTrue(propagatedLiterals1.contains(1));
        var formula1 = propagate1.getSimplifiedFormula();
        assertEquals(3, formula1.numberOfVariables());
        assertTrue(formula1.variables().contains(2));
        assertTrue(formula1.variables().contains(3));
        assertTrue(formula1.variables().contains(4));
        assertEquals(2, formula1.numberOfConstraints());

        // Assuming the literal -3 on the obtained formula.
        var propagateNot3 = formula1.assume(-3).propagate();
        assertTrue(propagateNot3.isUnknown());
        var propagatedLiteralsNot3 = propagateNot3.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot3.size());
        assertTrue(propagatedLiteralsNot3.contains(-3));
        var formulaNot3 = propagateNot3.getSimplifiedFormula();
        assertEquals(2, formulaNot3.numberOfVariables());
        assertTrue(formulaNot3.variables().contains(2));
        assertTrue(formulaNot3.variables().contains(4));
        assertEquals(2, formulaNot3.numberOfConstraints());

        // Assuming the literal -2 on the obtained formula.
        var propagateNot3Not2 = formulaNot3.assume(-2).propagate();
        assertTrue(propagateNot3Not2.isSatisfiable());
        var propagatedLiteralsNot3Not2 = propagateNot3Not2.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot3Not2.size());
        assertTrue(propagatedLiteralsNot3Not2.contains(-2));
        assertTrue(propagatedLiteralsNot3Not2.contains(-4));

        // Assuming now the literal 2.
        var propagateNot32 = formulaNot3.assume(2).propagate();
        assertTrue(propagateNot32.isSatisfiable());
        var propagatedLiteralsNot32 = propagateNot32.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot32.size());
        assertTrue(propagatedLiteralsNot32.contains(2));
        assertTrue(propagatedLiteralsNot32.contains(4));

        // Assuming now the literal 3.
        var propagate3 = formulaNot1.assume(3).propagate();
        assertTrue(propagate3.isSatisfiable());
        var propagatedLiterals3 = propagate3.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals3.size());
        assertTrue(propagatedLiterals3.contains(3));
    }

    /**
     * Tests that, when the whole search space is explored, satisfying and falsifying
     * assignments are detected as such on the formula {@code example-2.cnf}.
     */
    @Test
    @DisplayName("Exhaustive exploration on 'example-2.cnf' detects satsifying and falsifying assignments.")
    public void testExploreAllAssignments2() {
        var formula = readCnf("example-2.cnf");

        // At first, there is nothing to propagate.
        var propagate0 = formula.propagate();
        assertTrue(propagate0.isUnknown());
        assertTrue(propagate0.getPropagatedLiterals().isEmpty());
        var formula0 = propagate0.getSimplifiedFormula();
        assertEquals(3, formula0.numberOfVariables());
        assertEquals(3, formula0.numberOfConstraints());
        
        // Assuming the literal -1.
        var propagateNot1 = formula0.assume(-1).propagate();
        assertTrue(propagateNot1.isUnknown());
        var propagatedLiteralsNot1 = propagateNot1.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot1.size());
        assertTrue(propagatedLiteralsNot1.contains(-1));
        var formulaNot1 = propagateNot1.getSimplifiedFormula();
        assertEquals(2, formulaNot1.numberOfVariables());
        assertTrue(formulaNot1.variables().contains(2));
        assertTrue(formulaNot1.variables().contains(3));
        assertEquals(2, formulaNot1.numberOfConstraints());

        // Assuming the literal -3 on the obtained formula.
        var propagateNot3 = formulaNot1.assume(-3).propagate();
        assertTrue(propagateNot3.isSatisfiable());
        var propagatedLiteralsNot3 = propagateNot3.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot3.size());
        assertTrue(propagatedLiteralsNot3.contains(2));
        assertTrue(propagatedLiteralsNot3.contains(-3));

        // Assuming now the literal 3.
        var propagate3 = formulaNot1.assume(3).propagate();
        assertTrue(propagate3.isSatisfiable());
        var propagatedLiterals3 = propagate3.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals3.size());
        assertTrue(propagatedLiterals3.contains(-2));
        assertTrue(propagatedLiterals3.contains(3));

        // Assuming now the literal 1 on the original formula.
        var propagate1 = formula0.assume(1).propagate();
        assertTrue(propagate1.isSatisfiable());
        var propagatedLiterals1 = propagate1.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals1.size());
        assertTrue(propagatedLiterals1.contains(1));
        assertTrue(propagatedLiterals1.contains(-2));
    }
    
}

