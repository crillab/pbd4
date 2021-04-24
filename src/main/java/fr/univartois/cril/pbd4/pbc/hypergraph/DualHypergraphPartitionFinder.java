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

package fr.univartois.cril.pbd4.pbc.hypergraph;

import static java.util.Objects.requireNonNull;

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.KahyparContext;
import fr.univartois.cril.jkahypar.hypergraph.Hypergraph;

/**
 * The DualHypergraphPartitionFinder allows to compute a partition of a dual hypergraph.
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
public final class DualHypergraphPartitionFinder {

    /**
     * The single instance of this class.
     */
    private static DualHypergraphPartitionFinder instance = null;

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
    }

    /**
     * Initializes the configuration of JKaHyPar.
     */
    private void initKahypar() {
        this.context.configureFrom(requireNonNull(System.getProperty("kahypar.config")));
        this.context.setImbalance(0.03);
        this.context.setNumberOfBlocks(2);
    }

    /**
     * Gives the single instance of this class.
     * 
     * @return The single instance of this class.
     */
    public static DualHypergraphPartitionFinder instance() {
        if (instance == null) {
            instance = new DualHypergraphPartitionFinder();
            instance.initKahypar();
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
        if (hypergraph.getNumberOfVertices() == 1) {
            var cutset = new VecInt();
            for (int i = 1; i <= hypergraph.getNumberOfHyperedges(); i++) {
                cutset.push(i);
            }
            return cutset;
        }
        
        
        var partitioner = context.createPartitionerFor(hypergraph);
        int[] vertices = hypergraph.getHyperedgeVertices();
        long[] hyperedgeIndices = hypergraph.getHyperedgeIndices();
        var partition = partitioner.computePartition();
        var cutset = new VecInt();

        // Looking for hyperedges in the cutset.
        for (int i = 0; i < hypergraph.getNumberOfHyperedges(); i++) {
            int begin = (int) hyperedgeIndices[i];
            int end = (int) hyperedgeIndices[i + 1];

            // Comparing the block of the different vertices.
            int first = partition.blockOf(vertices[begin] + 1);
            for (int j = begin + 1; j < end; j++) {
                if (partition.blockOf(vertices[j] + 1) != first) {
                    // The hyperedge joins two vertices that are in different blocks.
                    // It is thus in the cutset.
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
        if (instance != null) {
            instance.context.close();
            instance = null;
        }
    }

}
