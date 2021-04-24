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
import java.math.BigInteger;

import fr.univartois.cril.pbd4.ddnnf.DecisionDnnf;
import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;
import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormulaReader;

/**
 * The D4 class makes easier the configuration and use of D4-based compilers or model
 * counters.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class D4 {

    /**
     * The pseudo-Boolean formula to consider in the algorithm.
     */
    private PseudoBooleanFormula formula;

    /**
     * The caching strategy to use in the algorithm.
     */
    private CachingStrategy<?> cache;

    /**
     * Creates a new D4 configurator.
     */
    private D4() {
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
     * Specifies the path of the file containing the input formula to compile.
     *
     * @param path The path of the input file.
     *
     * @return This configurator.
     *
     * @throws IOException If an I/O error occurs while reading the formula.
     */
    public D4 whenCompiling(String path) throws IOException {
        return whenCompiling(PseudoBooleanFormulaReader.read(path));
    }

    /**
     * Specifies the stream from which to read the input formula to compile.
     * The stream is supposed to use the CNF format.
     *
     * @param stream The input stream to read the formula from.
     * 
     * @return This configurator.
     *
     * @throws IOException If an I/O error occurs while reading the formula.
     */
    public D4 whenCompilingCnf(InputStream stream) throws IOException {
        return whenCompiling(PseudoBooleanFormulaReader.readCnf(stream));
    }

    /**
     * Specifies the stream from which to read the input formula to compile.
     * The stream is supposed to use the OPB format.
     *
     * @param stream The input stream to read the formula from.
     * 
     * @return This configurator.
     *
     * @throws IOException If an I/O error occurs while reading the formula.
     */
    public D4 whenCompilingOpb(InputStream stream) throws IOException {
        return whenCompiling(PseudoBooleanFormulaReader.readOpb(stream));
    }

    /**
     * Specifies the input formula to compile.
     *
     * @param formula The formula to compile.
     * 
     * @return This configurator.
     */
    public D4 whenCompiling(PseudoBooleanFormula formula) {
        this.formula = formula;
        return this;
    }

    /**
     * Gives the formula to compile.
     *
     * @return The formula to compile.
     */
    PseudoBooleanFormula getFormula() {
        return formula;
    }

    /**
     * Specifies the caching strategy to use during the compilation.
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
     * Gives the caching strategy to use during the compilation.
     *
     * @param <T> The type of the elements to store in the cache.
     *
     * @return The caching strategy to use.
     */
    @SuppressWarnings("unchecked")
    <T> CachingStrategy<T> getCache() {
        return (CachingStrategy<T>) cache;
    }

    /**
     * Counts the number of models of the formula.
     *
     * @return The number of models.
     */
    public BigInteger countModels() {
        var modelCounter = new D4ModelCounter(this);
        return modelCounter.compute();
    }

    /**
     * Compiles the formula into a decision-DNNF.
     *
     * @return A decision-DNNF representing the formula.
     */
    public DecisionDnnf compileToDecisionDnnf() {
        var compiler = new D4DecisionDnnfCompiler(this);
        return compiler.compute();
    }

}
