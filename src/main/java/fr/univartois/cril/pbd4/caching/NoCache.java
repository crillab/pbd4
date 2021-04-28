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

package fr.univartois.cril.pbd4.caching;

import java.util.Optional;

import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;

/**
 * The NoCache strategy is a caching strategy that does not cache any value.
 * It is a Singleton for a Null Object implementation of {@link CachingStrategy}.
 *
 * @param <T> The type of the values associated to the formulae (expected to be) in the cache.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
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
        // Nothing to do: Singleton Design Pattern.
    }

    /**
     * Gives the single instance of this class.
     *
     * @param <T> The type of the values associated to the formulae (expected to be) in
     *        the cache.
     *
     * @return The single instance of this class.
     */
    public static <T> CachingStrategy<T> instance() {
        @SuppressWarnings("unchecked")
        var actualCache = (CachingStrategy<T>) INSTANCE;
        return actualCache;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.caching.CachingStrategy#get(fr.univartois.cril.pbd4.pbc.
     * PseudoBooleanFormula)
     */
    @Override
    public Optional<T> get(PseudoBooleanFormula formula) {
        return Optional.empty();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.caching.CachingStrategy#put(fr.univartois.cril.pbd4.pbc.
     * PseudoBooleanFormula, java.lang.Object)
     */
    @Override
    public void put(PseudoBooleanFormula formula, T toCache) {
        // Nothing to do: Null Object Design Pattern.
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "no caching";
    }

}
