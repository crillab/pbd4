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
import fr.univartois.cril.pbd4.ddnnf.DecisionDnnfNode;
import fr.univartois.cril.pbd4.ddnnf.DecisionNode;
import fr.univartois.cril.pbd4.ddnnf.LiteralNode;

/**
 * The DecisionDnnfNodeFactory allows to create the decision-DNNF nodes appearing in the
 * compiled form of an input formula.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class DecisionDnnfNodeFactory {

    /**
     * The decision-DNNF nodes representing the literals.
     */
    private DecisionDnnfNode[] literals;

    /**
     * Creates a new DecisionDnnfNodeFactory.
     *
     * @param numberOfVariables The number of variables in the formula being compiled.
     */
    public DecisionDnnfNodeFactory(int numberOfVariables) {
        this.literals = new DecisionDnnfNode[2 + (numberOfVariables << 1)];
    }

    /**
     * Gives a decision-DNNF node representing a literal.
     *
     * @param dimacs The identifier of the literal to get the decision-DNNF node of.
     *
     * @return The decision-DNNF node representing the literal.
     */
    public DecisionDnnfNode literal(int dimacs) {
        int index = LiteralsUtils.toInternal(dimacs);
        if (literals[index] == null) {
            literals[index] = LiteralNode.literal(dimacs);
        }
        return literals[index];
    }

    /**
     * Creates a decision-DNNF node representing the conjunction of the given nodes.
     *
     * @param conjuncts The decision-DNNF nodes to create the conjunction of.
     *
     * @return The decision-DNNF representing the conjunctions of the given nodes.
     */
    public DecisionDnnfNode and(Collection<DecisionDnnfNode> conjuncts) {
        return ConjunctionNode.and(conjuncts);
    }

    /**
     * Creates a decision-DNNF node representing a decision taken on a given variable.
     *
     * @param variable The variable on which a decision is taken.
     * @param ifTrue The decision-DNNF node representing the case in which the variable is
     *        satisfied.
     * @param ifFalse The decision-DNNF node representing the case in which the variable
     *        is falsified.
     *
     * @return The decision-DNNF node representing the decision.
     */
    public DecisionDnnfNode or(int variable, DecisionDnnfNode ifTrue, DecisionDnnfNode ifFalse) {
        return DecisionNode.or(variable, ifTrue, ifFalse);
    }

}
