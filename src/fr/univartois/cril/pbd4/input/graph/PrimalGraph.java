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

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

public final class PrimalGraph implements PseudoBooleanFormulaGraph {
    
    private final IVecInt[] adjacencyList;

    public PrimalGraph(IVecInt[] adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    @Override
    public IVec<IVecInt> connectedComponents() {
        var components = new Vec<IVecInt>();
        boolean[] found = new boolean[adjacencyList.length];
        for (int i = 1; i < adjacencyList.length; i++) {
            if (!found[i]) {
                components.push(componentOf(i, found));
            }
        }
        return components;
    }

    private IVecInt componentOf(int i, boolean[] found) {
        var toExplore = new VecInt();
        toExplore.push(i);
        var explored = new VecInt();
        
        while (!toExplore.isEmpty()) {
            int v = toExplore.last();
            toExplore.pop();
            
            explored.push(v);
            found[v] = true;
            for (var it = adjacencyList[v].iterator(); it.hasNext();) {
                var n = it.next();
                if (!found[n]) {
                    toExplore.push(n);
                }
            }
        }
        
        return explored;
    }

}

