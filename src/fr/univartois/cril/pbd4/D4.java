package fr.univartois.cril.pbd4;

import org.sat4j.core.Vec;
import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.ddnnf.ConjunctionNode;
import fr.univartois.cril.pbd4.ddnnf.DecisionDnnf;
import fr.univartois.cril.pbd4.ddnnf.LeafNode;
import fr.univartois.cril.pbd4.input.hypergraph.PseudoBooleanFormulaHypergraph;
import fr.univartois.cril.pbd4.solver.PseudoBooleanSolver;
import fr.univartois.cril.pbd4.solver.SolverStatus;

public class D4 {
	
    private final PseudoBooleanSolver solver;
    
    private PseudoBooleanFormulaHypergraph graph;

    public D4(PseudoBooleanSolver solver) {
        super();
        this.solver = solver;
    }
	
	public DecisionDnnf compile() {
	    return compile(new VecInt());
	}

    private DecisionDnnf compile(IVecInt vecInt) {
        // Applying BCP to the formula.
        var status = solver.propagate();
        
        if (status == SolverStatus.UNSATISFIABLE) {
            // A conflict has been encountered while propagating.
            return LeafNode.FALSE;
        }
        
        var s = solver.getPropagatedLiterals();
        if (status == SolverStatus.SATISFIABLE) {
            // A solution has been found while propagating.
            return new ConjunctionNode(s);
        }
        
        // TODO: Manage the cache.
        
        var components = graph.connectedComponents();
        var lnd = new Vec<DecisionDnnf>();
        for (var it = components.iterator(); it.hasNext();) {
            var component = it.next();
            // TODO Compute HGP and recursively compile.
        }
        
        var node = new ConjunctionNode(s, lnd);
        // TODO Add to cache.
        return node;
    }

    private IVecInt restrict(IVecInt vecInt, IVecInt component) {
        var restricted = new VecInt();
        for (var it = vecInt.iterator(); it.hasNext();) {
            var v = it.next();
            if (component.contains(v)) {
                restricted.push(v);
            }
        }
        return restricted;
    }
	
}
