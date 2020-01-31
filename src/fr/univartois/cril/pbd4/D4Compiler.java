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

import java.util.ArrayList;
import java.util.Collection;

import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.ddnnf.DecisionDnnf;
import fr.univartois.cril.pbd4.ddnnf.LeafNode;
import fr.univartois.cril.pbd4.input.PseudoBooleanFormula;

/**
 * The D4Counter specifies the D4 algorithm for compiling the input formula into a d-DNNF.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class D4Compiler extends AbstractD4<DecisionDnnf> {

    /**
     * The factory used to create the different nodes of the d-DNNF being computed.
     */
    private final DecisionDnnfFactory factory;

    /**
     * Creates a new D4Compiler.
     *
     * @param configuration The configuration of the compiler.
     * @param numberOfVariables The number of variables in the formula being compiled.
     */
    D4Compiler(D4 configuration, int numberOfVariables) {
        super(configuration);
        this.factory = new DecisionDnnfFactory(numberOfVariables);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#unsatisfiable()
     */
    @Override
    protected DecisionDnnf unsatisfiable() {
        return LeafNode.FALSE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#model(fr.univartois.cril.pbd4.input.
     * PseudoBooleanFormula, org.sat4j.specs.IVecInt)
     */
    @Override
    protected DecisionDnnf model(PseudoBooleanFormula currentFormula, IVecInt propagatedLiterals) {
        return conjunctionOf(toDecisionDnnf(propagatedLiterals));
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#cached(org.sat4j.specs.IVecInt,
     * java.lang.Object)
     */
    @Override
    protected DecisionDnnf cached(IVecInt propagatedLiterals, DecisionDnnf cached) {
        var nodes = toDecisionDnnf(propagatedLiterals);
        nodes.add(cached);
        return conjunctionOf(nodes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#conjunctionOf(java.util.Collection)
     */
    @Override
    protected DecisionDnnf conjunctionOf(Collection<DecisionDnnf> conjuncts) {
        return factory.conjunctionOf(conjuncts);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#ifThenElse(int, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    protected DecisionDnnf ifThenElse(int variable, DecisionDnnf ifTrue, DecisionDnnf ifFalse) {
        return factory.ifThenElse(variable, ifTrue, ifFalse);
    }

    /**
     * Creates, for each literals in the vector, a d-DNNF representing this literal.
     *
     * @param literals The literal to create d-DNNFs for.
     *
     * @return The d-DNNFs representing the literals.
     */
    private Collection<DecisionDnnf> toDecisionDnnf(IVecInt literals) {
        var nodes = new ArrayList<DecisionDnnf>(literals.size() + 1);
        for (var it = literals.iterator(); it.hasNext();) {
            nodes.add(factory.literal(it.next()));
        }
        return nodes;
    }

}
