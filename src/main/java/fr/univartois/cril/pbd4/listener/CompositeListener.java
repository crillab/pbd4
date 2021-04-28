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

package fr.univartois.cril.pbd4.listener;

import java.util.Collection;
import java.util.LinkedList;

import org.sat4j.specs.IVecInt;

import fr.univartois.cril.pbd4.pbc.PseudoBooleanFormula;

/**
 * The CompositeListener allows to notify multiple instances of {@link D4Listener}
 * as if there were only one instance.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public final class CompositeListener implements D4Listener {

    /**
     * The listeners to notify when this listener is notified.
     */
    private final Collection<D4Listener> listeners = new LinkedList<>();

    /**
     * Adds a listener that will be notified when this listener is notified.
     *
     * @param listener The listener to add.
     */
    public void addListener(D4Listener listener) {
        listeners.add(listener);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.listener.D4Listener#init(fr.univartois.cril.pbd4.pbc.
     * PseudoBooleanFormula)
     */
    @Override
    public void init(PseudoBooleanFormula formula) {
        listeners.forEach(l -> l.init(formula));
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.listener.D4Listener#start()
     */
    @Override
    public void start() {
        listeners.forEach(D4Listener::start);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.listener.D4Listener#propagate()
     */
    @Override
    public void propagate() {
        listeners.forEach(D4Listener::propagate);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.listener.D4Listener#unsatisfiable(fr.univartois.cril.
     * pbd4.pbc.PseudoBooleanFormula)
     */
    @Override
    public void unsatisfiable(PseudoBooleanFormula subFormula) {
        listeners.forEach(l -> l.unsatisfiable(subFormula));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.listener.D4Listener#propagated(org.sat4j.specs.
     * IVecInt, int)
     */
    @Override
    public void propagated(IVecInt propagatedLiterals, int nbFreeVariables) {
        listeners.forEach(l -> l.propagated(propagatedLiterals, nbFreeVariables));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.listener.D4Listener#implicant(fr.univartois.cril.pbd4
     * .pbc.PseudoBooleanFormula)
     */
    @Override
    public void implicant(PseudoBooleanFormula subFormula) {
        listeners.forEach(l -> l.implicant(subFormula));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.listener.D4Listener#cached(fr.univartois.cril.pbd4.
     * pbc.PseudoBooleanFormula)
     */
    @Override
    public void cached(PseudoBooleanFormula cachedFormula) {
        listeners.forEach(l -> l.cached(cachedFormula));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.listener.D4Listener#computeConnectedComponents()
     */
    @Override
    public void computeConnectedComponents() {
        listeners.forEach(D4Listener::computeConnectedComponents);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.listener.D4Listener#connectedComponentsFound(java.
     * util.Collection)
     */
    @Override
    public void connectedComponentsFound(Collection<PseudoBooleanFormula> connectedComponents) {
        listeners.forEach(l -> l.connectedComponentsFound(connectedComponents));
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.listener.D4Listener#computeCutset()
     */
    @Override
    public void computeCutset() {
        listeners.forEach(D4Listener::computeCutset);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.listener.D4Listener#cutsetFound(org.sat4j.specs.
     * IVecInt)
     */
    @Override
    public void cutsetFound(IVecInt cutset) {
        listeners.forEach(l -> l.cutsetFound(cutset));
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.listener.D4Listener#branchOn(int)
     */
    @Override
    public void branchOn(int v) {
        listeners.forEach(l -> l.branchOn(v));
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.listener.D4Listener#cachingConjunction()
     */
    @Override
    public void cachingConjunction() {
        listeners.forEach(D4Listener::cachingConjunction);
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.cril.pbd4.listener.D4Listener#end()
     */
    @Override
    public void end() {
        listeners.forEach(D4Listener::end);
    }

}
