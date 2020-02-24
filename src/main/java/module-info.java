/**
 * The {@code fr.univartois.cril.pbd4} module provides a pseudo-Boolean-based
 * implementation of the D4 algorithm, to compile pseudo-Boolean formulae into
 * decision-DNNF, or to count their models.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */

module fr.univartois.cril.pbd4 {

    // Exported packages.

    exports fr.univartois.cril.pbd4;

    exports fr.univartois.cril.pbd4.ddnnf;

    // Modules required for dealing with pseudo-Boolean formulae.

    requires org.ow2.sat4j.core;

    requires org.ow2.sat4j.pb;

    // Module required for hypergraph partitioning.

    requires fr.univartois.cril.jkahypar;

}