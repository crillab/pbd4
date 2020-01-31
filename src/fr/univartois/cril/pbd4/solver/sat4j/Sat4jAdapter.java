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

import org.sat4j.core.VecInt;
import org.sat4j.minisat.core.Solver;
import org.sat4j.minisat.orders.SubsetVarOrder;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

import fr.univartois.cril.pbd4.solver.PseudoBooleanSolver;
import fr.univartois.cril.pbd4.solver.SolverStatus;

/**
 * The Sat4jAdapter adapts a {@link Solver} from Sat4j to use it as a
 * {@link PseudoBooleanSolver}.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class Sat4jAdapter implements PseudoBooleanSolver {

    /**
     * The solver being adapted.
     */
    private final Solver<?> adaptee;

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

    /**
     * Creates a new Sat4jAdapter.
     *
     * @param solver The solver to adapt.
     * @param dlcsScores The scores of the DLCS heuristic
     */
    public Sat4jAdapter(Solver<?> solver, int[] dlcsScores) {
        this.adaptee = solver;
        this.adaptee.setKeepSolverHot(true);
        this.listener = new UnitPropagationListener();
        this.vsidsScores = solver.getVariableHeuristics();
        this.dlcsScores = dlcsScores;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.solver.PseudoBooleanSolver#propagate(int[])
     */
    @Override
    public SolverStatus propagate(int... assumptions) {
        try {
            // Setting up the solver.
            listener.reset();
            adaptee.setSearchListener(listener);
            adaptee.setOrder(new SubsetVarOrder(new int[0]));

            if (adaptee.isSatisfiable(VecInt.of(assumptions))) {
                // The solver has found a solution.
                return SolverStatus.SATISFIABLE;
            }

            // The formula is necessarily unsatisfiable.
            // Otherwise, an exception would have been thrown.
            return SolverStatus.UNSATISFIABLE;

        } catch (TimeoutException e) {
            // The solver could not determine whether the formula was satisfiable.
            return SolverStatus.UNKNOWN;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.solver.PseudoBooleanSolver#getPropagatedLiterals()
     */
    @Override
    public IVecInt getPropagatedLiterals() {
        return listener.getPropagatedLiterals();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.solver.PseudoBooleanSolver#scoreOf(int)
     */
    @Override
    public double scoreOf(int variable) {
        return vsidsScores[variable] + .5 * dlcsScores[variable];
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.solver.PseudoBooleanSolver#numberOfVariables()
     */
    @Override
    public int numberOfVariables() {
        return adaptee.nVars();
    }

}
