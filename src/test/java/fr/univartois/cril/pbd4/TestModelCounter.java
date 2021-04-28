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

package fr.univartois.cril.pbd4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The TestModelCounter is a JUnit test case for testing the number of models computed by
 * {@link D4} when used as a model counter.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
@DisplayName("The number of models computed by D4 is correct.")
public final class TestModelCounter {

    /**
     * Tests that the number of models computed by D4 on a CNF formula is correct.
     *
     * @param file The name of the file containing the CNF formula.
     * @param expectedModelCount The number of models of the CNF formula.
     *
     * @throws IOException If an I/O error occurs while reading the formula.
     */
    @ParameterizedTest
    @MethodSource("generateModelCountsForCnf")
    @DisplayName("The number of models computed by D4 on a CNF formula is correct.")
    public void testCountModelOfCnf(String file, BigInteger expectedModelCount) throws IOException {
        try (var stream = TestModelCounter.class.getResourceAsStream("/cnf/" + file)) {
            var computedModelCount = D4.newInstance()
                .withConfiguration("src/test/resources/config/kahypar.ini")
                .onCnfInput(stream)
                .countModels();
            assertEquals(expectedModelCount, computedModelCount);
        }
    }

    /**
     * Generates the arguments for the test case of the model counter on CNF formulae.
     *
     * @return The arguments of the test case.
     */
    private static Stream<Arguments> generateModelCountsForCnf() {
        return Stream.of(
                Arguments.of("example-1.cnf", BigInteger.valueOf(8)),
                Arguments.of("example-2.cnf", BigInteger.valueOf(4)),
                Arguments.of("example-3.cnf", BigInteger.valueOf(576)));
    }

    /**
     * Tests that the number of models computed by D4 on a pseudo-Boolean formula is
     * correct.
     *
     * @param file The name of the file containing the pseudo-Boolean formula.
     * @param expectedModelCount The number of models of the pseudo-Boolean formula.
     *
     * @throws IOException If an I/O error occurs while reading the formula.
     */
    @ParameterizedTest
    @MethodSource("generateModelCountsForPb")
    @DisplayName("The number of models computed by D4 on a pseudo-Boolean formula is correct.")
    public void testCountModelOfPb(String file, BigInteger expectedModelCount) throws IOException {
        try (var stream = TestModelCounter.class.getResourceAsStream("/opb/" + file)) {
            var computedModelCount = D4.newInstance()
                .withConfiguration("src/test/resources/config/kahypar.ini")
                .onOpbInput(stream)
                .countModels();
            assertEquals(expectedModelCount, computedModelCount);
        }
    }

    /**
     * Generates the arguments for the test case of the model counter on pseudo-Boolean
     * formulae.
     *
     * @return The arguments of the test case.
     */
    private static Stream<Arguments> generateModelCountsForPb() {
        return Stream.of(
                Arguments.of("example-1.opb", BigInteger.valueOf(22)),
                Arguments.of("example-2.opb", BigInteger.valueOf(64)),
                Arguments.of("example-3.opb", BigInteger.valueOf(115)));
    }

}
