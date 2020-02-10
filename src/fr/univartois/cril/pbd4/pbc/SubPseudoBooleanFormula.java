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

import org.sat4j.core.LiteralsUtils;
import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
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

    private final OriginalPseudoBooleanFormula decorated;

    private final BitSet inactiveConstraint;

    private IVecInt variables;

    public SubPseudoBooleanFormula(OriginalPseudoBooleanFormula decorated, IVecInt assumptions) {
        this.decorated = decorated;
        this.inactiveConstraint = new BitSet(decorated.numberOfConstraints());
        this.assumptions = assumptions;
        assumptions.sort();
    }

    public SubPseudoBooleanFormula(OriginalPseudoBooleanFormula decorated, IVecInt assumptions,
            BitSet activeConstraints) {
        this.decorated = decorated;
        this.inactiveConstraint = activeConstraints;
        this.assumptions = assumptions;
    }

    @Override
    public int numberOfVariables() {
        return decorated.numberOfVariables() - assumptions.size();
    }

    @Override
    public int numberOfConstraints() {
        return inactiveConstraint.size() - inactiveConstraint.cardinality();
    }

    @Override
    public IVecInt variables() {

    }

    @Override
    public PseudoBooleanFormula satisfy(int literal) {
        var newAssumptions = new VecInt(assumptions.size() + 1);
        assumptions.copyTo(newAssumptions);
        newAssumptions.push(literal);
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
        var effectiveAssumptions = new VecInt();
        assumptions.copyTo(effectiveAssumptions);
        for (int i = 0; i < inactiveConstraint.size(); i++) {
            if (inactiveConstraint.get(i)) {
                effectiveAssumptions.push(LiteralsUtils.posLit(numberOfVariables() + i + 1));
            } else {
                effectiveAssumptions.push(LiteralsUtils.negLit(numberOfVariables() + i + 1));
            }
        }

        return decorated.propagate(effectiveAssumptions);
    }

}
