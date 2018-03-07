package com.utsavoza.rope;

/**
 * <p>A rope is a tree-like data structure that provides a more efficient way
 * of concatenating strings, with each internal node representing the concatenation
 * of its children, and the leaves consisting of flat strings, usually represented
 * as contiguous array of characters.</p>
 */
public interface Rope extends CharSequence, Comparable<Rope>, Iterable<Character> {

  Rope insert(int offset, CharSequence cs);

  Rope append(CharSequence suffix);

  Rope delete(int beginIndex, int endIndex);

  Rope rebalance();

  Rope subSequence(int beginIndex, int endIndex);

  boolean isEmpty();
}
