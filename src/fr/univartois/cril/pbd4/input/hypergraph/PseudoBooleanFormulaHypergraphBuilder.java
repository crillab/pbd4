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

package fr.univartois.cril.pbd4.input.hypergraph;

import org.sat4j.core.ConstrGroup;
import org.sat4j.specs.Constr;
import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVecInt;

/**
 * The PseudoBooleanFormulaHypergraphBuilder defines the interface for building a
 * hypergraph representation of a pseudo-Boolean formula.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface PseudoBooleanFormulaHypergraphBuilder {

    /**
     * Sets the number of variables in the formula to build the hypergraph of.
     *
     * @param numberOfVariables The number of variables.
     */
    void setNumberOfVariables(int numberOfVariables);

    /**
     * Sets the number of constraints in the formula to build the hypergraph of.
     *
     * @param numberOfConstraints The number of constraints.
     */
    void setNumberOfConstraints(int numberOfConstraints);

    /**
     * Adds a constraint to the hypergraph representation that is being built.
     *
     * @param constraint The constraint to add.
     * @param literals The literals of the constraint to add (as {@code constraint} may be
     *        a {@link ConstrGroup}, which does not support literal retrieval).
     */
    void addConstraint(IConstr constraint, IVecInt literals);

    /**
     * Adds a constraint to the hypergraph representation that is being built.
     *
     * @param constraint The constraint to add.
     */
    void addConstraint(Constr constraint);

    /**
     * Creates the hypergraph representation of a pseudo-Boolean formula that have been
     * built.
     *
     * @return The built hypergraph representation.
     */
    PseudoBooleanFormulaHypergraph build();

}
