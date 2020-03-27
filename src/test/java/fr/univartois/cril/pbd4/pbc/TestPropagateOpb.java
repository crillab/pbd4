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
 * The TestPropagateOpb is a JUnit test case testing Boolean Constraint Propagation
 * (BCP) on pseudo-Boolean formulae.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("BCP on pseudo-Boolean formulae behaves as expected.")
public final class TestPropagateOpb extends AbstractTestPseudoBooleanSolving {

    /**
     * Tests that, when variables are assumed in such a way that BCP identifies a model
     * of the formula, the partial assignment is detected as satisfying.
     */
    @Test
    @DisplayName("Partial satisfying assignments are detected as such.")
    public void testDetectSatisfyingAssignment() {
        var formula = readOpb("example-2.opb");

        // At first, there is nothing to propagate.
        var propagation = formula.propagate();
        assertTrue(propagation.isUnknown());
        assertTrue(propagation.getPropagatedLiterals().isEmpty());
        var simplifiedFormula = propagation.getSimplifiedFormula();
        assertEquals(10, simplifiedFormula.numberOfVariables());
        assertEquals(5, simplifiedFormula.numberOfConstraints());

        // When literal 1 is falsified, the status of the formula remains unknown.
        var propagationAssumingNot1 = simplifiedFormula.assume(-1).propagate();
        assertTrue(propagationAssumingNot1.isUnknown());
        var propagatedLiteralsNot1 = propagationAssumingNot1.getPropagatedLiterals();
        assertEquals(3, propagatedLiteralsNot1.size());
        assertTrue(propagatedLiteralsNot1.contains(-1));
        assertTrue(propagatedLiteralsNot1.contains(2));
        assertTrue(propagatedLiteralsNot1.contains(3));
        var simplifiedFormulaNot1 = propagationAssumingNot1.getSimplifiedFormula();
        assertEquals(3, simplifiedFormulaNot1.numberOfVariables());
        assertEquals(1, simplifiedFormulaNot1.numberOfConstraints());
        
        // When literal 5 is falsified, propagating satisfies the formula. 
        var propagationAssumingNot5 = simplifiedFormulaNot1.assume(-5).propagate();
        assertTrue(propagationAssumingNot5.isSatisfiable());
        var propagatedLiteralsNot5 = propagationAssumingNot5.getPropagatedLiterals();
        assertEquals(3, propagatedLiteralsNot5.size());
        assertTrue(propagatedLiteralsNot5.contains(-5));
        assertTrue(propagatedLiteralsNot5.contains(6));
        assertTrue(propagatedLiteralsNot5.contains(7));
        
        // When literal 6 is falsified, propagating satisfies the formula. 
        var propagationAssumingNot6 = simplifiedFormulaNot1.assume(-6).propagate();
        assertTrue(propagationAssumingNot6.isSatisfiable());
        var propagatedLiteralsNot6 = propagationAssumingNot6.getPropagatedLiterals();
        assertEquals(3, propagatedLiteralsNot6.size());
        assertTrue(propagatedLiteralsNot6.contains(5));
        assertTrue(propagatedLiteralsNot6.contains(-6));
        assertTrue(propagatedLiteralsNot6.contains(7));
        
        // When literal 7 is falsified, propagating satisfies the formula. 
        var propagationAssumingNot7 = simplifiedFormulaNot1.assume(-7).propagate();
        assertTrue(propagationAssumingNot7.isSatisfiable());
        var propagatedLiteralsNot7 = propagationAssumingNot7.getPropagatedLiterals();
        assertEquals(3, propagatedLiteralsNot7.size());
        assertTrue(propagatedLiteralsNot7.contains(5));
        assertTrue(propagatedLiteralsNot7.contains(6));
        assertTrue(propagatedLiteralsNot7.contains(-7));
    }

