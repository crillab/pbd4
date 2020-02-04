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

    private final PseudoBooleanFormula decorated;
    
    private final BitSet activeConstraint;
    
    public SubPseudoBooleanFormula(PseudoBooleanFormula decorated) {
        this.decorated = decorated;
        this.activeConstraint = new BitSet(decorated.numberOfConstraints());
    }
    
    public void activate(int constrIndex) {
        activeConstraint.set(constrIndex);
    }
    
    public boolean isActive(int constrIndex) {
        return activeConstraint.get(constrIndex);
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.input.PseudoBooleanFormula#numberOfVariables()
     */
    @Override
    public int numberOfVariables() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.input.PseudoBooleanFormula#numberOfConstraints()
     */
    @Override
    public int numberOfConstraints() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.input.PseudoBooleanFormula#variables()
     */
    @Override
    public IVecInt variables() {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.input.PseudoBooleanFormula#simplify(int[])
     */
    @Override
    public PseudoBooleanFormula simplify(int... v) {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.input.PseudoBooleanFormula#simplify(org.sat4j.specs.IVecInt)
     */
    @Override
    public PseudoBooleanFormula simplify(IVecInt assignment) {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.input.PseudoBooleanFormula#cutset()
     */
    @Override
    public IVecInt cutset() {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.input.PseudoBooleanFormula#connectedComponents()
     */
    @Override
    public Collection<PseudoBooleanFormula> connectedComponents() {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.input.PseudoBooleanFormula#assumptions()
     */
    @Override
    public IVecInt assumptions() {
        // TODO Auto-generated method stub
        return null;
    }

}

