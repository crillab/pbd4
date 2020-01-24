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

package fr.univartois.cril.pbd4.input.hypergraph;

import static fr.univartois.cril.jkahypar.hypergraph.UnweightedHyperedge.joining;

import org.sat4j.core.LiteralsUtils;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.hypergraph.HypergraphBuilder;

public final class DualHypergraphBuilder {

    private final int nbVariables;

    private final int nbConstraints;

    private final IVecInt[] constraintsContainingVariable;

    private int constraintIdentifier = 0;

    private DualHypergraphBuilder(int nbVariables, int nbConstraints) {
        this.nbVariables = nbVariables;
        this.nbConstraints = nbConstraints;
        this.constraintsContainingVariable = new IVecInt[nbVariables + 1];
    }

    public static DualHypergraphBuilder newInstance(int nbVariables, int nbConstraints) {
        return new DualHypergraphBuilder(nbVariables, nbConstraints);
    }

    public DualHypergraphBuilder addConstraint(IVecInt literals) {
        constraintIdentifier++;

        for (var it = literals.iterator(); it.hasNext();) {
            int variable = LiteralsUtils.var(it.next());

            if (constraintsContainingVariable[variable] == null) {
                constraintsContainingVariable[variable] = new VecInt();
            }

            constraintsContainingVariable[variable].push(constraintIdentifier);
        }
        return this;
    }

    public DualHypergraph build() {
        var builder = HypergraphBuilder.createHypergraph(nbConstraints, nbVariables);

        for (int i = 1; i < constraintsContainingVariable.length; i++) {
            var constraints = constraintsContainingVariable[i];
            if (constraints == null) {
                builder.withHyperedge(joining());

            } else {
                int[] vertices = new int[constraints.size()];
                constraints.sort();
                constraints.copyTo(vertices);
                builder.withHyperedge(joining(vertices));
            }
        }

        return new DualHypergraph(builder.build());
    }
}
