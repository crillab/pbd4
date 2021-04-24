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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The TestOpbDualHypergraphPartitioning is a JUnit test case for testing the partitioning
 * of the dual hypergraph of a pseudo-Boolean formula, and especially the computation of a cutset.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("A cutset of the dual hypergraph of a pseudo-Boolean formula is properly computed.")
public final class TestOpbDualHypergraphPartitioning extends AbstractTestDualHypergraphPartitioning {

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.pbc.AbstractTestDualHypergraphPartitioning#
     * allOriginalFormulae()
     */
    @Override
    protected List<PseudoBooleanFormula> allOriginalFormulae() {
        return List.of(readOpb("example-1.opb"), readOpb("example-2.opb"), readOpb("example-3.opb"));
    }

    /**
     * Tests that a cutset of the formula {@code example-1.opb} is properly computed.
     */
    @Test
    @DisplayName("A cutset of the formula 'example-1.opb' is properly computed.")
    public void testExample1() {
        // Computing a cutset of the original formula (literal 1 is propagated at level 0).
        var formula = bcp(readOpb("example-1.opb"));
        assertHasCutset(formula, 2);

        // Assuming the literal 2, and computing a cutset of its connected components.
        var formula2 = formula.assume(2);
        var components2 = connectedComponentsOf(formula2);
        assertEquals(1, components2.size());
        assertHasCutset(components2.get(0), 5, 7);

        // Computing a cutset of the connected components of the formula after BCP.
        var formula2BCP = bcp(formula2);
        var components2BCP = connectedComponentsOf(formula2BCP);
        assertEquals(1, components2BCP.size());
        assertHasCutset(components2BCP.get(0), 5, 7);

        // Assuming now the literal -2, and computing a cutset of its connected components.
        var formulaNot2 = formula.assume(-2);
        var componentsNot2 = connectedComponentsOf(formulaNot2);
        assertEquals(2, componentsNot2.size());
        assertHasCutset(componentsNot2.get(0), 3);
        assertHasCutset(componentsNot2.get(1), 5, 7);

        // Computing a cutset of the connected components of the formula after BCP.
        var formulaNot2BCP = bcp(formulaNot2);
        var componentsNot2BCP = connectedComponentsOf(formulaNot2BCP);
        assertEquals(1, componentsNot2BCP.size());
        assertHasCutset(componentsNot2BCP.get(0), 5, 7);
    }

    /**
     * Tests that a cutset of the formula {@code example-2.opb} is properly computed.
     */
    @Test
    @DisplayName("A cutset of the formula 'example-2.opb' is properly computed.")
    public void testExample2() {
        // Computing a cutset of the original formula.
        var formula = bcp(readOpb("example-2.opb"));
        assertHasCutset(formula, 2, 3);

        // Assuming the literal 2, and computing a cutset of its connected components.
        var formula2 = formula.assume(2);
        var components2 = connectedComponentsOf(formula2);
        assertEquals(2, components2.size());
        assertHasCutset(components2.get(0), 3);
        assertHasCutset(components2.get(1), 5, 6, 7);

        // Computing a cutset of the connected components of the formula after BCP.
        var formula2BCP = bcp(formula2);
        var components2BCP = connectedComponentsOf(formula2BCP);
        assertEquals(1, components2BCP.size());
        assertHasCutset(components2BCP.get(0), 5, 6, 7);

        // Assuming now the literal -2, and computing a cutset of its connected components.
        var formulaNot2 = formula.assume(-2);
        var componentsNot2 = connectedComponentsOf(formulaNot2);
        assertEquals(1, componentsNot2.size());
        assertHasCutset(componentsNot2.get(0), 3);
        
        // Satisfying literal -2 is actually conflicting.
        assertTrue(formulaNot2.propagate().isUnsatisfiable());

        // Assuming the literal 3, and computing a cutset of its connected components.
        var formula3 = formula.assume(3);
        var components3 = connectedComponentsOf(formula3);
        assertEquals(1, components3.size());
        assertHasCutset(components3.get(0), 2);

        // Computing a cutset of the connected components of the formula after BCP.
        var formula3BCP = bcp(formula3);
        var components3BCP = connectedComponentsOf(formula3BCP);
        assertEquals(1, components3BCP.size());
        assertHasCutset(components3BCP.get(0), 5, 6, 7);

        // Assuming now the literal -3, and computing a cutset of its connected components.
        var formulaNot3 = formula.assume(-3);
        var componentsNot3 = connectedComponentsOf(formulaNot3);
        assertEquals(2, componentsNot3.size());
        assertHasCutset(componentsNot3.get(0), 2);
        assertHasCutset(componentsNot3.get(1), 8, 9, 10);
        
        // Satisfying literal -3 is actually conflicting.
        assertTrue(formulaNot3.propagate().isUnsatisfiable());
    }

