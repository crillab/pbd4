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

package fr.univartois.cril.pbd4;

import fr.univartois.cril.pbd4.ddnnf.DecisionDnnf;
import fr.univartois.cril.pbd4.ddnnf.LiteralNode;

/**
 * The DecisionDnnfFactory allows to create or reuse d-DNNF representations.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class DecisionDnnfFactory {

    /**
     * The d-DNNF representations of the literals.
     */
    private DecisionDnnf[] literals;

    /**
     * Creates a new DecisionDnnfFactory.
     *
     * @param numberOfVariables The number of variables in the formula being compiled.
     */
    public DecisionDnnfFactory(int numberOfVariables) {
        this.literals = new LiteralNode[2 + (numberOfVariables << 1)];
    }

    /**
     * Gives a d-DNNF representing the given literal.
     *
     * @param literal The literal to get the d-DNNF representation of.
     *
     * @return The d-DNNF representing {@code literal}.
     */
    public DecisionDnnf literal(int literal) {
        int index = literal < 0 ? -literal << 1 ^ 1 : literal << 1;
        if (literals[index] == null) {
            literals[index] = LiteralNode.literal(literal);
        }
        return literals[index];
    }

}
