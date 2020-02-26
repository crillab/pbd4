/**
 * PBD4, a pseudo-Boolean based implementation of the D4 compiler.
 * Copyright (c) 2020 - Univ Artois & CNRS.
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

package fr.univartois.cril.pbd4.pbc;

import java.io.Serializable;
import java.util.Comparator;

import org.sat4j.specs.IVecInt;
import org.sat4j.specs.IteratorInt;

/**
 * The RangeVecInt is an implementation of {@link IVecInt} representing a range of
 * consecutive integers.
 * This implementation is immutable by definition.
 *
 * @author Romain WALLON
 *
 * @version 0.1.0
 */
public final class RangeVecInt implements IVecInt {

    /**
     * The {@code serialVersionUID} of this {@link Serializable} class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The lower bound (inclusive) of the range.
     */
    private final int lowerBound;

    /**
     * The upper bound (exclusive) of the range.
     */
    private final int upperBound;

    /**
     * Creates a new RangeVecInt.
     *
     * @param lowerBound The lower bound (inclusive) of the range.
     * @param upperBound The upper bound (exclusive) of the range.
     */
    private RangeVecInt(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Creates a new RangeVecInt.
     *
     * @param lowerBound The lower bound (inclusive) of the range.
     * @param upperBound The upper bound (exclusive) of the range.
     *
     * @return The created range.
     */
    public static IVecInt range(int lowerBound, int upperBound) {
        return new RangeVecInt(lowerBound, upperBound);
    }

    /**
     * Creates a new RangeVecInt, starting from {@code 0}.
     *
     * @param upperBound The upper bound (exclusive) of the range.
     *
     * @return The created range.
     */
    public static IVecInt range(int upperBound) {
        return new RangeVecInt(0, upperBound);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#clear()
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public IVecInt clone() {
        // As a range is immutable, returning this will not break anything.
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#contains(int)
     */
    @Override
    public boolean contains(int elt) {
        return (lowerBound <= elt) && (elt < upperBound);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#containsAt(int)
     */
    @Override
    public int containsAt(int elt) {
        return contains(elt) ? (elt - lowerBound) : -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#containsAt(int, int)
     */
    @Override
    public int containsAt(int elt, int from) {
        int index = containsAt(elt);
        return (index > from) ? index : -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#copyTo(org.sat4j.specs.IVecInt)
     */
    @Override
    public void copyTo(IVecInt vec) {
        for (int i = lowerBound; i < upperBound; i++) {
            vec.push(i);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#copyTo(int[])
     */
    @Override
    public void copyTo(int[] array) {
        for (int i = lowerBound; i < upperBound; i++) {
            array[i - lowerBound] = i;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#delete(int)
     */
    @Override
    public int delete(int index) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#ensure(int)
     */
    @Override
    public void ensure(int size) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#get(int)
     */
    @Override
    public int get(int i) {
        return lowerBound + i;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#growTo(int, int)
     */
    @Override
    public void growTo(int newSize, int pad) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#indexOf(int)
     */
    @Override
    public int indexOf(int elt) {
        return containsAt(elt);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#insertFirst(int)
     */
    @Override
    public void insertFirst(int elt) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return lowerBound == upperBound;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#iterator()
     */
    @Override
    public IteratorInt iterator() {
        return new IteratorInt() {

            private int current = lowerBound;

            @Override
            public int next() {
                return current++;
            }

            @Override
            public boolean hasNext() {
                return current < upperBound;
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#last()
     */
    @Override
    public int last() {
        return upperBound - 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#moveTo(org.sat4j.specs.IVecInt)
     */
    @Override
    public void moveTo(IVecInt vec) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#moveTo(int[])
     */
    @Override
    public void moveTo(int[] array) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#moveTo(int, int[])
     */
    @Override
    public void moveTo(int sourceStartingIndex, int[] dest) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#moveTo(int, int)
     */
    @Override
    public void moveTo(int source, int dest) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#moveTo2(org.sat4j.specs.IVecInt)
     */
    @Override
    public void moveTo2(IVecInt vec) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#pop()
     */
    @Override
    public IVecInt pop() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#push(int)
     */
    @Override
    public IVecInt push(int elt) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#remove(int)
     */
    @Override
    public void remove(int elt) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#set(int, int)
     */
    @Override
    public void set(int i, int elt) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#shrink(int)
     */
    @Override
    public void shrink(int nb) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#shrinkTo(int)
     */
    @Override
    public void shrinkTo(int newSize) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#size()
     */
    @Override
    public int size() {
        return upperBound - lowerBound;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#sort()
     */
    @Override
    public void sort() {
        // A range is already sorted.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#sort(java.util.Comparator)
     */
    @Override
    public void sort(Comparator<Integer> cmp) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#sortUnique()
     */
    @Override
    public void sortUnique() {
        // A range is already sorted and has unique values.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#subset(int)
     */
    @Override
    public IVecInt[] subset(int cardinal) {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#toArray()
     */
    @Override
    public int[] toArray() {
        int[] array = new int[size()];
        copyTo(array);
        return array;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#unsafeGet(int)
     */
    @Override
    public int unsafeGet(int i) {
        return get(i);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.sat4j.specs.IVecInt#unsafePush(int)
     */
    @Override
    public void unsafePush(int elt) {
        throw new UnsupportedOperationException();
    }

}
