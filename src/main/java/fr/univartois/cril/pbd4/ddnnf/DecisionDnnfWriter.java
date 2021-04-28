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

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.sat4j.core.VecInt;
import org.sat4j.specs.IVecInt;

/**
 * The DecisionDnnfWriter allows to write a decision-DNNF to an output stream, using
 * the NNF format.
 *
 * @author Romain WALLON
 *
 * @version 0.2.0
 */
final class DecisionDnnfWriter implements DecisionDnnfVisitor, Closeable {

    /**
     * The writer to use to print the decision-DNNF to an output stream.
     */
    private final PrintWriter writer;

    /**
     * The identifiers of the children of the nodes being explored.
     */
    private final IVecInt nodeIdentifiers;

    /**
     * The index of the constant node {@link ConstantNode#TRUE} in the output, or
     * {@code -1} if it has not been written yet.
     */
    private int trueIndex = -1;

    /**
     * The index of the constant node {@link ConstantNode#FALSE} in the output, or
     * {@code -1} if it has not been written yet.
     */
    private int falseIndex = -1;

    /**
     * The index of the current node in the output.
     */
    private int currentIndex;

    /**
     * The time stamp at which visiting has started (in nano-seconds since the epoch).
     */
    private long startTime;

    /**
     * Creates a new DecisionDnnfWriter.
     *
     * @param path The path of the file in which to write the decision-DNNF.
     *
     * @throws IOException If an I/O error occurs while opening the file.
     */
    public DecisionDnnfWriter(String path) throws IOException {
        this(Files.newOutputStream(Path.of(path)));
    }

    /**
     * Creates a new DecisionDnnfWriter.
     *
     * @param outputStream The output stream in which to write the decision-DNNF.
     */
    public DecisionDnnfWriter(OutputStream outputStream) {
        this(new PrintWriter(outputStream));
    }

    /**
     * Creates a new DecisionDnnfWriter.
     *
     * @param writer The writer to use to print the decision-DNNF to an output stream.
     */
    public DecisionDnnfWriter(PrintWriter writer) {
        this.writer = writer;
        this.nodeIdentifiers = new VecInt();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#enter(fr.univartois.cril.
     * pbd4.ddnnf.DecisionDnnf)
     */
    @Override
    public void enter(DecisionDnnf ddnnf) {
        this.startTime = System.nanoTime();

        // Writing the metadata of the decision-DNNF.
        writer.printf("nnf %d %d %d%n",
                ddnnf.getNumberOfNodes(),
                ddnnf.getNumberOfEdges(),
                ddnnf.getNumberOfVariables());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#exit(fr.univartois.cril.
     * pbd4.ddnnf.DecisionDnnf)
     */
    @Override
    public void exit(DecisionDnnf ddnnf) {
        writer.flush();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#enter(fr.univartois.cril.
     * pbd4.ddnnf.ConjunctionNode)
     */
    @Override
    public boolean enter(ConjunctionNode node) {
        return enterInternal(node);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.
     * pbd4.ddnnf.ConjunctionNode)
     */
    @Override
    public void visit(ConjunctionNode node) {
        // Nothing to do when visiting this node.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#exit(fr.univartois.cril.
     * pbd4.ddnnf.ConjunctionNode)
     */
    @Override
    public void exit(ConjunctionNode node) {
        writer.printf("A %s%n", collectChildrenOfCurrentNode());
        exitInternal(node);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#enter(fr.univartois.cril.
     * pbd4.ddnnf.DecisionNode)
     */
    @Override
    public boolean enter(DecisionNode node) {
        return enterInternal(node);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.
     * pbd4.ddnnf.DecisionNode)
     */
    @Override
    public void visit(DecisionNode node) {
        // Nothing to do when visiting this node.
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#exit(fr.univartois.cril.
     * pbd4.ddnnf.DecisionNode)
     */
    @Override
    public void exit(DecisionNode node) {
        writer.printf("O %d %s%n", node.getVariable(), collectChildrenOfCurrentNode());
        exitInternal(node);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.
     * pbd4.ddnnf.LiteralNode)
     */
    @Override
    public void visit(LiteralNode node) {
        if (node.getVisitStamp() != startTime) {
            // The literal has not been printed yet.
            node.setVisitStamp(startTime);
            node.cacheValue(currentIndex++);
            writer.printf("L %d%n", node.getLiteral());
        }

        nodeIdentifiers.push(node.getCached());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.
     * pbd4.ddnnf.ConstantNode)
     */
    @Override
    public void visit(ConstantNode node) {
        if (node == ConstantNode.TRUE) {
            if (trueIndex < 0) {
                trueIndex = currentIndex++;
                writer.println("A 0");
            }
            nodeIdentifiers.push(trueIndex);

        } else {
            if (falseIndex < 0) {
                falseIndex = currentIndex++;
                writer.println("O 0 0");
            }
            nodeIdentifiers.push(falseIndex);
        }
    }

    /**
     * Enters an internal node of the decision-DNNF to print.
     *
     * @param node The node to enter.
     *
     * @return Whether this visitor accepts to actually visit the node.
     */
    private boolean enterInternal(NonConstantDecisionDnnfNode node) {
        if (pushCashedIdentifier(node)) {
            // No need to print the children of this node.
            return false;
        }

        // The children of the node must be printed.
        // Pushing -1 as a delimiter for these children.
        nodeIdentifiers.push(-1);
        return true;
    }

    /**
     * Checks whether the identifier for the given node has already been assigned,
     * and pushes it on the stack of children if this is the case.
     *
     * @param node The node to check the cached identifier of.
     *
     * @return Whether there is a cached identifier for the node.
     */
    private boolean pushCashedIdentifier(NonConstantDecisionDnnfNode node) {
        if (node.getVisitStamp() == this.startTime) {
            nodeIdentifiers.push(node.getCached());
            return true;
        }
        return false;
    }

    /**
     * Gives a string describing the children of the current node.
     *
     * @return The string describing the children of the current node.
     */
    private String collectChildrenOfCurrentNode() {
        int nb = 0;
        var conjuncts = new StringBuilder();

        while (!nodeIdentifiers.isEmpty()) {
            // Adding the child to the string.
            conjuncts.append(nodeIdentifiers.last());
            nodeIdentifiers.pop();
            nb++;

            if (!nodeIdentifiers.isEmpty()) {
                if (nodeIdentifiers.last() >= 0) {
                    // There is another child after this one.
                    conjuncts.append(' ');

                } else {
                    // This is the last child of the current node.
                    // The -1 marker must be removed.
                    nodeIdentifiers.pop();
                    break;
                }
            }
        }

        // Building the representation of the children.
        return nb + " " + conjuncts.toString();
    }

    /**
     * Exits an internal node of the decision-DNNF to print.
     * Additionally, the node is marked as visited and its identifier is pushed
     * on the stack of children.
     *
     * @param node The node to exit.
     */
    private void exitInternal(NonConstantDecisionDnnfNode node) {
        node.setVisitStamp(startTime);
        node.cacheValue(currentIndex++);
        nodeIdentifiers.push(node.getCached());
    }

    /*
     * (non-Javadoc)
     *
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() {
        writer.close();
    }

}
