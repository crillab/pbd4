/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
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

package fr.univartois.cril.pbd4.partitioning;

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.KahyparContext;
import fr.univartois.cril.jkahypar.hypergraph.Hypergraph;
import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;

/**
 * The KahyparCutsetComputationStrategy is a strategy that computes the cutset of
 * the dual hypergraph associated to a pseudo-Boolean formula using the KaHyPar
 * partitioner.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public final class KahyparCutsetComputationStrategy implements CutsetComputationStrategy {

	/**
	 * The default value for the imbalance setting.
	 */
	public static final double DEFAULT_IMBALANCE = 0.03;

	/**
	 * The default value for the number of blocks in the partitions to find.
	 */
	public static final int DEFAULT_NUMBER_OF_BLOCKS = 2;

	/**
	 * The path to KaHyPar's configuration file.
	 */
	private final String kahyparConfig;

	/**
	 * The imbalance setting for KaHyPar.
	 */
	private final double imbalance;

	/**
	 * The number of blocks of the partitions to find.
	 */
	private final int numberOfBlocks;

    /**
     * The KaHyPar context that is used to compute hypergraph partitions.
     */
    private KahyparContext context;

	/**
	 * Creates a new KahyparCutsetComputationStrategy.
	 *
	 * @param kahyparConfig The path to KaHyPar's configuration file.
	 * @param imbalance The imbalance setting for KaHyPar.
	 * @param numberOfBlocks The number of blocks of the partitions to find.
	 */
	private KahyparCutsetComputationStrategy(String kahyparConfig, double imbalance, int numberOfBlocks) {
		this.kahyparConfig = kahyparConfig;
		this.imbalance = imbalance;
		this.numberOfBlocks = numberOfBlocks;
	}

	/**
	 * Creates a new KahyparCutsetComputationStrategy.
	 *
	 * @param kahyparConfig The path to KaHyPar's configuration file.
	 */
	public static KahyparCutsetComputationStrategy newInstance(String kahyparConfig) {
		return new KahyparCutsetComputationStrategy(kahyparConfig, DEFAULT_IMBALANCE, DEFAULT_NUMBER_OF_BLOCKS);
	}

	/**
	 * Creates a new KahyparCutsetComputationStrategy.
	 *
     * @param kahyparConfig The path to KaHyPar's configuration file.
     * @param imbalance The imbalance setting for KaHyPar.
     * @param numberOfBlocks The number of blocks of the partitions to find.
	 */
	public static KahyparCutsetComputationStrategy newInstance(String kahyparConfig,
			double imbalance, int numberOfBlocks) {
		return new KahyparCutsetComputationStrategy(kahyparConfig, imbalance, numberOfBlocks);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.univartois.cril.pbd4.partitioning.CutsetComputationStrategy#compilationStarts()
	 */
	@Override
	public void compilationStarts() {
		context = new KahyparContext();
		context.configureFrom(kahyparConfig);
		context.setImbalance(imbalance);
		context.setNumberOfBlocks(numberOfBlocks);
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see fr.univartois.cril.pbd4.partitioning.CutsetComputationStrategy#cutset(
	 * fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula)
	 */
	@Override
	public IVecInt cutset(PseudoBooleanFormula formula) {
		var dualHypergraph = formula.hypergraph();
		var cutset = cutset(dualHypergraph.asKahyparHypergraph());
		dualHypergraph.translateAsVariables(cutset);
		cutset.sort((x, y) -> Double.compare(formula.score(x), formula.score(y)));
		return cutset;
	}

    /**
     * Computes the cutset of the given hypergraph.
     *
     * @param hypergraph The hypergraph to compute the cutset of.
     *
     * @return The computed cutset.
     */
    private IVecInt cutset(Hypergraph hypergraph) {
    	// If there is only one vertex, then all hyperedges are in the cutset.
        if (hypergraph.getNumberOfVertices() == 1) {
            var cutset = new VecInt();
            for (int i = 1; i <= hypergraph.getNumberOfHyperedges(); i++) {
                cutset.push(i);
            }
            return cutset;
        }

        // Invoking KaHyPar on the hypergraph.
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

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.univartois.cril.pbd4.partitioning.CutsetComputationStrategy#compilationEnds()
	 */
	@Override
	public void compilationEnds() {
		context.close();
		context = null;
	}

}
