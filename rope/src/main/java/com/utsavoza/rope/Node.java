package com.utsavoza.rope;

import java.util.List;

final class Node {

  static final int MIN_LEAF = 511;
  static final int MAX_LEAF = 1024;
  static final int MIN_CHILDREN = 4;
  static final int MAX_CHILDREN = 8;

  private NodeBody nodeBody;

  Node(NodeBody nodeBody) {
    this.nodeBody = nodeBody;
  }

  static Node fromStringPiece(String piece) {
    if (piece.length() > MAX_LEAF) {
      throw new IllegalArgumentException("String piece exceeds MAX_LEAF limit");
    }

    NodeBody nodeBody = new NodeBody.Builder()
        .height(0)
        .length(piece.length())
        .newlineCount(Util.countNewLines(piece))
        .val(NodeVal.LEAF.instance(piece))
        .build();

    return new Node(nodeBody);
  }

  static Node fromPieces(List<Node> pieces) {
    if (pieces.size() < 2 || pieces.size() > MAX_CHILDREN) {
      throw new IllegalArgumentException("Nodes exceeds MAX_CHILDREN limit");
    }

    int height = pieces.get(0).height();
    int length = pieces.stream().mapToInt(Node::length).sum();
    int newlineCount = pieces.stream().mapToInt(Node::newlineCount).sum();
    NodeBody nodeBody = new NodeBody.Builder()
        .height(height)
        .length(length)
        .newlineCount(newlineCount)
        .val(NodeVal.INTERNAL.instance(pieces))
        .build();

    return new Node(nodeBody);
  }

  int height() {
    return this.nodeBody.height();
  }

  int length() {
    return this.nodeBody.length();
  }

  int newlineCount() {
    return this.nodeBody.newlineCount();
  }

  boolean isLeaf() {
    return this.height() == 0;
  }

  NodeBody nodeBody() {
    return this.nodeBody;
  }

  List<Node> getChildren() {
    if (this.nodeBody.val() == NodeVal.LEAF) {
      throw new UnsupportedOperationException("getChildren() called on leaf");
    }
    @SuppressWarnings("unchecked")
    List<Node> nodes = (List<Node>) this.nodeBody.val().get();
    return nodes;
  }

  String getLeaf() {
    if (this.nodeBody.val() == NodeVal.INTERNAL) {
      throw new UnsupportedOperationException("getLeaf() called on internal node");
    }
    return (String) this.nodeBody.val().get();
  }

  boolean isValidNode() {
    switch (this.nodeBody.val()) {
      case LEAF:
        return ((String) this.nodeBody.val().get()).length() >= MIN_LEAF;

      case INTERNAL:
        @SuppressWarnings("unchecked")
        List<Node> nodes = ((List<Node>) this.nodeBody.val().get());
        return nodes.stream().allMatch((node) -> node.length() >= MIN_CHILDREN);

      default:
        throw new UnsupportedOperationException("Unreachable state");
    }
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Node)) {
      return false;
    }
    Node otherNode = (Node) o;
    return this.nodeBody.equals(otherNode.nodeBody);
  }

  @Override public int hashCode() {
    int hash = 17;
    hash += 31 * this.nodeBody.hashCode() + hash;
    return hash;
  }

  @Override public String toString() {
    return "Node: {" + "\n\t" + this.nodeBody.toString() + "\n}";
  }
}
