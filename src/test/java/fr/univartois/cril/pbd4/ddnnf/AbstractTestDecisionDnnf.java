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

package fr.univartois.cril.pbd4.ddnnf;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * The AbstractTestDecisionDnnf is the parent class for the test cases of the
 * representation of decision-DNNFs.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public abstract class AbstractTestDecisionDnnf {

    /**
     * Reads a decision-DNNF from a resource file.
     *
     * @param filename The name of the file to read.
     *        This file is supposed to be located in the {@code ddnnf} resource folder.
     *
     * @return The read decision-DNNF.
     *
     * @throws UncheckedIOException If an I/O error occurs while reading.
     */
    protected DecisionDnnf read(String filename) {
        try (var reader = new DecisionDnnfReader(getClass().getResourceAsStream("/ddnnf/" + filename))) {
            return reader.read();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
