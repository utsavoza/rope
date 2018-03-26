package com.utsavoza.rope.extra;

public class Leaf implements NodeVal<String> {

  private String value;

  Leaf(String value) {
    this.value = value;
  }

  @Override public String getVal() {
    return this.value;
  }
}
