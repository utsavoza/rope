package com.github.utsavoza.rope;

import org.junit.Test;

import static com.github.utsavoza.rope.Util.NEW_LINE;
import static com.github.utsavoza.rope.Util.countOccurrence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NodeBodyTest {

  @Test public void testEquals() {
    String helloStr = "hello";
    NodeBody hello = new NodeBody.Builder()
        .height(0)
        .length(helloStr.length())
        .newlineCount(countOccurrence(helloStr, NEW_LINE))
        .val(new NodeBody.Leaf(helloStr))
        .build();

    String worldStr = "world";
    NodeBody world = new NodeBody.Builder()
        .height(0)
        .length(worldStr.length())
        .newlineCount(countOccurrence(worldStr, NEW_LINE))
        .val(new NodeBody.Leaf(worldStr))
        .build();

    assertNotEquals(hello, world);

    NodeBody worldCopy = new NodeBody.Builder()
        .height(0)
        .length(worldStr.length())
        .newlineCount(countOccurrence(worldStr, NEW_LINE))
        .val(new NodeBody.Leaf("world"))
        .build();

    assertEquals(world, worldCopy);
  }
}
