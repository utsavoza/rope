package com.utsavoza.rope;

import com.utsavoza.rope.Node.ChildIndexOffset;
import java.util.ArrayList;
import java.util.List;

import static com.utsavoza.rope.Node.MAX_CHILDREN;
import static com.utsavoza.rope.Node.MAX_LEAF;
import static com.utsavoza.rope.Node.getChildIndexOffset;
import static com.utsavoza.rope.Util.findLeafSplitForBulk;

/**
 * <p>A <a href="https://en.wikipedia.org/wiki/Rope_(data_structure)">rope</a> is a
 * tree-like data structure that provides a more efficient way of concatenating strings,
 * with each internal node representing the concatenation of its children, and the
 * leaves consisting of flat strings, usually represented as contiguous array of
 * characters.
 *
 * <p>Most operations (like insert, delete, substring) are O(log n). The following
 * implementation intends to provide immutable version of Ropes (also known as
 * <a href="https://en.wikipedia.org/wiki/Persistent_data_structure">persistent</a>.
 * Ideally, if there are many copies of similar strings, the common parts should be
 * shared.
 *
 * <p><strong>Examples:</strong>
 * <br>- Create a {@link Rope} from {@link String}
 * <pre>
 *   Rope a = Rope.from("hello");
 *   Rope b = Rope.from("world!);
 *   assertEquals(a.toString(), "hello");
 *   assertEquals(b.toString(), "world!");
 * </pre>
 *
 * <br>- Concatenate two Ropes.
 * <pre>
 *   Rope a = Rope.from("hello");
 *   Rope b = Rope.from(" world");
 *   Rope c = a.concat(b);
 *   assertEquals(c.toString(), "hello world");
 * </pre>
 *
 * <br>- Get a slice of a {@link Rope}
 * <pre>
 *   Rope a = Rope.from("hello world");
 *   Rope b = a.slice(1, 9);
 *   assertEquals(b.toString(), "ello wo");
 * </pre>
 *
 * <br>- Replace a part of a {@link Rope}
 * <pre>
 *   Rope a = Rope.from("hello world");
 *   Rope b = a.replace(1, 9, "era");
 *   assertEquals(b.toString(), "herald");
 * </pre>
 *
 * <br>- Rope builder utility
 * <pre>
 *   Rope a = new Rope.Builder()
 *      .pushString("<<")
 *      .pushRope(Rope.from("hello"))
 *      .pushString(">>")
 *      .build();
 * </pre>
 */
public final class Rope {

  private Node root;
  private int start;
  private int length;

  private Rope(Node root, int start, int length) {
    this.root = root;
    this.start = start;
    this.length = length;
  }

  /** Create a Rope from the given String. */
  public static Rope from(String s) {
    return Rope.fromNode(Node.fromString(s));
  }

  private static Rope fromNode(Node node) {
    return new Rope(node, 0, node.getLength());
  }

  private static String extractStringFrom(Rope rope) {
    StringBuilder sb = new StringBuilder();
    rope.toStringRec(sb);
    return sb.toString();
  }

  private boolean isFull() {
    return this.start == 0 && this.length == this.root.getLength();
  }

  /**
   * Returns the length of the string this rope holds. The length
   * is equal to number of Unicode code units in the string. The value
   * returned is equivalent to that returned by {@link String#length()}.
   */
  public int length() {
    return this.length;
  }

  /**
   * Returns a new rope that is a slice of this rope from interval
   * [start, end). The result is equivalent to that of {@link String#substring(int, int)}.
   */
  public Rope slice(int start, int end) {
    if (start < this.start || end > this.start + this.length) {
      throw new IllegalArgumentException(
          "[" + start + ", " + end + ") interval is out of bounds for current rope");
    }
    Node root = this.root;
    start += this.start;
    end += this.start - 1;
    while (root.getHeight() > 0) {
      ChildIndexOffset indexOffset = getChildIndexOffset(root.getChildren(), start, end);
      if (indexOffset != null) {
        int index = indexOffset.index;
        int offset = indexOffset.offset;
        root = root.getChildren().get(index);
        start -= offset;
        end -= offset;
      } else {
        break;
      }
    }
    // Could be optimized by implementing custom iterator for rope ?
    return Rope.from(root.getString().substring(start, end - start + 1));
  }

