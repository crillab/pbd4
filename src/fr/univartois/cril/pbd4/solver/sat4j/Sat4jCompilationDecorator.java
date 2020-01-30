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

package fr.univartois.cril.pbd4.solver.sat4j;

import java.math.BigInteger;

import org.sat4j.core.ConstrGroup;
import org.sat4j.pb.PBSolverDecorator;
import org.sat4j.pb.core.PBSolver;
import org.sat4j.specs.Constr;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.input.hypergraph.PseudoBooleanFormulaHypergraphBuilder;

/**
 * The Sat4jCompilationDecorator
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public class Sat4jCompilationDecorator extends PBSolverDecorator {

    private final PseudoBooleanFormulaHypergraphBuilder builder;

    public Sat4jCompilationDecorator(PBSolver solver,
            PseudoBooleanFormulaHypergraphBuilder builder) {
        super(solver);
        this.builder = builder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.PBSolverDecorator#addAtLeast(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVec, java.math.BigInteger)
     */
    @Override
    public IConstr addAtLeast(IVecInt literals, IVec<BigInteger> coeffs, BigInteger degree)
            throws ContradictionException {
        var constr = super.addAtLeast(literals, coeffs, degree);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.PBSolverDecorator#addAtLeast(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addAtLeast(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        var constr = super.addAtLeast(literals, coeffs, degree);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.PBSolverDecorator#addAtMost(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVec, java.math.BigInteger)
     */
    @Override
    public IConstr addAtMost(IVecInt literals, IVec<BigInteger> coeffs, BigInteger degree)
            throws ContradictionException {
        var constr = super.addAtMost(literals, coeffs, degree);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.PBSolverDecorator#addAtMost(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addAtMost(IVecInt literals, IVecInt coeffs, int degree)
            throws ContradictionException {
        var constr = super.addAtMost(literals, coeffs, degree);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.PBSolverDecorator#addExactly(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVec, java.math.BigInteger)
     */
    @Override
    public IConstr addExactly(IVecInt literals, IVec<BigInteger> coeffs, BigInteger weight)
            throws ContradictionException {
        var constr = super.addExactly(literals, coeffs, weight);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.PBSolverDecorator#addExactly(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addExactly(IVecInt literals, IVecInt coeffs, int weight)
            throws ContradictionException {
        var constr = super.addExactly(literals, coeffs, weight);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.pb.PBSolverDecorator#addPseudoBoolean(org.sat4j.specs.IVecInt,
     * org.sat4j.specs.IVec, boolean, java.math.BigInteger)
     */
    @Override
    public IConstr addPseudoBoolean(IVecInt lits, IVec<BigInteger> coeffs, boolean moreThan,
            BigInteger d) throws ContradictionException {
        var constr = super.addPseudoBoolean(lits, coeffs, moreThan, d);
        builder.addConstraint(constr, lits);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addAtLeast(org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addAtLeast(IVecInt literals, int degree) throws ContradictionException {
        var constr = super.addAtLeast(literals, degree);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addAtMost(org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addAtMost(IVecInt literals, int degree) throws ContradictionException {
        var constr = super.addAtMost(literals, degree);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addBlockingClause(org.sat4j.specs.IVecInt)
     */
    @Override
    public IConstr addBlockingClause(IVecInt literals) throws ContradictionException {
        var constr = super.addBlockingClause(literals);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addClause(org.sat4j.specs.IVecInt)
     */
    @Override
    public IConstr addClause(IVecInt literals) throws ContradictionException {
        var constr = super.addClause(literals);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addExactly(org.sat4j.specs.IVecInt, int)
     */
    @Override
    public IConstr addExactly(IVecInt literals, int n) throws ContradictionException {
        var constr = super.addExactly(literals, n);
        builder.addConstraint(constr, literals);
        return constr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.tools.SolverDecorator#addParity(org.sat4j.specs.IVecInt, boolean)
     */
    @Override
    public IConstr addParity(IVecInt literals, boolean even) {
        var constr = super.addParity(literals, even);
        builder.addConstraint(constr, literals);
        return constr;
    }
    
    @Override
    public IConstr addConstr(Constr constr) {
        builder.addConstraint(constr);
        return super.addConstr(constr);
    }
    
    public IConstr addIConstr(IConstr constr) {
        if (constr instanceof Constr) {
            return addConstr((Constr) constr);
        }
        
        if (constr instanceof ConstrGroup) {
            // Equality constraint.
            var group = (ConstrGroup) constr;
            builder.addConstraint((Constr) group.getConstr(0));
            for (int i = 0; i < group.size(); i++) {
                super.addConstr((Constr) group.getConstr(i));
            }
            return constr;
        }
        
        throw new AssertionError("Unexpected constrint type");
    }

}
