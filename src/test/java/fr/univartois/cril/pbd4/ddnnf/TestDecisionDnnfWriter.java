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

package fr.univartois.cril.pbd4.ddnnf;

import static fr.univartois.cril.pbd4.ddnnf.ConjunctionNode.and;
import static fr.univartois.cril.pbd4.ddnnf.DecisionNode.or;
import static fr.univartois.cril.pbd4.ddnnf.ConstantNode.TRUE;
import static fr.univartois.cril.pbd4.ddnnf.LiteralNode.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * The TestDecisionDnnfWriter is a JUnit test case testing the implementation of the
 * {@link DecisionDnnfWriter}.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class TestDecisionDnnfWriter {

    /**
     * Tests that the decision-DNNF created by {@link #createExample1()} is properly
     * written.
     */
    @Test
    public void testWriteExample1() {
        var ddnnf = createExample1();
        assertEquals(TestDecisionDnnfReader.nnfOfExample1(), ddnnf.toString());
    }

    /**
     * Creates the decision-DNNF used as an example in the C2D manual.
     *
     * @return The created decision-DNNF.
     */
    static DecisionDnnf createExample1() {
        // Creating the literal nodes.
        var l1 = literal(1);
        var l2 = literal(2);
        var notl2 = literal(-2);
        var l3 = literal(3);
        var notl3 = literal(-3);
        var l4 = literal(4);
        var notl4 = literal(-4);

        // Creating the internal nodes.
        var a3 = and(notl3, notl2, l1);
        var o5 = or(3, l3, a3);
        var a7 = and(o5, notl4);
        var a9 = and(l1, l4);
        var a10 = and(notl2, l3);
        var o12 = or(2, l2, a10);
        var a13 = and(a9, o12);

        // Creating the root node.
        var root = or(4, a13, a7);
        return new DecisionDnnf(4, 15, 17, root);
    }

    /**
     * Tests that the decision-DNNF created by {@link #createExample2()} is properly
     * written.
     */
    @Test
    public void testWriteExample2() {
        var ddnnf = createExample2();
        assertEquals(TestDecisionDnnfReader.nnfOfExample2(), ddnnf.toString());
    }

    /**
     * Creates the decision-DNNF used as an example on the D4 web page.
     *
     * @return The created decision-DNNF.
     */
    static DecisionDnnf createExample2() {
        // Creating the literal nodes.
        var l1 = literal(1);
        var notl1 = literal(-1);
        var l2 = literal(2);
        var notl2 = literal(-2);
        var l3 = literal(3);
        var notl3 = literal(-3);

        // Creating the internal nodes.
        var a4 = and(l3, notl1, TRUE);
        var a6 = and(l1, TRUE);
        var o7 = or(1, a6, a4);
        var a9 = and(notl2, o7);
        var a13 = and(notl3, l2, notl1, TRUE);

        // Creating the root node.
        var root = or(2, a13, a9);
        return new DecisionDnnf(3, 15, 14, root);
    }

}
