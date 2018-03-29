package com.utsavoza.rope;

import java.util.Arrays;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NodeTest {

  @Test public void testEquals() {
    Node a = Node.fromString("hello");
    Node b = Node.fromString("world");
    Node c = Node.fromString("world");
    assertNotEquals(a, b);
    assertEquals(b, c);

    Node d = Node.fromPieces(Arrays.asList(a, b));
    assertNotEquals(a, d);

    Node e = Node.fromPieces(Arrays.asList(a, c));
    assertEquals(d, e);
  }
}
