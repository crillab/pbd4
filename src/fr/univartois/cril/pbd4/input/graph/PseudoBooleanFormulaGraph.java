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

package fr.univartois.cril.pbd4.input.graph;

import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

/**
 * The PseudoBooleanFormulaGraph defines the contract for a graph representation
 * of a pseudo-Boolean formula.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface PseudoBooleanFormulaGraph {

    /**
     * Gives the connected components in this graph.
     * Connected components are returned as the vector of the identifiers of the vertices
     * in the component.
     * 
     * @return The vector of the connected components.
     */
    IVec<IVecInt> connectedComponents();

}
