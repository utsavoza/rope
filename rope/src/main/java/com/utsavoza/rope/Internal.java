package com.utsavoza.rope;

import java.util.List;

/** The internal nodes in the tree represents concatenation of its children. */
class Internal implements NodeVal {

  private List<Node> children;

  Internal(List<Node> children) {
    this.children = children;
  }

  @Override public Object get() {
    return children;
  }

  @Override public String toString() {
    return this.children.toString();
  }
}
