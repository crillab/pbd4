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

package fr.univartois.cril.pbd4;

import java.math.BigInteger;
import java.util.Collection;

import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.input.PseudoBooleanFormula;

/**
 * The D4Counter specifies the D4 algorithm for counting the number of models of the input
 * formula.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class D4Counter extends AbstractD4<BigInteger> {

    /**
     * Creates a new D4Counter.
     *
     * @param configuration The configuration of the counter.
     */
    D4Counter(D4 configuration) {
        super(configuration);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#unsatisfiable()
     */
    @Override
    protected BigInteger unsatisfiable() {
        return BigInteger.ZERO;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#model(org.sat4j.specs.IVecInt)
     */
    @Override
    protected BigInteger model(PseudoBooleanFormula formula, IVecInt propagatedLiterals) {
        int numberOfVariables = formula.numberOfVariables();
        int remainingVariables = numberOfVariables - propagatedLiterals.size();
        return BigInteger.ONE.shiftLeft(remainingVariables);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#cached(org.sat4j.specs.IVecInt,
     * java.lang.Object)
     */
    @Override
    protected BigInteger cached(IVecInt propagatedLiterals, BigInteger cached) {
        return cached;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#conjunctionOf(java.util.Collection)
     */
    @Override
    protected BigInteger conjunctionOf(Collection<BigInteger> conjuncts) {
        return conjuncts.stream().reduce(BigInteger.ONE, BigInteger::multiply);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#ifThenElse(int, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    protected BigInteger ifThenElse(int variable, BigInteger ifTrue, BigInteger ifFalse) {
        return ifTrue.add(ifFalse);
    }

}
