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

package fr.univartois.cril.pbd4.partitioning;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.univartois.cril.pbd4.pbc.AbstractTestPseudoBooleanSolving;
import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;

/**
 * The AbstractTestDualHypergraphPartitioning is the parent class for the test
 * cases for the computation of a cutset of the dual hypergraph associated to a
 * formula.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public abstract class AbstractTestDualHypergraphPartitioning extends AbstractTestPseudoBooleanSolving {

    /**
     * The cutset computation strategy that uses the KaHyPar library.
     */
    private KahyparCutsetComputationStrategy cutsetComputer;

    /**
     * Sets up the {@link #cutsetComputer} used to compute cutsets.
     */
    @BeforeEach
    public void configureJKahypar() {
        cutsetComputer = KahyparCutsetComputationStrategy.newInstance("src/test/resources/config/kahypar.ini");
        cutsetComputer.compilationStarts();
    }

    /**
     * Gives the list of the original formulae considered by this test case.
     *
     * @return The list of formulae.
     */
    protected abstract List<PseudoBooleanFormula> allOriginalFormulae();

    /**
     * Tests that it is not possible to compute a cutset of an original formula.
     */
    @Test
    @DisplayName("Computation of cutsets cannot be done on an original formula.")
    public void testCannotComputeOnOriginalFormula() {
        for (var formula : allOriginalFormulae()) {
            assertEquals("fr.univartois.cril.pbd4.pbc.OriginalPseudoBooleanFormula", formula.getClass().getName());
            assertThrows(UnsupportedOperationException.class, () -> cutsetComputer.cutset(formula));
        }
    }

    /**
     * Ensures that the cutset of the given formula is the expected one.
     *
     * @param formula The formula to test the cutset of.
     * @param variables The variables that should be in the cutset.
     */
    protected void assertHasCutset(PseudoBooleanFormula formula, int... variables) {
        var cutset = cutsetComputer.cutset(formula);

        // Checking the size of the cutset.
        assertEquals(variables.length, cutset.size(),
                "Cutset has " + cutset.size() + " variable(s), but expected " + variables.length);

        // Checking the variables in the cutset.
        for (int v : variables) {
            assertTrue(cutset.contains(v),
                    "Variable " + v + " is missing from the cutset");
        }
    }

    /**
     * Applies Boolean Constraint Propagation (BCP) to the given formula.
     *
     * @param formula The formula on which to apply BCP.
     *
     * @return The formula obtained after BCP.
     */
    protected static PseudoBooleanFormula bcp(PseudoBooleanFormula formula) {
        return formula.propagate().getSimplifiedFormula();
    }

    /**
     * Computes the connected components of the given formula, and returns the
     * {@link List} of these components.
     * 
     * @param formula The formula to compute the connected components of.
     *
     * @return The list of connected components.
     */
    protected static List<PseudoBooleanFormula> connectedComponentsOf(PseudoBooleanFormula formula) {
        return (List<PseudoBooleanFormula>) formula.connectedComponents();
    }

    /**
     * Frees the memory used by JKaHyPar.
     */
    @AfterEach
    public void freeKahypar() {
        cutsetComputer.compilationEnds();
    }

}