    /**
     * Tests that a cutset of the formula {@code example-3.opb} is properly computed.
     */
    @Test
    @DisplayName("A cutset of the formula 'example-3.opb' is properly computed.")
    public void testExample3() {
        // Computing a cutset of the original formula.
        var formula = bcp(readOpb("example-3.opb"));
        assertHasCutset(formula, 1);

        // Assuming the literal 1, and computing a cutset of its connected components.
        var formula1 = formula.assume(1);
        var components1 = connectedComponentsOf(formula1);
        assertEquals(2, components1.size());
        assertHasCutset(components1.get(0), 2);
        assertHasCutset(components1.get(1), 3);

        // Computing a cutset of the connected components of the formula after BCP.
        var formula1BCP = bcp(formula1);
        var components1BCP = connectedComponentsOf(formula1BCP);
        assertEquals(3, components1BCP.size());
        assertHasCutset(components1BCP.get(0), 2);
        assertHasCutset(components1BCP.get(1), 8, 9);
        assertHasCutset(components1BCP.get(2), 10, 11);
        
        // Assuming the literal 2 in the first component, and computing again cutsets.
        var formula2 = components1BCP.get(0).assume(2);
        var components2 = connectedComponentsOf(formula2);
        assertEquals(2, components2.size());
        assertHasCutset(components2.get(0), 4, 5);
        assertHasCutset(components2.get(1), 6, 7);

        // Computing a cutset of the connected components after BCP.
        var formula2BCP = bcp(formula2);
        var components2BCP = connectedComponentsOf(formula2BCP);
        assertEquals(1, components2BCP.size());
        assertHasCutset(components2BCP.get(0), 4, 5);
        
        // Assuming now the literal -2, and computing again cutsets.
        var formulaNot2 = components1BCP.get(0).assume(-2);
        var componentsNot2 = connectedComponentsOf(formulaNot2);
        assertEquals(1, componentsNot2.size());
        assertHasCutset(componentsNot2.get(0), 4, 5);

        // Satisfying -2 actually satisfies the formula after BCP.
        assertTrue(formulaNot2.propagate().isSatisfiable());

        // Assuming now the literal -1, and computing a cutset of its connected components.
        var formulaNot1 = formula.assume(-1);
        var componentsNot1 = connectedComponentsOf(formulaNot1);
        assertEquals(2, componentsNot1.size());
        assertHasCutset(componentsNot1.get(0), 2);
        assertHasCutset(componentsNot1.get(1), 3);

        // Computing a cutset of the connected components of the formula after BCP.
        var formulaNot1BCP = bcp(formulaNot1);
        var componentsNot1BCP = connectedComponentsOf(formulaNot1BCP);
        assertEquals(1, componentsNot1BCP.size());
        assertHasCutset(componentsNot1BCP.get(0), 3);

        // Assuming the literal 3 in the component, and computing again cutsets.
        var formula3 = componentsNot1BCP.get(0).assume(3);
        var components3 = connectedComponentsOf(formula3);
        assertEquals(2, components3.size());
        assertHasCutset(components3.get(0), 8, 9);
        assertHasCutset(components3.get(1), 10, 11);

        // Computing a cutset of the connected components after BCP.
        var formula3BCP = bcp(formula3);
        var components3BCP = connectedComponentsOf(formula3BCP);
        assertEquals(2, components3BCP.size());
        assertHasCutset(components3BCP.get(0), 8, 9);
        assertHasCutset(components3BCP.get(1), 10, 11);

        // Assuming now the literal -3, and computing again cutsets.
        var formulaNot3 = componentsNot1BCP.get(0).assume(-3);
        var componentsNot3 = connectedComponentsOf(formulaNot3);
        assertEquals(1, componentsNot3.size());
        assertHasCutset(componentsNot3.get(0), 8, 9);
        
        // Satisfying -3 actually satisfies the formula after BCP.
        assertTrue(formulaNot3.propagate().isSatisfiable());
    }

}
