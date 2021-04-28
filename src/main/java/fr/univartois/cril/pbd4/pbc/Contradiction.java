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

import java.util.Collection;
import java.util.List;

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.pbc.hypergraph.DualHypergraph;

/**
 * The Contradiction represents a pseudo-Boolean formula that is unsatisfiable.
 * The methods it defines are mainly place-holders.
 * The main purpose of this class is to return {@link PropagationOutput#unsatisfiable()}
 * when propagating.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public final class Contradiction implements PseudoBooleanFormula {

    /**
     * The single instance of this class.
     */
    private static final PseudoBooleanFormula INSTANCE = new Contradiction();

    /**
     * Disables external instantiation.
     */
    private Contradiction() {
        // Nothing to do: Singleton Design Pattern.
    }

    /**
     * Gives the single instance of this class.
     *
     * @return The single instance of this class.
     */
    public static PseudoBooleanFormula instance() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#numberOfVariables()
     */
    @Override
    public int numberOfVariables() {
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#numberOfConstraints()
     */
    @Override
    public int numberOfConstraints() {
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#variables()
     */
    @Override
    public IVecInt variables() {
        return VecInt.EMPTY;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#score(int)
     */
    @Override
    public double score(int variable) {
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#assume(int)
     */
    @Override
    public PseudoBooleanFormula assume(int literal) {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#assume(IVecInt)
     */
    @Override
    public PseudoBooleanFormula assume(IVecInt literals) {
        return this;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#connectedComponents()
     */
    @Override
    public Collection<PseudoBooleanFormula> connectedComponents() {
        return List.of(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#propagate()
     */
    @Override
    public PropagationOutput propagate() {
        return PropagationOutput.unsatisfiable();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula#hypergraph()
     */
    @Override
    public DualHypergraph hypergraph() {
        throw new UnsupportedOperationException("No DualHypergraph for contradictory formula!");
    }

}