    /**
     * Tests that, when variables are assumed in such a way that BCP identifies a conflict,
     * the partial assignment is detected as falsifying.
     */
    @Test
    @DisplayName("Partial falsifying assignments are detected as such.")
    public void testDetectFalsifyingAssignment() {
        var formula = readOpb("example-2.opb");

        // At first, there is nothing to propagate.
        var propagation = formula.propagate();
        assertTrue(propagation.isUnknown());
        assertTrue(propagation.getPropagatedLiterals().isEmpty());
        var simplifiedFormula = propagation.getSimplifiedFormula();
        assertEquals(10, simplifiedFormula.numberOfVariables());
        assertEquals(5, simplifiedFormula.numberOfConstraints());

        // When literal 1 is satisfied, the formula becomes unsatisfiable.
        var propagationAssuming1 = simplifiedFormula.assume(1).propagate();
        assertTrue(propagationAssuming1.isUnsatisfiable());

        // When literal 2 is falsified, the formula becomes unsatisfiable.
        var propagationAssumingNot2 = simplifiedFormula.assume(-2).propagate();
        assertTrue(propagationAssumingNot2.isUnsatisfiable());

        // When literal 3 is falsified, the formula becomes unsatisfiable.
        var propagationAssumingNot3 = simplifiedFormula.assume(-3).propagate();
        assertTrue(propagationAssumingNot3.isUnsatisfiable());
    }
    
    /**
     * Tests that, if a variable does not appear in the formula after applying BCP,
     * then this variable is not reported as a variable of the formula anymore.
     */
    @Test
    @DisplayName("Variables that do not appear after propagation are not considered anymore.")
    public void testDetectVariablesNotAppearing() {
        var formula = readOpb("example-1.opb");

        // Literal 1 is propagated at level 0.
        var propagation = formula.propagate();
        assertTrue(propagation.isUnknown());
        var propagatedLiterals = propagation.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals.size());
        assertTrue(propagatedLiterals.contains(1));
        var simplifiedFormula = propagation.getSimplifiedFormula();
        assertEquals(3, simplifiedFormula.numberOfConstraints());

