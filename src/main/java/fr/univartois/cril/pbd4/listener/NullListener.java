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

/**
 * The NullListener is a {@link D4Listener} that does nothing when notified
 * about any event occurring during the execution of the D4 algorithm.
 * It is a Singleton for a Null Object.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
public final class NullListener implements D4Listener {

    /**
     * The single instance of this class.
     */
    private static final D4Listener INSTANCE = new NullListener();

    /**
     * Disables external instantiation.
     */
    private NullListener() {
        // Nothing to do: Singleton Design Pattern.
    }

    /**
     * Gives the single instance of this class.
     *
     * @return The instance of NullListener.
     */
    public static D4Listener instance() {
        return INSTANCE;
    }

}
