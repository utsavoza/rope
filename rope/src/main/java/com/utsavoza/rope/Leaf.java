package com.utsavoza.rope;

import com.utsavoza.rope.NodeBody.NodeVal;

/** The leaf nodes in the tree consists of flat strings. */
final class Leaf implements NodeVal {

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
