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

import org.sat4j.specs.IVecInt;

/**
 * The PseudoBooleanFormula defines the interface for the formulae considered by
 * the compiler or model counter.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface PseudoBooleanFormula {

	/**
	 * Gives the number of variables in this formula.
	 * Only variables that are not assigned yet are counted.
	 *
	 * @return The number of variables in this formula.
	 */
	int numberOfVariables();

	/**
	 * Gives the number of constraints in this formula.
	 * Only constraints that are not satisfied yet are counted.
	 *
	 * @return The number of constraints in this formula.
	 */
	int numberOfConstraints();

	/**
	 * Gives the variables appearing in this formula.
	 * Only variables that are not assigned yet are present.
	 *
	 * @return The variables in this formula.
	 */
	IVecInt variables();
	
	double score(int variable);

	/**
	 * Gives the pseudo-Boolean formula obtained from this formula by satisfying the
	 * given literal.
	 *
	 * @param literal The literal to satisfy.
	 *
	 * @return The formula obtained by satisfying {@code literal}.
	 */
	PseudoBooleanFormula satisfy(int literal);

	/**
	 * Gives a cutset of this formula, i.e., a set of variables to assign so as to
	 * get a formula that have at least two connected components.
	 *
	 * @return The variables in the cutset.
	 */
	IVecInt cutset();

	/**
	 * Gives the connected components of this formula, i.e., a collection of
	 * pseudo-Boolean formulae that are sub-formulae of this formula that do not
	 * share any variable.
	 *
	 * @return The connected components of this formula.
	 */
	Collection<PseudoBooleanFormula> connectedComponents();

	/**
	 * Applies Boolean Constraint Propagation (BCP) on this formula.
	 *
	 * @return The output of the propagation.
	 */
	PropagationOutput propagate();

}
