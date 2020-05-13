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

package fr.univartois.cril.pbd4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import fr.univartois.cril.pbd4.pbc.hypergraph.DualHypergraphPartitionFinder;

/**
 * The TestModelCounter is a JUnit test case for testing the number of models computed by
 * {@link D4} when used as a model counter.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("The number of models computed by D4 is correct")
public final class TestModelCounter {

    /**
     * Sets up the configuration of JKaHyPar, the hypergraph partitioner used to compute
     * the cutsets.
     */
    @BeforeEach
    public void configureJKahypar() {
        System.setProperty("kahypar.config", "src/test/resources/config/kahypar.ini");
    }

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
    @DisplayName("The number of models computed by D4 on a CNF formula is correct")
    public void testCountModelOfCnf(String file, BigInteger expectedModelCount) throws IOException {
        try (var stream = TestModelCounter.class.getResourceAsStream("/cnf/" + file)) {
            var computedModelCount = D4.newInstance().whenCompilingCnf(stream).countModels();
            assertEquals(expectedModelCount, computedModelCount);
        }
    }

    /**
     * Generates the arguments for the test case of the model counter on a CNF formula.
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
     * Tests that the number of models computed by D4 on a pseudo-Boolean formula is correct.
     *
     * @param file The name of the file containing the pseudo-Boolean formula.
     * @param expectedModelCount The number of models of the pseudo-Boolean formula.
     *
     * @throws IOException If an I/O error occurs while reading the formula.
     */
    @ParameterizedTest
    @MethodSource("generateModelCountsForPb")
    @DisplayName("The number of models computed by D4 on a pseudo-Boolean formula is correct")
    public void testCountModelOfPb(String file, BigInteger expectedModelCount) throws IOException {
        try (var stream = TestModelCounter.class.getResourceAsStream("/opb/" + file)) {
            var computedModelCount = D4.newInstance().whenCompilingOpb(stream).countModels();
            assertEquals(expectedModelCount, computedModelCount);
        }
    }

    /**
     * Generates the arguments for the test case of the model counter on a pseudo-Boolean
     * formula.
     *
     * @return The arguments of the test case.
     */
    private static Stream<Arguments> generateModelCountsForPb() {
        return Stream.of(
                Arguments.of("example-1.opb", BigInteger.valueOf(22)),
                Arguments.of("example-2.opb", BigInteger.valueOf(64)),
                Arguments.of("example-3.opb", BigInteger.valueOf(115)));
    }

    /**
     * Frees the memory used by JKaHyPar.
     */
    @AfterEach
    public void freeKahypar() {
        DualHypergraphPartitionFinder.clearInstance();
    }

}
