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

/**
 * The D4Launcher allows to execute PBD4 from the command line.
 *
 * @author Romain WALLON
 * 
 * @version 0.1.0
 */
@Params("0..1")
public final class D4Launcher {

    /**
     * The logger used to log the events of the program.
     */
    private static final Logger LOGGER = Logger.getLogger("fr.univartois.cril.pbd4");

    @ShortName("f")
    @LongName("input-format")
    @Description("The format of the input.")
    @Args(value = 1, names = { "cnf", "opb" })
    private String inputFormat = "opb";

    @ShortName("c")
    @LongName("caching-strategy")
    @Description("The strategy for caching formulae.")
    @Args(value = 1, names = { "none" })
    private String cachingStrategy = "none";

    @ShortName("o")
    @LongName("output")
    @Description("The type of the output.")
    @Args(value = 1, names = { "count", "ddnnf" })
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
     * Parses the command line arguments, and initializes the fields accordingly.
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
        d4.useCachingStrategy(getCachingStrategy());
        readInput(d4);
        launch(d4);
    }

    /**
     * Gives the caching strategy specified in the command line.
     *
     * @param <T> The type of the elements to cache.
     *
     * @return The specified caching strategy.
     */
    private CachingStrategy<?> getCachingStrategy() {
        switch (cachingStrategy) {
            case "none":
                return NoCache.instance();

            default:
                throw new IllegalArgumentException("Unrecognized caching strategy: " + cachingStrategy);
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
            // Reading the input from STDIN.
            if ("cnf".equals(inputFormat)) {
                d4.whenCompilingCnf(System.in);

            } else {
                d4.whenCompilingOpb(System.in);
            }

        } else {
            // Reading the input from the specified file.
            d4.whenCompiling(parameters.get(0));
        }
    }

    /**
     * Launches PBD4 in the specified mode.
     *
     * @param d4 The configuration of PBD4 to use.
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
     * @param d4 The configuration of PBD4 to use.
     */
    private void launchModelCounter(D4 d4) {
        var count = d4.countModels();
        System.out.println("s " + count);
    }

    /**
     * Launches PBD4 as a decision-DNNF compiler.
     *
     * @param d4 The configuration of PBD4 to use.
     */
    private void launchDecisionDnnfCompiler(D4 d4) {
        var ddnnf = d4.compileToDecisionDnnf();
        ddnnf.writeTo(System.out);
    }

    /**
     * Executes the program.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            var launcher = new D4Launcher();
            launcher.parse(args);
            launcher.launch();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An exception as occurred.", e);
            System.exit(1);
        }
    }

}
