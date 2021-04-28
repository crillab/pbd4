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
 * The CachingStrategy defines a strategy for caching pseudo-Boolean formulae
 * encountered during the execution of the D4 algorithm.
 *
 * @param <T> The type of the values associated to the formulae in the cache.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public interface CachingStrategy<T> {

    /**
     * Puts a value computed for a pseudo-Boolean formula into the cache.
     *
     * @param formula The formula to put the value of.
     * @param value The value to cache for the formula.
     *
     * @implSpec This method does <b>not</b> guarantee that the cached value may be
     *           retrieved later on: it expects a best-effort approach (even though it is
     *           not required).
     *
     * @see #get(PseudoBooleanFormula)
     */
    void put(PseudoBooleanFormula formula, T toCache);

    /**
     * Gives the value that has been cached for the given formula.
     *
     * @param formula The formula to get the cached value of.
     *
     * @return The value cached for the given formula, or {@link Optional#empty()} if no
     *         corresponding value is cached.
     *
     * @implSpec The cached value is <b>not</b> guaranteed to be retrieved by this method:
     *           a best-effort approach is expected (even though it is not required).
     *
     * @see #put(PseudoBooleanFormula, Object)
     */
    Optional<T> get(PseudoBooleanFormula formula);

}
