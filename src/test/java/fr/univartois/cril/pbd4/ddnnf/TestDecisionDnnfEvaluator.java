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

package fr.univartois.cril.pbd4.ddnnf;

import static fr.univartois.cril.pbd4.ddnnf.ConjunctionNode.and;
import static fr.univartois.cril.pbd4.ddnnf.ConstantNode.TRUE;
import static fr.univartois.cril.pbd4.ddnnf.DecisionNode.decision;
import static fr.univartois.cril.pbd4.ddnnf.LiteralNode.literal;

import org.junit.jupiter.api.DisplayName;

/**
 * The TestDecisionDnnfEvaluator is a JUnit test case testing the implementation
 * of the evaluation of d-DNNFs.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
@DisplayName("The evaluation of a d-DNNF yields the expected truth value.")
public final class TestDecisionDnnfEvaluator extends AbstractTestDecisionDnnfEvaluation {

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.univartois.cril.pbd4.ddnnf.AbstractTestDecisionDnnfEvaluation#
	 * createExample1()
	 */
	@Override
	protected DecisionDnnf createExample1() {
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
		var o5 = decision(3, l3, a3);
		var a7 = and(o5, notl4);
		var a9 = and(l1, l4);
		var a10 = and(notl2, l3);
		var o12 = decision(2, l2, a10);
		var a13 = and(a9, o12);

		// Creating the root node.
		var root = decision(4, a13, a7);
		return new DecisionDnnf(4, 15, 17, root);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.univartois.cril.pbd4.ddnnf.AbstractTestDecisionDnnfEvaluation#
	 * createExample2()
	 */
	@Override
	protected DecisionDnnf createExample2() {
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
		var o7 = decision(1, a6, a4);
		var a9 = and(notl2, o7);
		var a13 = and(notl3, l2, notl1, TRUE);

		// Creating the root node.
		var root = decision(2, a13, a9);
		return new DecisionDnnf(3, 15, 14, root);
	}

}
