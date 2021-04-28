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

package fr.univartois.cril.pbd4.listener;

import java.util.Collection;

import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;

/**
 * The D4Listener interface defines the contract for listening to events emitted
 * during the execution of D4's algorithm.
 * By default, all methods do nothing when invoked, so that listeners only need
 * to implement relevant methods depending on their needs.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public interface D4Listener {

    /**
     * Initializes this listener at the beginning of the compilation process.
     *
     * @param formula The input formula to compile.
     */
    default void init(PseudoBooleanFormula formula) {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that the compilation starts.
     */
    default void start() {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that Boolean Constraint Propagation is being run.
     */
    default void propagate() {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that a sub-formula is proven to be unsatisfiable.
     *
     * @param subFormula The unsatisfiable sub-formula.
     */
    default void unsatisfiable(PseudoBooleanFormula subFormula) {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that some literals are being propagated.
     *
     * @param propagatedLiterals The propagated literals.
     * @param nbFreeVariables The number of variables that remain unassigned in the
     *        sub-formula.
     */
    default void propagated(IVecInt propagatedLiterals, int nbFreeVariables) {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that an implicant of a sub-formula has been found.
     *
     * @param subFormula The sub-formula for which an implicant has been found.
     */
    default void implicant(PseudoBooleanFormula subFormula) {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that a sub-formula has been retrieved in the cache.
     *
     * @param cachedFormula The formula that has been retrieved.
     */
    default void cached(PseudoBooleanFormula cachedFormula) {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that the connected components of the current formula
     * are being computed.
     */
    default void computeConnectedComponents() {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that the connected components of the current formula
     * have been computed.
     *
     * @param connectedComponents The computed components.
     */
    default void connectedComponentsFound(Collection<PseudoBooleanFormula> connectedComponents) {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that a cutset of the current formula is being computed.
     */
    default void computeCutset() {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that a cutset of the current formula has been computed.
     *
     * @param cutset The computed cutset, as a vector of variables.
     */
    default void cutsetFound(IVecInt cutset) {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that the compiler tries to branch on a variable.
     *
     * @param v The variable on which to branch.
     */
    default void branchOn(int v) {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that a conjunction of sub-formulae is being cached.
     */
    default void cachingConjunction() {
        // Nothing to do by default.
    }

    /**
     * Notifies this listener that the compilation is finished.
     */
    default void end() {
        // Nothing to do by default.
    }

}
