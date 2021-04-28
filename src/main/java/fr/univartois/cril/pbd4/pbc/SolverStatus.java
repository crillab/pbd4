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

/**
 * The SolverStatus enumeration represents the different statuses that a pseudo-Boolean
 * solver may return.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
enum SolverStatus {

    /**
     * The UNSATISFIABLE status, meaning that the considered pseudo-Boolean formula does
     * not have any model.
     */
    UNSATISFIABLE,

    /**
     * The SATISFIABLE status, meaning that the considered pseudo-Boolean formula has at
     * least one model, that the solver has found.
     * An input is only considered as SATISFIABLE when Boolean constraint propagation is
     * enough to find a model (in the context of the compiler, of course).
     */
    SATISFIABLE,

    /**
     * The UNKNOWN status, meaning that the solver does not know yet whether the
     * considered pseudo-Boolean formula is SATISFIABLE or UNSATISFIABLE.
     */
    UNKNOWN;

}
