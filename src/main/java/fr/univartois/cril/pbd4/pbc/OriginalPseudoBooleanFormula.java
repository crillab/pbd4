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

import static fr.univartois.cril.pbd4.pbc.RangeVecInt.range;
import java.util.Collection;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.core.ICDCL;
import org.sat4j.minisat.orders.SubsetVarOrder;
import org.sat4j.pb.constraints.pb.PBConstr;
import org.sat4j.specs.ISolverService;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

/**
 * The OriginalPseudoBooleanFormula represents the original pseudo-Boolean formula, used
 * as input for the compiler or model counter.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class OriginalPseudoBooleanFormula implements PseudoBooleanFormula {

    /**
     * The decorator for the solver, used to retrieve the informations about the formula.
     */
    private final PBSelectorSolver solver;

    /**
     * The solving engine, which actually performs the search.
     */
    private final ISolverService engine;

    /**
     * The unit-propagation listener used to record the propagations performed during the
     * execution of the solver.
     */
    private final UnitPropagationListener listener;

    /**
     * The variable of this formula.
     */
    private final IVecInt variables;

    /**
     * The scores of the VSIDS heuristic for each variable.
     */
    private final double[] vsidsScores;

    /**
     * The scores of the DLCS heuristic for each variable (i.e., the number of occurrences
     * of each variable in the formula).
     */
    private final int[] dlcsScores;

    /**
     * Creates a new OriginalPseudoBooleanFormula.
     *
     * @param solver The decorator for the solver, used to retrieve the informations about
     *        the formula.
     */
    OriginalPseudoBooleanFormula(PBSelectorSolver solver) {
        this.solver = solver;
        this.engine = (ISolverService) solver.getSolvingEngine();
        this.listener = new UnitPropagationListener();
        this.variables = range(1, numberOfVariables() + 1);
        this.vsidsScores = engine.getVariableHeuristics();
        this.dlcsScores = new int[solver.nVars() + 1];
        init();
    }

    /**
     * Initializes this formula, by retrieving the information from the solver.
     */
    private void init() {
        // Configuring the solver.
        solver.setKeepSolverHot(true);
        solver.setSearchListener(listener);
        ((ICDCL<?>) engine).setOrder(new SubsetVarOrder(new int[0]));

        // Computing the DLCS scores.
        for (int v = 1; v < numberOfVariables(); v++) {
            dlcsScores[v] = solver.getConstraintsContaining(v).size();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#numberOfVariables()
     */
    @Override
    public int numberOfVariables() {
        return solver.nVars();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#numberOfConstraints()
     */
    @Override
    public int numberOfConstraints() {
        return solver.nConstraints();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#variables()
     */
    @Override
    public IVecInt variables() {
        return variables;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#score(int)
     */
    @Override
    public double score(int variable) {
        return vsidsScores[variable] + 0.5 * dlcsScores[variable];
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#satisfy(int)
     */
    @Override
    public PseudoBooleanFormula satisfy(int literal) {
        return new SubPseudoBooleanFormula(this, VecInt.of(literal));
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#cutset()
     */
    @Override
    public IVecInt cutset() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#connectedComponents()
     */
    @Override
    public Collection<PseudoBooleanFormula> connectedComponents() {
        throw new UnsupportedOperationException();
    }

    /* 
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#propagate()
     */
    @Override
    public PropagationOutput propagate() {
        solver.externalState();
        return internalPropagate(VecInt.EMPTY);
    }

    /**
     * Applies Boolean Constraint Propagation (BCP) on this formula.
     *
     * @param assumptions The assumptions to make when propagating.
     *
     * @return The output of the propagation.
     */
    PropagationOutput propagate(IVecInt assumptions) {
        solver.internalState();
        return internalPropagate(assumptions);
    }

    /**
     * Applies Boolean Constraint Propagation (BCP) on this formula.
     *
     * @param assumptions The assumptions to make when propagating.
     *
     * @return The output of the propagation.
     */
    private PropagationOutput internalPropagate(IVecInt assumptions) {
        listener.reset();
        
        try {
            if (solver.isSatisfiable(assumptions)) {
                // The solver has found a solution.
                return PropagationOutput.satisfiable(listener.getPropagatedLiterals());
            }

            // The formula is necessarily unsatisfiable.
            // Otherwise, an exception would have been thrown.
            return PropagationOutput.unsatisfiable();

        } catch (TimeoutException e) {
            // The solver could not determine whether the formula was satisfiable.
            return PropagationOutput.unknown(listener.getPropagatedLiterals(), this);
        }
    }

    /**
     * Gives the constraint at the given index in this formula.
     *
     * @param i The index of the constraint to get.
     *
     * @return The {@code i}-the constraint of this formula.
     */
    PBConstr getConstraint(int i) {
        return solver.getConstraint(i);
    }
    
}
