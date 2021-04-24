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

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The TestCnfDualHypergraphPartitioning is a JUnit test case for testing the partitioning
 * of the dual hypergraph of a CNF formula, and especially the computation of a cutset.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("A cutset of the dual hypergraph of a CNF formula is properly computed.")
public final class TestCnfDualHypergraphPartitioning extends AbstractTestDualHypergraphPartitioning {

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.pbc.AbstractTestDualHypergraphPartitioning#
     * allOriginalFormulae()
     */
    @Override
    protected List<PseudoBooleanFormula> allOriginalFormulae() {
        return List.of(readCnf("example-1.cnf"), readCnf("example-2.cnf"), readCnf("example-3.cnf"));
    }

    /**
     * Tests that a cutset of the formula {@code example-1.cnf} is properly computed.
     */
    @Test
    @DisplayName("A cutset of the formula 'example-1.cnf' is properly computed.")
    public void testExample1() {
        assertHasCutset(bcp(readCnf("example-1.cnf")), 2, 3, 4);
    }

    /**
     * Tests that a cutset of the formula {@code example-2.cnf} is properly computed.
     */
    @Test
    @DisplayName("A cutset of the formula 'example-2.cnf' is properly computed.")
    public void testExample2() {
        assertHasCutset(bcp(readCnf("example-2.cnf")), 2, 3);
    }

    /**
     * Tests that a cutset of the formula {@code example-3.cnf} is properly computed.
     */
    @Test
    @DisplayName("A cutset of the formula 'example-3.cnf' is properly computed.")
    public void testExample3() {
        // Computing a cutset of the original formula.
        var formula = bcp(readCnf("example-3.cnf"));
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
        assertEquals(2, components1BCP.size());
        assertHasCutset(components1BCP.get(0), 2);
        assertHasCutset(components1BCP.get(1), 10, 11);

        // Assuming now the literal -1, and computing a cutset of its connected components.
        var formulaNot1 = formula.assume(-1);
        var componentsNot1 = connectedComponentsOf(formulaNot1);
        assertEquals(2, componentsNot1.size());
        assertHasCutset(componentsNot1.get(0), 2);
        assertHasCutset(componentsNot1.get(1), 3);

        // Computing a cutset of the connected components of the formula after BCP.
        var formulaNot1BCP = bcp(formulaNot1);
        var componentsNot1BCP = connectedComponentsOf(formulaNot1BCP);
        assertEquals(2, componentsNot1BCP.size());
        assertHasCutset(componentsNot1BCP.get(0), 3);
        assertHasCutset(componentsNot1BCP.get(1), 4, 5);
    }

}
