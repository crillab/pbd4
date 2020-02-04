/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Romain WALLON.
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 * If not, see {@link http://www.gnu.org/licenses}.
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
 * The DecisionDnnfWriter allows to write a d-DNNF to an output stream, using the NNF format.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class DecisionDnnfWriter implements DecisionDnnfVisitor, Closeable {

    /**
     * The writer to use to print the d-DNNF to an output stream.
     */
    private final PrintWriter writer;
    
    /**
     * The children of the node being explored.
     */
    private final IVecInt children;
    
    /**
     * The index of the current node in the output.
     */
    private int currentIndex;

    /**
     * Creates a new DecisionDnnfWriter.
     *
     * @param path The path of the file in which to write the d-DNNF.
     *
     * @throws IOException If an I/O error occurs while opening the file.
     */
    public DecisionDnnfWriter(String path) throws IOException {
        this(Files.newOutputStream(Path.of(path)));
    }

    /**
     * Creates a new DecisionDnnfWriter.
     *
     * @param outputStream The output stream in which to write the d-DNNF.
     */
    public DecisionDnnfWriter(OutputStream outputStream) {
        this(new PrintWriter(outputStream));
    }

    /**
     * Creates a new DecisionDnnfWriter.
     *
     * @param writer The writer to use to print the d-DNNF to an output stream.
     */
    public DecisionDnnfWriter(PrintWriter writer) {
        this.writer = writer;
        this.children = new VecInt();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.
     * ddnnf.ConjunctionNode)
     */
    @Override
    public void visit(ConjunctionNode node) {
        writer.printf("A %d", children.size());
        printCurrentChildren();
        pushNode();
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.
     * ddnnf.DecisionNode)
     */
    @Override
    public void visit(DecisionNode node) {
        writer.printf("O %d %d", node.getVariable(), children.size());
        printCurrentChildren();
        pushNode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.
     * ddnnf.LiteralNode)
     */
    @Override
    public void visit(LiteralNode node) {
        writer.printf("L %d%n", node.getLiteral());
        pushNode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#visit(fr.univartois.cril.pbd4.
     * ddnnf.LeafNode)
     */
    @Override
    public void visit(LeafNode node) {
        writer.println(node.toNNF());
        pushNode();
    }

    /**
     * Prints the children of the current node to the output stream, and removes them once finished.
     */
    private void printCurrentChildren() {
        // TODO
    }

    /**
     * Adds a child of the node currently visited.
     */
    private void pushNode() {
        children.push(currentIndex);
        currentIndex++;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.univartois.cril.pbd4.ddnnf.DecisionDnnfVisitor#exit(fr.univartois.cril.pbd4.
     * ddnnf.DecisionDnnf)
     */
    @Override
    public void exit(DecisionDnnf root) {
        writer.flush();
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
