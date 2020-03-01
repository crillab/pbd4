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

import java.util.LinkedList;
import java.util.List;

import org.sat4j.core.LiteralsUtils;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.ddnnf.ConjunctionNode;
import fr.univartois.cril.pbd4.ddnnf.ConstantNode;
import fr.univartois.cril.pbd4.ddnnf.DecisionDnnf;
import fr.univartois.cril.pbd4.ddnnf.DecisionDnnfNode;
import fr.univartois.cril.pbd4.ddnnf.DecisionNode;
import fr.univartois.cril.pbd4.ddnnf.LiteralNode;

/**
 * The D4DecisionDnnfCompiler specifies the D4 algorithm for compiling the input formula
 * into a decision-DNNF.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class D4DecisionDnnfCompiler extends AbstractD4<DecisionDnnfNode, DecisionDnnf> {

    /**
     * The number of variables in the decision-DNNF.
     */
    private final int numberOfVariables;

    /**
     * The number of nodes in the decision-DNNF.
     */
    private int numberOfNodes;

    /**
     * The number of edges in the decision-DNNF.
     */
    private int numberOfEdges;

    /**
     * The decision-DNNF nodes representing the literals.
     */
    private final DecisionDnnfNode[] literals;

    /**
     * Creates a new D4DecisionDnnfCompiler.
     *
     * @param configuration The configuration of the compiler.
     */
    D4DecisionDnnfCompiler(D4 configuration) {
        super(configuration);
        this.numberOfVariables = configuration.getFormula().numberOfVariables();
        this.numberOfNodes = 2;
        this.numberOfEdges = 0;
        this.literals = new DecisionDnnfNode[2 + (numberOfVariables << 1)];
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.AbstractD4#unsatisfiable()
     */
    @Override
    protected DecisionDnnfNode unsatisfiable() {
        return ConstantNode.FALSE;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.AbstractD4#implicant(int, org.sat4j.specs.IVecInt)
     */
    @Override
    protected DecisionDnnfNode implicant(int nbFreeVariables, IVecInt implicant) {
        return cached(nbFreeVariables, implicant, ConstantNode.TRUE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#cached(int, org.sat4j.specs.IVecInt,
     * java.lang.Object)
     */
    @Override
    protected DecisionDnnfNode cached(int nbFreeVariables, IVecInt propagatedLiterals,
            DecisionDnnfNode cached) {
        var list = new LinkedList<DecisionDnnfNode>();
        list.add(cached);
        return conjunction(nbFreeVariables, propagatedLiterals, list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#decision(int, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    protected DecisionDnnfNode decision(int variable, DecisionDnnfNode ifTrue,
            DecisionDnnfNode ifFalse) {
        // There is one more node: the created decision node.
        numberOfNodes++;

        // There is an edge for each of two children.
        numberOfEdges += 2;

        // Creating the new node.
        return DecisionNode.decision(variable, ifTrue, ifFalse);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#conjunction(int, org.sat4j.specs.IVecInt,
     * java.util.List)
     */
    @Override
    protected DecisionDnnfNode conjunction(int nbFreeVariables, IVecInt literals,
            List<DecisionDnnfNode> conjuncts) {
        // There is one more node: the created conjunction node.
        numberOfNodes++;

        // There is an edge for each of the literals that are connected to the new node.
        // There is also an edge for each of the conjuncts.
        numberOfEdges += literals.size() + conjuncts.size();

        // Creating the new node.
        toDecisionDnnf(literals, conjuncts);
        return ConjunctionNode.and(conjuncts);
    }

    /**
     * Creates, for each literal in the vector, a decision-DNNF representing this literal.
     *
     * @param literals The literals to create decision-DNNFs for.
     * @param output The list in which to store the decision-DNNFs representing the
     *        literals.
     */
    private void toDecisionDnnf(IVecInt literals, List<DecisionDnnfNode> output) {
        for (var it = literals.iterator(); it.hasNext();) {
            output.add(literal(it.next()));
        }
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
            numberOfNodes++;
        }
        return literals[index];
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.AbstractD4#toFinalResult(java.lang.Object)
     */
    @Override
    protected DecisionDnnf toFinalResult(DecisionDnnfNode intermediateResult) {
        return new DecisionDnnf(numberOfVariables, numberOfNodes, numberOfEdges, intermediateResult);
    }

}
