package fr.univartois.cril.pbd4;

import org.sat4j.specs.ISolver;
import org.sat4j.tools.SolverDecorator;

public class D4SolverAdapter<T extends ISolver> extends SolverDecorator<T> {

	public D4SolverAdapter(T solver) {
		super(solver);
	}
	
	

}
