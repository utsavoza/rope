package com.utsavoza.rope;

import com.utsavoza.rope.NodeBody.NodeVal;
import java.util.List;

/** The internal nodes in the tree represents concatenation of its children. */
final class Internal implements NodeVal {

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
