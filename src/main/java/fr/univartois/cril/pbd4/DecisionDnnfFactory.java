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

import java.util.Collection;

import org.sat4j.core.LiteralsUtils;

import fr.univartois.cril.pbd4.ddnnf.ConjunctionNode;
import fr.univartois.cril.pbd4.ddnnf.DecisionDnnf;
import fr.univartois.cril.pbd4.ddnnf.DecisionNode;
import fr.univartois.cril.pbd4.ddnnf.LiteralNode;

/**
 * The DecisionDnnfFactory allows to create the d-DNNF representations used in the
 * compiled form of the input formula.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class DecisionDnnfFactory {

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
        int index = LiteralsUtils.toInternal(literal);
        if (literals[index] == null) {
            literals[index] = LiteralNode.literal(literal);
        }
        return literals[index];
    }

    /**
     * Creates a d-DNNF representing the conjunction of the given d-DNNFs.
     *
     * @param conjuncts The d-DNNF representations to create the conjunction of.
     *
     * @return The d-DNNF representing the conjunctions of the given d-DNNFs.
     */
    public DecisionDnnf conjunctionOf(Collection<DecisionDnnf> conjuncts) {
        return ConjunctionNode.and(conjuncts);
    }

    /**
     * Creates a d-DNNF representing a decision taken on a given variable.
     *
     * @param variable The variable on which a decision is taken.
     * @param ifTrue The d-DNNF representing the case in which the variable is satisfied.
     * @param ifFalse The d-DNNF representing the case in which the variable is falsified.
     *
     * @return The created d-DNNF.
     */
    public DecisionDnnf ifThenElse(int variable, DecisionDnnf ifTrue, DecisionDnnf ifFalse) {
        return DecisionNode.ifThenElse(variable, ifTrue, ifFalse);
    }

}
