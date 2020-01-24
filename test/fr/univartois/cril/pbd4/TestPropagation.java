package fr.univartois.cril.pbd4;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.orders.SubsetVarOrder;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;


class TestPropagation {

    @Test
    void test() throws ContradictionException, TimeoutException {
        var solver = SolverFactory.newCuttingPlanes();
        solver.newVar(3);
        solver.addClause(VecInt.of(1, 2, 3));
        solver.addClause(VecInt.of(1));
        solver.addClause(VecInt.of(2, 3));
        solver.setOrder(new SubsetVarOrder(new int[0]));
        assertTrue(solver.isSatisfiable());
    }

}

