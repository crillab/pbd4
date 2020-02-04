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

package fr.univartois.cril.pbd4.input;

import org.sat4j.specs.IVecInt;

/**
 * The PropagationOutput
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public class PropagationOutput {

    private final SolverStatus status;
    
    private final IVecInt propagatedLiterals;
    
    private final PseudoBooleanFormula simplifiedFormula;

    public PropagationOutput(SolverStatus status, IVecInt propagatedLiterals,
            PseudoBooleanFormula simplifiedFormula) {
        super();
        this.status = status;
        this.propagatedLiterals = propagatedLiterals;
        this.simplifiedFormula = simplifiedFormula;
    }
    

    
    public SolverStatus getStatus() {
        return status;
    }

    
    public IVecInt getPropagatedLiterals() {
        return propagatedLiterals;
    }

    
    public PseudoBooleanFormula getSimplifiedFormula() {
        return simplifiedFormula;
    }
    
}

