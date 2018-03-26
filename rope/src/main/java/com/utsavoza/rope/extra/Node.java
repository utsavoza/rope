package com.utsavoza.rope.extra;

final class Node {

  static final int MIN_LEAF = 511;
  static final int MAX_LEAF = 1024;
  static final int MIN_CHILDREN = 4;
  static final int MAX_CHILDREN = 8;

  private final NodeBody nodeBody;

  Node(NodeBody nodeBody) {
    this.nodeBody = nodeBody;
  }
}
