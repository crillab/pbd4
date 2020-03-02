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
import java.util.Objects;

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;

/**
 * The AbstractD4 class implements the "skeleton" of the D4 algorithm [Lagniez and Marquis, 2017].
 * It relies on the Template Method design pattern to allow the computation of different outputs.
 *
 * @param <T> The type of intermediate results of the algorithm.
 * @param <R> The type of the value eventually computed by the algorithm.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public abstract class AbstractD4<T, R> {

    /**
     * The input pseudo-Boolean formula.
     */
    protected final PseudoBooleanFormula formula;

    /**
     * The cache in which formulae that have already been treated are stored.
     */
    private final CachingStrategy<T> cache;

    /**
     * Creates a new AbstractD4.
     *
     * @param configuration The configuration of the algorithm.
     */
    protected AbstractD4(D4 configuration) {
        this.formula = Objects.requireNonNull(configuration.getFormula());
        this.cache = Objects.requireNonNull(configuration.getCache());
    }

    /**
     * Executes the D4 algorithm on the associated pseudo-Boolean formula.
     *
     * @return The output of the algorithm on the input formula.
     */
    public final R compute() {
        T intermediateResult = compute(formula, VecInt.EMPTY);
        return toFinalResult(intermediateResult);
    }

    /**
     * Executes the D4 algorithm on the given pseudo-Boolean formula.
     *
     * @param subFormula The sub-formula for which a computation must be performed.
     * @param variables The subset of variables to consider while performing the
     *        computation.
     *
     * @return The intermediate result of the computation on the given formula.
     */
    private T compute(PseudoBooleanFormula subFormula, IVecInt variables) {
        // Applying BCP to the formula.
        var output = subFormula.propagate();

        if (output.isUnsatisfiable()) {
            // A conflict has been encountered while propagating.
            return unsatisfiable();
        }

        // Retrieving the literals that have been propagated.
        var propagatedLiterals = output.getPropagatedLiterals();
        int nbFreeVariables = subFormula.numberOfVariables() - propagatedLiterals.size();
        
        if (output.isSatisfiable()) {
            // A solution has been found while propagating.
            // The propagated literals form an implicant of the sub-formula.
            return implicant(nbFreeVariables, propagatedLiterals);
        }
        
        // Looking for the resulting formula in the cache.
        var simplifiedFormula = output.getSimplifiedFormula();
        nbFreeVariables -= simplifiedFormula.numberOfVariables();
        var cached = cache.get(simplifiedFormula);
        if (cached.isPresent()) {
            return cached(nbFreeVariables, propagatedLiterals, cached.get());
        }

        // Recursively compiling the connected components of the resulting formula.
        var conjuncts = new LinkedList<T>();
        for (var component : simplifiedFormula.connectedComponents()) {
            // Updating the variables to consider.
            var componentVariables = restrict(variables, component.variables());
            if (componentVariables.isEmpty()) {
                componentVariables = component.cutset();
            }

            // Making a decision.
            var v = componentVariables.last();
            componentVariables = componentVariables.pop();
            conjuncts.add(decision(v,
                    compute(component.assume(v), componentVariables),
                    compute(component.assume(-v), componentVariables)));
        }

        // Computing the result of the conjunction, and caching the result.
        T result = conjunction(nbFreeVariables, propagatedLiterals, conjuncts);
        cache.put(simplifiedFormula, result);
        simplifiedFormula.onCaching();
        return result;
    }

    /**
     * Produces the intermediate result of the algorithm in the case of an unsatisfiable
     * formula.
     *
     * @return The intermediate result of the algorithm on an unsatisfiable formula.
     */
    protected abstract T unsatisfiable();

    /**
     * Produces the intermediate result of the algorithm when an implicant of the input
     * formula is found.
     *
     * @param nbFreeVariables The number of free variables (i.e., that may take any truth
     *        value regardless of the assignment of the other literals).
     * @param implicant The literals forming an implicant of the formula.
     *
     * @return The intermediate result of the algorithm on a satisfiable formula.
     */
    protected abstract T implicant(int nbFreeVariables, IVecInt implicant);

    /**
     * Produces the intermediate result of the algorithm when BCP yields a formula that
     * has been cached.
     *
     * @param nbFreeVariables The number of free variables (i.e., that may take any truth
     *        value regardless of the assignment of the other literals).
     * @param propagatedLiterals The literals that have been propagated, yielding a
     *        cached formula.
     * @param cached The intermediate value that have been cached.
     *
     * @return The intermediate result of the algorithm for the cached value.
     */
    protected abstract T cached(int nbFreeVariables, IVecInt propagatedLiterals, T cached);

    /**
     * Restrict the content of an {@link IVecInt} to the elements appearing in an other
     * {@link IVecInt}.
     *
     * @param elements The elements to filter out.
     * @param toKeep The elements to keep in the vector.
     *
     * @return The restricted {@link IVecInt}.
     */
    private IVecInt restrict(IVecInt elements, IVecInt toKeep) {
        var restricted = new VecInt();
        for (var it = elements.iterator(); it.hasNext();) {
            var v = it.next();
            if (toKeep.contains(v)) {
                restricted.push(v);
            }
        }
        return restricted;
    }

    /**
     * Produces the intermediate result of the algorithm when a decision is made on a
     * variable.
     *
     * @param variable The variable on which a decision is made.
     * @param ifTrue The intermediate result of the algorithm when considering the
     *        variable as satisfied.
     * @param ifFalse The intermediate result of the algorithm when considering the
     *        variable as falsified.
     *
     * @return The intermediate result of the algorithm for the decision.
     */
    protected abstract T decision(int variable, T ifTrue, T ifFalse);

    /**
     * Produces the intermediate result of the algorithm for a conjunction.
     *
     * @param nbFreeVariables The number of free variables (i.e., that may take any truth
     *        value regardless of the assignment of the other literals).
     * @param literals The literals taking part in the conjunction.
     * @param conjuncts The intermediate results of the algorithm to conjunct.
     *
     * @return The intermediate result of the algorithm for the conjunction.
     */
    protected abstract T conjunction(int nbFreeVariables, IVecInt literals, List<T> conjuncts);

    /**
     * Produces the final result of the algorithm from the intermediate result it has produced.
     *
     * @param intermediateResult The intermediate result produced by the algorithm.
     *
     * @return The final result of the algorithm.
     */
    protected abstract R toFinalResult(T intermediateResult);

}
