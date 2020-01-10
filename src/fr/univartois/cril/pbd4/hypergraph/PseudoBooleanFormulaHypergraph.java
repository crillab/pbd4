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

package fr.univartois.cril.pbd4.hypergraph;

import org.sat4j.specs.IVecInt;

/**
 * The PseudoBooleanFormulaHypergraph defines the contract for a hypergraph representation
 * of a pseudo-Boolean formula.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface PseudoBooleanFormulaHypergraph {

    /**
     * Gives the cutset of this hypergraph, i.e. the identifier of the hyperedges to
     * remove in order to get a hypergraph made of (at least) two separate sub-hypergraph.
     * 
     * @return The cutset of this hypergraph.
     */
    IVecInt cutset();

}
