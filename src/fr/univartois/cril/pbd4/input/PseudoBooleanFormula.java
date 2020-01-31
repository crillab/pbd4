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

package fr.univartois.cril.pbd4.input;

import java.util.Collection;

import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.input.hypergraph.PseudoBooleanFormulaHypergraph;

/**
 * The PseudoBooleanFormula stores all data
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface PseudoBooleanFormula {

    int numberOfVariables();
    
    int numberOfConstraints();
    
    IVecInt variables();
    
    PseudoBooleanFormula simplify(int... v);

    PseudoBooleanFormula simplify(IVecInt assignment);
    
    IVecInt cutset();
    
    Collection<PseudoBooleanFormula> connectedComponents();
    
    IVecInt assumptions();
    
}

