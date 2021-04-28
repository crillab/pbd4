/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Univ Artois & CNRS.
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package fr.univartois.cril.pbd4.pbc;

import static fr.univartois.cril.pbd4.pbc.PropagationOutput.satisfiable;
import static fr.univartois.cril.pbd4.pbc.PropagationOutput.unknown;
import static fr.univartois.cril.pbd4.pbc.PropagationOutput.unsatisfiable;
import static fr.univartois.cril.pbd4.pbc.RangeVecInt.range;

import java.util.Collection;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.core.ICDCL;
import org.sat4j.pb.constraints.pb.PBConstr;
import org.sat4j.specs.ISolverService;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.SearchListener;
import org.sat4j.specs.TimeoutException;

import fr.univartois.cril.pbd4.pbc.hypergraph.DualHypergraph;
import fr.univartois.cril.pbd4.pbc.solver.PBSelectorSolver;
import fr.univartois.cril.pbd4.pbc.solver.SwitchableOrder;
import fr.univartois.cril.pbd4.pbc.solver.UnitPropagationListener;

/**
 * The OriginalPseudoBooleanFormula represents the original pseudo-Boolean
 * formula used as input for the compiler or model counter.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
final class OriginalPseudoBooleanFormula implements PseudoBooleanFormula {

    /**
     * The solver that actually manages the formula.
     */
    private final PBSelectorSolver solver;

    /**
     * The solving engine, which actually performs the search.
     */
    private final ISolverService engine;

    /**
     * The order used by the solver as branching heuristic.
     */
    private final SwitchableOrder order;

    /**
     * The original listener used in the solver.
     */
    private final SearchListener<?> originalListener;

    /**
     * The unit-propagation listener used to record the propagations performed
     * during the execution of the solver.
     */
    private final UnitPropagationListener unitPropagationListener;

    /**
     * The variables of this formula.
     */
    private final IVecInt variables;

    /**
     * The score of the DLCS heuristic for each variable (i.e., the number of
     * occurrences of each variable in the formula).
     */
    private final int[] dlcsScores;

    /**
     * Creates a new OriginalPseudoBooleanFormula.
     *
     * @param solver The solver that actually manages the formula.
     */
    OriginalPseudoBooleanFormula(PBSelectorSolver solver) {
        this.solver = solver;
        this.engine = (ISolverService) solver.getSolvingEngine();
        this.order = new SwitchableOrder();
        this.originalListener = ((ICDCL<?>) engine).getSearchListener();
        this.unitPropagationListener = new UnitPropagationListener();
        this.variables = range(1, numberOfVariables() + 1);
        this.dlcsScores = new int[solver.nVars() + 1];
        init();
    }

    /**
     * Initializes this formula, by retrieving the information from the solver,
     * and setting up the solver for future calls.
     */
    private void init() {
        // Setting up the solver.
        ((ICDCL<?>) engine).setOrder(order);
        solver.setKeepSolverHot(true);

        // Computing the DLCS scores.
        for (int v = 1; v <= numberOfVariables(); v++) {
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
     * @see
     * fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#numberOfConstraints()
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
        return score(variable, dlcsScores[variable]);
    }

    /**
     * Gives the VSADS score of a variable in this formula.
     *
     * @param variable The variable to get the score of.
     * @param dlcs The DLCS score of the variable.
     *
     * @return The score of the variable.
     */
    double score(int variable, int dlcs) {
        return engine.getVariableHeuristics()[variable] + 0.5 * dlcs;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#assume(int)
     */
    @Override
    public PseudoBooleanFormula assume(int literal) {
        throw new UnsupportedOperationException("Cannot assume a literal on the original formula!");
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#assume(IVecInt)
     */
    @Override
    public PseudoBooleanFormula assume(IVecInt literals) {
        return SubPseudoBooleanFormulaBuilder.of(this)
            .newAssumptions(literals)
            .build();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#connectedComponents()
     */
    @Override
    public Collection<PseudoBooleanFormula> connectedComponents() {
        throw new UnsupportedOperationException("Cannot compute the connected components of the original formula!");
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
     * If the result is unknown, the solver is then completely run to check whether the
     * formula is satisfiable.
     *
     * @param assumptions The assumptions to make when propagating.
     *
     * @return The output of the propagation.
     */
    private PropagationOutput internalPropagate(IVecInt assumptions) {
        unitPropagationListener.reset();

        // Trying to solve the formula using BCP.
        order.switchToBCP();
        solver.setSearchListener(unitPropagationListener);
        var bcpOutput = solve(assumptions);

        if (bcpOutput.isUnknown()) {
            // Solving completely the formula
            order.switchToComplete();
            solver.setSearchListener(originalListener);
            var completeOutput = solve(assumptions);

            // Only unsatisfiable outputs are considered as is:
            // satisfiable outputs require further exploration.
            if (completeOutput.isUnsatisfiable()) {
                return completeOutput;
            }
        }

        return bcpOutput;
    }

    /**
     * Solves this formula under the given assumptions.
     *
     * @param assumptions The assumptions to make when solving.
     *
     * @return The output of the solver.
     */
    private PropagationOutput solve(IVecInt assumptions) {
        try {
            if (solver.isSatisfiable(assumptions)) {
                // The solver has found a solution.
                return satisfiable(unitPropagationListener.getPropagatedLiterals());
            }

            // The formula is unsatisfiable.
            return unsatisfiable();

        } catch (TimeoutException e) {
            // The solver could not determine whether the formula was satisfiable.
            return unknown(unitPropagationListener.getPropagatedLiterals(), this);
        }
    }

    /**
     * Gives the {@code i}-th constraint in this formula.
     *
     * @param i The index of the constraint to get.
     *
     * @return The {@code i}-th constraint.
     */
    PBConstr getConstraint(int i) {
        return solver.getConstraint(i);
    }

    /**
     * Gives the indices of the constraints containing the given variable.
     *
     * @param variable The variable to consider.
     *
     * @return The indices of the constraints containing {@code variable}.
     */
    IVecInt getConstraintsContaining(int v) {
        return solver.getConstraintsContaining(v);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#hypergraph()
     */
    @Override
    public DualHypergraph hypergraph() {
        throw new UnsupportedOperationException("No DualHypergraph for original formula!");
    }

}
