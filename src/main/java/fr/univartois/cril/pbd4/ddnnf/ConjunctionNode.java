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

package fr.univartois.cril.pbd4.ddnnf;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The ConjunctionNode represents a conjunction of several decision-DNNF nodes (there may
 * be arbitrary many conjuncts).
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class ConjunctionNode implements DecisionDnnfNode {

    /**
     * The decision-DNNF nodes for which this node is a representation of the conjunction.
     */
    private final Collection<DecisionDnnfNode> conjuncts;

    /**
     * Creates a new ConjunctionNode.
     *
     * @param conjuncts The decision-DNNF nodes for which the node is a representation of
     *        the conjunction.
     */
    private ConjunctionNode(Collection<DecisionDnnfNode> conjuncts) {
        this.conjuncts = conjuncts;
    }

    /**
     * Creates a new ConjunctionNode.
     *
     * @param conjuncts The decision-DNNF nodes for which the node is a representation of
     *        the conjunction.
     *
     * @return The created node.
     */
    public static final DecisionDnnfNode and(DecisionDnnfNode... conjuncts) {
        return new ConjunctionNode(List.of(conjuncts));
    }

    /**
     * Creates a new ConjunctionNode.
     *
     * @param conjuncts The decision-DNNF nodes for which the node is a representation of
     *        the conjunction.
     *
     * @return The created node.
     */
    public static final DecisionDnnfNode and(Collection<DecisionDnnfNode> conjuncts) {
        return new ConjunctionNode(Collections.unmodifiableCollection(conjuncts));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfNode#depthFirstAccept(fr.univartois.cril.
     * pbd4.ddnnf.DecisionDnnfVisitor)
     */
    @Override
    public void depthFirstAccept(DecisionDnnfVisitor visitor) {
        visitor.enter(this);
        conjuncts.forEach(c -> c.depthFirstAccept(visitor));
        visitor.visit(this);
        visitor.exit(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfNode#breadthFirstAccept(fr.univartois.
     * cril.pbd4.ddnnf.DecisionDnnfVisitor)
     */
    @Override
    public void breadthFirstAccept(DecisionDnnfVisitor visitor) {
        visitor.enter(this);
        visitor.visit(this);
        conjuncts.forEach(c -> c.breadthFirstAccept(visitor));
        visitor.exit(this);
    }

}
