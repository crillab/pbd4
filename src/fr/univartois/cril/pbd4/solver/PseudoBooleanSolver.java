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
 * The PseudoBooleanSolver interface defines the contract for a pseudo-Boolean solver
 * used during the compilation process.
 * 
 * This interface is designed to allow any solver to be plugged into the compiler by
 * implementing adapters.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface PseudoBooleanSolver {

    /**
     * Applies Boolean Constraint Propagation (BCP) to the underlying formula.
     *
     * @return The status of the BCP.
     */
    SolverStatus propagate();

    /**
     * Gives the vector of the literals that have been propagated.
     * The content of the vector may be inconsistent if {@link #propagate()} returned
     * {@code false} on the last call.
     * 
     * @return The propagated literals.
     * 
     * @see #propagate()
     */
    IVecInt getPropagatedLiterals();
    

    
    default int compare(int var1, int var2) {
        return Double.compare(scoreOf(var2), scoreOf(var1));
    }

    double scoreOf(int variable);

}
