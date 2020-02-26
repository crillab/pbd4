/**
 * 
 */
package fr.univartois.cril.pbd4.pbc;

import java.util.Collection;

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

/**
 * @author romainwallon
 *
 */
public class Contradiction implements PseudoBooleanFormula {
	
	private static final PseudoBooleanFormula INSTANCE = new Contradiction();
	
	public static PseudoBooleanFormula getInstance() {
		return INSTANCE;
	}

	@Override
	public int numberOfVariables() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int numberOfConstraints() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IVecInt variables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PseudoBooleanFormula satisfy(int literal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IVecInt cutset() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PseudoBooleanFormula> connectedComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropagationOutput propagate() {
		return new PropagationOutput(SolverStatus.UNSATISFIABLE, VecInt.EMPTY, null);
	}

}
