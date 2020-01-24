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

package fr.univartois.cril.pbd4.input.hypergraph;

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.KahyparContext;
import fr.univartois.cril.jkahypar.hypergraph.Hypergraph;

/**
 * The DualHypergraphPartitioner
 *
 * @author Romain WALLON
 *
 * @version 1.0
 */
public class DualHypergraphPartitioner {

    private static final DualHypergraphPartitioner INSTANCE = new DualHypergraphPartitioner();

    private final KahyparContext context;

    private DualHypergraphPartitioner() {
        this.context = new KahyparContext();
        this.context.configureFrom(System.getProperty("kahypar.config"));
    }

    public IVecInt cutsetOf(Hypergraph hypergraph) {
        var partitioner = context.createPartitionerFor(hypergraph);
        var partition = partitioner.computePartition();
        int[] vertices = hypergraph.getHyperedgeVertices();
        var cutset = new VecInt();
        
        for (int i = 0; i < hypergraph.getNumberOfHyperedges(); i++) {
            long[] hyperedgeIndices = hypergraph.getHyperedgeIndices();
            int begin = (int) hyperedgeIndices[i];
            int end = (int) hyperedgeIndices[i + 1];
            
            // Copying the vertices, considering that vertices are currently shifted.
            int first = partition.blockOf(vertices[begin]);
            for (int j = begin + 1; j < end; j++) {
                if (partition.blockOf(vertices[j]) != first) {
                    cutset.push(i + 1);
                    break;
                }
            }

            
        }
        return cutset;
    }

}
