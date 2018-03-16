package com.utsavoza.rope;

final class NodeBody {

  private int height;
  private int length;
  private int newlineCount;
  private NodeVal val;

  NodeBody(int height, int length, int newlineCount, NodeVal val) {
    this.height = height;
    this.length = length;
    this.newlineCount = newlineCount;
    this.val = val;
  }

  NodeBody(Builder builder) {
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
        + "\n\t\tNodeVal: " + this.val.toString()
        + "\n\t}";
  }

  @Override public int hashCode() {
    int hash = 17;
    hash += 31 * hash + this.height;
    hash += 31 * hash + this.length;
    hash += 31 * hash + this.newlineCount;
    hash += 31 * hash + val.hashCode();
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

  public static class Builder {
    private int height;
    private int length;
    private int newlineCount;
    private NodeVal val;

    public Builder height(int height) {
      this.height = height;
      return this;
    }

    public Builder length(int length) {
      this.length = length;
      return this;
    }

    public Builder newlineCount(int newlineCount) {
      this.newlineCount = newlineCount;
      return this;
    }

    public Builder val(NodeVal val) {
      this.val = val;
      return this;
    }

    public NodeBody build() {
      return new NodeBody(this);
    }
  }
}
