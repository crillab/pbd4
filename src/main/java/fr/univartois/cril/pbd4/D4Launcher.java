/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Univ Artois & CNRS.
 * All rights reserved.
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

package fr.univartois.cril.pbd4;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.cril.cli.ClassParser;
import fr.cril.cli.CliArgsParser;
import fr.cril.cli.CliOptionDefinitionException;
import fr.cril.cli.CliUsageException;
import fr.cril.cli.annotations.Args;
import fr.cril.cli.annotations.Description;
import fr.cril.cli.annotations.LongName;
import fr.cril.cli.annotations.Params;
import fr.cril.cli.annotations.ShortName;
import fr.univartois.cril.pbd4.caching.CachingStrategy;
import fr.univartois.cril.pbd4.caching.NoCache;
import fr.univartois.cril.pbd4.partitioning.KahyparCutsetComputationStrategy;

/**
 * The D4Launcher allows to execute PBD4 from the command line.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
@Params("0..1")
public final class D4Launcher {

    /**
     * The logger used to log the events of PBD4.
     */
    private static final Logger LOGGER = Logger.getLogger("fr.univartois.cril.pbd4");

    @ShortName("h")
    @LongName("help")
    @Description("Displays the help of PBD4.")
    @Args(0)
    private boolean help;

    @ShortName("s")
    @LongName("solver")
    @Description("The name of the solver to use as a SAT oracle.")
    @Args(value = 1, names = "solver-name")
    private String solverName = null;

    @ShortName("k")
    @LongName("kahypar-configuration")
    @Description("The path of the INI file specifying the configuration of KaHyPar.")
    @Args(value = 1, names = "path-to-ini")
    private String kahyparConfig = defaultKahyparConfig();

    @ShortName("b")
    @LongName("imbalance")
    @Description("The imbalance setting for KaHyPar.")
    @Args(value = 1, names = "epsilon")
    private String imbalance = Double.toString(KahyparCutsetComputationStrategy.DEFAULT_IMBALANCE);

    @ShortName("p")
    @LongName("partition-size")
    @Description("The size of the partitions of the formula to compute.")
    @Args(value = 1, names = "nb")
    private int partitionSize = KahyparCutsetComputationStrategy.DEFAULT_NUMBER_OF_BLOCKS;

    @ShortName("f")
    @LongName("input-format")
    @Description("The format of the input, when read from the standard input.")
    @Args(value = 1, names = "opb,cnf")
    private String inputFormat = "opb";

    @ShortName("c")
    @LongName("caching-strategy")
    @Description("The strategy for caching the results computed on sub-formulae.")
    @Args(value = 1, names = "none")
    private String cachingStrategy = "none";

    @ShortName("o")
    @LongName("output")
    @Description("The type of the output.")
    @Args(value = 1, names = "count,ddnnf")
    private String output = "count";

    /**
     * The other parameters of the command line.
     * It may contain at most one element: the path of the input file.
     */
    private List<String> parameters;

    /**
     * Disables external instantiation.
     */
    private D4Launcher() {
        // Nothing to do: fields will be injected later on.
    }

    /**
     * Gives the path of the default configuration file for KaHyPar, located
     * in PBD4's home directory.
     * This directory is supposed to be specified in the {@code PBD4_HOME}
     * environment variable.
     *
     * @return The path of the default configuration file for KaHyPar.
     */
    private static String defaultKahyparConfig() {
        var home = System.getenv("PBD4_HOME");
        if ((home == null) || home.isBlank()) {
            home = "dist/home";
        }
        return Paths.get(home, "kahypar.ini").toString();
    }

    /**
     * Parses the command line arguments, and initializes the fields
     * accordingly.
     *
     * @param args The command line arguments.
     *
     * @throws Exception If an error occurs while parsing the command line.
     */
    private void parse(String[] args) throws Exception {
        var classParser = new ClassParser<>(D4Launcher.class);
        var argsParser = new CliArgsParser<>(classParser);

        try {
            argsParser.parse(this, args);
            this.parameters = argsParser.getParameters();

            if (help) {
                // If the help is asked, PBD4 must NOT be launched.
                // So, the usage is displayed and the program exits.
                classParser.printOptionUsage(new PrintWriter(System.err));
                System.exit(0);
            }

        } catch (CliUsageException | CliOptionDefinitionException e) {
            classParser.printOptionUsage(new PrintWriter(System.err));
            throw e;
        }
    }

    /**
     * Sets up PBD4 as specified in the command line, and starts it.
     *
     * @throws Exception If an error occurs during the execution.
     */
    private void launch() throws Exception {
        var d4 = D4.newInstance();

        // Configuring the SAT oracle used by PBD4.
        if (solverName != null) {
            d4.useSolver(solverName);
        }

        // Configuring the instance of KaHyPar used by PBD4.
        d4.ofSize(partitionSize);
        d4.withConfiguration(kahyparConfig);
        d4.withImbalance(Double.parseDouble(imbalance));

        // Configuring the other strategies.
        d4.useCachingStrategy(getCachingStrategy());

        // Reading the input formula.
        readInput(d4);

        // Actually launching PBD4.
        printHeader(d4);
        launch(d4);
    }

    /**
     * Gives the caching strategy specified in the command line.
     *
     * @return The specified caching strategy.
     */
    private CachingStrategy<?> getCachingStrategy() {
        switch (cachingStrategy) {
            case "none":
                return NoCache.instance();

            default:
                throw new IllegalArgumentException(
                        "Unrecognized caching strategy: " + cachingStrategy);
        }
    }

    /**
     * Reads the input for PBD4.
     *
     * @param d4 The configuration to set up.
     *
     * @throws IOException If an I/O error occurs while reading.
     */
    private void readInput(D4 d4) throws IOException {
        if (parameters.isEmpty()) {
            // Reading the formula from the standard input.
            switch (inputFormat) {
                case "cnf":
                    d4.onCnfInput(System.in);
                    break;

                case "opb":
                    d4.onOpbInput(System.in);
                    break;

                default:
                    throw new IllegalArgumentException("Unrecognized input format: " + inputFormat);
            }

        } else {
            // Reading the formula from the file specified as parameter.
            d4.onInput(parameters.get(0));
        }
    }

    /**
     * Launches PBD4 in the specified mode.
     *
     * @param d4 The configuration of PBD4 to launch.
     */
    private void launch(D4 d4) {
        switch (output) {
            case "count":
                launchModelCounter(d4);
                break;

            case "ddnnf":
                launchDecisionDnnfCompiler(d4);
                break;

            default:
                throw new IllegalArgumentException("Unrecognized output: " + output);
        }
    }

    /**
     * Launches PBD4 as a model counter.
     *
     * @param d4 The configuration of PBD4 to launch.
     */
    private void launchModelCounter(D4 d4) {
        System.out.println("c Running PBD4 as a model counter.");
        System.out.println("c");

        var count = d4.countModels();
        System.out.println("s " + count);
    }

    /**
     * Launches PBD4 as a decision-DNNF compiler.
     *
     * @param d4 The configuration of PBD4 to launch.
     */
    private void launchDecisionDnnfCompiler(D4 d4) {
        System.out.println("c Running PBD4 as a decision-DNNF compiler.");
        System.out.println("c");

        var ddnnf = d4.compileToDecisionDnnf();
        ddnnf.writeTo(System.out);
    }

    /**
     * Displays on the console some pieces of information about PBD4.
     */
    private void printHeader(D4 d4) {
        System.out.println(
                "c This is PBD4, a pseudo-Boolean based implementation of the D4 compiler.");
        System.out.println("c Copyright (c) 2020 - Univ Artois & CNRS.");
        System.out.println("c This software is distributed under GNU GPL.");
        System.out.println("c");
        System.out.println(d4);
        System.out.println("c");
    }

    /**
     * Executes the program.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        var timeBefore = System.nanoTime();

        try {
            var launcher = new D4Launcher();
            launcher.parse(args);
            launcher.launch();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception as occurred.", e);
            System.exit(1);

        } finally {
            var wallTime = System.nanoTime() - timeBefore;
            System.out.println("c");
            System.out.println("c wall time: " + (wallTime * 1e-9) + "s");
        }
    }

}