        // Now, some variables do not appear anymore.
        var remainingVariables = simplifiedFormula.variables();
        assertEquals(5, remainingVariables.size());
        assertTrue(remainingVariables.contains(2));
        assertTrue(remainingVariables.contains(3));
        assertTrue(remainingVariables.contains(5));
        assertTrue(remainingVariables.contains(6));
        assertTrue(remainingVariables.contains(7));
    }

    /**
     * Tests that, when the whole search space is explored, satisfying and falsifying
     * assignments are detected as such on the formula {@code example-1.opb}.
     */
    @Test
    @DisplayName("Exhaustive exploration on 'example-1.opb' detects satsifying and falsifying assignments.")
    public void testExploreAllAssignments1() {
        var formula = readOpb("example-1.opb");

        // Literal 1 is propagated at level 0.
        var propagate0 = formula.propagate();
        assertTrue(propagate0.isUnknown());
        var propagatedLiterals0 = propagate0.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals0.size());
        assertTrue(propagatedLiterals0.contains(1));
        var formula0 = propagate0.getSimplifiedFormula();
        assertEquals(5, formula0.numberOfVariables());
        assertTrue(formula0.variables().contains(2));
        assertTrue(formula0.variables().contains(3));
        assertTrue(formula0.variables().contains(5));
        assertTrue(formula0.variables().contains(6));
        assertTrue(formula0.variables().contains(7));
        assertEquals(3, formula0.numberOfConstraints());

        // Assuming the literal -2.
        var propagateNot2 = formula0.assume(-2).propagate();
        assertTrue(propagateNot2.isUnknown());
        var propagatedLiteralsNot2 = propagateNot2.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot2.size());
        assertTrue(propagatedLiteralsNot2.contains(-2));
        assertTrue(propagatedLiteralsNot2.contains(3));
        var formulaNot2 = propagateNot2.getSimplifiedFormula();
        assertEquals(3, formulaNot2.numberOfVariables());
        assertTrue(formulaNot2.variables().contains(5));
        assertTrue(formulaNot2.variables().contains(6));
        assertTrue(formulaNot2.variables().contains(7));
        assertEquals(2, formulaNot2.numberOfConstraints());

        // Assuming the literal -7.
        var propagateNot7 = formulaNot2.assume(-7).propagate();
        assertTrue(propagateNot7.isSatisfiable());
        var propagatedLiteralsNot7 = propagateNot7.getPropagatedLiterals();
        assertEquals(3, propagatedLiteralsNot7.size());
        assertTrue(propagatedLiteralsNot7.contains(-5));
        assertTrue(propagatedLiteralsNot7.contains(6));
        assertTrue(propagatedLiteralsNot7.contains(-7));

        // Assuming now the literal 7.
        var propagate7 = formulaNot2.assume(7).propagate();
        assertTrue(propagate7.isSatisfiable());
        var propagatedLiterals7 = propagate7.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals7.size());
        assertTrue(propagatedLiterals7.contains(7));

        // Assuming now the literal 2.
        var propagate2 = formula0.assume(2).propagate();
        assertTrue(propagate2.isUnknown());
        var propagatedLiterals2 = propagate2.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals2.size());
        assertTrue(propagatedLiterals2.contains(2));
        var formula2 = propagate2.getSimplifiedFormula();
        assertEquals(3, formula2.numberOfVariables());
        assertTrue(formula2.variables().contains(5));
        assertTrue(formula2.variables().contains(6));
        assertTrue(formula2.variables().contains(7));
        assertEquals(2, formula2.numberOfConstraints());

        // Assuming the literal -5.
        var propagateNot5 = formula2.assume(-5).propagate();
        assertTrue(propagateNot5.isSatisfiable());
        var propagatedLiteralsNot5 = propagateNot5.getPropagatedLiterals();
        assertEquals(3, propagatedLiteralsNot5.size());
        assertTrue(propagatedLiteralsNot5.contains(-5));
        assertTrue(propagatedLiteralsNot5.contains(6));
        assertTrue(propagatedLiteralsNot5.contains(7));

        // Assuming now the literal 5.
        var propagate5 = formula2.assume(5).propagate();
        assertTrue(propagate5.isSatisfiable());
        var propagatedLiterals5 = propagate5.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals5.size());
        assertTrue(propagatedLiterals5.contains(5));
        assertTrue(propagatedLiterals5.contains(7));
    }

    /**
     * Tests that, when the whole search space is explored, satisfying and falsifying
     * assignments are detected as such on the formula {@code example-2.opb}.
     */
    @Test
    @DisplayName("Exhaustive exploration on 'example-2.opb' detects satsifying and falsifying assignments.")
    public void testExploreAllAssignments2() {
        var formula = readOpb("example-2.opb");

        // At first, there is nothing to propagate.
        var propagate0 = formula.propagate();
        assertTrue(propagate0.isUnknown());
        assertTrue(propagate0.getPropagatedLiterals().isEmpty());
        var formula0 = propagate0.getSimplifiedFormula();
        assertEquals(10, formula0.numberOfVariables());
        assertEquals(5, formula0.numberOfConstraints());
        
        // Assuming the literal -2.
        var propagateNot2 = formula0.assume(-2).propagate();
        assertTrue(propagateNot2.isUnsatisfiable());
        
        // Assuming now the literal 2.
        var propagate2 = formula0.assume(2).propagate();
        assertTrue(propagate2.isUnknown());
        var propagatedLiterals2 = propagate2.getPropagatedLiterals();
        assertEquals(3, propagatedLiterals2.size());
        assertTrue(propagatedLiterals2.contains(-1));
        assertTrue(propagatedLiterals2.contains(2));
        assertTrue(propagatedLiterals2.contains(3));
        var formula2 = propagate2.getSimplifiedFormula();
        assertEquals(3, formula2.numberOfVariables());
        assertTrue(formula2.variables().contains(5));
        assertTrue(formula2.variables().contains(6));
        assertTrue(formula2.variables().contains(7));
        assertEquals(1, formula2.numberOfConstraints());

        // Assuming the literal -5.
        var propagateNot5 = formula2.assume(-5).propagate();
        assertTrue(propagateNot5.isSatisfiable());
        var propagatedLiteralsNot5 = propagateNot5.getPropagatedLiterals();
        assertEquals(3, propagatedLiteralsNot5.size());
        assertTrue(propagatedLiteralsNot5.contains(-5));
        assertTrue(propagatedLiteralsNot5.contains(6));
        assertTrue(propagatedLiteralsNot5.contains(7));

        // Assuming now the literal 5.
        var propagate5 = formula2.assume(5).propagate();
        assertTrue(propagate5.isUnknown());
        var propagatedLiterals5 = propagate5.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals5.size());
        assertTrue(propagatedLiterals5.contains(5));
        var formula5 = propagate5.getSimplifiedFormula();
        assertEquals(2, formula5.numberOfVariables());
        assertTrue(formula5.variables().contains(6));
        assertTrue(formula5.variables().contains(7));
        assertEquals(1, formula5.numberOfConstraints());
        
        // Assuming the literal -6.
        var propagateNot6 = formula5.assume(-6).propagate();
        assertTrue(propagateNot6.isSatisfiable());
        var propagatedLiteralsNot6 = propagateNot6.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot6.size());
        assertTrue(propagatedLiteralsNot6.contains(-6));
        assertTrue(propagatedLiteralsNot6.contains(7));

        // Assuming now the literal 6.
        var propagate6 = formula5.assume(6).propagate();
        assertTrue(propagate6.isSatisfiable());
        var propagatedLiterals6 = propagate6.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals6.size());
        assertTrue(propagatedLiterals6.contains(6));
    }

    /**
     * Tests that, when the whole search space is explored, satisfying and falsifying
     * assignments are detected as such on the formula {@code example-3.opb}.
     */
    @Test
    @DisplayName("Exhaustive exploration on 'example-3.opb' detects satsifying and falsifying assignments.")
    public void testExploreAllAssignments3() {
        var formula = readOpb("example-3.opb");

        // At first, there is nothing to propagate.
        var propagate0 = formula.propagate();
        assertTrue(propagate0.isUnknown());
        assertTrue(propagate0.getPropagatedLiterals().isEmpty());
        var formula0 = propagate0.getSimplifiedFormula();
        assertEquals(11, formula0.numberOfVariables());
        assertEquals(6, formula0.numberOfConstraints());
        
        // Assuming the literal -3.
        var propagateNot3 = formula0.assume(-3).propagate();
        assertTrue(propagateNot3.isSatisfiable());
        var propagatedLiteralsNot3 = propagateNot3.getPropagatedLiterals();
        assertEquals(7, propagatedLiteralsNot3.size());
        assertTrue(propagatedLiteralsNot3.contains(-1));
        assertTrue(propagatedLiteralsNot3.contains(-2));
        assertTrue(propagatedLiteralsNot3.contains(-3));
        assertTrue(propagatedLiteralsNot3.contains(4));
        assertTrue(propagatedLiteralsNot3.contains(-5));
        assertTrue(propagatedLiteralsNot3.contains(8));
        assertTrue(propagatedLiteralsNot3.contains(9));
        
        // Assuming now the literal 3.
        var propagate3 = formula0.assume(3).propagate();
        assertTrue(propagate3.isUnknown());
        var propagatedLiterals3 = propagate3.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals3.size());
        assertTrue(propagatedLiterals3.contains(3));
        var formula3 = propagate3.getSimplifiedFormula();
        assertEquals(10, formula3.numberOfVariables());
        assertTrue(formula3.variables().contains(1));
        assertTrue(formula3.variables().contains(2));
        assertTrue(formula3.variables().contains(4));
        assertTrue(formula3.variables().contains(5));
        assertTrue(formula3.variables().contains(6));
        assertTrue(formula3.variables().contains(7));
        assertTrue(formula3.variables().contains(8));
        assertTrue(formula3.variables().contains(9));
        assertTrue(formula3.variables().contains(10));
        assertTrue(formula3.variables().contains(11));
        assertEquals(5, formula3.numberOfConstraints());
        
        // Assuming the literal -2.
        var propagateNot2 = formula3.assume(-2).propagate();
        assertTrue(propagateNot2.isUnknown());
        var propagatedLiteralsNot2 = propagateNot2.getPropagatedLiterals();
        assertEquals(3, propagatedLiteralsNot2.size());
        assertTrue(propagatedLiteralsNot2.contains(-2));
        assertTrue(propagatedLiteralsNot2.contains(4));
        assertTrue(propagatedLiteralsNot2.contains(-5));
        var formulaNot2 = propagateNot2.getSimplifiedFormula();
        assertEquals(4, formulaNot2.numberOfVariables());
        assertTrue(formulaNot2.variables().contains(8));
        assertTrue(formulaNot2.variables().contains(9));
        assertTrue(formulaNot2.variables().contains(10));
        assertTrue(formulaNot2.variables().contains(11));
        assertEquals(2, formulaNot2.numberOfConstraints());
        
        // Assuming the literal -8.
        var propagateNot8 = formulaNot2.assume(-8).propagate();
        assertTrue(propagateNot8.isUnknown());
        var propagatedLiteralsNot8 = propagateNot8.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot8.size());
        assertTrue(propagatedLiteralsNot8.contains(-8));
        assertTrue(propagatedLiteralsNot8.contains(9));
        var formulaNot8 = propagateNot8.getSimplifiedFormula();
        assertEquals(2, formulaNot8.numberOfVariables());
        assertTrue(formulaNot8.variables().contains(10));
        assertTrue(formulaNot8.variables().contains(11));
        assertEquals(1, formulaNot8.numberOfConstraints());
        
        // Assuming the literal -10.
        var propagateNot8Not10 = formulaNot8.assume(-10).propagate();
        assertTrue(propagateNot8Not10.isSatisfiable());
        var propagatedLiteralsNot8Not10 = propagateNot8Not10.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot8Not10.size());
        assertTrue(propagatedLiteralsNot8Not10.contains(-10));
        assertTrue(propagatedLiteralsNot8Not10.contains(-11));
        
        // Assuming now the literal 10.
        var propagateNot810 = formulaNot8.assume(10).propagate();
        assertTrue(propagateNot810.isSatisfiable());
        var propagatedLiteralsNot810 = propagateNot810.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot810.size());
        assertTrue(propagatedLiteralsNot810.contains(10));
        
        // Assuming now the literal 8.
        var propagate8 = formulaNot2.assume(8).propagate();
        assertTrue(propagate8.isUnknown());
        var propagatedLiterals8 = propagate8.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals8.size());
        assertTrue(propagatedLiterals8.contains(8));
        var formula8 = propagate8.getSimplifiedFormula();
        assertEquals(2, formula8.numberOfVariables());
        assertTrue(formula8.variables().contains(10));
        assertTrue(formula8.variables().contains(11));
        assertEquals(1, formula8.numberOfConstraints());
        
        // Assuming the literal -11.
        var propagate8Not11 = formula8.assume(-11).propagate();
        assertTrue(propagate8Not11.isSatisfiable());
        var propagatedLiterals8Not11 = propagate8Not11.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals8Not11.size());
        assertTrue(propagatedLiterals8Not11.contains(-11));
        
        // Assuming now the literal 11.
        var propagate811 = formula8.assume(11).propagate();
        assertTrue(propagate811.isSatisfiable());
        var propagatedLiterals811 = propagate811.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals811.size());
        assertTrue(propagatedLiterals811.contains(10));
        assertTrue(propagatedLiterals811.contains(11));
        
        // Assuming now the literal 2.
        var propagate2 = formula3.assume(2).propagate();
        assertTrue(propagate2.isUnknown());
        var propagatedLiterals2 = propagate2.getPropagatedLiterals();
        assertEquals(4, propagatedLiterals2.size());
        assertTrue(propagatedLiterals2.contains(1));
        assertTrue(propagatedLiterals2.contains(2));
        assertTrue(propagatedLiterals2.contains(-6));
        assertTrue(propagatedLiterals2.contains(7));
        var formula2 = propagate2.getSimplifiedFormula();
        assertEquals(6, formula2.numberOfVariables());
        assertTrue(formula2.variables().contains(4));
        assertTrue(formula2.variables().contains(5));
        assertTrue(formula2.variables().contains(8));
        assertTrue(formula2.variables().contains(9));
        assertTrue(formula2.variables().contains(10));
        assertTrue(formula2.variables().contains(11));
        assertEquals(3, formula2.numberOfConstraints());
        
        // Assuming the literal -9.
        var propagateNot9 = formula2.assume(-9).propagate();
        assertTrue(propagateNot9.isUnknown());
        var propagatedLiteralsNot9 = propagateNot9.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot9.size());
        assertTrue(propagatedLiteralsNot9.contains(8));
        assertTrue(propagatedLiteralsNot9.contains(-9));
        var formulaNot9 = propagateNot9.getSimplifiedFormula();
        assertEquals(4, formulaNot9.numberOfVariables());
        assertTrue(formulaNot9.variables().contains(4));
        assertTrue(formulaNot9.variables().contains(5));
        assertTrue(formulaNot9.variables().contains(10));
        assertTrue(formulaNot9.variables().contains(11));
        assertEquals(2, formulaNot9.numberOfConstraints());
        
        // Assuming the literal -4.
        var propagateNot4 = formulaNot9.assume(-4).propagate();
        assertTrue(propagateNot4.isUnknown());
        var propagatedLiteralsNot4 = propagateNot4.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot4.size());
        assertTrue(propagatedLiteralsNot4.contains(-4));
        assertTrue(propagatedLiteralsNot4.contains(-5));
        var formulaNot4 = propagateNot4.getSimplifiedFormula();
        assertEquals(2, formulaNot4.numberOfVariables());
        assertTrue(formulaNot4.variables().contains(10));
        assertTrue(formulaNot4.variables().contains(11));
        assertEquals(1, formulaNot4.numberOfConstraints());
        
        // Assuming the literal -11.
        var propagateNot4Not11 = formulaNot4.assume(-11).propagate();
        assertTrue(propagateNot4Not11.isSatisfiable());
        var propagatedLiteralsNot4Not11 = propagateNot4Not11.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot4Not11.size());
        assertTrue(propagatedLiteralsNot4Not11.contains(-11));
        
        // Assuming now the literal 11.
        var propagateNot411 = formulaNot4.assume(11).propagate();
        assertTrue(propagateNot411.isSatisfiable());
        var propagatedLiteralsNot411 = propagateNot411.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot411.size());
        assertTrue(propagatedLiteralsNot411.contains(10));
        assertTrue(propagatedLiteralsNot411.contains(11));
        
        // Assuming the literal 4.
        var propagate4 = formulaNot9.assume(4).propagate();
        assertTrue(propagate4.isUnknown());
        var propagatedLiterals4 = propagate4.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals4.size());
        assertTrue(propagatedLiterals4.contains(4));
        var formula4 = propagate4.getSimplifiedFormula();
        assertEquals(2, formula4.numberOfVariables());
        assertTrue(formula4.variables().contains(10));
        assertTrue(formula4.variables().contains(11));
        assertEquals(1, formula4.numberOfConstraints());
        
        // Assuming the literal -11.
        var propagate4Not11 = formula4.assume(-11).propagate();
        assertTrue(propagate4Not11.isSatisfiable());
        var propagatedLiterals4Not11 = propagate4Not11.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals4Not11.size());
        assertTrue(propagatedLiterals4Not11.contains(-11));
        
        // Assuming now the literal 11.
        var propagate411 = formula4.assume(11).propagate();
        assertTrue(propagate411.isSatisfiable());
        var propagatedLiterals411 = propagate411.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals411.size());
        assertTrue(propagatedLiterals411.contains(10));
        assertTrue(propagatedLiterals411.contains(11));
        
        // Assuming the literal 9.
        var propagate9 = formula2.assume(9).propagate();
        assertTrue(propagate9.isUnknown());
        var propagatedLiterals9 = propagate9.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals9.size());
        assertTrue(propagatedLiterals9.contains(9));
        var formula9 = propagate9.getSimplifiedFormula();
        assertEquals(4, formula9.numberOfVariables());
        assertTrue(formula9.variables().contains(4));
        assertTrue(formula9.variables().contains(5));
        assertTrue(formula9.variables().contains(10));
        assertTrue(formula9.variables().contains(11));
        assertEquals(2, formula9.numberOfConstraints());
        
        // Assuming the literal -5.
        var propagateNot5 = formula9.assume(-5).propagate();
        assertTrue(propagateNot5.isUnknown());
        var propagatedLiteralsNot5 = propagateNot5.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot5.size());
        assertTrue(propagatedLiteralsNot5.contains(-5));
        var formulaNot5 = propagateNot5.getSimplifiedFormula();
        assertEquals(2, formulaNot5.numberOfVariables());
        assertTrue(formulaNot5.variables().contains(10));
        assertTrue(formulaNot5.variables().contains(11));
        assertEquals(1, formulaNot5.numberOfConstraints());
        
        // Assuming the literal -10.
        var propagateNot5Not10 = formulaNot5.assume(-10).propagate();
        assertTrue(propagateNot5Not10.isSatisfiable());
        var propagatedLiteralsNot5Not10 = propagateNot5Not10.getPropagatedLiterals();
        assertEquals(2, propagatedLiteralsNot5Not10.size());
        assertTrue(propagatedLiteralsNot5Not10.contains(-10));
        assertTrue(propagatedLiteralsNot5Not10.contains(-11));
        
        // Assuming now the literal 10.
        var propagateNot510 = formulaNot5.assume(10).propagate();
        assertTrue(propagateNot510.isSatisfiable());
        var propagatedLiteralsNot510 = propagateNot510.getPropagatedLiterals();
        assertEquals(1, propagatedLiteralsNot510.size());
        assertTrue(propagatedLiteralsNot510.contains(10));
        
        // Assuming the literal 5.
        var propagate5 = formula9.assume(5).propagate();
        assertTrue(propagate5.isUnknown());
        var propagatedLiterals5 = propagate5.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals5.size());
        assertTrue(propagatedLiterals5.contains(4));
        assertTrue(propagatedLiterals5.contains(5));
        var formula5 = propagate5.getSimplifiedFormula();
        assertEquals(2, formula5.numberOfVariables());
        assertTrue(formula5.variables().contains(10));
        assertTrue(formula5.variables().contains(11));
        assertEquals(1, formula5.numberOfConstraints());

        // Assuming the literal -10.
        var propagate5Not10 = formula5.assume(-10).propagate();
        assertTrue(propagate5Not10.isSatisfiable());
        var propagatedLiterals5Not10 = propagate5Not10.getPropagatedLiterals();
        assertEquals(2, propagatedLiterals5Not10.size());
        assertTrue(propagatedLiterals5Not10.contains(-10));
        assertTrue(propagatedLiterals5Not10.contains(-11));
        
        // Assuming now the literal 10.
        var propagate510 = formula5.assume(10).propagate();
        assertTrue(propagate510.isSatisfiable());
        var propagatedLiterals510 = propagate510.getPropagatedLiterals();
        assertEquals(1, propagatedLiterals510.size());
        assertTrue(propagatedLiterals510.contains(10));
    }
    
}
