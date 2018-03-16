package com.utsavoza.rope;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilTest {

  @Test public void testCountNewLines() {
    assertEquals(3, Util.countNewLines("\nHello,\nWorld!\n"));
    assertEquals(2, Util.countNewLines("Hello, World\n\n"));
  }

  @Test public void testIsCharBoundary() {
    String s = "Löwe 老虎 Léopard";
    assertTrue(Util.isCharBoundary(s, 0));
    assertTrue(Util.isCharBoundary(s, 6));
    assertTrue(Util.isCharBoundary(s, s.length()));
    // second byte of ö
    assertFalse(Util.isCharBoundary(s, 2));
    // third byte of 老
    assertFalse(Util.isCharBoundary(s, 8));
  }
}
