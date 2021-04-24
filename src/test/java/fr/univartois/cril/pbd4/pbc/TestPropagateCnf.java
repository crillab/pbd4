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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The TestPropagateCnf is a JUnit test case testing Boolean Constraint Propagation
 * (BCP) on formulae in Conjunctive Normal Form (CNF).
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
     * Tests that, if a variable does not appear in the formula after applying BCP,
     * then this variable is not reported as a variable of the formula anymore.
     */
    @Test
    @DisplayName("Variables that do not appear after propagation are not considered anymore.")
    public void testDetectVariablesNotAppearing() {
        var formula = readCnf("example-3.cnf");

        // At first, there is nothing to propagate.
        var propagation = formula.propagate();
        assertTrue(propagation.isUnknown());
        assertTrue(propagation.getPropagatedLiterals().isEmpty());
        var simplifiedFormula = propagation.getSimplifiedFormula();
        assertEquals(11, simplifiedFormula.numberOfVariables());
        assertEquals(6, simplifiedFormula.numberOfConstraints());

        // When satisfying literal -1, literal -2 is propagated.
        var propagationAssumingNot1 = simplifiedFormula.assume(-1).propagate();
        assertTrue(propagationAssumingNot1.isUnknown());
        var propagatedLiteralsNot1 = propagationAssumingNot1.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot1.size());
        assertTrue(propagatedLiteralsNot1.contains(-1));
        assertTrue(propagatedLiteralsNot1.contains(-2));

        // Moreover, some variables do not appear anymore.
        var remainingVariables = propagationAssumingNot1.getSimplifiedFormula().variables();
        assertEquals(7, remainingVariables.size());
        assertTrue(remainingVariables.contains(3));
        assertTrue(remainingVariables.contains(4));
        assertTrue(remainingVariables.contains(5));
        assertTrue(remainingVariables.contains(8));
        assertTrue(remainingVariables.contains(9));
        assertTrue(remainingVariables.contains(10));
        assertTrue(remainingVariables.contains(11));
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

    /**
     * Tests that, when the whole search space is explored, satisfying and falsifying
     * assignments are detected as such on the formula {@code example-3.cnf}.
     */
    @Test
    @DisplayName("Exhaustive exploration on 'example-3.cnf' detects satsifying and falsifying assignments.")
    public void testExploreAllAssignments3() {
        var formula = readCnf("example-3.cnf");

        // At first, there is nothing to propagate.
        var propagate0 = formula.propagate();
        assertTrue(propagate0.isUnknown());
        assertTrue(propagate0.getPropagatedLiterals().isEmpty());
        var formula0 = propagate0.getSimplifiedFormula();
        assertEquals(11, formula0.numberOfVariables());
        assertEquals(6, formula0.numberOfConstraints());
        
        // Assuming the literal -2.
        var propagateNot2 = formula0.assume(-2).propagate();
        assertTrue(propagateNot2.isUnknown());
        var propagatedLiteralsNot2 = propagateNot2.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot2.size());
        assertTrue(propagatedLiteralsNot2.contains(-2));
        var formulaNot2 = propagateNot2.getSimplifiedFormula();
        assertEquals(8, formulaNot2.numberOfVariables());
        assertTrue(formulaNot2.variables().contains(1));
        assertTrue(formulaNot2.variables().contains(3));
        assertTrue(formulaNot2.variables().contains(4));
        assertTrue(formulaNot2.variables().contains(5));
        assertTrue(formulaNot2.variables().contains(8));
        assertTrue(formulaNot2.variables().contains(9));
        assertTrue(formulaNot2.variables().contains(10));
        assertTrue(formulaNot2.variables().contains(11));
        assertEquals(4, formulaNot2.numberOfConstraints());
        
        // Assuming the literal -3.
        var propagateNot3 = formulaNot2.assume(-3).propagate();
        assertTrue(propagateNot3.isUnknown());
        var propagatedLiteralsNot3 = propagateNot3.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot3.size());
        assertTrue(propagatedLiteralsNot3.contains(-1));
        assertTrue(propagatedLiteralsNot3.contains(-3));
        var formulaNot3 = propagateNot3.getSimplifiedFormula();
        assertEquals(4, formulaNot3.numberOfVariables());
        assertTrue(formulaNot3.variables().contains(4));
        assertTrue(formulaNot3.variables().contains(5));
        assertTrue(formulaNot3.variables().contains(8));
        assertTrue(formulaNot3.variables().contains(9));
        assertEquals(2, formulaNot3.numberOfConstraints());
        
        // Assuming the literal -8.
        var propagateNot8 = formulaNot3.assume(-8).propagate();
        assertTrue(propagateNot8.isUnknown());
        var propagatedLiteralsNot8 = propagateNot8.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot8.size());
        assertTrue(propagatedLiteralsNot8.contains(-8));
        assertTrue(propagatedLiteralsNot8.contains(9));
        var formulaNot8 = propagateNot8.getSimplifiedFormula();
        assertEquals(2, formulaNot8.numberOfVariables());
        assertTrue(formulaNot8.variables().contains(4));
        assertTrue(formulaNot8.variables().contains(5));
        assertEquals(1, formulaNot8.numberOfConstraints());
        
        // Assuming the literal -4.
        var propagateNot8Not4 = formulaNot8.assume(-4).propagate();
        assertTrue(propagateNot8Not4.isSatisfiable());
        var propagatedLiteralsNot8Not4 = propagateNot8Not4.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot8Not4.size());
        assertTrue(propagatedLiteralsNot8Not4.contains(-4));
        assertTrue(propagatedLiteralsNot8Not4.contains(-5));
        
        // Assuming now the literal 4.
        var propagateNot84 = formulaNot8.assume(4).propagate();
        assertTrue(propagateNot84.isSatisfiable());
        var propagatedLiteralsNot84 = propagateNot84.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot84.size());
        assertTrue(propagatedLiteralsNot84.contains(4));
        
        // Assuming now the literal 8.
        var propagate8 = formulaNot3.assume(8).propagate();
        assertTrue(propagate8.isUnknown());
        var propagatedLiterals8 = propagate8.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals8.size());
        assertTrue(propagatedLiterals8.contains(8));
        var formula8 = propagate8.getSimplifiedFormula();
        assertEquals(2, formula8.numberOfVariables());
        assertTrue(formula8.variables().contains(4));
        assertTrue(formula8.variables().contains(5));
        assertEquals(1, formula8.numberOfConstraints());
        
        // Assuming the literal -5.
        var propagate8Not5 = formula8.assume(-5).propagate();
        assertTrue(propagate8Not5.isSatisfiable());
        var propagatedLiterals8Not5 = propagate8Not5.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals8Not5.size());
        assertTrue(propagatedLiterals8Not5.contains(-5));
        
        // Assuming now the literal 5.
        var propagate85 = formula8.assume(5).propagate();
        assertTrue(propagate85.isSatisfiable());
        var propagatedLiterals85 = propagate85.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals85.size());
        assertTrue(propagatedLiterals85.contains(4));
        assertTrue(propagatedLiterals85.contains(5));
        
        // Assuming now the literal 3.
        var propagate3 = formulaNot2.assume(3).propagate();
        assertTrue(propagate3.isUnknown());
        var propagatedLiterals3 = propagate3.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals3.size());
        assertTrue(propagatedLiterals3.contains(3));
        var formula3 = propagate3.getSimplifiedFormula();
        assertEquals(4, formula3.numberOfVariables());
        assertTrue(formula3.variables().contains(4));
        assertTrue(formula3.variables().contains(5));
        assertTrue(formula3.variables().contains(10));
        assertTrue(formula3.variables().contains(11));
        assertEquals(2, formula3.numberOfConstraints());
        
        // Assuming the literal -10.
        var propagateNot10 = formula3.assume(-10).propagate();
        assertTrue(propagateNot10.isUnknown());
        var propagatedLiteralsNot10 = propagateNot10.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot10.size());
        assertTrue(propagatedLiteralsNot10.contains(-10));
        assertTrue(propagatedLiteralsNot10.contains(-11));
        var formulaNot10 = propagateNot10.getSimplifiedFormula();
        assertEquals(2, formulaNot10.numberOfVariables());
        assertTrue(formulaNot10.variables().contains(4));
        assertTrue(formulaNot10.variables().contains(5));
        assertEquals(1, formulaNot10.numberOfConstraints());
        
        // Assuming the literal -4.
        var propagateNot10Not4 = formulaNot10.assume(-4).propagate();
        assertTrue(propagateNot10Not4.isSatisfiable());
        var propagatedLiteralsNot10Not4 = propagateNot10Not4.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot10Not4.size());
        assertTrue(propagatedLiteralsNot10Not4.contains(-4));
        assertTrue(propagatedLiteralsNot10Not4.contains(-5));
        
        // Assuming now the literal 4.
        var propagateNot104 = formulaNot10.assume(4).propagate();
        assertTrue(propagateNot104.isSatisfiable());
        var propagatedLiteralsNot104 = propagateNot104.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot104.size());
        assertTrue(propagatedLiteralsNot104.contains(4));
        
        // Assuming now the literal 10.
        var propagate10 = formula3.assume(10).propagate();
        assertTrue(propagate10.isUnknown());
        var propagatedLiterals10 = propagate10.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals10.size());
        assertTrue(propagatedLiterals10.contains(10));
        var formula10 = propagate10.getSimplifiedFormula();
        assertEquals(2, formula10.numberOfVariables());
        assertTrue(formula10.variables().contains(4));
        assertTrue(formula10.variables().contains(5));
        assertEquals(1, formula10.numberOfConstraints());
        
        // Assuming the literal -5.
        var propagate10Not5 = formula10.assume(-5).propagate();
        assertTrue(propagate10Not5.isSatisfiable());
        var propagatedLiterals10Not5 = propagate10Not5.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals10Not5.size());
        assertTrue(propagatedLiterals10Not5.contains(-5));
        
        // Assuming now the literal 5.
        var propagate105 = formula10.assume(5).propagate();
        assertTrue(propagate105.isSatisfiable());
        var propagatedLiterals105 = propagate105.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals105.size());
        assertTrue(propagatedLiterals105.contains(4));
        assertTrue(propagatedLiterals105.contains(5));
        
        // Assuming now the literal 2.
        var propagate2 = formula0.assume(2).propagate();
        assertTrue(propagate2.isUnknown());
        var propagatedLiterals2 = propagate2.getPropagatedLiterals();
        assertEquals(3, propagatedLiterals2.size());
        assertTrue(propagatedLiterals2.contains(1));
        assertTrue(propagatedLiterals2.contains(2));
        assertTrue(propagatedLiterals2.contains(3));
        var formula2 = propagate2.getSimplifiedFormula();
        assertEquals(4, formula2.numberOfVariables());
        assertTrue(formula2.variables().contains(6));
        assertTrue(formula2.variables().contains(7));
        assertTrue(formula2.variables().contains(10));
        assertTrue(formula2.variables().contains(11));
        assertEquals(2, formula2.numberOfConstraints());
        
        // Assuming the literal -11.
        var propagateNot11 = formula2.assume(-11).propagate();
        assertTrue(propagateNot11.isUnknown());
        var propagatedLiteralsNot11 = propagateNot11.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot11.size());
        assertTrue(propagatedLiteralsNot11.contains(-11));
        var formulaNot11 = propagateNot11.getSimplifiedFormula();
        assertEquals(2, formulaNot11.numberOfVariables());
        assertTrue(formulaNot11.variables().contains(6));
        assertTrue(formulaNot11.variables().contains(7));
        assertEquals(1, formulaNot11.numberOfConstraints());
        
        // Assuming the literal -6.
        var propagateNot6 = formulaNot11.assume(-6).propagate();
        assertTrue(propagateNot6.isSatisfiable());
        var propagatedLiteralsNot6 = propagateNot6.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot6.size());
        assertTrue(propagatedLiteralsNot6.contains(-6));
        
        // Assuming now the literal 6.
        var propagate6 = formulaNot11.assume(6).propagate();
        assertTrue(propagate6.isSatisfiable());
        var propagatedLiterals6 = propagate6.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals6.size());
        assertTrue(propagatedLiterals6.contains(6));
        assertTrue(propagatedLiterals6.contains(7));
        
        // Assuming now the literal 11.
        var propagate11 = formula2.assume(11).propagate();
        assertTrue(propagate11.isUnknown());
        var propagatedLiterals11 = propagate11.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals11.size());
        assertTrue(propagatedLiterals11.contains(10));
        assertTrue(propagatedLiterals11.contains(11));
        var formula11 = propagate11.getSimplifiedFormula();
        assertEquals(2, formula11.numberOfVariables());
        assertTrue(formula11.variables().contains(6));
        assertTrue(formula11.variables().contains(7));
        assertEquals(1, formula11.numberOfConstraints());
        
        // Assuming the literal -7.
        var propagateNot7 = formula11.assume(-7).propagate();
        assertTrue(propagateNot7.isSatisfiable());
        var propagatedLiteralsNot7 = propagateNot7.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot7.size());
        assertTrue(propagatedLiteralsNot7.contains(-6));
        assertTrue(propagatedLiteralsNot7.contains(-7));
        
        // Assuming now the literal 7.
        var propagate7 = formula11.assume(7).propagate();
        assertTrue(propagate7.isSatisfiable());
        var propagatedLiterals7 = propagate7.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals7.size());
        assertTrue(propagatedLiterals7.contains(7));
    }
    
}

