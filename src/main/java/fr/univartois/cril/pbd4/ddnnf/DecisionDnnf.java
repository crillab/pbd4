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

package fr.univartois.cril.pbd4.ddnnf;

import java.io.OutputStream;

/**
 * The DecisionDnnf defines the interface for manipulating d-DNNFs.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public interface DecisionDnnf {

    /**
     * Accepts a {@link DecisionDnnfVisitor}.
     * Visiting a d-DNNF is performed in a depth-first manner.
     *
     * @param visitor The visitor to accept.
     */
    void accept(DecisionDnnfVisitor visitor);

    /**
     * Writes this d-DNNF to the given output stream, using the NNF format.
     *
     * @param outputStream The output stream in which to write this d-DNNF.
     */
    default void writeTo(OutputStream outputStream) {
        try (var visitor = new DecisionDnnfWriter(outputStream)) {
            visitor.enter(this);
            accept(visitor);
            visitor.exit(this);
        }
    }

    /**
     * Computes a String representation of this d-DNNF, using the NNF format.
     *
     * @return A String representation of this d-DNNF.
     */
    String toString();

}
