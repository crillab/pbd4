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

/**
 * The D4 class makes easier the configuration and use of D4-based compilers or model
 * counters.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class D4 {

    private D4() {
        // Disables external instantiation.
    }

    public static D4 newInstance() {
        return new D4();
    }
    
    public D4 forFormula(String path) {
        return this;
    }

    public BigInteger countModels() {
        return null;
    }

    public DecisionDnnf compile() {
        return null;
    }

}
