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

package fr.univartois.cril.pbd4;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Supplier;

import org.sat4j.pb.SolverFactory;

import fr.univartois.cril.pbd4.caching.CachingStrategy;
import fr.univartois.cril.pbd4.caching.NoCache;
import fr.univartois.cril.pbd4.ddnnf.DecisionDnnf;
import fr.univartois.cril.pbd4.listener.CompositeListener;
import fr.univartois.cril.pbd4.listener.D4Listener;
import fr.univartois.cril.pbd4.partitioning.CutsetComputationStrategy;
import fr.univartois.cril.pbd4.partitioning.CutsetUpdateStrategy;
import fr.univartois.cril.pbd4.partitioning.KahyparCutsetComputationStrategy;
import fr.univartois.cril.pbd4.partitioning.LargeChangeCutsetUpdateStrategy;
import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;
import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormulaReader;
import fr.univartois.cril.pbd4.pbc.solver.SolverProvider;

/**
 * The D4 class makes easier the configuration and use of D4-based compilers or
 * model counters.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public final class D4 {

    /**
     * The supplier for the input formula.
     */
    private Supplier<PseudoBooleanFormula> formulaSupplier;

    /**
     * The provider for the solver to use as a SAT oracle.
     */
    private SolverProvider solverProvider;

    /**
     * The name of the solver to use as a SAT oracle.
     */
    private String solverName;

    /**
     * The path of KaHyPar's configuration file.
     */
    private String kahyparConfig;

    /**
     * The imbalance setting for KaHyPar.
     */
    private double imbalance;

    /**
     * The number of blocks in the partitions to find.
     */
    private int partitionSize;

    /**
     * The caching strategy to use.
     */
    private CachingStrategy<?> cache;

    private CompositeListener listener = new CompositeListener();

    private CutsetUpdateStrategy cutsetUpdateStrategy = LargeChangeCutsetUpdateStrategy.instance();

    /**
     * Creates a new D4 configurator.
     */
    private D4() {
        this.imbalance = KahyparCutsetComputationStrategy.DEFAULT_IMBALANCE;
        this.partitionSize = KahyparCutsetComputationStrategy.DEFAULT_NUMBER_OF_BLOCKS;
        this.solverProvider = SolverProvider.defaultProvider();
        this.solverName = solverProvider.toString();
        this.formulaSupplier = () -> null;
        this.cache = NoCache.instance();
    }

    /**
     * Creates a new D4 configurator.
     *
     * @return The created configurator
     */
    public static D4 newInstance() {
        return new D4();
    }

    /**
     * Specifies the path of the file containing the input formula.
     *
     * @param path The path of the input file.
     *
     * @return This configurator.
     */
    public D4 onInput(String path) {
        this.formulaSupplier = () -> {
            try {
                var reader = new PseudoBooleanFormulaReader(solverProvider);
                return reader.read(path);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
        return this;
    }

    /**
     * Specifies the stream from which to read the input formula. The stream is
     * supposed to use the CNF format.
     *
     * @param stream The input stream to read the formula from.
     *
     * @return This configurator.
     */
    public D4 onCnfInput(InputStream stream) {
        this.formulaSupplier = () -> {
            try {
                var reader = new PseudoBooleanFormulaReader(solverProvider);
                return reader.readCnf(stream);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
        return this;
    }

    /**
     * Specifies the stream from which to read the input formula. The stream is
     * supposed to use the OPB format.
     *
     * @param stream The input stream to read the formula from.
     *
     * @return This configurator.
     */
    public D4 onOpbInput(InputStream stream) {
        this.formulaSupplier = () -> {
            try {
                var reader = new PseudoBooleanFormulaReader(solverProvider);
                return reader.readOpb(stream);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
        return this;
    }

    /**
     * Specifies the input formula to compile.
     *
     * @param formula The formula to compile.
     *
     * @return This configurator.
     */
    public D4 onInput(PseudoBooleanFormula formula) {
        this.formulaSupplier = () -> formula;
        return this;
    }

    /**
     * Gives the formula to compile.
     *
     * @return The formula to compile.
     *
     * @throws UncheckedIOException If the formula is to be read and an I/O
     *         error
     *         occurs while reading.
     */
    PseudoBooleanFormula getFormula() {
        return formulaSupplier.get();
    }

    /**
     * Specifies the name of the solver to use as a SAT oracle during the
     * execution
     * of the D4 algorithm.
     *
     * @param solverName The name of the solver to use.
     *
     * @return This configurator.
     *
     * @see SolverFactory#createSolverByName(String)
     */
    public D4 useSolver(String solverName) {
        this.solverProvider = SolverProvider.forSolver(solverName);
        this.solverName = solverName;
        return this;
    }

    /**
     * Specifies the path of KaHyPar's configuration file.
     *
     * @param kahyparConfig The path of KaHyPar's configuration file.
     *
     * @return This configurator.
     *
     * @throws IllegalArgumentException If {@code kahyparConfig} is not the path
     *         of
     *         a readable file.
     */
    public D4 withConfiguration(String kahyparConfig) {
        if (Files.isReadable(Paths.get(kahyparConfig))) {
            this.kahyparConfig = kahyparConfig;
            return this;
        }

        throw new IllegalArgumentException(kahyparConfig + " cannot be used as configuration file");
    }

    /**
     * Specifies the imbalance setting used by KaHyPar.
     *
     * @param imbalance The imbalance setting for KaHyPar.
     *
     * @return This configurator.
     */
    public D4 withImbalance(double imbalance) {
        this.imbalance = imbalance;
        return this;
    }

    /**
     * Specifies the size of the partitions of the formula to compute.
     *
     * @param partitionSize The size of the partitions to compute.
     *
     * @return This configurator.
     */
    public D4 ofSize(int partitionSize) {
        this.partitionSize = partitionSize;
        return this;
    }

    /**
     * Specifies the caching strategy to use during the execution of the D4
     * algorithm.
     *
     * @param cachingStrategy The caching strategy to use.
     *
     * @return This configurator.
     */
    public D4 useCachingStrategy(CachingStrategy<?> cachingStrategy) {
        this.cache = cachingStrategy;
        return this;
    }

    /**
     * Gives the caching strategy to use during the execution of the D4
     * algorithm.
     *
     * @param <T> The type of the values associated to the formulae in the
     *        cache.
     *
     * @return The caching strategy to use.
     */
    <T> CachingStrategy<T> getCache() {
        @SuppressWarnings("unchecked")
        var actualCache = (CachingStrategy<T>) cache;
        return actualCache;
    }

    public D4 notifyListener(D4Listener listener) {
        this.listener.addListener(listener);
        return this;
    }

    D4Listener getListener() {
        return listener;
    }

    CutsetComputationStrategy getCutsetComputationStrategy() {
        return KahyparCutsetComputationStrategy.newInstance(kahyparConfig, imbalance,
                partitionSize);
    }

    D4 useCutsetUpdateStrategy(CutsetUpdateStrategy cutsetUpdateStrategy) {
        this.cutsetUpdateStrategy = cutsetUpdateStrategy;
        return this;
    }

    CutsetUpdateStrategy getCutsetUpdateStrategy() {
        return cutsetUpdateStrategy;
    }

    /**
     * Counts the number of models of the formula.
     *
     * @return The number of models.
     */
    public BigInteger countModels() {
        return compute(D4ModelCounter::new);
    }

    /**
     * Compiles the formula into a decision-DNNF.
     *
     * @return A decision-DNNF representing the formula.
     */
    public DecisionDnnf compileToDecisionDnnf() {
        return compute(D4DecisionDnnfCompiler::new);
    }

    /**
     * Computes the result of an implementation of D4 on the formula.
     *
     * @param <R> The type of the result to compute.
     *
     * @param factory The function used to instantiate the implementation of D4.
     *
     * @return The computed result.
     */
    public <R> R compute(Function<D4, AbstractD4<?, R>> factory) {
        var d4Implementation = factory.apply(this);
        return d4Implementation.compute();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "c ============================ PBD4 CONFIGURATION ============================\n"
                + "c\n"
                + "c Solver used as SAT oracle: " + solverName + "\n"
                + "c Location of KaHyPar INI file: "
                + kahyparConfig + "\n" + "c Imbalance value for KaHyPar: " + imbalance + "\n"
                + "c Size of the partitions computed by KaHyPar: " + partitionSize + "\n"
                + "c Caching strategy: "
                + cache + "\n" + "c\n"
                + "c ============================================================================";
    }

}
