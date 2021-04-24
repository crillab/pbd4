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

package fr.univartois.cril.pbd4.pbc;

import java.io.Serializable;

import org.sat4j.core.VecInt;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.ISolverService;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.SearchListenerAdapter;

/**
 * The UnitPropagationListener allows to record all propagations that were performed
 * during an execution of Sat4j.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class UnitPropagationListener extends SearchListenerAdapter<ISolverService> {

    /**
     * The {@code serialVersionUID} of this {@link Serializable} class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The number of variables in the formula.
     */
    private int numberOfVariables;
    
    /**
     * The vector in which to store propagated literals.
     */
    private IVecInt propagatedLiterals;

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.SearchListenerAdapter#init(org.sat4j.specs.ISolverService)
     */
    @Override
    public void init(ISolverService solverService) {
    	if (propagatedLiterals == null) {
            numberOfVariables = solverService.nVars();
    		propagatedLiterals = new VecInt(numberOfVariables);
    	}
    }

    /* 
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.SearchListenerAdapter#enqueueing(int, org.sat4j.specs.IConstr)
     */
    @Override
    public void enqueueing(int p, IConstr reason) {
        if ((reason != null) && (Math.abs(p) <= numberOfVariables)) {
            propagatedLiterals.push(p);
        }
    }

    /**
     * Gives the literals that have been propagated while this listener was listening, in
     * the DIMACS format.
     *
     * @return The vector of propagated literals, which may be modified afterwards.
     */
    public IVecInt getPropagatedLiterals() {
        return propagatedLiterals;
    }

    /**
     * Resets this listener, to forget all literals that were propagated previously.
     */
    public void reset() {
        if (propagatedLiterals != null) {
            propagatedLiterals.clear();
        }
    }

}
