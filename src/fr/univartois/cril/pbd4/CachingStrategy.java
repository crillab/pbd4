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

import java.util.Optional;

import fr.univartois.cril.pbd4.input.PseudoBooleanFormula;

/**
 * The CachingStrategy defines a strategy for caching pseudo-Boolean formulae encountered
 * during the compilation process.
 *
 * @param <T> The type of the values in the cache.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface CachingStrategy<T> {

    /**
     * Gives the value that has been cached for the given formula.
     *
     * @param formula The formula to get the cached value of.
     *
     * @return The value cached for the given formula, or {@link Optional#empty()} if no
     *         value is cached.
     */
    Optional<T> get(PseudoBooleanFormula formula);

    /**
     * Puts a value computed for a pseudo-Boolean formula into the cache.
     *
     * @param formula The formula to put the value of.
     * @param value The value to cache for the formula.
     */
    void put(PseudoBooleanFormula formula, T toCache);

}