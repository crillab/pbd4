/**
 * The {@code fr.univartois.cril.pbd4} module provides a pseudo-Boolean based
 * implementation of the D4 compiler.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */

module fr.univartois.cril.pbd4 {

    // Exported packages.

    exports fr.univartois.cril.pbd4;

    exports fr.univartois.cril.pbd4.input;

    exports fr.univartois.cril.pbd4.ddnnf;

    exports fr.univartois.cril.pbd4.solver;

    // Modules required for solving PB formulae.

    requires transitive org.ow2.sat4j.core;

    requires transitive org.ow2.sat4j.pb;

    // Module required for hypergraph partitioning.

    requires fr.univartois.cril.jkahypar;

}
