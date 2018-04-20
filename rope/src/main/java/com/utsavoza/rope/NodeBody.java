package com.utsavoza.rope;

import java.util.List;

/**
 * Represents the body of a node in the tree. The current implementation
 * is {@link Rope} specific, and can possibly be generalized. The internal
 * nodes in the tree represents concatenation of its children whereas the
 * leaves consists of flat strings.
 *
 * @see Leaf
 * @see Internal
 */
final class NodeBody {

  private int height;
  private int length;
  private int newlineCount;
  private NodeVal val;

  private NodeBody(Builder builder) {
    nullCheck(builder);
    this.height = builder.height;
    this.length = builder.length;
    this.newlineCount = builder.newlineCount;
    this.val = builder.val;
  }

  int height() {
    return this.height;
  }

  int length() {
    return this.length;
  }

  int newlineCount() {
    return this.newlineCount;
  }

  NodeVal val() {
    return this.val;
  }

  // could be stricter
  private void nullCheck(Builder builder) {
    if (builder.val == null) {
      throw new IllegalArgumentException("NodeBody val is not assigned");
    }
  }

  @Override public String toString() {
    return "NodeBody: {"
        + "\n\t\theight: " + this.height
        + "\n\t\tlength: " + this.length
        + "\n\t\tnewlineCount: " + this.newlineCount
        + "\n\t\tNodeVal: " + ((this.val instanceof Leaf) ? this.val.get() : this.val.toString())
        + "\n\t}";
  }

  @Override public int hashCode() {
    int hash = 17;
    hash += 31 * hash + this.height;
    hash += 31 * hash + this.length;
    hash += 31 * hash + this.newlineCount;
    hash += 31 * hash + this.val.hashCode();
    return hash;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NodeBody)) {
      return false;
    }
    NodeBody otherNodeBody = (NodeBody) o;
    return otherNodeBody.height == this.height
        && otherNodeBody.length == this.length
        && otherNodeBody.newlineCount == this.newlineCount
        && otherNodeBody.val.equals(this.val);
  }

  interface NodeVal {
    Object get();
  }

  /** The leaf nodes in the tree consists of flat strings. */
  static class Leaf implements NodeVal {

    private String val;

    Leaf(String val) {
      this.val = val;
    }

    @Override public Object get() {
      return val;
    }

    @Override public String toString() {
      return this.val;
    }

    @Override public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Leaf)) {
        return false;
      }
      Leaf otherLeaf = (Leaf) obj;
      return this.val.equals(otherLeaf.val);
    }

    @Override public int hashCode() {
      return this.val.hashCode();
    }
  }

  /** The internal nodes in the tree represents concatenation of its children. */
  static class Internal implements NodeVal {

    private List<Node> children;

    Internal(List<Node> children) {
      this.children = children;
    }

    @Override public Object get() {
      return children;
    }

    @Override public String toString() {
      return "INTERNAL " + this.children.size(); // better formatting and output ??
    }

    @Override public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Internal)) {
        return false;
      }
      Internal otherNode = (Internal) obj;
      return this.children.equals(otherNode.children);
    }

    @Override public int hashCode() {
      int hash = 17;
      hash += 31 * this.children.hashCode() + hash;
      return hash;
    }
  }

  static class Builder {
    private int height;
    private int length;
    private int newlineCount;
    private NodeVal val;

    Builder height(int height) {
      this.height = height;
      return this;
    }

    Builder length(int length) {
      this.length = length;
      return this;
    }

    Builder newlineCount(int newlineCount) {
      this.newlineCount = newlineCount;
      return this;
    }

    Builder val(NodeVal val) {
      this.val = val;
      return this;
    }

    NodeBody build() {
      return new NodeBody(this);
    }
  }
}