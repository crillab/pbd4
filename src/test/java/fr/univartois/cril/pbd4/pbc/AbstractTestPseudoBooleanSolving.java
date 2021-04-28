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

import java.io.IOException;
import java.io.UncheckedIOException;

import fr.univartois.cril.pbd4.pbc.solver.SolverProvider;

/**
 * The AbstractTestPseudoBooleanSolving is the parent class for the test cases of the
 * representation of pseudo-Boolean formulae.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public abstract class AbstractTestPseudoBooleanSolving {

    /**
     * Creates the default PseudoBooleanFormulaReader.
     * It is mainly designed to be used for test cases.
     *
     * @return The default reader, which uses {@link SolverProvider#defaultProvider()}
     *         as provider for the solver to use to deal with the read pseudo-Boolean
     *         formulae.
     */
    static PseudoBooleanFormulaReader defaultReader() {
        return new PseudoBooleanFormulaReader(SolverProvider.defaultProvider());
    }

    /**
     * Reads a pseudo-Boolean formula from a CNF resource file.
     *
     * @param filename The name of the file to read.
     *        This file is supposed to be located in the {@code cnf} resource folder.
     *
     * @return The read pseudo-Boolean formula.
     *
     * @throws UncheckedIOException If an I/O error occurs while reading.
     */
    protected OriginalPseudoBooleanFormula readCnf(String filename) {
        try (var input = getClass().getResourceAsStream("/cnf/" + filename)) {
            return (OriginalPseudoBooleanFormula) defaultReader().readCnf(input);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Reads a pseudo-Boolean formula from an OPB resource file.
     *
     * @param filename The name of the file to read.
     *        This file is supposed to be located in the {@code opb} resource folder.
     *
     * @return The read pseudo-Boolean formula.
     *
     * @throws UncheckedIOException If an I/O error occurs while reading.
     */
    protected OriginalPseudoBooleanFormula readOpb(String filename) {
        try (var input = getClass().getResourceAsStream("/opb/" + filename)) {
            return (OriginalPseudoBooleanFormula) defaultReader().readOpb(input);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
