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

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The TestConnectedComponentsCnf is a JUnit test case testing the computation
 * of the connected components of formulae in Conjunctive Normal Form (CNF).
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("The connected components of CNF formulae are properly computed.")
public final class TestConnectedComponentsCnf extends AbstractTestConnectedComponents {

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.pbc.AbstractTestConnectedComponents#allOriginalFormulae()
     */
    @Override
    protected List<PseudoBooleanFormula> allOriginalFormulae() {
        return List.of(readCnf("example-1.cnf"), readCnf("example-2.cnf"), readCnf("example-3.cnf"));
    }

    /**
     * Tests that the connected components of the formula {@code example-1.cnf}
     * are properly computed.
     */
    @Test
    @DisplayName("The connected components of the formula 'example-1.cnf' are properly computed.")
    public void testExample1() {
        var components = connectedComponentsOf(readCnf("example-1.cnf"));
        assertEquals(1, components.size());
        var component0 = components.get(0);
        assertEquals(4, component0.numberOfVariables());
        assertTrue(component0.variables().contains(1));
        assertTrue(component0.variables().contains(2));
        assertTrue(component0.variables().contains(3));
        assertTrue(component0.variables().contains(4));
        assertEquals(4, component0.numberOfConstraints());
        
        var subComponents1 = connectedComponentsOf(component0.assume(1));
        assertEquals(1, subComponents1.size());
        var subComponent10 = subComponents1.get(0);
        assertEquals(3, subComponent10.numberOfVariables());
        assertTrue(subComponent10.variables().contains(2));
        assertTrue(subComponent10.variables().contains(3));
        assertTrue(subComponent10.variables().contains(4));
        assertEquals(2, subComponent10.numberOfConstraints());
        
        var subComponents2 = connectedComponentsOf(component0.assume(2));
        assertEquals(1, subComponents2.size());
        var subComponent20 = subComponents2.get(0);
        assertEquals(3, subComponent20.numberOfVariables());
        assertTrue(subComponent20.variables().contains(1));
        assertTrue(subComponent20.variables().contains(3));
        assertTrue(subComponent20.variables().contains(4));
        assertEquals(2, subComponent20.numberOfConstraints());
        
        var subComponents3 = connectedComponentsOf(component0.assume(3));
        assertEquals(1, subComponents3.size());
        var subComponent30 = subComponents3.get(0);
        assertEquals(2, subComponent30.numberOfVariables());
        assertTrue(subComponent30.variables().contains(1));
        assertTrue(subComponent30.variables().contains(4));
        assertEquals(1, subComponent30.numberOfConstraints());
        
        var subComponents4 = connectedComponentsOf(component0.assume(4));
        assertEquals(1, subComponents4.size());
        var subComponent40 = subComponents4.get(0);
        assertEquals(3, subComponent40.numberOfVariables());
        assertTrue(subComponent40.variables().contains(1));
        assertTrue(subComponent40.variables().contains(2));
        assertTrue(subComponent40.variables().contains(3));
        assertEquals(3, subComponent40.numberOfConstraints());
    }

    /**
     * Tests that the connected components of the formula {@code example-2.cnf}
     * are properly computed.
     */
    @Test
    @DisplayName("The connected components of the formula 'example-2.cnf' are properly computed.")
    public void testExample2() {
        var components = connectedComponentsOf(readCnf("example-2.cnf"));
        assertEquals(1, components.size());
        var component0 = components.get(0);
        assertEquals(3, component0.numberOfVariables());
        assertTrue(component0.variables().contains(1));
        assertTrue(component0.variables().contains(2));
        assertTrue(component0.variables().contains(3));
        assertEquals(3, component0.numberOfConstraints());
        
        var subComponents1 = connectedComponentsOf(component0.assume(1));
        assertEquals(1, subComponents1.size());
        var subComponent10 = subComponents1.get(0);
        assertEquals(2, subComponent10.numberOfVariables());
        assertTrue(subComponent10.variables().contains(2));
        assertTrue(subComponent10.variables().contains(3));
        assertEquals(2, subComponent10.numberOfConstraints());
        
        var subComponents2 = connectedComponentsOf(component0.assume(2));
        assertEquals(2, subComponents2.size());
        var subComponent20 = subComponents2.get(0);
        assertEquals(1, subComponent20.numberOfVariables());
        assertTrue(subComponent20.variables().contains(1));
        assertEquals(1, subComponent20.numberOfConstraints());
        var subComponent21 = subComponents2.get(1);
        assertEquals(1, subComponent21.numberOfVariables());
        assertTrue(subComponent21.variables().contains(3));
        assertEquals(1, subComponent21.numberOfConstraints());
        
        var subComponents3 = connectedComponentsOf(component0.assume(3));
        assertEquals(1, subComponents3.size());
        var subComponent30 = subComponents3.get(0);
        assertEquals(2, subComponent30.numberOfVariables());
        assertTrue(subComponent30.variables().contains(1));
        assertTrue(subComponent30.variables().contains(2));
        assertEquals(2, subComponent30.numberOfConstraints());
    }

