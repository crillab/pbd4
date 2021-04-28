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

package fr.univartois.cril.pbd4.ddnnf;

import java.util.NoSuchElementException;

/**
 * The NonConstantDecisionDnnfNode is the parent class of the decision-DNNF nodes
 * that do not represent constant Boolean values.
 * Its main purpose is to provide the ability to cache values that have been
 * computed by a {@link DecisionDnnfVisitor}.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
abstract class NonConstantDecisionDnnfNode implements DecisionDnnfNode {

    /**
     * The time stamp at which this node has been visited.
     */
    private long timestamp;

    /**
     * The value that has been cached by the visitor (if any).
     */
    private Object cachedValue;

    /**
     * Sets the time stamp at which this node is visited.
     *
     * @param timestamp The time stamp at which this node is visited.
     */
    void setVisitStamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gives the time stamp at which this node has been visited.
     *
     * @return The time stamp at which this node has been visited.
     */
    long getVisitStamp() {
        return timestamp;
    }

    /**
     * Caches a value computed for this node.
     *
     * @param value The value to cache.
     */
    void cacheValue(Object value) {
        this.cachedValue = value;
    }

    /**
     * Gives the value that has been cached for this node.
     *
     * @param <T> The type of the cached value.
     *
     * @return The cached value.
     *
     * @throws NoSuchElementException If there is no value cached for this node.
     */
    <T> T getCached() {
        if (cachedValue == null) {
            throw new NoSuchElementException("Nothing has been cached for this node!");
        }

        @SuppressWarnings("unchecked")
        T castCache = (T) cachedValue;
        return castCache;
    }

}
