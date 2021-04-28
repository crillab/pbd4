/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package fr.univartois.cril.pbd4.pbc.solver;

import java.io.PrintWriter;

import org.sat4j.minisat.core.ILits;
import org.sat4j.minisat.core.IOrder;
import org.sat4j.minisat.core.IPhaseSelectionStrategy;
import org.sat4j.minisat.orders.SubsetVarOrder;
import org.sat4j.minisat.orders.VarOrderHeap;

/**
 * The SwitchableOrder is a branching heuristic aggregating two orders: a
 * {@link SubsetVarOrder} (allowing to restrict the solver to BCP), and a
 * {@link VarOrderHeap} (allowing to completely solve a problem).
 * 
 * Only one of these orders is used at a given moment, except for initialization
 * operations.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public final class SwitchableOrder implements IOrder {

    /**
     * The heuristic to use to restrict the solver to BCP.
     */
    private final IOrder bcp = new SubsetVarOrder(new int[0]);

    /**
     * The heuristic to use to completely solve a problem.
     */
    private final IOrder complete = new VarOrderHeap();

    /**
     * The heuristic that is currently used by the solver.
     */
    private IOrder current;

    /**
     * Selects the BCP heuristic as the current one.
     */
    public void switchToBCP() {
        this.current = bcp;
    }

    /**
     * Selects the complete heuristic as the current one.
     */
    public void switchToComplete() {
        this.current = complete;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#setLits(org.sat4j.minisat.core.ILits)
     */
    @Override
    public void setLits(ILits lits) {
        bcp.setLits(lits);
        complete.setLits(lits);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#select()
     */
    @Override
    public int select() {
        return current.select();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#undo(int)
     */
    @Override
    public void undo(int x) {
        current.undo(x);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#updateVar(int)
     */
    @Override
    public void updateVar(int p) {
        current.updateVar(p);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#updateVar(int, double)
     */
    @Override
    public void updateVar(int p, double value) {
        current.updateVar(p, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#init()
     */
    @Override
    public void init() {
        bcp.init();
        complete.init();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#printStat(java.io.PrintWriter,
     * java.lang.String)
     */
    @Override
    public void printStat(PrintWriter out, String prefix) {
        bcp.printStat(out, prefix);
        complete.printStat(out, prefix);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#setVarDecay(double)
     */
    @Override
    public void setVarDecay(double d) {
        bcp.setVarDecay(d);
        complete.setVarDecay(d);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#varDecayActivity()
     */
    @Override
    public void varDecayActivity() {
        current.varDecayActivity();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#varActivity(int)
     */
    @Override
    public double varActivity(int p) {
        return current.varActivity(p);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#assignLiteral(int)
     */
    @Override
    public void assignLiteral(int p) {
        current.assignLiteral(p);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.sat4j.minisat.core.IOrder#setPhaseSelectionStrategy(org.sat4j.minisat
     * .core.IPhaseSelectionStrategy)
     */
    @Override
    public void setPhaseSelectionStrategy(IPhaseSelectionStrategy strategy) {
        bcp.setPhaseSelectionStrategy(strategy);
        complete.setPhaseSelectionStrategy(strategy);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#getPhaseSelectionStrategy()
     */
    @Override
    public IPhaseSelectionStrategy getPhaseSelectionStrategy() {
        return current.getPhaseSelectionStrategy();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#updateVarAtDecisionLevel(int)
     */
    @Override
    public void updateVarAtDecisionLevel(int q) {
        current.updateVarAtDecisionLevel(q);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.sat4j.minisat.core.IOrder#getVariableHeuristics()
     */
    @Override
    public double[] getVariableHeuristics() {
        // This method only makes sense on the complete heuristic.
        return complete.getVariableHeuristics();
    }

}
