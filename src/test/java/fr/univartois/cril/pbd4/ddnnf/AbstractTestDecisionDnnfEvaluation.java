/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The AbstractTestDecisionDnnfEvaluation provides test cases for evaluating the
 * two d-DNNFs used as examples on all possible assignments.
 *
 * Sub-classes are expected to produce equivalent d-DNNFs, but in different
 * manners.
 * This class may thus be used either to check the evaluation of a d-DNNF or
 * to check that the expected d-DNNF is produced.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public abstract class AbstractTestDecisionDnnfEvaluation {

    /**
     * Tests that the d-DNNF corresponding to that in the file {@code example-1.nnf}
     * is properly evaluated.
     * This d-DNNF is that used as an example in the C2D manual.
     */
    @Test
    @DisplayName("The d-DNNF of Example #1 has the expected models and counter-models.")
    public void testEvaluateExample1() {
        var ddnnf = createExample1();

        assertTrue(ddnnf.evaluate(1, 2, 3, 4));
        assertTrue(ddnnf.evaluate(1, 2, 3, -4));
        assertTrue(ddnnf.evaluate(1, 2, -3, 4));
        assertFalse(ddnnf.evaluate(1, 2, -3, -4));
        assertTrue(ddnnf.evaluate(1, -2, 3, 4));
        assertTrue(ddnnf.evaluate(1, -2, 3, -4));
        assertFalse(ddnnf.evaluate(1, -2, -3, 4));
        assertTrue(ddnnf.evaluate(1, -2, -3, -4));
        assertFalse(ddnnf.evaluate(-1, 2, 3, 4));
        assertTrue(ddnnf.evaluate(-1, 2, 3, -4));
        assertFalse(ddnnf.evaluate(-1, 2, -3, 4));
        assertFalse(ddnnf.evaluate(-1, 2, -3, -4));
        assertFalse(ddnnf.evaluate(-1, -2, 3, 4));
        assertTrue(ddnnf.evaluate(-1, -2, 3, -4));
        assertFalse(ddnnf.evaluate(-1, -2, -3, 4));
        assertFalse(ddnnf.evaluate(-1, -2, -3, -4));
    }

    /**
     * Creates a d-DNNF that is (supposed to be) equivalent to that in the file
     * {@code example-1.nnf}.
     *
     * @return The created d-DNNF.
     */
    protected abstract DecisionDnnf createExample1();

    /**
     * Tests that the d-DNNF corresponding to that in the file {@code example-2.nnf}
     * is properly evaluated.
     * This d-DNNF is that used as an example on the D4 web page.
     */
    @Test
    @DisplayName("The d-DNNF of Example #2 has the expected models and counter-models.")
    public void testEvaluateExample2() {
        var ddnnf = createExample2();

        assertFalse(ddnnf.evaluate(1, 2, 3));
        assertFalse(ddnnf.evaluate(1, 2, -3));
        assertTrue(ddnnf.evaluate(1, -2, 3));
        assertTrue(ddnnf.evaluate(1, -2, -3));
        assertFalse(ddnnf.evaluate(-1, 2, 3));
        assertTrue(ddnnf.evaluate(-1, 2, -3));
        assertTrue(ddnnf.evaluate(-1, -2, 3));
        assertFalse(ddnnf.evaluate(-1, -2, -3));
    }

    /**
     * Creates a d-DNNF that is (supposed to be) equivalent to that in the file
     * {@code example-2.nnf}.
     *
     * @return The created d-DNNF.
     */
    protected abstract DecisionDnnf createExample2();

}
