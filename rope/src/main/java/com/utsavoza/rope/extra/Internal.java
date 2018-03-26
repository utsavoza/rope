package com.utsavoza.rope.extra;

import com.utsavoza.rope.Node;
import java.util.List;

public class Internal implements NodeVal<List<Node>> {

  private List<Node> nodes;

  public Internal(List<Node> nodes) {
    this.nodes = nodes;
  }

  @Override public List<Node> getVal() {
    return nodes;
  }
}
