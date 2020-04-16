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

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.univartois.cril.pbd4.pbc.hypergraph.DualHypergraphPartitionFinder;

/**
 * The TestDualHypergraphPartitioning is a JUnit test case for testing the partitioning
 * of the dual hypergraph of a formula, and especially the computation of a cutset. 
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
@DisplayName("The cutset of the dual hypergraph of a formula is properly computed.")
public final class TestDualHypergraphPartitioning extends AbstractTestPseudoBooleanSolving {
    
    @BeforeEach
    public void configureKahypar() {
        System.setProperty("kahypar.config", "src/test/resources/config/km1_direct_kway_sea18.ini");
    }

    /**
     * Test that the cutset of ...
     */
    @Test
    @DisplayName("The cutset of...")
    public void test() {
        var formula = readCnf("example-1.cnf").propagate().getSimplifiedFormula();
        System.out.println(formula.cutset());
    }
    
    @AfterEach
    public void freeKahypar() {
        DualHypergraphPartitionFinder.clearInstance();
    }

}

