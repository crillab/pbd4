package fr.univartois.cril.pbd4.oracle;

import java.util.Optional;

import org.sat4j.minisat.core.ICDCL;
import org.sat4j.minisat.orders.SubsetVarOrder;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

public class BooleanConstraintPropagator {

	private ICDCL<?> solver;

	private IVecInt propagated;
	
	private boolean isSatisfiable;

	public Optional<IVecInt> propagate() {
		try {
            solver.setOrder(new SubsetVarOrder(new int[0]));

            var listener = new UnitPropagationListener();
            solver.setSearchListener(listener);
            
            if (solver.isSatisfiable()) {
            	return Optional.of(listener.getPropagatedLiterals());
            }
            
            return Optional.empty();
            
        } catch (TimeoutException e) {
            throw new AssertionError("Timeout should not have been set!", e);
        }
	}
}
