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

package fr.univartois.cril.pbd4.ddnnf;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The ConjunctionNode represents the conjunction of several d-DNNFs (there may be
 * arbitrary many conjuncts).
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class ConjunctionNode extends InternalNode {

    /**
     * The d-DNNFs for which this node is a representation of the conjunction.
     */
    private final Collection<DecisionDnnf> conjuncts;

    /**
     * Creates a new ConjunctionNode.
     *
     * @param conjuncts The d-DNNFs for which the node is a representation of the conjunction.
     */
    private ConjunctionNode(Collection<DecisionDnnf> conjuncts) {
        this.conjuncts = conjuncts;
    }

    /**
     * Creates a new ConjunctionNode.
     *
     * @param conjuncts The d-DNNFs for which the node is a representation of the conjunction.
     *
     * @return The created node.
     */
    public static final DecisionDnnf and(DecisionDnnf... conjuncts) {
        return new ConjunctionNode(List.of(conjuncts));
    }

    /**
     * Creates a new ConjunctionNode.
     *
     * @param conjuncts The d-DNNFs for which the node is a representation of the conjunction.
     *
     * @return The created node.
     */
    public static final DecisionDnnf and(Collection<DecisionDnnf> conjuncts) {
        return new ConjunctionNode(Collections.unmodifiableCollection(conjuncts));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnf#accept(fr.univartois.cril.pbd4.ddnnf.
     * DecisionDnnfVisitor)
     */
    @Override
    public void accept(DecisionDnnfVisitor visitor) {
        conjuncts.forEach(c -> c.accept(visitor));
        visitor.visit(this);
    }

}
