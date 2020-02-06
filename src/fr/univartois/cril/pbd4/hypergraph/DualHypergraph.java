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

package fr.univartois.cril.pbd4.hypergraph;

import org.sat4j.specs.IConstr;
import org.sat4j.specs.IVec;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.hypergraph.Hypergraph;

/**
 * The DualHypergraph represents the dual hypergraph of a pseudo-Boolean formula, i.e. the
 * hypergraph in which the vertices are the constraints of the formula and a hyperedge
 * represents a variable shared between the constraints joined by this hyperedge.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class DualHypergraph implements PseudoBooleanFormulaHypergraph {

    /**
     * The constraints of the formula.
     */
    private final IConstr[] constraints;

    /**
     * The JKaHyPar representation of the hypergraph.
     */
    private final Hypergraph hypergraph;

    /**
     * The array storing, for each constraint, the identifiers of the hyperedges in which
     * it appears.
     */
    private final IVecInt[] hyperedgesContainingConstraint;

    /**
     * Creates a new DualHypergraph.
     * 
     * @param constraints The constraints of the formula.
     * @param hypergraph The JKaHyPar representation of the hypergraph.
     * @param hyperedgesContainingConstraint The array storing, for each constraint, the
     *        identifiers of the hyperedges in which it appears.
     */
    public DualHypergraph(IConstr[] constraints, Hypergraph hypergraph,
            IVecInt[] hyperedgesContainingConstraint) {
        this.constraints = constraints;
        this.hypergraph = hypergraph;
        this.hyperedgesContainingConstraint = hyperedgesContainingConstraint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.input.graph.PseudoBooleanFormulaGraph#connectedComponents()
     */
    @Override
    public IVec<IVec<IConstr>> connectedComponents() {
        var finder = new DualHypergraphConnectedComponentFinder(
                constraints, hyperedgesContainingConstraint);
        return finder.connectedComponents();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.input.hypergraph.PseudoBooleanFormulaHypergraph#cutset()
     */
    @Override
    public IVecInt cutset() {
        return DualHypergraphPartitionFinder.instance().cutsetOf(hypergraph);
    }

}
