package com.utsavoza.rope;

/** The leaf nodes in the tree consists of flat strings. */
class Leaf implements NodeVal {

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
}
