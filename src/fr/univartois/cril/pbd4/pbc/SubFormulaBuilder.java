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

import static fr.univartois.cril.jkahypar.hypergraph.HypergraphBuilder.createHypergraph;
import static fr.univartois.cril.jkahypar.hypergraph.UnweightedHyperedge.joining;

import java.math.BigInteger;
import java.util.BitSet;

import org.sat4j.core.LiteralsUtils;
import org.sat4j.core.VecInt;
import org.sat4j.pb.constraints.pb.PBConstr;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.jkahypar.hypergraph.Hypergraph;
import fr.univartois.cril.jkahypar.hypergraph.HypergraphBuilder;
import fr.univartois.cril.pbd4.hypergraph.DualHypergraph;
import fr.univartois.cril.pbd4.hypergraph.PseudoBooleanFormulaHypergraph;

/**
 * The SubFormulaBuilder
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public class SubFormulaBuilder {

    private final OriginalPseudoBooleanFormula formula;
    
    private final IVecInt assumptions;
    
    private final IVecInt satisfiedLiterals;
    
    private final BitSet inactiveConstraints;
    
    private final int[] assignments;
    
    private final int[] constraintsId;
    
    private int nbConstr;

    private IVecInt allAssumptions;
    /**
     * The array storing, for each variable, the identifiers of the constraints in which
     * it appears.
     */
    private final IVecInt[] constraintsContainingVariable;

    /**
     * The array storing, for each constraint, the identifiers of the hyperedges in which
     * it appears.
     */
    private final IVecInt[] hyperedgesContainingConstraint;

    public SubFormulaBuilder(OriginalPseudoBooleanFormula formula, IVecInt assumptions,
            IVecInt satisfiedLiterals) {
        this.formula = formula;
        this.assumptions = assumptions;
        this.satisfiedLiterals = satisfiedLiterals;
        this.inactiveConstraints = new BitSet(formula.numberOfConstraints());
        this.assignments = new int[formula.numberOfVariables() + 1];
        this.constraintsContainingVariable = new IVecInt[assignments.length];
        this.hyperedgesContainingConstraint = new IVecInt[formula.numberOfConstraints()];
        this.constraintsId = new int[formula.numberOfConstraints()];
    }
    
    boolean isFalsified(int lit) {
        int as = assignments[lit >> 1];
        return as < 0 && lit > 0 || as > 0 && lit < 0;
    }
    
    boolean isSatisfied(int lit) {
        int as = assignments[lit >> 1];
        return as < 0 && lit < 0 || as > 0 && lit > 0;
    }
    
    
    PseudoBooleanFormula build() {
        init();
        
        for (int i = 0, nConstr = formula.numberOfConstraints(); i < nConstr; i++) {
            if (inactiveConstraints.get(i)) {
                // This constraint is not a part of the formula anymore.
                continue;
            }
            
            var constr = formula.getConstraint(i);
            var current = BigInteger.ZERO;
            BigInteger degree = constr.getDegree();
            for (int l = 0; l < constr.size() && current.compareTo(degree) < 0; l++) {
                int lit = constr.get(l);
                if (isSatisfied(lit)) {
                    current = current.add(constr.getCoef(l));
                    
                } else if (!isFalsified(lit)) {
                    // The literal is not assigned, so it still appears in the constraint.
                    addConstraintContainingVariable(LiteralsUtils.var(lit), i);
                }
            }
            
            if (current.compareTo(degree) < 0) {
                constraintsId[i] = nbConstr++;
            } else {
                inactiveConstraints.set(i);
            }
        }
        return null;
    }
    
    private void init() {
        allAssumptions = new VecInt(assumptions.size() + satisfiedLiterals.size());
        for (var it = assumptions.iterator(); it.hasNext();) {
            int lit = it.next();
            setAssignment(lit);
        }
        for (var it = satisfiedLiterals.iterator(); it.hasNext();) {
            int lit = it.next();
            setAssignment(lit);
        }
    }

    private void setAssignment(int lit) {
        int var = LiteralsUtils.toDimacs(lit) >> 1;
        
        if (var < assignments.length && assignments[var] != 0) {
            assignments[var] = lit > 0 ? 1 : -1 ;
            allAssumptions.push(lit);
        } else if (lit > 0){
            inactiveConstraints.set(var - assignments.length);
        }
    }
    
    /**
     * Associates the given constraints to a hyperedge joining them in
     * {@link #hyperedgesContainingConstraint}.
     *
     * @param hyperedgeId The identifier of the hyperedge joining the constraints.
     * @param joinedConstraints The constraints joined by the hyperedge.
     */
    private void associate(int hyperedgeId, IVecInt joinedConstraints) {
        for (var it = joinedConstraints.iterator(); it.hasNext();) {
            int constraintId = it.next();
            var vec = hyperedgesContainingConstraint[constraintId];

            if (vec == null) {
                vec = new VecInt();
                hyperedgesContainingConstraint[constraintId] = vec;
            }

            vec.push(hyperedgeId);
        }
    }


    /**
     * Adds the current constraint as containing the given variable.
     *
     * @param variable The variable to add a constraint to.
     */
    private void addConstraintContainingVariable(int variable, int constr) {
        var vec = constraintsContainingVariable[variable];

        if (vec == null) {
            vec = new VecInt();
            constraintsContainingVariable[variable] = vec;
        }

        vec.push(constr);
    }
    
    public Hypergraph buildHyper() {
        var builder = createHypergraph(nbConstr, constraintsContainingVariable.length - 1);

        // Adding the hyperedges.
        for (int v = 1; v < constraintsContainingVariable.length; v++) {
            var constraintsWithV = constraintsContainingVariable[v];
            addHyperedge(builder, constraintsWithV);
            associate(v - 1, constraintsWithV);
        }
        
        return builder.build();
    }
    


    /**
     * Adds a hyperedge joining the given vertices to the hypergraph built by the given
     * builder.
     * 
     * @param builder The builder used to build the hypergraph.
     * @param vertices The vertices joined by the hyperedge to add.
     */
    private void addHyperedge(HypergraphBuilder builder, IVecInt vertices) {
        if (vertices == null) {
            builder.withHyperedge(joining());
            return;
        }
        
        // TODO : copy only active constraints
        int[] asArray = new int[vertices.size()];
        vertices.copyTo(asArray);
        builder.withHyperedge(joining(asArray));
    }
}

