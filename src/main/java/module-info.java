/**
 * The {@code fr.univartois.cril.pbd4} module provides a pseudo-Boolean-based
 * implementation of the D4 algorithm, to compile pseudo-Boolean formulae into
 * decision-DNNFs, or to count their models.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */

module fr.univartois.cril.pbd4 {

    // Exported packages.

    exports fr.univartois.cril.pbd4;

    exports fr.univartois.cril.pbd4.caching;

    exports fr.univartois.cril.pbd4.ddnnf;

    exports fr.univartois.cril.pbd4.listener;

    exports fr.univartois.cril.pbd4.partitioning;

    exports fr.univartois.cril.pbd4.pbc;

    exports fr.univartois.cril.pbd4.pbc.hypergraph;

    exports fr.univartois.cril.pbd4.pbc.solver;

    // Required Java modules.

    requires java.logging;

    // Module required for parsing the command line.

    requires fr.cril.cli;

    opens fr.univartois.cril.pbd4 to fr.cril.cli;

    // Modules required for dealing with pseudo-Boolean formulae.

    requires transitive org.ow2.sat4j.core;

    requires transitive org.ow2.sat4j.pb;

    // Module required for hypergraph partitioning.

    requires transitive fr.univartois.cril.jkahypar;

}
