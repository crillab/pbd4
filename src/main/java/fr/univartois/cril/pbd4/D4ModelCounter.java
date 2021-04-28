/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Univ Artois & CNRS.
 * All rights reserved.
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

package fr.univartois.cril.pbd4;

import java.math.BigInteger;
import java.util.List;

import org.sat4j.specs.IVecInt;

/**
 * The D4ModelCounter implements the D4 algorithm for counting the number of
 * models of the input formula.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
final class D4ModelCounter extends AbstractD4<BigInteger, BigInteger> {

    /**
     * Creates a new D4ModelCounter.
     *
     * @param configuration The configuration of the counter.
     */
    D4ModelCounter(D4 configuration) {
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
     * @see fr.univartois.cril.pbd4.AbstractD4#implicant(int,
     * org.sat4j.specs.IVecInt)
     */
    @Override
    protected BigInteger implicant(int nbFreeVariables, IVecInt implicant) {
        return cached(nbFreeVariables, implicant, BigInteger.ONE);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.AbstractD4#cached(int,
     * org.sat4j.specs.IVecInt, java.lang.Object)
     */
    @Override
    protected BigInteger cached(int nbFreeVariables, IVecInt propagatedLiterals, BigInteger cached) {
        return conjunction(nbFreeVariables, propagatedLiterals, List.of(cached));
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.AbstractD4#decision(int, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    protected BigInteger decision(int variable, BigInteger ifTrue, BigInteger ifFalse) {
        return ifTrue.add(ifFalse);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.AbstractD4#conjunction(int,
     * org.sat4j.specs.IVecInt, java.util.List)
     */
    @Override
    protected BigInteger conjunction(int nbFreeVariables, IVecInt literals, List<BigInteger> conjuncts) {
        var conjunctsModels = conjuncts.stream().reduce(BigInteger.ONE, BigInteger::multiply);
        return conjunctsModels.shiftLeft(nbFreeVariables);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.AbstractD4#toFinalResult(java.lang.Object)
     */
    @Override
    protected BigInteger toFinalResult(BigInteger intermediateResult) {
        return intermediateResult;
    }

}
