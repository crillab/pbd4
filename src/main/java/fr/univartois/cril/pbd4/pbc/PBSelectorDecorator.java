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

package fr.univartois.cril.pbd4.pbc;

import java.math.BigInteger;

import org.sat4j.core.ConstrGroup;
import org.sat4j.core.Vec;
import org.sat4j.pb.GroupPBSelectorSolver;
import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.constraints.pb.PBConstr;
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
final class PBSelectorDecorator extends GroupPBSelectorSolver {

    private int groupId = 1;

    private IVec<IConstr> constraints;

    PBSelectorDecorator(IPBSolver solver) {
        super(solver);
    }

    @Override
    public void setExpectedNumberOfClauses(int nb) {
        super.setExpectedNumberOfClauses(nb);
        this.constraints = new Vec<>(nb);
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, IVec<BigInteger> coeffs, BigInteger degree)
            throws ContradictionException {
        var constr = addAtLeast(literals, coeffs, degree, groupId++);
        constraints.push(constr);
        return constr;
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        var constr = addAtLeast(literals, coeffs, degree, groupId++);
        constraints.push(constr);
        return constr;
    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVec<BigInteger> coeffs, BigInteger degree)
            throws ContradictionException {
        var constr = addAtMost(literals, coeffs, degree, groupId++);
        constraints.push(constr);
        return constr;
    }

    @Override
    public IConstr addAtMost(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        var constr = addAtMost(literals, coeffs, degree, groupId++);
        constraints.push(constr);
        return constr;
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVec<BigInteger> coeffs, BigInteger weight)
            throws ContradictionException {
        var constr = addExactly(literals, coeffs, weight, groupId++);
        var group = (ConstrGroup) constr;
        constraints.push(group.getConstr(0));
        return constr;
    }

    @Override
    public IConstr addExactly(IVecInt literals, IVecInt coeffs, int weight)
            throws ContradictionException {
        var constr = addExactly(literals, coeffs, weight, groupId++);
        var group = (ConstrGroup) constr;
        constraints.push(group.getConstr(0));
        return constr;
    }

    @Override
    public IConstr addPseudoBoolean(IVecInt lits, IVec<BigInteger> coeffs, boolean moreThan,
            BigInteger d) throws ContradictionException {
        var constr = moreThan ? addAtLeast(lits, coeffs, d) : addAtMost(lits, coeffs, d);
        constraints.push(constr);
        return constr;
    }

    @Override
    public void addAllClauses(IVec<IVecInt> clauses) throws ContradictionException {
        for (var it = clauses.iterator(); it.hasNext();) {
            addClause(it.next());
        }
    }

    @Override
    public IConstr addAtLeast(IVecInt literals, int degree) throws ContradictionException {
        var constr = addAtLeast(literals, degree, groupId++);
        constraints.push(constr);
        return constr;
    }

    @Override
    public IConstr addAtMost(IVecInt literals, int degree) throws ContradictionException {
        var constr = addAtMost(literals, degree, groupId++);
        constraints.push(constr);
        return constr;
    }

    @Override
    public IConstr addBlockingClause(IVecInt literals) throws ContradictionException {
        var constr = addClause(literals);
        constraints.push(constr);
        return constr;
    }

    @Override
    public IConstr addClause(IVecInt literals) throws ContradictionException {
        var constr = addClause(literals, groupId++);
        constraints.push(constr);
        return constr;
    }

    @Override
    public IConstr addExactly(IVecInt literals, int n) throws ContradictionException {
        var constr = addExactly(literals, n, groupId++);
        var group = (ConstrGroup) constr;
        constraints.push(group.getConstr(0));
        return constr;
    }

    @Override
    public IConstr addParity(IVecInt literals, boolean even) {
        throw new UnsupportedOperationException();
    }
    
    public PBConstr getConstraint(int i) {
        return (PBConstr) constraints.get(i);
    }

}
