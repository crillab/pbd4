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

import java.util.BitSet;
import java.util.Collection;

import org.sat4j.specs.IVecInt;


/**
 * The SubPseudoBooleanFormula
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class SubPseudoBooleanFormula implements PseudoBooleanFormula {

    private final IVecInt assumptions;
    
    private final PseudoBooleanFormula decorated;
    
    private final BitSet activeConstraint;
    
    public SubPseudoBooleanFormula(OriginalPseudoBooleanFormula decorated, IVecInt assumptions) {
        this.decorated = decorated;
        this.activeConstraint = new BitSet(decorated.numberOfConstraints());
        this.assumptions = assumptions;
    }

    @Override
    public int numberOfVariables() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int numberOfConstraints() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IVecInt variables() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PseudoBooleanFormula satisfy(int literal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IVecInt cutset() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<PseudoBooleanFormula> connectedComponents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropagationOutput propagate() {
        // TODO Auto-generated method stub
        return null;
    }
    

}

