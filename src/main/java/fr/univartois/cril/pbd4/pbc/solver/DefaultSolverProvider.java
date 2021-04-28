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

package fr.univartois.cril.pbd4.pbc.solver;

import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.SolverFactory;

/**
 * The DefaultSolverProvider is the default implementation of {@link SolverProvider},
 * which creates cutting-planes-based solvers on pseudo-Boolean inputs and
 * resolution-based solvers on CNF inputs.
 * 
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
final class DefaultSolverProvider implements SolverProvider {

    /**
     * The single instance of this class.
     */
    private static final SolverProvider INSTANCE = new DefaultSolverProvider();

    /**
     * Disables external instantiation.
     */
    private DefaultSolverProvider() {
        // Nothing to do: Singleton Design Pattern.
    }

    /**
     * Gives the single instance of this class.
     *
     * @return The instance of {@link DefaultSolverProvider}.
     */
    public static SolverProvider instance() {
        return INSTANCE;
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.solver.SolverProvider#createPBSolver()
     */
    @Override
    public IPBSolver createPBSolver() {
        return SolverFactory.newPartialRoundingSatPOS2020WL();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.solver.SolverProvider#createSolver()
     */
    @Override
    public IPBSolver createSolver() {
        return SolverFactory.newDefault();
    }

}
