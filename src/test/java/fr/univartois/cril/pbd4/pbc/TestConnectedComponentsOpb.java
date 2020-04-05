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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The TestConnectedComponentsOpb is a JUnit test case testing the computation
 * of the connected components of pseudo-Boolean formulae.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("The connected components of pseudo-Boolean formulae are properly computed.")
public final class TestConnectedComponentsOpb extends AbstractTestConnectedComponents {

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.pbc.AbstractTestConnectedComponents#allOriginalFormulae()
     */
    @Override
    protected List<PseudoBooleanFormula> allOriginalFormulae() {
        return List.of(readOpb("example-1.opb"), readOpb("example-2.opb"),
                readOpb("example-3.opb"));
    }

    /**
     * Tests that the connected components of the formula {@code example-1.opb}
     * are properly computed.
     */
    @Test
    @DisplayName("The connected components of the formula 'example-1.opb' are properly computed.")
    public void testExample1() {
        var components = connectedComponentsOf(readOpb("example-1.opb"));
        assertEquals(1, components.size());
        var component0 = components.get(0);
        assertEquals(5, component0.numberOfVariables());
        assertTrue(component0.variables().contains(2));
        assertTrue(component0.variables().contains(3));
        assertTrue(component0.variables().contains(5));
        assertTrue(component0.variables().contains(6));
        assertTrue(component0.variables().contains(7));
        assertEquals(3, component0.numberOfConstraints());

        var subComponents2 = connectedComponentsOf(component0.assume(2));
        assertEquals(1, subComponents2.size());
        
        var subComponent20 = subComponents2.get(0);
        assertEquals(3, subComponent20.numberOfVariables());
        assertTrue(subComponent20.variables().contains(5));
        assertTrue(subComponent20.variables().contains(6));
        assertTrue(subComponent20.variables().contains(7));
        assertEquals(2, subComponent20.numberOfConstraints());
        var subComponent20BCP = bcp(subComponent20);
        assertEquals(3, subComponent20BCP.numberOfVariables());
        assertTrue(subComponent20BCP.variables().contains(5));
        assertTrue(subComponent20BCP.variables().contains(6));
        assertTrue(subComponent20BCP.variables().contains(7));
        assertEquals(2, subComponent20BCP.numberOfConstraints());

        var subComponents5 = connectedComponentsOf(component0.assume(5));
        assertEquals(1, subComponents5.size());
        
        var subComponent50 = subComponents5.get(0);
        assertEquals(4, subComponent50.numberOfVariables());
        assertTrue(subComponent50.variables().contains(2));
        assertTrue(subComponent50.variables().contains(3));
        assertTrue(subComponent50.variables().contains(6));
        assertTrue(subComponent50.variables().contains(7));
        assertEquals(3, subComponent50.numberOfConstraints());
        var subComponent50BCP = bcp(subComponent50, 7);
        assertEquals(2, subComponent50BCP.numberOfVariables());
        assertTrue(subComponent50BCP.variables().contains(2));
        assertTrue(subComponent50BCP.variables().contains(3));
        assertEquals(1, subComponent50BCP.numberOfConstraints());

        var subComponents7 = connectedComponentsOf(component0.assume(7));
        assertEquals(1, subComponents7.size());
        
        var subComponent70 = subComponents7.get(0);
        assertEquals(4, subComponent70.numberOfVariables());
        assertTrue(subComponent70.variables().contains(2));
        assertTrue(subComponent70.variables().contains(3));
        assertTrue(subComponent70.variables().contains(5));
        assertTrue(subComponent70.variables().contains(6));
        assertEquals(2, subComponent70.numberOfConstraints());
        var subComponent70BCP = bcp(subComponent70);
        assertEquals(4, subComponent70BCP.numberOfVariables());
        assertTrue(subComponent70.variables().contains(2));
        assertTrue(subComponent70.variables().contains(3));
        assertTrue(subComponent70.variables().contains(5));
        assertTrue(subComponent70.variables().contains(6));
        assertEquals(2, subComponent70BCP.numberOfConstraints());
    }

