package com.utsavoza.rope;

/**
 * <p>A <a href="https://en.wikipedia.org/wiki/Rope_(data_structure)">rope</a> is a
 * tree-like data structure that provides a more efficient way of concatenating strings,
 * with each internal node representing the concatenation of its children, and the
 * leaves consisting of flat strings, usually represented as contiguous array of
 * characters.
 *
 * <p>Most operations (like insert, delete, substring) are O(log n). The following
 * implementation provides immutable version of Ropes (also known as
 * <a href="https://en.wikipedia.org/wiki/Persistent_data_structure">persistent</a>.
 * Ideally, if there are many copies of similar strings, the common parts are shared.
 *
 * <p><strong>Examples: (includes a rough intended API)</strong>
 * <br>- Create a {@link Rope} from {@link String}
 * <pre>
 *   Rope a = Rope.from("hello");
 *   Rope b = Rope.from("world!);
 *   assertEquals(a.toString(), "hello");
 *   assertEquals(b.toString(), "world!");
 * </pre>
 *
 * <br>- Get a slice of a {@link Rope}
 * <pre>
 *   Rope a = Rope.from("hello world");
 *   Rope b = a.slice(1, 9);
 *   assertEquals(b.toString(), "ello wor");
 * </pre>
 *
 * <br>- Replace a part of a {@link Rope}
 * <pre>
 *   Rope a = Rope.from("hello world");
 *   Rope b = a.replace(1, 9, "era");
 *   assertEquals(b.toString(), "herald");
 * </pre>
 */
public final class Rope {

}
