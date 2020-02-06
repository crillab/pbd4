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

import java.io.Serializable;

import org.sat4j.core.LiteralsUtils;
import org.sat4j.core.VecInt;
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
    private static final long serialVersionUID = -8556073018543337864L;

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
        propagatedLiterals = new VecInt(solverService.nVars());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.SearchListenerAdapter#propagating(int)
     */
    @Override
    public void propagating(int p) {
        propagatedLiterals.push(LiteralsUtils.toDimacs(p));
    }

    /**
     * Gives the literals that have been propagated while this listener was listening.
     *
     * @return The vector of propagated literals.
     */
    public IVecInt getPropagatedLiterals() {
        return propagatedLiterals;
    }

    /**
     * Resets this listener, to forget all literals that were propagated previously.
     */
    public void reset() {
        propagatedLiterals.clear();
    }

}
