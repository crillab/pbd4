package fr.univartois.cril.pbd4.solver.sat4j;

import org.sat4j.core.VecInt;
import org.sat4j.specs.ISolverService;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.SearchListenerAdapter;

class UnitPropagationListener extends SearchListenerAdapter<ISolverService> {

	/**
     * The serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private IVecInt propagated;
	
	@Override
	public void init(ISolverService solverService) {
		propagated = new VecInt(solverService.nVars());
	}

	@Override
	public void propagating(int p) {
		propagated.push(p);
	}
	
	public IVecInt getPropagatedLiterals() {
		return propagated;
	}
	
	public void reset() {
	    propagated.clear();
	}
	
	
}
