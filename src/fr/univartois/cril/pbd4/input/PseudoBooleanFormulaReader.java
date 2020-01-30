/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Romain WALLON.
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

package fr.univartois.cril.pbd4.input;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.sat4j.pb.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;

import fr.univartois.cril.pbd4.solver.PseudoBooleanSolver;
import fr.univartois.cril.pbd4.solver.sat4j.Sat4jAdapter;

/**
 * The PseudoBooleanFormulaReader allows to read a pseudo-Boolean formula from an input file and to 
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class PseudoBooleanFormulaReader implements Closeable {
    
    private final InputStream input;
    
    public PseudoBooleanFormulaReader() {
        this(System.in);
    }
    
    public PseudoBooleanFormulaReader(String path) throws IOException {
        this(Files.newInputStream(Path.of(path)));
    }

    public PseudoBooleanFormulaReader(InputStream inputStream) {
        this.input = inputStream;
    }

    public PseudoBooleanSolver read() throws ParseFormatException, ContradictionException, IOException {
        var solver = SolverFactory.newCuttingPlanes();
        var reader = new DimacsReader(solver);
        reader.parseInstance(input);
        return new Sat4jAdapter(solver, null);
    }

    @Override
    public void close() throws IOException {
        input.close();
    }
    
}

