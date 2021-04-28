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

package fr.univartois.cril.pbd4.partitioning;

import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;

/**
 * The CutsetComputationStrategy defines a strategy for computing a cutset of
 * the dual hypergraph associated to a pseudo-Boolean formula.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
@FunctionalInterface
public interface CutsetComputationStrategy {

    /**
     * Notifies this strategy that the compilation starts.
     * It may be used to perform some preliminary initializations, for instance.
     *
     * @implSpec The default implementation does nothing.
     */
    default void compilationStarts() {
        // Nothing to do by default.
    }

    /**
     * Computes a cutset of the dual hypergraph of the given pseudo-Boolean formula.
     *
     * @param formula The formula to compute a cutset of.
     *
     * @return The computed cutset, as a vector of variables.
     */
    IVecInt cutset(PseudoBooleanFormula formula);

    /**
     * Notifies this strategy that the compilation has ended.
     * It may be used to perform some clean up, for instance.
     *
     * @implSpec The default implementation does nothing.
     */
    default void compilationEnds() {
        // Nothing to do by default.
    }

}
