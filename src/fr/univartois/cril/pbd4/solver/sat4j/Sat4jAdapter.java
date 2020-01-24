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

import org.sat4j.minisat.core.Solver;
import org.sat4j.minisat.orders.SubsetVarOrder;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

import fr.univartois.cril.pbd4.solver.PseudoBooleanSolver;
import fr.univartois.cril.pbd4.solver.SolverStatus;

/**
 * The Sat4jAdapter
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public class Sat4jAdapter implements PseudoBooleanSolver {

    private final Solver<?> adaptee;

    private final UnitPropagationListener listener;
    
    private final double[] vsidsScores;
    
    private final int[] dlcsScores;

    public Sat4jAdapter(Solver<?> adaptee) {
        this.adaptee = adaptee;
        this.adaptee.setKeepSolverHot(true);
        this.listener = new UnitPropagationListener();
        this.vsidsScores = adaptee.getVariableHeuristics();
        this.dlcsScores = new int[adaptee.nVars() + 1];
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.solver.PseudoBooleanSolver#propagate()
     */
    @Override
    public SolverStatus propagate() {
        try {
            adaptee.setOrder(new SubsetVarOrder(new int[0]));
            listener.reset();
            adaptee.setSearchListener(listener);
            if (adaptee.isSatisfiable()) {
                return SolverStatus.SATISFIABLE;
            }
            
            return SolverStatus.UNSATISFIABLE;

        } catch (TimeoutException e) {
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
    
    @Override
    public double scoreOf(int variable) {
        return vsidsScores[variable] + .5 * dlcsScores[variable];
    }

}
