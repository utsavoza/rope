package com.utsavoza.rope;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NodeBodyTest {

  @Test public void testEquals() {
    NodeBody nodeBody = new NodeBody.Builder()
        .height(0)
        .length(10)
        .newlineCount(0)
        .val(NodeVal.LEAF)
        .build();

    NodeBody otherNodeBody = new NodeBody.Builder()
        .height(0)
        .length(10)
        .newlineCount(0)
        .val(NodeVal.INTERNAL)
        .build();
    assertNotEquals(nodeBody, otherNodeBody);

    otherNodeBody = new NodeBody.Builder()
        .height(0)
        .length(10)
        .newlineCount(0)
        .val(NodeVal.LEAF)
        .build();
    assertEquals(nodeBody, otherNodeBody);

    otherNodeBody = new NodeBody.Builder()
        .height(1)
        .length(10)
        .newlineCount(0)
        .val(NodeVal.LEAF)
        .build();
    assertNotEquals(nodeBody, otherNodeBody);
  }
}
