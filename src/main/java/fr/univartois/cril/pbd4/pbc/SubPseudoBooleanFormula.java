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

import java.util.BitSet;
import java.util.Collection;
import java.util.OptionalInt;

import org.sat4j.core.LiteralsUtils;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

/**
 * The SubPseudoBooleanFormula represents a sub-part of a pseudo-Boolean formula that is
 * being compiled.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
final class SubPseudoBooleanFormula implements PseudoBooleanFormula {

    /**
     * The original formula, from which this sub-formula has been extracted.
     */
    private final OriginalPseudoBooleanFormula decorated;

    /**
     * The literal on which a decision has been made (if any).
     */
    private final OptionalInt decision;

    /**
     * The assumptions that have been made to create this sub-formula.
     */
    private final IVecInt assumptions;

    /**
     * The literals that have been satisfied.
     */
    private final BitSet satisfiedLiterals;

    /**
     * The constraints that have been deactivated.
     */
    private final BitSet inactiveConstraints;

    /**
     * The variables appearing in this formula.
     */
    private final IVecInt variables;

    /**
     * The DLCS scores of the variables, updated w.r.t. the constraints in this sub-formula.
     */
    private final int[] updatedDlcsScores;
    
    /**
     * Creates a new SubPseudoBooleanFormula.
     *
     * @param builder The builder used to initialize the formula.
     */
    SubPseudoBooleanFormula(SubPseudoBooleanFormulaBuilder builder) {
        this.decorated = builder.getOriginalFormula();
        this.decision = builder.getDecision();
        this.assumptions = builder.getAssumptions();
        this.satisfiedLiterals = builder.getSatisfiedLiterals();
        this.inactiveConstraints = builder.getInactiveConstraints();
        this.variables = builder.getVariables();
        this.updatedDlcsScores = builder.getUpdatedDlcsScores();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#numberOfVariables()
     */
    @Override
    public int numberOfVariables() {
        return variables.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#numberOfConstraints()
     */
    @Override
    public int numberOfConstraints() {
        return decorated.numberOfConstraints() - inactiveConstraints.cardinality();
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
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#score()
     */
    @Override
    public double score(int variable) {
        return decorated.score(variable, updatedDlcsScores[variable]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#assume(int)
     */
    @Override
    public PseudoBooleanFormula assume(int literal) {
        return SubPseudoBooleanFormulaBuilder.of(decorated)
                .initialAssumptions(assumptions)
                .satisfiedLiterals((BitSet) satisfiedLiterals.clone())
                .inactiveConstraints((BitSet) inactiveConstraints.clone())
                .possibleVariable(variables)
                .decision(literal)
                .build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#assume(IVecInt)
     */
    @Override
    public PseudoBooleanFormula assume(IVecInt literals) {
        return SubPseudoBooleanFormulaBuilder.of(decorated)
                .initialAssumptions(assumptions)
                .satisfiedLiterals((BitSet) satisfiedLiterals.clone())
                .inactiveConstraints((BitSet) inactiveConstraints.clone()) 
                .possibleVariable(variables)
                .newAssumptions(literals)
                .build();
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
        // Applying BCP to the formula with the assumptions characterizing this sub-formula.
        var output = decorated.propagate(computeAssumptions());

        if (output.isUnsatisfiable()) {
            // There is nothing more to do.
            return output;
        }

        // The decision, if any, must be added to the propagated literals.
        var propagatedLiterals = output.getPropagatedLiterals();
        decision.ifPresent(propagatedLiterals::push);

        // Returning the appropriate output.
        if (output.isSatisfiable()) {
            return PropagationOutput.satisfiable(propagatedLiterals);
        }
        return PropagationOutput.unknown(propagatedLiterals, this);
    }

    /**
     * Computes the assumptions characterizing this sub-formula.
     *
     * @return The assumptions characterizing this sub-formula.
     */
    private IVecInt computeAssumptions() {
        // Copying the true literal assumptions.
        var effectiveAssumptions = new VecInt(assumptions.size() + inactiveConstraints.size());
        assumptions.copyTo(effectiveAssumptions);

        // Computing the assumptions for the constraint selectors.
        for (int i = 0; i < inactiveConstraints.size(); i++) {
            if (inactiveConstraints.get(i)) {
                // This constraint must be ignored.
                effectiveAssumptions.push(LiteralsUtils.posLit(numberOfVariables() + i + 1));

            } else {
                // This constraint must be considered.
                effectiveAssumptions.push(LiteralsUtils.negLit(numberOfVariables() + i + 1));
            }
        }
        return effectiveAssumptions;
    }

}
