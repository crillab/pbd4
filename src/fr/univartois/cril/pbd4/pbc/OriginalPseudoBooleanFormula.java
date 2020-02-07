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

import static fr.univartois.cril.pbd4.pbc.RangeVecInt.range;
import java.util.Collection;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.core.ICDCL;
import org.sat4j.minisat.orders.SubsetVarOrder;
import org.sat4j.pb.constraints.pb.PBConstr;
import org.sat4j.specs.ISolverService;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;


/**
 * The OriginalPseudoBooleanFormula
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public class OriginalPseudoBooleanFormula implements PseudoBooleanFormula {


    private final PBSelectorDecorator solver;

    private final ISolverService engine;
    
    /**
     * The unit-propagation listener used to record the propagations performed during the
     * execution of {@link #adaptee}.
     */
    private final UnitPropagationListener listener;

    /**
     * The scores of the VSIDS heuristic used by the solver.
     */
    private final double[] vsidsScores;

    /**
     * The scores of the DLCS heuristic (i.e., the number of occurrences of each variable
     * in the formula).
     */
    private final int[] dlcsScores;
    
    private final IVecInt variables;
    
    /**
     * Creates a new Sat4jAdapter.
     *
     * @param solver The solver to adapt.
     * @param dlcsScores The scores of the DLCS heuristic
     */
    OriginalPseudoBooleanFormula(PBSelectorDecorator solver, int[] dlcsScores) {
        this.solver = solver;
        this.solver.setKeepSolverHot(true);
        this.engine = (ISolverService) solver.getSolvingEngine();
        this.listener = new UnitPropagationListener();
        this.vsidsScores = engine.getVariableHeuristics();
        this.dlcsScores = dlcsScores;
        this.variables = range(1, numberOfVariables());
    }

    @Override
    public int numberOfVariables() {
        return solver.nVars() - solver.getVarToHighLevel().size();
    }

    @Override
    public int numberOfConstraints() {
        return solver.nConstraints();
    }

    @Override
    public IVecInt variables() {
        return variables;
    }

    @Override
    public PseudoBooleanFormula satisfy(int literal) {
        return new SubPseudoBooleanFormula(this, VecInt.of(literal));
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
        return propagate(VecInt.EMPTY);
    }
    
    public PropagationOutput propagate(IVecInt assumptions) {
        try {
            solver.externalState();
            // Setting up the solver.
            listener.reset();
            solver.setSearchListener(listener);
            ((ICDCL<?>) engine).setOrder(new SubsetVarOrder(new int[0]));

            if (solver.isSatisfiable(assumptions)) {
                // The solver has found a solution.
                return null;
            }

            // The formula is necessarily unsatisfiable.
            // Otherwise, an exception would have been thrown.
            return null;

        } catch (TimeoutException e) {
            // The solver could not determine whether the formula was satisfiable.
            var formula = new SubPseudoBooleanFormula(this, listener.getPropagatedLiterals());
            return null;
        }
    }
    
    PBConstr getConstraint(int i) {
        return solver.getConstraint(i);
    }
}

