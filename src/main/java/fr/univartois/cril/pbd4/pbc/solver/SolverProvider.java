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

import org.sat4j.pb.SolverFactory;
import org.sat4j.pb.IPBSolver;

/**
 * The SolverProvider interface defines an abstract factory for Sat4j solvers
 * that allows to instantiate solvers that are most likely to perform well
 * depending on the type of the input formula (CNF or PB).
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
@FunctionalInterface
public interface SolverProvider {

    /**
     * Creates the default provider used when no specific solver is set up.
     *
     * @return A {@link SolverProvider} that creates cutting-planes-based
     *         solvers on pseudo-Boolean inputs and resolution-based solvers
     *         on CNF inputs.
     */
    static SolverProvider defaultProvider() {
        return DefaultSolverProvider.instance();
    }

    /**
     * Creates a provider that always instantiate the solver with the given name.
     *
     * @param name The name of the solver instantiated by the provider.
     *
     * @return A {@link SolverProvider} that creates solvers with the given name.
     *
     * @see SolverFactory#createSolverByName(String)
     */
    static SolverProvider forSolver(String name) {
        return () -> SolverFactory.instance().createSolverByName(name);
    }

    /**
     * Creates a solver that is expected to perform well on pseudo-Boolean inputs.
     *
     * @return The created solver.
     *
     * @implSpec By default, this method returns the solver created by
     *           {@link #createSolver()}.
     */
    default IPBSolver createPBSolver() {
        return createSolver();
    }

    /**
     * Creates a solver that is expected to perform well on CNF inputs.
     *
     * @return The created solver.
     *
     * @implSpec By default, this method returns the solver created by
     *           {@link #createSolver()}.
     */
    default IPBSolver createSatSolver() {
        return createSolver();
    }

    /**
     * Creates the default solver that is expected to perform well on any inputs.
     *
     * @return The created solver.
     */
    IPBSolver createSolver();

}
