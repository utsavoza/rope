package com.utsavoza.rope;

import java.util.ArrayList;
import java.util.List;

import static com.utsavoza.rope.Node.MAX_CHILDREN;
import static com.utsavoza.rope.Node.MAX_LEAF;
import static com.utsavoza.rope.Node.findLeafSplitForBulk;

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
 *   assertEquals(b.toString(), "ello wor");
 * </pre>
 *
 * <br>- Replace a part of a {@link Rope}
 * <pre>
 *   Rope a = Rope.from("hello world");
 *   Rope b = a.replace(1, 9, "era");
 *   assertEquals(b.toString(), "herald");
 * </pre>
 *
 * <br>- Possibly ??
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

  private boolean isFull() {
    return this.start == 0 && this.length == this.root.getLength();
  }

  /** Returns the length of Rope. */
  public int length() {
    return this.length;
  }

  /** Builder utility to create an instance of Rope. */
  public static class Builder {

    private Node root;

    public Builder pushRope(Rope rope) {
      rope.root.subsequence(this, rope.start, rope.start + rope.length);
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

    public Builder pushString(String s) {
      if (s.length() <= MAX_LEAF) {
        if (!s.isEmpty()) {
          return pushShortString(s);
        }
      }
      List<List<Node>> stack = new ArrayList<>();
      while (!s.isEmpty()) {
        int splitPoint = findLeafSplitForBulk(s);
        Node newNode = Node.fromStringPiece(s.substring(0, splitPoint));
        s = s.substring(splitPoint);
        while (true) {
          List<Node> lastList = stack.get(stack.size() - 1);
          Node finalNewNode = newNode;
          if (lastList.stream().allMatch(node -> node.getHeight() != finalNewNode.getHeight())) {
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

    Node getRootNode() {
      if (this.root == null) {
        this.root = Node.fromStringPiece("");
      }
      return this.root;
    }
  }
}
