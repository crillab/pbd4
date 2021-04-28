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
 * The NoCutsetUpdateStrategy never triggers an update of the cutset.
 * It is a Singleton for a Null Object.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public final class NoCutsetUpdateStrategy implements CutsetUpdateStrategy {

    /**
     * The single instance of this class.
     */
    private static final CutsetUpdateStrategy INSTANCE = new NoCutsetUpdateStrategy();

    /**
     * Disables external instantiation.
     */
    private NoCutsetUpdateStrategy() {
        // Nothing to do: Singleton Design Pattern.
    }

    /**
     * Gives the single instance of NoCutsetUpdateStrategy.
     *
     * @return The single instance of this class.
     */
    public static CutsetUpdateStrategy instance() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.partitioning.CutsetUpdateStrategy#shouldUpdate(fr.
     * univartois.cril.pbd4.pbc.PseudoBooleanFormula,
     * fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula)
     */
    @Override
    public boolean shouldUpdate(PseudoBooleanFormula previous, PseudoBooleanFormula current) {
        return false;
    }

}
