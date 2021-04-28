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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.junit.jupiter.api.DisplayName;

/**
 * The TestDecisionDnnfWriter is a JUnit test case testing the writing of a
 * d-DNNF in the NNF format.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
@DisplayName("Reading a d-DNNF produces the expected d-DNNF.")
public final class TestDecisionDnnfWriter extends AbstractTestDecisionDnnfEvaluation {

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.univartois.cril.pbd4.ddnnf.AbstractTestDecisionDnnfEvaluation#
	 * createExample1()
	 */
	@Override
	protected DecisionDnnf createExample1() {
		var ddnnf = createCopyOf("example-1.nnf");
		assertEquals(4, ddnnf.getNumberOfVariables());
		assertEquals(15, ddnnf.getNumberOfNodes());
		assertEquals(17, ddnnf.getNumberOfEdges());
		return ddnnf;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.univartois.cril.pbd4.ddnnf.AbstractTestDecisionDnnfEvaluation#
	 * createExample2()
	 */
	@Override
	protected DecisionDnnf createExample2() {
		var ddnnf = createCopyOf("example-2.nnf");
		assertEquals(3, ddnnf.getNumberOfVariables());
		assertEquals(14, ddnnf.getNumberOfNodes());
		assertEquals(15, ddnnf.getNumberOfEdges());
		return ddnnf;
	}

	/**
	 * Creates a copy of the d-DNNF stored in a resource file.
	 * The copy is made by reading the d-DNNF, writing it as a String and reading this String again.
	 *
	 * @param filename The name of the file to read.
	 *        This file is supposed to be located in the {@code ddnnf} resource folder.
	 *
	 * @return The created copy of the d-DNNF.
	 */
	private static DecisionDnnf createCopyOf(String file) {
		var origin = TestDecisionDnnfReader.read(file);
		var originAsString = origin.toString();

		try (var reader = new DecisionDnnfReader(new ByteArrayInputStream(originAsString.getBytes()))) {
			return reader.read();

		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}
