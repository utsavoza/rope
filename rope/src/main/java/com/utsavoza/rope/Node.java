package com.utsavoza.rope;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.utsavoza.rope.Util.NEW_LINE;
import static com.utsavoza.rope.Util.compare;
import static com.utsavoza.rope.Util.isCharBoundary;

/** Represents a node in the tree. */
final class Node {

  static final int MIN_LEAF = 511;
  static final int MAX_LEAF = 1024;
  static final int MIN_CHILDREN = 4;
  static final int MAX_CHILDREN = 8;

  private final NodeBody nodeBody;

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
        .val(new Leaf(piece))
        .build();

    return new Node(nodeBody);
  }

  static Node fromPieces(List<Node> pieces) {
    if (pieces.size() < 2 || pieces.size() > MAX_CHILDREN) {
      throw new IllegalArgumentException("Nodes exceeds MAX_CHILDREN limit");
    }

    int height = pieces.get(0).getHeight() + 1;
    int length = pieces.stream().mapToInt(Node::getLength).sum();
    int newlineCount = pieces.stream().mapToInt(Node::getNewlineCount).sum();
    NodeBody nodeBody = new NodeBody.Builder()
        .height(height)
        .length(length)
        .newlineCount(newlineCount)
        .val(new Internal(pieces))
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
      // Splitting at midpoint is also an option
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
    if (rope1.getLength() >= MIN_LEAF && rope2.getLength() >= MIN_LEAF) {
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

  static Node concat(Node rope1, Node rope2) {
    int rope1Height = rope1.getHeight();
    int rope2Height = rope2.getHeight();

    switch (compare(rope1Height, rope2Height)) {
      case LESS: {
        List<Node> rope2Children = rope2.getChildren();
        if (rope1Height == rope2Height - 1 && rope1.isValidNode()) {
          return mergeNodes(Collections.singletonList(rope1), rope2Children);
        }
        Node newRope = concat(rope1, rope2Children.get(0));
        List<Node> rope2ChildrenSubList = rope2Children.subList(1, rope2Children.size());
        if (newRope.getHeight() == rope2Height - 1) {
          return mergeNodes(Collections.singletonList(newRope), rope2ChildrenSubList);
        } else {
          return mergeNodes(newRope.getChildren(), rope2ChildrenSubList);
        }
      }

      case EQUAL: {
        if (rope1.isValidNode() && rope2.isValidNode()) {
          return Node.fromPieces(Arrays.asList(rope1, rope2));
        }
        if (rope1Height == 0) {
          return mergeLeaves(rope1, rope2);
        }
        return mergeNodes(rope1.getChildren(), rope2.getChildren());
      }

      case GREATER: {
        List<Node> rope1Children = rope1.getChildren();
        if (rope2Height == rope1Height - 1) {
          return Node.mergeNodes(rope1Children, Collections.singletonList(rope2));
        }
        int lastChildIndex = rope1Children.size() - 1;
        Node newRope = Node.concat(rope1Children.get(lastChildIndex), rope2);
        List<Node> rope1ChildrenSubList = rope1Children.subList(1, lastChildIndex + 1);
        if (newRope.getHeight() == rope1Height - 1) {
          return mergeNodes(rope1ChildrenSubList, Collections.singletonList(newRope));
        } else {
          return mergeNodes(rope1ChildrenSubList, newRope.getChildren());
        }
      }

      default:
        throw new IllegalStateException("unreachable state");
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

  int getHeight() {
    return this.nodeBody.height();
  }

  int getLength() {
    return this.nodeBody.length();
  }

  int getNewlineCount() {
    return this.nodeBody.newlineCount();
  }

  boolean isLeaf() {
    return this.getHeight() == 0;
  }

  NodeBody getNodeBody() {
    return this.nodeBody;
  }

  List<Node> getChildren() {
    if (this.nodeBody.val() instanceof Leaf) {
      throw new UnsupportedOperationException("getChildren() called on leaf");
    }
    @SuppressWarnings("unchecked")
    List<Node> nodes = (List<Node>) this.nodeBody.val().get();
    return nodes;
  }

  String getLeaf() {
    if (this.nodeBody.val() instanceof Internal) {
      throw new UnsupportedOperationException("getLeaf() called on internal node");
    }
    return (String) this.nodeBody.val().get();
  }

  boolean isValidNode() {
    if (this.nodeBody.val() instanceof Leaf) {
      return ((String) this.nodeBody.val().get()).length() >= MIN_LEAF;
    } else if (this.nodeBody.val() instanceof Internal) {
      @SuppressWarnings("unchecked")
      List<Node> nodes = ((List<Node>) this.nodeBody.val().get());
      return nodes.stream().allMatch((node) -> node.getLength() >= MIN_CHILDREN);
    } else {
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