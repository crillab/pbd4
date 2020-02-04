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

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.input.PseudoBooleanFormula;
import fr.univartois.cril.pbd4.input.SolverStatus;

/**
 * The AbstractD4 class implements the "skeleton" of the D4 algorithm [Lagniez and
 * Marquis, 2017].
 * It relies on the Template Method design pattern to allow the computation of either the
 * number of models of the input pseudo-Boolean formula, or its representation as a
 * d-DNNF.
 *
 * @param <T> The type of the value computed by the D4 algorithm.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public abstract class AbstractD4<T> {

    /**
     * The pseudo-Boolean solver to use as an oracle during the compilation process.
     */
    private final PseudoBooleanFormula formula;

    /**
     * The cache in which formula that have already been treated are stored.
     */
    private final CachingStrategy<T> cache;
    
    private final IVec<PseudoBooleanFormula> stack;

    /**
     * Creates a new AbstractD4.
     *
     * @param configuration The configuration of the algorithm.
     */
    protected AbstractD4(D4 configuration) {
        this.formula = Objects.requireNonNull(configuration.getFormula());
        this.cache = Objects.requireNonNull(configuration.getCache());
        this.stack = new Vec<>();
    }

    /**
     * Executes the D4 algorithm on the associated pseudo-Boolean formula.
     *
     * @return The output of the algorithm on the input formula.
     */
    public T compute() {
        stack.push(formula);
        return compile(formula, new VecInt());
    }

    /**
     * Compiles the associated pseudo-Boolean formula into a d-DNNF.
     * 
     * @param variables TODO
     *
     * @return The root node of a d-DNNF equivalent to the associated formula.
     */
    private T compile(PseudoBooleanFormula currentFormula, IVecInt variables) {
        // Applying BCP to the formula.
        var output = currentFormula.propagate();
        var status = output.getStatus();

        if (status == SolverStatus.UNSATISFIABLE) {
            // A conflict has been encountered while propagating.
            return unsatisfiable();
        }

        var propagatedLiterals = output.getPropagatedLiterals();
        if (status == SolverStatus.SATISFIABLE) {
            // A solution has been found while propagating.
            return model(currentFormula, propagatedLiterals);
        }

        // Retrieving the formula from the cache.
        var simplifiedFormula = output.getSimplifiedFormula();
        var cached = cache.get(simplifiedFormula);
        if (cached.isPresent()) {
            return cached(propagatedLiterals, cached.get());
        }

        var lnd = new LinkedList<T>();
        for (var c : simplifiedFormula.connectedComponents()) {
            var lvc = restrict(variables, simplifiedFormula.variables());
            if (lvc.isEmpty()) {
                lvc = simplifiedFormula.cutset();
            }

            var v = lvc.last();
            lvc = lvc.pop();
            lnd.add(ifThenElse(v, compile(simplifiedFormula.simplify(v), lvc),
                    compile(simplifiedFormula.simplify(-v), lvc)));
        }

        // Caching the result.
        var result = conjunctionOf(lnd);
        cache.put(simplifiedFormula, result);
        return result;

    }
    
    /**
     * Produces the output of the algorithm in the case of an unsatisfiable formula.
     *
     * @return The output of the algorithm on an unsatisfiable formula.
     */
    protected abstract T unsatisfiable();

    /**
     * Produces the output of the algorithm when a model of the input formula is found.
     * 
     * @param currentFormula The formula of which a model has been found.
     * @param propagatedLiterals The literals that were propagated after the 
     * 
     * @return
     */
    protected abstract T model(PseudoBooleanFormula currentFormula, IVecInt propagatedLiterals);

    protected abstract T ifThenElse(int v, T compile, T compile2);

    protected abstract T conjunctionOf(Collection<T> lnd);

    protected abstract T cached(IVecInt propagatedLiterals, T cached);

    /**
     * Restrict the content of an {@link IVecInt} to the elements appearing in an other
     * {@link IVecInt}.
     * 
     * @param elements The elements to filter out.
     * @param toKeep The elements to keep in the vector.
     * @return
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

}
