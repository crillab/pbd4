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

package fr.univartois.cril.pbd4.cache;

import java.util.Optional;

import fr.univartois.cril.pbd4.CachingStrategy;
import fr.univartois.cril.pbd4.input.PseudoBooleanFormula;

/**
 * The NoCache strategy is a caching strategy that does not cache any value.
 * It is a Null Object implementation of {@link CachingStrategy}.
 *
 * @param <T> The type of the values in the cache.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class NoCache<T> implements CachingStrategy<T> {

    /**
     * The single instance of this class.
     */
    private static final CachingStrategy<?> INSTANCE = new NoCache<>();

    /**
     * Disables external instantiation.
     */
    private NoCache() {
        // Nothing to do.
    }

    /**
     * Gives the single instance of this class.
     *
     * @param <T> The type of the elements in the cache.
     *
     * @return The single instance of this class.
     */
    @SuppressWarnings("unchecked")
    public static <T> CachingStrategy<T> instance() {
        return (CachingStrategy<T>) INSTANCE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.CachingStrategy#get(fr.univartois.cril.pbd4.input.
     * PseudoBooleanFormula)
     */
    @Override
    public Optional<T> get(PseudoBooleanFormula formula) {
        return Optional.empty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.CachingStrategy#put(fr.univartois.cril.pbd4.input.
     * PseudoBooleanFormula, java.lang.Object)
     */
    @Override
    public void put(PseudoBooleanFormula formula, T toCache) {
        // Nothing to do: Null Object.
    }

}
