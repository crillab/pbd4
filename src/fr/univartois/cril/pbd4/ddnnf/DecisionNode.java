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


/**
 * The DecisionNode
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public class DecisionNode implements DecisionDnnf {

    private final int variable;
    
    private final DecisionDnnf positiveDecision;
    
    private final DecisionDnnf negativeDecision;

    public DecisionNode(int variable, DecisionDnnf positiveDecision,
            DecisionDnnf negativeDecision) {
        super();
        this.variable = variable;
        this.positiveDecision = positiveDecision;
        this.negativeDecision = negativeDecision;
    }
    
}

