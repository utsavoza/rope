package com.utsavoza.rope;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.utsavoza.rope.Util.NEW_LINE;
import static com.utsavoza.rope.Util.countNewLines;
import static com.utsavoza.rope.Util.isCharBoundary;

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
        .newlineCount(countNewLines(piece))
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

  static Node mergeNodes(List<Node> children1, List<Node> children2) {
    int totalChildren = children1.size() + children2.size();
    List<Node> children =
        Stream.concat(children1.stream(), children2.stream()).collect(Collectors.toList());
    if (totalChildren <= MAX_CHILDREN) {
      return Node.fromPieces(children);
    } else {
      // Splitting at midpoint is also an option.
      int splitPoint = Math.min(MAX_CHILDREN, totalChildren - MIN_CHILDREN);
      List<Node> left = children.subList(0, splitPoint);
      List<Node> right = children.subList(splitPoint, children.size());
      List<Node> parent = Arrays.asList(Node.fromPieces(left), Node.fromPieces(right));
      return Node.fromPieces(parent);
    }
  }

  static Node mergeLeaves(Node rope1, Node rope2) {
    if (!rope1.isLeaf() || !rope2.isLeaf()) {
      throw new IllegalArgumentException("mergeLeaves() called with non-leaf node");
    }
    if (rope1.length() >= MIN_LEAF && rope2.length() >= MIN_LEAF) {
      return Node.fromPieces(Arrays.asList(rope1, rope2));
    }
    String rope1String = ((String) rope1.nodeBody.val().get());
    String rope2String = ((String) rope2.nodeBody.val().get());
    String ropeString = rope1String + rope2String;
    if (ropeString.length() <= MAX_LEAF) {
      return Node.fromStringPiece(ropeString);
    } else {
      int splitPoint = findLeafSplitForMerge(ropeString);
      String leftString = ropeString.substring(0, splitPoint);
      String rightString = ropeString.substring(splitPoint);
      Node leftNode = Node.fromStringPiece(leftString);
      Node rightNode = Node.fromStringPiece(rightString);
      return Node.fromPieces(Arrays.asList(leftNode, rightNode));
    }
  }

  private static int findLeafSplitForMerge(String s) {
    return findLeafSplit(s, Math.max(MIN_LEAF, s.length() - MAX_LEAF));
  }

  private static int findLeafSplitForBulk(String s) {
    return findLeafSplit(s, MIN_LEAF);
  }

  private static int findLeafSplit(String s, int minSplit) {
    int splitPoint = Math.min(MAX_LEAF, s.length() - MIN_LEAF);
    int newlineCharIndex = s.indexOf(NEW_LINE);
    if (newlineCharIndex != -1) {
      return minSplit + newlineCharIndex;
    } else {
      while (!isCharBoundary(s, splitPoint)) {
        splitPoint -= 1;
      }
      return splitPoint;
    }
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
