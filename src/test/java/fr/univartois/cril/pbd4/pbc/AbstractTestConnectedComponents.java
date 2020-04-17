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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The AbstractTestConnectedComponents is the parent class for the test cases for the
 * computation of connected components.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public abstract class AbstractTestConnectedComponents extends AbstractTestPseudoBooleanSolving {

    /**
     * Gives the list of the original formulae considered by this test case.
     *
     * @return The list of formulae.
     */
    protected abstract List<PseudoBooleanFormula> allOriginalFormulae();

    /**
     * Tests that it is not possible to compute the connected components of
     * an original formula.
     */
    @Test
    @DisplayName("Computation of connected components cannot be done on an original formula.")
    public void testCannotComputeOnOriginalFormula() {
        for (var formula : allOriginalFormulae()) {
            assertEquals(OriginalPseudoBooleanFormula.class, formula.getClass());
            assertThrows(UnsupportedOperationException.class, formula::connectedComponents);
        }
    }

    /**
     * Computes the connected components of the given original formula, and returns the
     * {@link List} of these components.
     * As original formulae do not support computation of connected components, BCP is applied
     * on the formula before actually computing its connected components.
     * 
     * @param formula The formula to compute the connected components of.
     *
     * @return The list of connected components.
     */
    protected static List<PseudoBooleanFormula> connectedComponentsOf(OriginalPseudoBooleanFormula formula) {
        return connectedComponentsOf(formula.propagate().getSimplifiedFormula());
    }

    /**
     * Computes the connected components of the given formula, and returns the
     * {@link List} of these components.
     * 
     * @param formula The formula to compute the connected components of.
     *
     * @return The list of connected components.
     */
    protected static List<PseudoBooleanFormula> connectedComponentsOf(PseudoBooleanFormula formula) {
        return (List<PseudoBooleanFormula>) formula.connectedComponents();
    }

    /**
     * Applies Boolean Constraint Propagation (BCP) to the given formula.
     *
     * @param formula The formula on which to apply BCP.
     * @param propagatedLiterals The literals expected to be propagated.
     *
     * @return The (simplified) formula obtained by applying BCP to the given formula.
     */
    protected static PseudoBooleanFormula bcp(PseudoBooleanFormula formula, int... propagatedLiterals) {
        var propagation = formula.propagate();
        
        // Checking the propagated literals.
        var effectivePropagatedLiterals = propagation.getPropagatedLiterals();
        assertEquals(propagatedLiterals.length, effectivePropagatedLiterals.size());
        for (int literal : propagatedLiterals) {
            assertTrue(effectivePropagatedLiterals.contains(literal));
        }

        return propagation.isUnknown() ? propagation.getSimplifiedFormula() : null;
    }

}
