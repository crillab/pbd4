/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
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

package fr.univartois.cril.pbd4.partitioning;

import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;

/**
 * The CutsetUpdateStrategy defines a strategy for (heuristically) determining
 * when the cutset of the dual hypergraph associated to a pseudo-Boolean formula
 * should be updated.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
@FunctionalInterface
public interface CutsetUpdateStrategy {

	/**
	 * Determines whether the cutset of the dual hypergraph associated to a
	 * pseudo-Boolean formula should be updated.
	 *
	 * @param previous The previous pseudo-Boolean formula that was considered.
	 * @param current The formula that is currently being considered.
	 *
	 * @return Whether to update the cutset.
	 */
	boolean shouldUpdate(PseudoBooleanFormula previous, PseudoBooleanFormula current);

}
