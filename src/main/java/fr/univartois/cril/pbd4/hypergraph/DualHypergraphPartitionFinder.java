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

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.KahyparContext;
import fr.univartois.cril.jkahypar.hypergraph.Hypergraph;

/**
 * The DualHypergraphPartitionFinder allows to compute a partition of a hypergraph.
 * 
 * The system property {@code kahypar.config} must have been set to a INI file containing
 * the configuration for JKaHyPar to be able to compute partitions.
 * 
 * This class is a singleton that is NOT thread-safe.
 *
 * @author Romain WALLON
 *
 * @version 1.0
 */
final class DualHypergraphPartitionFinder {

    /**
     * The single instance of this class.
     */
    private static DualHypergraphPartitionFinder instance = new DualHypergraphPartitionFinder();

    /**
     * The JKaHyPar context used to compute partitions.
     */
    private final KahyparContext context;

    /**
     * Creates a new DualHypergraphPartitionFinder.
     * It initializes the context to be used.
     */
    private DualHypergraphPartitionFinder() {
        this.context = new KahyparContext();
        this.context.configureFrom(System.getProperty("kahypar.config"));
    }

    /**
     * Gives the single instance of this class.
     * 
     * @return The single instance of this class.
     */
    public static DualHypergraphPartitionFinder instance() {
        if (instance == null) {
            instance = new DualHypergraphPartitionFinder();
        }
        return instance;
    }

    /**
     * Computes the cutset of the given hypergraph.
     *
     * @param hypergraph The hypergraph to compute the cutset of.
     *
     * @return The computed cutset.
     */
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

    /**
     * Disposes the underlying JKaHyPar context to delete the memory it uses.
     * Do not forget to invoke this method to avoid memory leaks.
     */
    public static void clearInstance() {
        instance.context.close();
        instance = null;
    }

}
