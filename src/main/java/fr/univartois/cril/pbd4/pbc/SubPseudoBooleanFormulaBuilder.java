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

import java.math.BigInteger;
import java.util.BitSet;
import java.util.OptionalInt;

import org.sat4j.core.LiteralsUtils;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

/**
 * The SubPseudoBooleanFormulaBuilder makes easier the building of sub-formulae.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
final class SubPseudoBooleanFormulaBuilder {

    /**
     * The original formula, from which the sub-formula is extracted.
     */
    private final OriginalPseudoBooleanFormula original;

    /**
     * The literal on which a decision has been made (if any).
     */
    private OptionalInt decision;

    /**
     * The assumptions that have been made to create the initial formula.
     */
    private IVecInt initialAssumptions;

    /**
     * The assumptions that have been added to create the new sub-formula.
     */
    private IVecInt newAssumptions;

    /**
     * The whole set of assumptions, characterizing the new sub-formula.
     */
    private IVecInt allAssumptions;

    /**
     * The literals that are satisfied.
     */
    private BitSet satisfiedLiterals;

    /**
     * The constraints that are deactivated.
     */
    private BitSet inactiveConstraints;

    /**
     * The variables that may appear in the formula.
     */
    private IVecInt possibleVariables;

    /**
     * The variables appearing in the formula.
     */
    private IVecInt variables;

    /**
     * The DLCS scores of the variables, updated w.r.t. the constraints in the
     * sub-formula.
     */
    private final int[] updatedDlcsScores;

    /**
     * Creates a new SubPseudoBooleanFormulaBuilder.
     *
     * @param original The original formula, from which the sub-formula is extracted.
     */
    private SubPseudoBooleanFormulaBuilder(OriginalPseudoBooleanFormula original) {
        this.original = original;
        this.decision = OptionalInt.empty();
        this.initialAssumptions = VecInt.EMPTY;
        this.newAssumptions = VecInt.EMPTY;
        this.possibleVariables = original.variables();
        this.updatedDlcsScores = new int[original.numberOfVariables() + 1];
    }

    /**
     * Creates a new SubPseudoBooleanFormulaBuilder.
     *
     * @param original The original formula, from which the sub-formula is extracted.
     *
     * @return The created builder.
     */
    public static SubPseudoBooleanFormulaBuilder of(OriginalPseudoBooleanFormula original) {
        return new SubPseudoBooleanFormulaBuilder(original);
    }

    /**
     * Gives the original formula, from which the sub-formula is extracted.
     *
     * @return The original formula.
     */
    OriginalPseudoBooleanFormula getOriginalFormula() {
        return original;
    }

    /**
     * Sets the decision that has been taken.
     * 
     * @param literal The literal that is satisfied by the decision.
     *
     * @return This builder.
     */
    SubPseudoBooleanFormulaBuilder decision(int literal) {
        this.decision = OptionalInt.of(literal);
        this.newAssumptions = VecInt.of(literal);
        return this;
    }

    /**
     * Gives the literal that is satisfied by the decision (if any).
     * 
     * @return The literal on which a decision has been made.
     */
    OptionalInt getDecision() {
        return decision;
    }

    /**
     * Sets the assumptions that have been made to create the initial formula.
     *
     * @param assumptions The assumptions that have been made.
     *
     * @return This builder.
     */
    SubPseudoBooleanFormulaBuilder initialAssumptions(IVecInt assumptions) {
        this.initialAssumptions = assumptions;
        return this;
    }

    /**
     * Sets the assumptions that have been added to create the new sub-formula.
     *
     * @param assumptions The assumptions that have been added.
     *
     * @return This builder.
     */
    SubPseudoBooleanFormulaBuilder newAssumptions(IVecInt assumptions) {
        this.newAssumptions = assumptions;
        return this;
    }

    /**
     * Gives the whole set of assumptions, characterizing the new sub-formula.
     *
     * @return The whole set of assumptions.
     */
    IVecInt getAssumptions() {
        return allAssumptions;
    }

    /**
     * Sets the literals that are satisfied.
     *
     * @param satisfiedLiterals The literals that are satisfied.
     *
     * @return This builder.
     */
    SubPseudoBooleanFormulaBuilder satisfiedLiterals(BitSet satisfiedLiterals) {
        this.satisfiedLiterals = satisfiedLiterals;
        return this;
    }

    /**
     * Gives the literals that are satisfied.
     *
     * @return The literals that are satisfied.
     */
    BitSet getSatisfiedLiterals() {
        if (satisfiedLiterals == null) {
            satisfiedLiterals = new BitSet(2 + (original.numberOfVariables() << 1));
        }

        return satisfiedLiterals;
    }

    /**
     * Sets the constraints that are deactivated.
     *
     * @param inactiveConstraints The constraints that are deactivated.
     *
     * @return This builder.
     */
    SubPseudoBooleanFormulaBuilder inactiveConstraints(BitSet inactiveConstraints) {
        this.inactiveConstraints = inactiveConstraints;
        return this;
    }

    /**
     * Gives the constraints that are deactivated.
     *
     * @return The constraints that are deactivated.
     */
    BitSet getInactiveConstraints() {
        if (inactiveConstraints == null) {
            inactiveConstraints = new BitSet(original.numberOfConstraints());
        }

        return inactiveConstraints;
    }

    /**
     * Sets the variables that may appear in the formula.
     *
     * @param variables The variables that may appear in the formula.
     *
     * @return This builder.
     */
    SubPseudoBooleanFormulaBuilder possibleVariables(IVecInt variables) {
        this.possibleVariables = variables;
        return this;
    }

    /**
     * Gives the variables appearing in the formula.
     *
     * @return The variables appearing in the formula.
     */
    IVecInt getVariables() {
        return variables;
    }

    /**
     * Gives the DLCS scores of the variables, updated w.r.t. the constraints in the
     * sub-formula.
     *
     * @return The DLCS scores of the variables.
     */
    int[] getUpdatedDlcsScores() {
        return updatedDlcsScores;
    }

    /**
     * Creates the sub-formula that has been built.
     *
     * @return The built sub-formula.
     */
    SubPseudoBooleanFormula build() {
        // Updating the internal structure w.r.t. the new assumptions.
        allAssumptions = new VecInt(initialAssumptions.size() + newAssumptions.size());
        initialAssumptions.copyTo(allAssumptions);
        updateAssumptions();

        // Updating the internal structure w.r.t. the remaining variables.
        updateVariables();

        // Creating the sub-formula.
        return new SubPseudoBooleanFormula(this);
    }

    /**
     * Updates this sub-formula to consider the new assumptions.
     */
    private void updateAssumptions() {
        for (var it = newAssumptions.iterator(); it.hasNext();) {
            int dimacs = it.next();
            updateAssumption(dimacs);
        }
    }

    /**
     * Updates this sub-formula to consider a new assumed literal.
     * 
     * @param dim The assumed literal to consider.
     */
    private void updateAssumption(int dimacs) {
        int variable = Math.abs(dimacs);
        int literal = LiteralsUtils.toInternal(dimacs);

        // Updating the literal data structure.
        allAssumptions.push(dimacs);
        getSatisfiedLiterals().set(literal);

        // Updating the constraint data structure.
        for (var it = original.getConstraintsContaining(variable).iterator(); it.hasNext();) {
            int constrIndex = it.next();

            if (getInactiveConstraints().get(constrIndex)) {
                // This constraint is inactive, and is thus ignored.
                continue;
            }

            // Computing the current value of the constraint.
            var constr = original.getConstraint(constrIndex);
            var constrValue = BigInteger.ZERO;
            for (int i = 0; i < constr.size(); i++) {
                int lit = constr.get(i);

                if (LiteralsUtils.var(lit) > original.numberOfVariables()) {
                    // This literal is used as a selector, and must be ignored.
                    continue;
                }

                if (satisfiedLiterals.get(lit)) {
                    constrValue = constrValue.add(constr.getCoef(i));
                }
            }

            // Is the constraint to be deactivated?
            if (constrValue.compareTo(constr.getDegree()) >= 0) {
                getInactiveConstraints().set(constrIndex);
            }
        }
    }

    /**
     * Computes the variables appearing in the sub-formula, and those that have been removed.
     */
    private void updateVariables() {
        // Collecting the variables that remain in this formula.
        variables = new VecInt(possibleVariables.size());
        for (var it = possibleVariables.iterator(); it.hasNext();) {
            int variable = it.next();

            if (isAssigned(variable)) {
                // This variable does not appear anymore in the formula.
                continue;
            }

            // Counting the occurrences of the variable in the remaining constraints.
            int occurrences = countOccurrences(variable);
            if (occurrences > 0) {
                updatedDlcsScores[variable] = occurrences;
                variables.push(variable);
            }
        }
    }

    /**
     * Checks whether {@code variable} is assigned.
     *
     * @param variable The variable to check.
     *
     * @return Whether {@code variable} is assigned.
     */
    private boolean isAssigned(int variable) {
        int posLit = LiteralsUtils.posLit(variable);
        int negLit = LiteralsUtils.negLit(variable);
        return getSatisfiedLiterals().get(posLit) || getSatisfiedLiterals().get(negLit);
    }

    /**
     * Counts how many times a variable appears in the sub-formula.
     *
     * @param variable The variable to count the occurrences of.
     *
     * @return The number of occurrences of {@code variable} in the sub-formula.
     */
    private int countOccurrences(int variable) {
        int occurrences = 0;
        for (var it = original.getConstraintsContaining(variable).iterator(); it.hasNext();) {
            int constr = it.next();
            if (!getInactiveConstraints().get(constr)) {
                occurrences++;
            }
        }
        return occurrences;
    }

}
