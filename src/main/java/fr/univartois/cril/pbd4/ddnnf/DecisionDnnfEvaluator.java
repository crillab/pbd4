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

import java.util.Set;

/**
 * The DecisionDnnfEvaluator
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public class DecisionDnnfEvaluator implements DecisionDnnfVisitor {

    private final Set<Integer> assignment;
    
    
    
    public DecisionDnnfEvaluator(Set<Integer> assignment) {
        this.assignment = assignment;
    }
    
    public DecisionDnnfEvaluator(Integer... assignment) {
        this(Set.of(assignment));
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.ddnnf.ConjunctionNode)
     */
    @Override
    public void visit(ConjunctionNode node) {
        // TODO Auto-generated method stub

    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.ddnnf.DecisionNode)
     */
    @Override
    public void visit(DecisionNode node) {
        // TODO Auto-generated method stub

    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.ddnnf.LiteralNode)
     */
    @Override
    public void visit(LiteralNode node) {
        // TODO Auto-generated method stub

    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.ddnnf.LeafNode)
     */
    @Override
    public void visit(LeafNode node) {
        // TODO Auto-generated method stub

    }

    public boolean evaluate() {
        return false;
    }

    @Override
    public void visit(DecisionDnnf ddnnf) {
        // TODO Auto-generated method stub
        
    }
}

