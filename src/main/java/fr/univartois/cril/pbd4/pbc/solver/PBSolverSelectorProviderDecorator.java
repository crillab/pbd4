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
import org.sat4j.pb.constraints.PuebloPBMinClauseCardConstrDataStructure;
import org.sat4j.pb.core.IPBCDCLSolver;
import org.sat4j.pb.core.PBDataStructureFactory;

/**
 * The PBSolverSelectorProviderDecorator is a decorator for solver providers,
 * which wraps each solver created by the decorated provider inside a
 * {@link PBSelectorSolver}.
 * 
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public final class PBSolverSelectorProviderDecorator implements SolverProvider {

    /**
     * The decorated solver provider.
     */
    private final SolverProvider decorated;

    /**
     * Creates a new PBSolverSelectorProviderDecorator.
     *
     * @param decorated The solver provider to decorate.
     */
    private PBSolverSelectorProviderDecorator(SolverProvider decorated) {
        this.decorated = decorated;
    }

    /**
     * Creates a new PBSolverSelectorProviderDecorator.
     *
     * @param decorated The solver provider to decorate.
     *
     * @return The created PBSolverSelectorProviderDecorator.
     */
    public static PBSolverSelectorProviderDecorator of(SolverProvider decorated) {
        return new PBSolverSelectorProviderDecorator(decorated);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.solver.SolverProvider#createPBSolver()
     */
    @Override
    public PBSelectorSolver createPBSolver() {
        return asPBSelectorSolver(decorated.createPBSolver());
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.solver.SolverProvider#createSatSolver()
     */
    @Override
    public PBSelectorSolver createSatSolver() {
        return asPBSelectorSolver(decorated.createSatSolver());
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.pbc.solver.SolverProvider#createSolver()
     */
    @Override
    public PBSelectorSolver createSolver() {
        return asPBSelectorSolver(decorated.createSolver());
    }

    /**
     * Decorates the given solver with an instance of {@link PBSelectorSolver}.
     *
     * @param solver The solver to decorate.
     *
     * @return An instance of {@link PBSelectorSolver} decorating the given solver.
     */
    private static PBSelectorSolver asPBSelectorSolver(IPBSolver solver) {
        // First, making sure that the solver uses the expected data structure factory.
        @SuppressWarnings("unchecked")
        var cdclSolver = (IPBCDCLSolver<PBDataStructureFactory>) solver;
        cdclSolver.setDataStructureFactory(new PuebloPBMinClauseCardConstrDataStructure());

        // Decorating the solver.
        return new PBSelectorSolver(solver);
    }

}
