/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Romain WALLON.
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

package fr.univartois.cril.pbd4.solver;

import org.sat4j.specs.IVecInt;

/**
 * The PseudoBooleanSolver provides an interface for interacting with a pseudo-Boolean
 * solver to be used during the compilation process.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface PseudoBooleanSolver {

    /**
     * Applies Boolean Constraint Propagation (BCP) to the underlying pseudo-Boolean
     * formula.
     *
     * @param assumptions The assumptions to make on the variables of the formula.
     *
     * @return The status of the solver after it has applied BCP.
     */
    SolverStatus propagate(int... assumptions);

    /**
     * Gives the vector of the literals that have been propagated.
     * The content of the vector may be inconsistent if the previous call to
     * {@link #propagate()} returned {@link SolverStatus#UNSATISFIABLE}.
     *
     * @return The propagated literals.
     *
     * @see #propagate()
     */
    IVecInt getPropagatedLiterals();

    /**
     * Gives the score of the given variable, as used by the solver to decide which
     * variable to assign next.
     *
     * @param variable The variable to get the score of.
     *
     * @return The score of the variable.
     */
    double scoreOf(int variable);

}
