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

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

/**
 * The PropagationOutput represents the result of Boolean Constraint Propagation on a
 * pseudo-Boolean formula.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class PropagationOutput {

    /**
     * The status of the solver after BCP.
     */
    private final SolverStatus status;

    /**
     * The literals that have been propagated during BCP.
     */
    private final IVecInt propagatedLiterals;

    /**
     * The original formula on which BCP is applied.
     */
    private final OriginalPseudoBooleanFormula original;

    /**
     * Creates a new PropagationOutput.
     *
     * @param status The status of the solver after BCP.
     * @param propagatedLiterals The literals that have been propagated during BCP.
     * @param original The original formula on which BCP is applied.
     */
    private PropagationOutput(SolverStatus status, IVecInt propagatedLiterals,
            OriginalPseudoBooleanFormula original) {
        this.status = status;
        this.propagatedLiterals = propagatedLiterals;
        this.original = original;
    }
    
    /**
     * Creates a PropagationOutput for an unsatisfiable formula.
     *
     * @return The created propagation output.
     */
    static PropagationOutput unsatisfiable() {
        return new PropagationOutput(SolverStatus.UNSATISFIABLE, VecInt.EMPTY, null);
    }

    /**
     * Creates a PropagationOutput for a satisfiable formula.
     *
     * @param propagatedLiterals The literals that have been propagated during BCP.
     *
     * @return The created propagation output.
     */
    static PropagationOutput satisfiable(IVecInt propagatedLiterals) {
        return new PropagationOutput(SolverStatus.SATISFIABLE, propagatedLiterals, null);
    }

    /**
     * Creates a PropagationOutput for a formula for which the status is not known yet.
     *
     * @param propagatedLiterals The literals that have been propagated during BCP.
     * @param original The original formula on which BCP is applied.
     *
     * @return The created propagation output.
     */
    static PropagationOutput unknown(IVecInt propagatedLiterals, OriginalPseudoBooleanFormula original) {
        return new PropagationOutput(SolverStatus.UNKNOWN, propagatedLiterals, original);
    }

    /**
     * Checks whether the propagation has proven that the formula is satisfiable.
     * In this case, the propagated literals form an implicant of the formula.
     *
     * @return Whether the formula is satisfiable.
     */
    public boolean isSatisfiable() {
        return status == SolverStatus.SATISFIABLE;
    }

    /**
     * Checks whether the propagation has proven that the formula is unsatisfiable.
     *
     * @return Whether the formula is unsatisfiable.
     */
    public boolean isUnsatisfiable() {
        return status == SolverStatus.UNSATISFIABLE;
    }

    /**
     * Checks whether the propagation has not been able to prove anything about the formula.
     *
     * @return Whether the status of the formula is unknown.
     */
    public boolean isUnknown() {
        return status == SolverStatus.UNKNOWN;
    }

    /**
     * Gives the literals that have been propagated during BCP.
     * The result of this method is undefined if the solver status was
     * {@link SolverStatus#UNSATISFIABLE}.
     *
     * @return The literals that have been propagated during BCP.
     */
    public IVecInt getPropagatedLiterals() {
        return propagatedLiterals;
    }

    /**
     * Gives the formula obtained after having simplified the formula with respect to the
     * literals that have been propagated.
     * The result of this method is undefined if the solver status was
     * {@link SolverStatus#UNSATISFIABLE}.
     * 
     * @return The simplified pseudo-Boolean formula.
     *
     * @see #getStatus()
     */
    public PseudoBooleanFormula getSimplifiedFormula() {
        for (int i = 0; i < original.numberOfConstraints(); i++) {
            var constr = original.getConstraint(i);
            for (int l = 0; l < constr.size(); l++) {
                // Ajouter les coef
                // Ajouter les liens dans l'hypergraphe

            }
        }
        return null;
    }

}
