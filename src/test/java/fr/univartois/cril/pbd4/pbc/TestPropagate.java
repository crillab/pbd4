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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class TestPropagate extends AbstractTestPseudoBooleanSolving {

    @Test
    public void testCnfExample1() {
        var formula = readCnf("example-1.cnf");
        
        var res = formula.propagate();
        assertEquals(0, res.getPropagatedLiterals().size());
        assertTrue(res.isUnknown());
        
        var outFormula = res.getSimplifiedFormula();
		assertEquals(4, formula.numberOfVariables());
		assertEquals(4, formula.numberOfConstraints());
        
		var fNot1 = outFormula.satisfy(-1);
		var res2 = fNot1.propagate();
		var propagatedLit = res2.getPropagatedLiterals();
		assertEquals(3, fNot1.numberOfVariables());
		assertEquals(4, fNot1.numberOfConstraints());
		assertEquals(1, propagatedLit.size());
		assertTrue(propagatedLit.contains(-4));
        assertTrue(res2.isUnknown());
        
        var sf = res2.getSimplifiedFormula();
		assertEquals(2, sf.numberOfVariables());
		assertEquals(2, sf.numberOfConstraints());
        var res2bis = sf.satisfy(-3).propagate();

        assertTrue(res2bis.isUnsatisfiable());
        
        var fNot12 = res2.getSimplifiedFormula().satisfy(-2);
        var res3 = fNot12.propagate();
		var propagatedLit2 = res3.getPropagatedLiterals();
		assertEquals(3, propagatedLit2.size());
		assertTrue(propagatedLit2.contains(3));
        assertTrue(res3.isSatisfiable());
    }

}