    /**
     * Tests that the connected components of the formula {@code example-2.opb}
     * are properly computed.
     */
    @Test
    @DisplayName("The connected components of the formula 'example-2.opb' are properly computed.")
    public void testExample2() {
        var components = connectedComponentsOf(readOpb("example-2.opb"));
        assertEquals(1, components.size());
        var component0 = components.get(0);
        assertEquals(10, component0.numberOfVariables());
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
        assertEquals(5, component0.numberOfConstraints());

        var subComponents2 = connectedComponentsOf(component0.assume(2));
        assertEquals(2, subComponents2.size());
        
        var subComponent20 = subComponents2.get(0);
        assertEquals(6, subComponent20.numberOfVariables());
        assertTrue(subComponent20.variables().contains(1));
        assertTrue(subComponent20.variables().contains(3));
        assertTrue(subComponent20.variables().contains(4));
        assertTrue(subComponent20.variables().contains(8));
        assertTrue(subComponent20.variables().contains(9));
        assertTrue(subComponent20.variables().contains(10));
        assertEquals(4, subComponent20.numberOfConstraints());
        var subComponent20BCP = bcp(subComponent20, -1, 3);
        assertNull(subComponent20BCP);
        
        var subComponent21 = subComponents2.get(1);
        assertEquals(3, subComponent21.numberOfVariables());
        assertTrue(subComponent21.variables().contains(5));
        assertTrue(subComponent21.variables().contains(6));
        assertTrue(subComponent21.variables().contains(7));
        assertEquals(1, subComponent21.numberOfConstraints());
        var subComponent21BCP = bcp(subComponent21);
        assertEquals(3, subComponent21BCP.numberOfVariables());
        assertTrue(subComponent21BCP.variables().contains(5));
        assertTrue(subComponent21BCP.variables().contains(6));
        assertTrue(subComponent21BCP.variables().contains(7));
        assertEquals(1, subComponent21BCP.numberOfConstraints());

        var subComponents3 = connectedComponentsOf(component0.assume(3));
        assertEquals(1, subComponents3.size());
        
        var subComponent30 = subComponents3.get(0);
        assertEquals(6, subComponent30.numberOfVariables());
        assertTrue(subComponent30.variables().contains(1));
        assertTrue(subComponent30.variables().contains(2));
        assertTrue(subComponent30.variables().contains(4));
        assertTrue(subComponent30.variables().contains(5));
        assertTrue(subComponent30.variables().contains(6));
        assertTrue(subComponent30.variables().contains(7));
        assertEquals(4, subComponent30.numberOfConstraints());
        var subComponent30BCP = bcp(subComponent30, -1, 2);
        assertEquals(3, subComponent30BCP.numberOfVariables());
        assertTrue(subComponent30BCP.variables().contains(5));
        assertTrue(subComponent30BCP.variables().contains(6));
        assertTrue(subComponent30BCP.variables().contains(7));
        assertEquals(1, subComponent30BCP.numberOfConstraints());
    }