  /**
   * Replace the range of unicode code points in range [start, end) from the
   * Rope with the given string. Returns a new rope resulting from replacing
   * the given range with {@code newString}.
   */
  public Rope replace(int start, int end, String newString) {
    if (start < this.start || end > this.start + this.length) {
      throw new IllegalArgumentException(
          "[" + start + ", " + end + ") interval is out of bounds for current rope");
    }
    // is a trivial replace operation worth making a new rope copy ??
    if (this.isFull()) {
      Node newRoot = new Node(this.root.getNodeBody());
      // defaults to avoiding replacing in place
      newRoot.replaceString(start, end, newString, false);
      return Rope.fromNode(newRoot);
    } else {
      Rope.Builder builder = new Rope.Builder();
      this.root.subsequence(builder, this.start, this.start + start);
      builder.pushString(newString);
      this.root.subsequence(builder, this.start + end, this.start + this.length);
      return builder.build();
    }
  }

  /**
   * Concatenate {@code anotherRope} with this rope, and return a new rope
   * resulting from the concatenation.
   */
  public Rope concat(Rope anotherRope) {
    if (anotherRope == null) {
      // should this be handled silently?
      throw new IllegalArgumentException("Attempting to concat this rope with null");
    }
    Node newRoot = new Node(this.root.getNodeBody());
    return Rope.fromNode(newRoot.concat(anotherRope.root));
  }

  private void toStringRec(StringBuilder sb) {
    this.root.toStringRec(sb);
  }

  private Rope normalize() {
    if (this.isFull()) {
      return this;
    } else {
      return new Rope.Builder()
          .pushRope(this)
          .build();
    }
  }

  @Override public String toString() {
    if (this.isFull()) {
      return this.root.getString();
    } else {
      return extractStringFrom(this);
    }
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Rope)) {
      return false;
    }
    Rope otherRope = (Rope) obj;
    return this.length == otherRope.length
        && this.start == otherRope.start
        && this.root.equals(otherRope.root);
  }

  @Override public int hashCode() {
    int hash = 17;
    hash += 31 * this.length + hash;
    hash += 31 * this.start + hash;
    hash += 31 * this.root.hashCode() + hash;
    return hash;
  }

  /** Builder utility to create an instance of Rope. */
  public static class Builder {

    private Node root;

    public Builder pushRope(Rope rope) {
      rope.root.subsequence(this, rope.start, rope.start + rope.length);
      return this;
    }

    public Builder pushString(String s) {
      if (s.length() <= MAX_LEAF) {
        if (!s.isEmpty()) {
          return pushShortString(s);
        }
      }
      List<List<Node>> stack = new ArrayList<>();
      while (!s.isEmpty()) {
        int splitPoint = s.length() > MAX_LEAF ? findLeafSplitForBulk(s) : s.length();
        Node newNode = Node.fromStringPiece(s.substring(0, splitPoint));
        s = s.substring(splitPoint);
        while (true) {
          Node finalNewNode = newNode;
          if (stack.size() == 0 ||
              stack.get(stack.size() - 1)
                  .stream()
                  .allMatch(node -> node.getHeight() != finalNewNode.getHeight())) {
            stack.add(new ArrayList<>());
          }
          stack.get(stack.size() - 1).add(newNode);
          if (stack.get(stack.size() - 1).size() < MAX_CHILDREN) {
            break;
          }
          newNode = Node.fromPieces(stack.remove(stack.size() - 1));
        }
      }
      for (List<Node> list : stack) {
        for (Node node : list) {
          push(node);
        }
      }
      return this;
    }

    Builder push(Node node) {
      if (this.root == null) {
        this.root = node;
      } else {
        this.root = Node.concat(root, node);
      }
      return this;
    }

    Builder pushShortString(String s) {
      if (s.length() > MAX_LEAF) {
        throw new IllegalArgumentException("string exceeds MAX_LEAF limit");
      }
      return push(Node.fromStringPiece(s));
    }

    Node getRootNode() {
      if (this.root == null) {
        this.root = Node.fromStringPiece("");
      }
      return this.root;
    }

    public Rope build() {
      return Rope.fromNode(getRootNode());
    }
  }
}
