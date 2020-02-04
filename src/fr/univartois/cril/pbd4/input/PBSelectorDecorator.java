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

import java.math.BigInteger;

import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.PBSolverDecorator;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

/**
 * The Sat4jCompilationDecorator
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class PBSelectorDecorator extends PBSolverDecorator {

    private int realNVar;

    private int selector;

    public PBSelectorDecorator(IPBSolver solver) {
        super(solver);
    }

    @Override
    public void setExpectedNumberOfClauses(int nb) {
        realNVar = nVars();
        selector = realNVar + 1;
        newVar(nb);
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, IVec<BigInteger> coeffs, BigInteger degree)
            throws ContradictionException {
        literals.push(selector);
        coeffs.push(degree);
        selector++;
        return super.addAtLeast(literals, coeffs, degree);
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        literals.push(selector);
        coeffs.push(degree);
        selector++;
        return super.addAtLeast(literals, coeffs, degree);
    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVec<BigInteger> coeffs, BigInteger degree)
            throws ContradictionException {
        return super.addAtMost(literals, coeffs, degree);
    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        return super.addAtMost(literals, coeffs, degree);
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVec<BigInteger> coeffs, BigInteger weight)
            throws ContradictionException {
        return super.addExactly(literals, coeffs, weight);
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVecInt coeffs, int weight)
            throws ContradictionException {
        return super.addExactly(literals, coeffs, weight);
    }

    @Override
    public IConstr addPseudoBoolean(IVecInt lits, IVec<BigInteger> coeffs, boolean moreThan,
            BigInteger d) throws ContradictionException {
        return super.addPseudoBoolean(lits, coeffs, moreThan, d);
    }

    @Override
    public void addAllClauses(IVec<IVecInt> clauses) throws ContradictionException {
        for (var it = clauses.iterator(); it.hasNext();) {
            addClause(it.next());
        }
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, int degree) throws ContradictionException {
        return super.addAtLeast(literals, degree);
    }

    @Override
    public IConstr addAtMost(IVecInt literals, int degree) throws ContradictionException {
        return super.addAtMost(literals, degree);
    }

    @Override
    public IConstr addBlockingClause(IVecInt literals) throws ContradictionException {
        literals.push(selector);
        selector++;
        return super.addBlockingClause(literals);
    }

    @Override
    public IConstr addClause(IVecInt literals) throws ContradictionException {
        literals.push(selector);
        selector++;
        return super.addClause(literals);
    }

    @Override
    public IConstr addExactly(IVecInt literals, int n) throws ContradictionException {
        return super.addExactly(literals, n);
    }

}
