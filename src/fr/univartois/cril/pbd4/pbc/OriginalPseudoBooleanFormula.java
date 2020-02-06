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

package fr.univartois.cril.pbd4.pbc;

import java.util.Collection;

import org.sat4j.pb.IPBSolver;
import org.sat4j.specs.IVecInt;


/**
 * The OriginalPseudoBooleanFormula
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public class OriginalPseudoBooleanFormula implements PseudoBooleanFormula {
    
    private final IPBSolver solver;
    
    private final IVecInt variables;

    OriginalPseudoBooleanFormula(IPBSolver solver, IVecInt variables) {
        this.solver = solver;
        this.variables = variables;
    }

    @Override
    public int numberOfVariables() {
        return solver.nVars();
    }

    @Override
    public int numberOfConstraints() {
        return solver.nConstraints();
    }

    @Override
    public IVecInt variables() {
        return variables;
    }

    @Override
    public PseudoBooleanFormula satisfy(int literal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IVecInt cutset() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<PseudoBooleanFormula> connectedComponents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropagationOutput propagate() {
        // TODO Auto-generated method stub
        return null;
    }
}

