package com.utsavoza.rope;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static com.utsavoza.rope.Node.MAX_CHILDREN;
import static com.utsavoza.rope.Node.MAX_LEAF;
import static com.utsavoza.rope.Util.NEW_LINE;
import static com.utsavoza.rope.Util.countOccurrence;
import static com.utsavoza.rope.Util.readSampleFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class NodeTest {

  private String text;

  @Before public void setup() {
    this.text = readSampleFile();
  }

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

  @Test public void testMergeLeaves() {
    Node hello = Node.fromString("hello");
    Node world = Node.fromString("world");
    Node mergedLeaves = Node.mergeLeaves(hello, world);
    Node helloWorld = Node.fromString("helloworld");
    assertEquals(mergedLeaves, helloWorld);
    assertNotEquals(mergedLeaves, hello);
    assertNotEquals(mergedLeaves, world);
  }

  @Test public void testMergeNodes() {
    Node readme = Node.fromString(text);
    Node first = Node.fromString(text.substring(0, MAX_LEAF));
    Node second = Node.fromString(text.substring(MAX_LEAF, 2 * MAX_LEAF));
    Node third = Node.fromString(text.substring(2 * MAX_LEAF));
    Node parent = Node.mergeNodes(Arrays.asList(first, second), Collections.singletonList(third));
    assertEquals(parent, readme);
  }

  @Test public void testConcat() {
    Node hello = Node.fromString("hello");
    Node world = Node.fromString(" world");
    Node helloWorld = Node.concat(hello, world);
    assertEquals(helloWorld.getString(), "hello world");
    helloWorld = hello.concat(world);
    assertEquals(helloWorld.getString(), "hello world");
  }

  @Test public void testNodeAttrs() {
    Node helloWorld = Node.fromString("hello world");
    assertEquals(0, helloWorld.getHeight());
    assertEquals(11, helloWorld.getLength());
    assertEquals(0, helloWorld.getNewlineCount());
    assertEquals("hello world", helloWorld.getString());

    Node readme = Node.fromString(text);
    assertEquals(1, readme.getHeight());
    assertEquals(text.length(), readme.getLength());
    assertEquals(countOccurrence(text, NEW_LINE), readme.getNewlineCount());
    assertEquals(text, readme.getString());
  }
}
