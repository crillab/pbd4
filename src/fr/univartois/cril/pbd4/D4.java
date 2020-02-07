/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Univ Artois & CNRS.
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

package fr.univartois.cril.pbd4;

import java.math.BigInteger;

import fr.univartois.cril.pbd4.ddnnf.DecisionDnnf;
import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;

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
     * Specifies the path to the input formula to compile.
     *
     * @param path The path to the input file.
     * 
     * @return This configurator.
     */
    public D4 whenCompiling(String path) {
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
     * Counts the number of models of the associated formula.
     *
     * @return The number of models.
     */
    public BigInteger countModels() {
        var modelCounter = new D4Counter(this);
        return modelCounter.compute();
    }

    /**
     * Compiles the associated formula into a d-DNNF.
     *
     * @return A d-DNNF representing the associated formula.
     */
    public DecisionDnnf compile() {
        var compiler = new D4Compiler(this);
        return compiler.compute();
    }

}
