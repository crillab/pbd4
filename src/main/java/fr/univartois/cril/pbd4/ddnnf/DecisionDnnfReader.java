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

package fr.univartois.cril.pbd4.ddnnf;

import static fr.univartois.cril.pbd4.ddnnf.LiteralNode.literal;
import static fr.univartois.cril.pbd4.ddnnf.ConjunctionNode.and;
import static fr.univartois.cril.pbd4.ddnnf.DecisionNode.decision;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The DecisionDnnfReader allows to read a decision-DNNF from an {@link InputStream}.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class DecisionDnnfReader implements Closeable {

    /**
     * The input to read the decision-DNNF from.
     */
    private final BufferedReader input;

    /**
     * The decision-DNNF nodes that have been read so far.
     */
    private List<DecisionDnnfNode> readNodes;

    /**
     * Creates a new DecisionDnnfReader.
     *
     * @param path The path of the file from which to read the input decision-DNNF.
     *
     * @throws IOException If an I/O error occurs while opening the file.
     */
    public DecisionDnnfReader(String path) throws IOException {
        this.input = Files.newBufferedReader(Path.of(path));
    }

    /**
     * Creates a new DecisionDnnfReader.
     *
     * @param input The input stream to read the decision-DNNF from.
     */
    public DecisionDnnfReader(InputStream input) {
        this.input = new BufferedReader(new InputStreamReader(input));
    }

    /**
     * Reads a decision-DNNF from the associated input.
     *
     * @return The read decision-DNNF.
     */
    public DecisionDnnf read() throws IOException {
        // Reading the description of the decision-DNNF.
        var description = input.readLine().split("\\s+");
        int nbNodes = Integer.parseInt(description[1]);
        int nbEdges = Integer.parseInt(description[2]);
        int nbVariables = Integer.parseInt(description[3]);
        
        // Reading the nodes of the decision-DNNF.
        readNodes = new ArrayList<>(nbNodes);
        for (int i = 0; i < nbNodes; i++) {
            readNodes.add(next());
        }

        // The root of the decision-DNNF is the last node of the list.
        var rootNode = readNodes.get(nbNodes - 1);
        return new DecisionDnnf(nbVariables, nbNodes, nbEdges, rootNode);
    }

    /**
     * Reads the next node from the associated input.
     *
     * @return The read node.
     *
     * @throws IOException If an I/O error occurs while reading the file.
     */
    private DecisionDnnfNode next() throws IOException {
        var line = input.readLine().split("\\s+");

        if ("L".equals(line[0])) {
            // The next node represents a literal.
            return nextLiteral(line);
        }

        if ("A".equals(line[0])) {
            // The next node represents a conjunction.
            return nextConjunctionNode(line);
        }

        if ("O".equals(line[0])) {
            // The next node represents a decision.
            return nextDecisionNode(line);
        }

        // The node uses an unrecognized type.
        throw new UnsupportedOperationException("Cannot add node type: " + line[0]);
    }

    /**
     * Reads the next literal node from the given line.
     *
     * @param line The line to read the next node from.
     *
     * @return The read node.
     */
    private DecisionDnnfNode nextLiteral(String[] line) {
        return literal(Integer.parseInt(line[1]));
    }

    /**
     * Reads the next conjunction node from the given line.
     *
     * @param line The line to read the next node from.
     *
     * @return The read node.
     */
    private DecisionDnnfNode nextConjunctionNode(String[] line) {
        int nbConjuncts = Integer.parseInt(line[1]);

        if (nbConjuncts == 0) {
            // The node actually represents the Boolean constant TRUE.
            return ConstantNode.TRUE;
        }

        // Retrieving the conjuncts.
        var conjuncts = new ArrayList<DecisionDnnfNode>(nbConjuncts);
        for (int i = 0; i < nbConjuncts; i++) {
            int conjunct = Integer.parseInt(line[i + 2]);
            conjuncts.add(readNodes.get(conjunct));
        }
        return and(conjuncts);
    }

    /**
     * Reads the next decision node from the given line.
     *
     * @param line The line to read the next node from.
     *
     * @return The read node.
     */
    private DecisionDnnfNode nextDecisionNode(String[] line) {
        int variable = Integer.parseInt(line[1]);

        if (variable == 0) {
            // The node actually represents the Boolean constant FALSE.
            return ConstantNode.FALSE;
        }

        // Retrieving the two children of the node.
        return decision(variable,
                readNodes.get(Integer.parseInt(line[3])),
                readNodes.get(Integer.parseInt(line[4])));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
        input.close();
    }

}
