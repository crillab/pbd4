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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The TestConnectedComponentsCnf is a JUnit test case testing the computation
 * of the connected components of formulae in Conjunctive Normal Form (CNF).
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
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
        var subComponent10BCP = bcp(subComponent10);
        assertEquals(3, subComponent10BCP.numberOfVariables());
        assertTrue(subComponent10BCP.variables().contains(2));
        assertTrue(subComponent10BCP.variables().contains(3));
        assertTrue(subComponent10BCP.variables().contains(4));
        assertEquals(2, subComponent10BCP.numberOfConstraints());
        
        var subComponents2 = connectedComponentsOf(component0.assume(2));
        assertEquals(1, subComponents2.size());
        
        var subComponent20 = subComponents2.get(0);
        assertEquals(3, subComponent20.numberOfVariables());
        assertTrue(subComponent20.variables().contains(1));
        assertTrue(subComponent20.variables().contains(3));
        assertTrue(subComponent20.variables().contains(4));
        assertEquals(2, subComponent20.numberOfConstraints());
        var subComponent20BCP = bcp(subComponent20);
        assertEquals(3, subComponent20BCP.numberOfVariables());
        assertTrue(subComponent20BCP.variables().contains(1));
        assertTrue(subComponent20BCP.variables().contains(3));
        assertTrue(subComponent20BCP.variables().contains(4));
        assertEquals(2, subComponent20BCP.numberOfConstraints());
        
        var subComponents3 = connectedComponentsOf(component0.assume(3));
        assertEquals(1, subComponents3.size());
        
        var subComponent30 = subComponents3.get(0);
        assertEquals(2, subComponent30.numberOfVariables());
        assertTrue(subComponent30.variables().contains(1));
        assertTrue(subComponent30.variables().contains(4));
        assertEquals(1, subComponent30.numberOfConstraints());
        var subComponent30BCP = bcp(subComponent30);
        assertEquals(2, subComponent30BCP.numberOfVariables());
        assertTrue(subComponent30BCP.variables().contains(1));
        assertTrue(subComponent30BCP.variables().contains(4));
        assertEquals(1, subComponent30BCP.numberOfConstraints());
        
        var subComponents4 = connectedComponentsOf(component0.assume(4));
        assertEquals(1, subComponents4.size());
        
        var subComponent40 = subComponents4.get(0);
        assertEquals(3, subComponent40.numberOfVariables());
        assertTrue(subComponent40.variables().contains(1));
        assertTrue(subComponent40.variables().contains(2));
        assertTrue(subComponent40.variables().contains(3));
        assertEquals(3, subComponent40.numberOfConstraints());
        var subComponent40BCP = bcp(subComponent40, 1);
        assertEquals(2, subComponent40BCP.numberOfVariables());
        assertTrue(subComponent40BCP.variables().contains(2));
        assertTrue(subComponent40BCP.variables().contains(3));
        assertEquals(1, subComponent40BCP.numberOfConstraints());
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
        var subComponent10BCP = bcp(subComponent10, -2);
        assertNull(subComponent10BCP);
        
        var subComponents2 = connectedComponentsOf(component0.assume(2));
        assertEquals(2, subComponents2.size());
        
        var subComponent20 = subComponents2.get(0);
        assertEquals(1, subComponent20.numberOfVariables());
        assertTrue(subComponent20.variables().contains(1));
        assertEquals(1, subComponent20.numberOfConstraints());
        var subComponent20BCP = bcp(subComponent20, -1);
        assertNull(subComponent20BCP);
        
        var subComponent21 = subComponents2.get(1);
        assertEquals(1, subComponent21.numberOfVariables());
        assertTrue(subComponent21.variables().contains(3));
        assertEquals(1, subComponent21.numberOfConstraints());
        var subComponent21BCP = bcp(subComponent21, -3);
        assertNull(subComponent21BCP);
        
        var subComponents3 = connectedComponentsOf(component0.assume(3));
        assertEquals(1, subComponents3.size());
        
        var subComponent30 = subComponents3.get(0);
        assertEquals(2, subComponent30.numberOfVariables());
        assertTrue(subComponent30.variables().contains(1));
        assertTrue(subComponent30.variables().contains(2));
        assertEquals(2, subComponent30.numberOfConstraints());
        var subComponent30BCP = bcp(subComponent30, -2);
        assertNull(subComponent30BCP);
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
        var subComponent10BCP = bcp(subComponent10);
        assertEquals(5, subComponent10BCP.numberOfVariables());
        assertTrue(subComponent10BCP.variables().contains(2));
        assertTrue(subComponent10BCP.variables().contains(4));
        assertTrue(subComponent10BCP.variables().contains(5));
        assertTrue(subComponent10BCP.variables().contains(6));
        assertTrue(subComponent10BCP.variables().contains(7));
        assertEquals(2, subComponent10BCP.numberOfConstraints());
        
        var subComponent11 = subComponents1.get(1);
        assertEquals(5, subComponent11.numberOfVariables());
        assertTrue(subComponent11.variables().contains(3));
        assertTrue(subComponent11.variables().contains(8));
        assertTrue(subComponent11.variables().contains(9));
        assertTrue(subComponent11.variables().contains(10));
        assertTrue(subComponent11.variables().contains(11));
        assertEquals(3, subComponent11.numberOfConstraints());
        var subComponent11BCP = bcp(subComponent11, 3);
        assertEquals(2, subComponent11BCP.numberOfVariables());
        assertTrue(subComponent11BCP.variables().contains(10));
        assertTrue(subComponent11BCP.variables().contains(11));
        assertEquals(1, subComponent11BCP.numberOfConstraints());

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
        var subComponent20BCP = bcp(subComponent20, 1, 3);
        assertEquals(2, subComponent20BCP.numberOfVariables());
        assertTrue(subComponent20BCP.variables().contains(10));
        assertTrue(subComponent20BCP.variables().contains(11));
        assertEquals(1, subComponent20BCP.numberOfConstraints());
        
        var subComponent21 = subComponents2.get(1);
        assertEquals(2, subComponent21.numberOfVariables());
        assertTrue(subComponent21.variables().contains(6));
        assertTrue(subComponent21.variables().contains(7));
        assertEquals(1, subComponent21.numberOfConstraints());
        var subComponent21BCP = bcp(subComponent21);
        assertEquals(2, subComponent21BCP.numberOfVariables());
        assertTrue(subComponent21BCP.variables().contains(6));
        assertTrue(subComponent21BCP.variables().contains(7));
        assertEquals(1, subComponent21BCP.numberOfConstraints());

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
        var subComponent30BCP = bcp(subComponent30);
        assertEquals(6, subComponent30BCP.numberOfVariables());
        assertTrue(subComponent30BCP.variables().contains(1));
        assertTrue(subComponent30BCP.variables().contains(2));
        assertTrue(subComponent30BCP.variables().contains(4));
        assertTrue(subComponent30BCP.variables().contains(5));
        assertTrue(subComponent30BCP.variables().contains(6));
        assertTrue(subComponent30BCP.variables().contains(7));
        assertEquals(3, subComponent30BCP.numberOfConstraints());
        
        var subComponent31 = subComponents3.get(1);
        assertEquals(2, subComponent31.numberOfVariables());
        assertTrue(subComponent31.variables().contains(10));
        assertTrue(subComponent31.variables().contains(11));
        assertEquals(1, subComponent31.numberOfConstraints());
        var subComponent31BCP = bcp(subComponent31);
        assertEquals(2, subComponent31BCP.numberOfVariables());
        assertTrue(subComponent31BCP.variables().contains(10));
        assertTrue(subComponent31BCP.variables().contains(11));
        assertEquals(1, subComponent31BCP.numberOfConstraints());
    }

}