    /**
     * Tests that the connected components of the formula {@code example-3.opb}
     * are properly computed.
     */
    @Test
    @DisplayName("The connected components of the formula 'example-3.opb' are properly computed.")
    public void testExample3() {
        var components = connectedComponentsOf(readOpb("example-3.opb"));
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
        var subSubComponents10BCP2 = connectedComponentsOf(subComponent10BCP.assume(2));
        assertEquals(2, subSubComponents10BCP2.size());
        var subSubComponent10BCP20 = subSubComponents10BCP2.get(0);
        assertEquals(2, subSubComponent10BCP20.numberOfVariables());
        assertTrue(subSubComponent10BCP20.variables().contains(4));
        assertTrue(subSubComponent10BCP20.variables().contains(5));
        assertEquals(1, subSubComponent10BCP20.numberOfConstraints());
        var subSubComponent10BCP21 = subSubComponents10BCP2.get(1);
        assertEquals(2, subSubComponent10BCP21.numberOfVariables());
        assertTrue(subSubComponent10BCP21.variables().contains(6));
        assertTrue(subSubComponent10BCP21.variables().contains(7));
        assertEquals(1, subSubComponent10BCP21.numberOfConstraints());

        var subComponent11 = subComponents1.get(1);
        assertEquals(5, subComponent11.numberOfVariables());
        assertTrue(subComponent11.variables().contains(3));
        assertTrue(subComponent11.variables().contains(8));
        assertTrue(subComponent11.variables().contains(9));
        assertTrue(subComponent11.variables().contains(10));
        assertTrue(subComponent11.variables().contains(11));
        assertEquals(3, subComponent11.numberOfConstraints());
        var subComponent11BCP = bcp(subComponent11, 3);
        assertEquals(4, subComponent11BCP.numberOfVariables());
        assertTrue(subComponent11BCP.variables().contains(8));
        assertTrue(subComponent11BCP.variables().contains(9));
        assertTrue(subComponent11BCP.variables().contains(10));
        assertTrue(subComponent11BCP.variables().contains(11));
        assertEquals(2, subComponent11BCP.numberOfConstraints());
        var subSubComponents11BCP = connectedComponentsOf(subComponent11BCP);
        assertEquals(2, subSubComponents11BCP.size());
        var subSubComponent11BCP0 = subSubComponents11BCP.get(0);
        assertEquals(2, subSubComponent11BCP0.numberOfVariables());
        assertTrue(subSubComponent11BCP0.variables().contains(8));
        assertTrue(subSubComponent11BCP0.variables().contains(9));
        assertEquals(1, subSubComponent11BCP0.numberOfConstraints());
        var subSubComponent11BCP1 = subSubComponents11BCP.get(1);
        assertEquals(2, subSubComponent11BCP1.numberOfVariables());
        assertTrue(subSubComponent11BCP1.variables().contains(10));
        assertTrue(subSubComponent11BCP1.variables().contains(11));
        assertEquals(1, subSubComponent11BCP1.numberOfConstraints());

        var subComponents2 = connectedComponentsOf(component0.assume(2));
        assertEquals(3, subComponents2.size());
        
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
        assertEquals(4, subComponent20BCP.numberOfVariables());
        assertTrue(subComponent20BCP.variables().contains(8));
        assertTrue(subComponent20BCP.variables().contains(9));
        assertTrue(subComponent20BCP.variables().contains(10));
        assertTrue(subComponent20BCP.variables().contains(11));
        assertEquals(2, subComponent20BCP.numberOfConstraints());
        var subSubComponents20BCP = connectedComponentsOf(subComponent20BCP);
        assertEquals(2, subSubComponents20BCP.size());
        var subSubComponent20BCP0 = subSubComponents20BCP.get(0);
        assertEquals(2, subSubComponent20BCP0.numberOfVariables());
        assertTrue(subSubComponent20BCP0.variables().contains(8));
        assertTrue(subSubComponent20BCP0.variables().contains(9));
        assertEquals(1, subSubComponent20BCP0.numberOfConstraints());
        var subSubComponent20BCP1 = subSubComponents20BCP.get(1);
        assertEquals(2, subSubComponent20BCP1.numberOfVariables());
        assertTrue(subSubComponent20BCP1.variables().contains(10));
        assertTrue(subSubComponent20BCP1.variables().contains(11));
        assertEquals(1, subSubComponent20BCP1.numberOfConstraints());

        var subComponent21 = subComponents2.get(1);
        assertEquals(2, subComponent21.numberOfVariables());
        assertTrue(subComponent21.variables().contains(4));
        assertTrue(subComponent21.variables().contains(5));
        assertEquals(1, subComponent21.numberOfConstraints());
        var subComponent21BCP = bcp(subComponent21);
        assertEquals(2, subComponent21BCP.numberOfVariables());
        assertTrue(subComponent21BCP.variables().contains(4));
        assertTrue(subComponent21BCP.variables().contains(5));
        assertEquals(1, subComponent21BCP.numberOfConstraints());

        var subComponent22 = subComponents2.get(2);
        assertEquals(2, subComponent22.numberOfVariables());
        assertTrue(subComponent22.variables().contains(6));
        assertTrue(subComponent22.variables().contains(7));
        assertEquals(1, subComponent22.numberOfConstraints());
        var subComponent22BCP = bcp(subComponent22, -6, 7);
        assertNull(subComponent22BCP);

        var subComponents3 = connectedComponentsOf(component0.assume(3));
        assertEquals(3, subComponents3.size());
        
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
        var subSubComponents30BCP2 = connectedComponentsOf(subComponent30BCP.assume(2));
        assertEquals(3, subSubComponents30BCP2.size());
        var subSubComponent30BCP20 = subSubComponents30BCP2.get(0);
        assertEquals(1, subSubComponent30BCP20.numberOfVariables());
        assertTrue(subSubComponent30BCP20.variables().contains(1));
        assertEquals(1, subSubComponent30BCP20.numberOfConstraints());
        var subSubComponent30BCP21 = subSubComponents30BCP2.get(1);
        assertEquals(2, subSubComponent30BCP21.numberOfVariables());
        assertTrue(subSubComponent30BCP21.variables().contains(4));
        assertTrue(subSubComponent30BCP21.variables().contains(5));
        assertEquals(1, subSubComponent30BCP21.numberOfConstraints());
        var subSubComponent30BCP22 = subSubComponents30BCP2.get(2);
        assertEquals(2, subSubComponent30BCP22.numberOfVariables());
        assertTrue(subSubComponent30BCP22.variables().contains(6));
        assertTrue(subSubComponent30BCP22.variables().contains(7));
        assertEquals(1, subSubComponent30BCP22.numberOfConstraints());

        var subComponent31 = subComponents3.get(1);
        assertEquals(2, subComponent31.numberOfVariables());
        assertTrue(subComponent31.variables().contains(8));
        assertTrue(subComponent31.variables().contains(9));
        assertEquals(1, subComponent31.numberOfConstraints());
        var subComponent31BCP = bcp(subComponent31);
        assertEquals(2, subComponent31BCP.numberOfVariables());
        assertTrue(subComponent31BCP.variables().contains(8));
        assertTrue(subComponent31BCP.variables().contains(9));
        assertEquals(1, subComponent31BCP.numberOfConstraints());

        var subComponent32 = subComponents3.get(2);
        assertEquals(2, subComponent32.numberOfVariables());
        assertTrue(subComponent32.variables().contains(10));
        assertTrue(subComponent32.variables().contains(11));
        assertEquals(1, subComponent32.numberOfConstraints());
        var subComponent32BCP = bcp(subComponent32);
        assertEquals(2, subComponent32BCP.numberOfVariables());
        assertTrue(subComponent32BCP.variables().contains(10));
        assertTrue(subComponent32BCP.variables().contains(11));
        assertEquals(1, subComponent32BCP.numberOfConstraints());
    }

}
