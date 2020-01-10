package fr.univartois.cril.pbd4;

import org.sat4j.pb.IPBSolver;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import fr.univartois.cril.pbd4.ddnnf.DecisionDnnf;
import fr.univartois.cril.pbd4.ddnnf.Leaf;

public class D4 {
	
	public DecisionDnnf compile(IPBSolver solver) throws TimeoutException {
		if (!solver.isSatisfiable()) {
			return Leaf.FALSE;
		}
		return null;
	}
	
	
	
}
