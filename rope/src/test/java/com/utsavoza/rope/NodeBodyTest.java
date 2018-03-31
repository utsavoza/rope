package com.utsavoza.rope;

import org.junit.Test;

import static com.utsavoza.rope.Util.NEW_LINE;
import static com.utsavoza.rope.Util.countOccurrence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class NodeBodyTest {

  @Test public void testEquals() {
    String helloStr = "hello";
    NodeBody hello = new NodeBody.Builder()
        .height(0)
        .length(helloStr.length())
        .newlineCount(countOccurrence(helloStr, NEW_LINE))
        .val(new Leaf(helloStr))
        .build();

    String worldStr = "world";
    NodeBody world = new NodeBody.Builder()
        .height(0)
        .length(worldStr.length())
        .newlineCount(countOccurrence(worldStr, NEW_LINE))
        .val(new Leaf(worldStr))
        .build();

    assertNotEquals(hello, world);

    NodeBody worldCopy = new NodeBody.Builder()
        .height(0)
        .length(worldStr.length())
        .newlineCount(countOccurrence(worldStr, NEW_LINE))
        .val(new Leaf("world"))
        .build();

    assertEquals(world, worldCopy);
  }
}