    /**
     * Tests that the connected components of the formula {@code example-3.cnf}
     * are properly computed.
     */
    @Test
    @DisplayName("The connected components of the formula 'example-3.cnf' are properly computed.")
    public void testExample3() {
        var components = connectedComponentsOf(readCnf("example-3.cnf"));
        assertEquals(1, components.size());
        var component0 = components.get(0);
        assertEquals(11, component0.numberOfVariables());
        assertTrue(component0.variables().contains(1));
        assertTrue(component0.variables().contains(2));
        assertTrue(component0.variables().contains(3));
        assertTrue(component0.variables().contains(4));
        assertTrue(component0.variables().contains(5));
        assertTrue(component0.variables().contains(6));
        assertTrue(component0.variables().contains(7));
        assertTrue(component0.variables().contains(8));
        assertTrue(component0.variables().contains(9));
        assertTrue(component0.variables().contains(10));
        assertTrue(component0.variables().contains(11));
        assertEquals(6, component0.numberOfConstraints());

        var subComponents1 = connectedComponentsOf(component0.assume(1));
        assertEquals(2, subComponents1.size());
        var subComponent10 = subComponents1.get(0);
        assertEquals(5, subComponent10.numberOfVariables());
        assertTrue(subComponent10.variables().contains(2));
        assertTrue(subComponent10.variables().contains(4));
        assertTrue(subComponent10.variables().contains(5));
        assertTrue(subComponent10.variables().contains(6));
        assertTrue(subComponent10.variables().contains(7));
        assertEquals(2, subComponent10.numberOfConstraints());
        var subComponent11 = subComponents1.get(1);
        assertEquals(5, subComponent11.numberOfVariables());
        assertTrue(subComponent11.variables().contains(3));
        assertTrue(subComponent11.variables().contains(8));
        assertTrue(subComponent11.variables().contains(9));
        assertTrue(subComponent11.variables().contains(10));
        assertTrue(subComponent11.variables().contains(11));
        assertEquals(3, subComponent11.numberOfConstraints());

        var subComponents2 = connectedComponentsOf(component0.assume(2));
        assertEquals(2, subComponents2.size());
        var subComponent20 = subComponents2.get(0);
        assertEquals(6, subComponent20.numberOfVariables());
        assertTrue(subComponent20.variables().contains(1));
        assertTrue(subComponent20.variables().contains(3));
        assertTrue(subComponent20.variables().contains(8));
        assertTrue(subComponent20.variables().contains(9));
        assertTrue(subComponent20.variables().contains(10));
        assertTrue(subComponent20.variables().contains(11));
        assertEquals(4, subComponent20.numberOfConstraints());
        var subComponent21 = subComponents2.get(1);
        assertEquals(2, subComponent21.numberOfVariables());
        assertTrue(subComponent21.variables().contains(6));
        assertTrue(subComponent21.variables().contains(7));
        assertEquals(1, subComponent21.numberOfConstraints());

        var subComponents3 = connectedComponentsOf(component0.assume(3));
        assertEquals(2, subComponents3.size());
        var subComponent30 = subComponents3.get(0);
        assertEquals(6, subComponent30.numberOfVariables());
        assertTrue(subComponent30.variables().contains(1));
        assertTrue(subComponent30.variables().contains(2));
        assertTrue(subComponent30.variables().contains(4));
        assertTrue(subComponent30.variables().contains(5));
        assertTrue(subComponent30.variables().contains(6));
        assertTrue(subComponent30.variables().contains(7));
        assertEquals(3, subComponent30.numberOfConstraints());
        var subComponent31 = subComponents3.get(1);
        assertEquals(2, subComponent31.numberOfVariables());
        assertTrue(subComponent31.variables().contains(10));
        assertTrue(subComponent31.variables().contains(11));
        assertEquals(1, subComponent31.numberOfConstraints());
    }

}
