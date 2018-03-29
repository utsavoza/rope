package com.utsavoza.rope;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RopeTest {

  @Test public void testRopeFrom() {
    Rope a = Rope.from("hello");
    assertEquals(a.toString(), "hello");

    Rope b = Rope.from("world");
    assertEquals(b.toString(), "world");
  }

  @Test public void testRopeBuilder() {
    Rope rope = new Rope.Builder()
        .pushString("<<")
        .pushRope(Rope.from("hello"))
        .pushString(">>")
        .build();
    assertEquals(rope.toString(), "<<hello>>");
  }

  @Test public void testConcat() {
    Rope a = Rope.from("hello");
    Rope b = Rope.from(" world");
    Rope c = a.concat(b);
    assertEquals(c.toString(), "hello world");
    assertEquals(a.toString(), "hello");
    assertEquals(b.toString(), " world");
  }

  @Test public void testReplace() {
    Rope a = Rope.from("hello world");
    Rope b = a.replace(1, 9, "era");
    assertEquals(b.toString(), "herald");
    assertEquals(a.toString(), "hello world");
  }

  @Test public void testEquals() {
    Rope a = Rope.from("hello");
    Rope b = Rope.from("world");
    Rope c = Rope.from("hello");
    assertNotEquals(a, b);
    assertEquals(a, c);
  }
}
