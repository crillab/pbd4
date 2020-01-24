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

import org.sat4j.core.LiteralsUtils;
import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

/**
 * The PrimalGraphBuilder allows to build the primal graph of a pseudo-Boolean formula.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class PrimalGraphBuilder implements PseudoBooleanFormulaGraphBuilder {

    private final IVecInt[] adjacencyList;

    private PrimalGraphBuilder(int nbVariables) {
        this.adjacencyList = new IVecInt[nbVariables + 1];
    }

    public static PseudoBooleanFormulaGraphBuilder newInstance(int nbVariables) {
        return new PrimalGraphBuilder(nbVariables);
    }

    @Override
    public void addConstraintOver(IVecInt literals) {
        for (var it = literals.iterator(); it.hasNext();) {
            addNeighbors(LiteralsUtils.var(it.next()), literals);
        }
    }

    private void addNeighbors(int variable, IVecInt literals) {
        var neighbors = adjacencyList[variable];
        if (neighbors == null) {
            neighbors = new VecInt();
            adjacencyList[variable] = neighbors;
        }
        
        for (var it = literals.iterator(); it.hasNext();) {
            int v = LiteralsUtils.var(it.next());
            
            if (v != variable) {
                neighbors.push(v);
            }
        }
    }

    @Override
    public PseudoBooleanFormulaGraph build() {
        return new PrimalGraph(adjacencyList);
    }

}
