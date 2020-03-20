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

import java.io.Serializable;
import java.math.BigInteger;

import org.sat4j.core.ConstrGroup;
import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.pb.GroupPBSelectorSolver;
import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.constraints.pb.PBConstr;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

/**
 * The PBSelectorSolver decorates an {@link IPBSolver} to allow to deactivate any of its
 * constraints.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class PBSelectorSolver extends GroupPBSelectorSolver {

    /**
     * The {@code serialVersionUID} of this {@link Serializable} class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The vector of the constraints in the formula to solve.
     */
    private IVec<IConstr> constraints;

    /**
     * The array associating to each variable the index of the constraints in which it
     * appears.
     */
    private IVecInt[] constraintsContainingVariable;

    /**
     * The identifier of the current constraint.
     */
    private int currentIdentifier = 1;

    /**
     * Creates a new PBSelectorSolver.
     *
     * @param solver The solver to decorate.
     */
    public PBSelectorSolver(IPBSolver solver) {
        super(solver);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#setExpectedNumberOfClauses(int)
     */
    @Override
    public void setExpectedNumberOfClauses(int nb) {
        super.setExpectedNumberOfClauses(nb);
        this.constraints = new Vec<>(nb);
        this.constraintsContainingVariable = new IVecInt[nVars() + 1];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#nConstraints()
     */
    @Override
    public int nConstraints() {
        return constraints.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.GroupPBSelectorSolver#addAtLeast(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVec, java.math.BigInteger)
     */
    @Override
    public IConstr addAtLeast(IVecInt literals, IVec<BigInteger> coeffs, BigInteger degree)
            throws ContradictionException {
        var constr = addAtLeast(literals, coeffs, degree, currentIdentifier++);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.GroupPBSelectorSolver#addAtLeast(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addAtLeast(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        var constr = addAtLeast(literals, coeffs, degree, currentIdentifier++);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.GroupPBSelectorSolver#addAtMost(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVec, java.math.BigInteger)
     */
    @Override
    public IConstr addAtMost(IVecInt literals, IVec<BigInteger> coeffs, BigInteger degree)
            throws ContradictionException {
        var constr = addAtMost(literals, coeffs, degree, currentIdentifier++);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.GroupPBSelectorSolver#addAtMost(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addAtMost(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        var constr = addAtMost(literals, coeffs, degree, currentIdentifier++);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.GroupPBSelectorSolver#addExactly(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVec, java.math.BigInteger)
     */
    @Override
    public IConstr addExactly(IVecInt literals, IVec<BigInteger> coeffs, BigInteger weight)
            throws ContradictionException {
        var group = new ConstrGroup();

        // Adding the at-least constraint.
        var atLeast = addAtLeast(literals, coeffs, weight, currentIdentifier++);
        keepConstraint(literals, atLeast);
        group.add(atLeast);

        // Removing the selector that has been added when adding the at-least constraint.
        literals.pop();
        coeffs.pop();

        // Adding the at-most constraint.
        var atMost = addAtMost(literals, coeffs, weight, currentIdentifier++);
        keepConstraint(literals, atMost);
        group.add(atMost);

        return group;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.GroupPBSelectorSolver#addExactly(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addExactly(IVecInt literals, IVecInt coeffs, int weight)
            throws ContradictionException {
        // Converting the coefficients to big integers.
        var bigCoeffs = new Vec<BigInteger>(coeffs.size());
        for (var it = coeffs.iterator(); it.hasNext();) {
            bigCoeffs.push(BigInteger.valueOf(it.next()));
        }

        return addExactly(literals, bigCoeffs, BigInteger.valueOf(weight));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.GroupPBSelectorSolver#addPseudoBoolean(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVec, boolean, java.math.BigInteger)
     */
    @Override
    public IConstr addPseudoBoolean(IVecInt literals, IVec<BigInteger> coeffs, boolean moreThan,
            BigInteger d) throws ContradictionException {
        var constr = moreThan ? addAtLeast(literals, coeffs, d) : addAtMost(literals, coeffs, d);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addAllClauses(org.sat4j.specs.IVec)
     */
    @Override
    public void addAllClauses(IVec<IVecInt> clauses) throws ContradictionException {
        for (var it = clauses.iterator(); it.hasNext();) {
            addClause(it.next());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addAtLeast(org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addAtLeast(IVecInt literals, int degree) throws ContradictionException {
        var constr = addAtLeast(literals, degree, currentIdentifier++);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addAtMost(org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addAtMost(IVecInt literals, int degree) throws ContradictionException {
        var constr = addAtMost(literals, degree, currentIdentifier++);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addBlockingClause(org.sat4j.specs.IVecInt)
     */
    @Override
    public IConstr addBlockingClause(IVecInt literals) throws ContradictionException {
        var constr = addClause(literals);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addClause(org.sat4j.specs.IVecInt)
     */
    @Override
    public IConstr addClause(IVecInt literals) throws ContradictionException {
        var constr = addClause(literals, currentIdentifier++);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addExactly(org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addExactly(IVecInt literals, int n) throws ContradictionException {
        var constr = addExactly(literals, n, currentIdentifier++);
        keepConstraint(literals, constr);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addParity(org.sat4j.specs.IVecInt, boolean)
     */
    @Override
    public IConstr addParity(IVecInt literals, boolean even) {
        throw new UnsupportedOperationException();
    }

    /**
     * Keeps a constraint in the structure managing the constraints and their variables.
     *
     * @param literals The literals of the constraint to add.
     * @param constr The constraint to add.
     */
    private void keepConstraint(IVecInt literals, IConstr constr) {
        int index = constraints.size();
        int nbConstr = storeConstraint(constr);
        storeVariables(literals, index, nbConstr);
    }

    /**
     * Stores a constraint in the structure managing the constraints.
     * If {@code constr} is a {@link ConstrGroup}, all the constraints are added, instead
     * of just the group.
     *
     * @param constr The constraint to store.
     *
     * @return The number of constraints that have been stored.
     */
    private int storeConstraint(IConstr constr) {
        if (constr instanceof ConstrGroup) {
            // The constraints are retrieved from the group.
            var group = (ConstrGroup) constr;
            int nbConstr = group.size();

            for (int i = 0; i < nbConstr; i++) {
                constraints.push(group.getConstr(i));
            }

            return nbConstr;
        }

        // Simply adding this (single) constraint.
        constraints.push(constr);
        return 1;
    }

    /**
     * Associates to each variable in {@code literals} some constraints in which it
     * appears.
     *
     * @param literals The literals to store.
     * @param startIndex The index at which the constraint(s) is (are) stored in
     *        {@link #constraints}.
     * @param nbConstr The number of successive constraints containing the variables.
     */
    private void storeVariables(IVecInt literals, int startIndex, int nbConstr) {
        for (var it = literals.iterator(); it.hasNext();) {
            var variable = Math.abs(it.next());

            if (variable > nVars()) {
                // This variable is a selector.
                continue;
            }

            if (constraintsContainingVariable[variable] == null) {
                constraintsContainingVariable[variable] = new VecInt();
            }

            for (int i = startIndex; i < nbConstr + startIndex; i++) {
                constraintsContainingVariable[variable].push(i);
            }
        }
    }

    /**
     * Gives the {@code i}-th constraint in the formula.
     * 
     * @param i The index of the constraint to get.
     *
     * @return The {@code i}-th constraint.
     */
    public PBConstr getConstraint(int i) {
        return (PBConstr) constraints.get(i);
    }

    /**
     * Gives the indices of the constraint containing the given variable.
     *
     * @param variable The variable to consider.
     *
     * @return The indices of the constraints containing {@code variable}.
     */
    public IVecInt getConstraintsContaining(int variable) {
        return constraintsContainingVariable[variable];
    }

}
