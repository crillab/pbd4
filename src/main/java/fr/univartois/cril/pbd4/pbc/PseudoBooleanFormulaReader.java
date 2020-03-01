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
import java.io.InputStream;

import org.sat4j.pb.SolverFactory;
import org.sat4j.pb.reader.PBInstanceReader;
import org.sat4j.reader.LecteurDimacs;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;

/**
 * The PseudoBooleanFormulaReader is a utility class designed to make easier the
 * reading of pseudo-Boolean formulae.
 * Both the CNF and OPB formats may be read.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class PseudoBooleanFormulaReader {

    /**
     * Disables instantiation.
     */
    private PseudoBooleanFormulaReader() {
        throw new AssertionError("No PseudoBooleanFormulaReader instances for you!");
    }

    /**
     * Reads a pseudo-Boolean formula from a given file.
     * The file may be in CNF or OPB format, and may have been compressed with
     * {@code gzip} or {@code bzip2}.
     *
     * @param path The path of the input file to read.
     *
     * @return The read formula.
     *
     * @throws IOException If an I/O error occurs while reading.
     */
    public static PseudoBooleanFormula read(String path) throws IOException {
        try {
            var solver = new PBSelectorSolver(SolverFactory.newDefault());
            var reader = new PBInstanceReader(solver);
            reader.parseInstance(path);
            return new OriginalPseudoBooleanFormula(solver);

        } catch (ContradictionException e) {
            // The read formula is unsatisfiable.
            return Contradiction.getInstance();

        } catch (ParseFormatException e) {
            // The input format is incorrect.
            throw new IOException(e);
        }
    }

    /**
     * Reads a pseudo-Boolean formula from a CNF stream.
     *
     * @param inputStream The stream to read.
     *
     * @return The read formula.
     *
     * @throws IOException If an I/O error occurs while reading.
     */
    public static PseudoBooleanFormula readCnf(InputStream inputStream) throws IOException {
        try {
            var solver = new PBSelectorSolver(SolverFactory.newDefault());
            var reader = new LecteurDimacs(solver);
            reader.parseInstance(inputStream);
            return new OriginalPseudoBooleanFormula(solver);

        } catch (ContradictionException e) {
            // The read formula is unsatisfiable.
            return Contradiction.getInstance();

        } catch (ParseFormatException e) {
            // The input format is incorrect.
            throw new IOException(e);
        }
    }

    /**
     * Reads a pseudo-Boolean formula from an OPB stream.
     *
     * @param inputStream The stream to read.
     *
     * @return The read formula.
     *
     * @throws IOException If an I/O error occurs while reading.
     */
    public static PseudoBooleanFormula readOpb(InputStream inputStream) throws IOException {
        try {
            var solver = new PBSelectorSolver(SolverFactory.newDefault());
            var reader = new PBInstanceReader(solver);
            reader.parseInstance(inputStream);
            return new OriginalPseudoBooleanFormula(solver);

        } catch (ContradictionException e) {
            // The read formula is unsatisfiable.
            return Contradiction.getInstance();

        } catch (ParseFormatException e) {
            // The input format is incorrect.
            throw new IOException(e);
        }
    }

}
