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

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * The AbstractTestPseudoBooleanSolving is the parent class for the test cases of the
 * representation of pseudo-Boolean formulae.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public abstract class AbstractTestPseudoBooleanSolving {

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
    protected PseudoBooleanFormula readCnf(String filename) {
        try (var input = getClass().getResourceAsStream("/cnf/" + filename)) {
            return PseudoBooleanFormulaReader.readCnf(input);

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
    protected PseudoBooleanFormula readOpb(String filename) {
        try (var input = getClass().getResourceAsStream("/opb/" + filename)) {
            return PseudoBooleanFormulaReader.readOpb(input);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
