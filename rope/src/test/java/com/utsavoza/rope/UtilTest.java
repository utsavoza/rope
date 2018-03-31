package com.utsavoza.rope;

import org.junit.Test;

import static com.utsavoza.rope.Util.NEW_LINE;
import static com.utsavoza.rope.Util.countOccurrence;
import static com.utsavoza.rope.Util.isCharBoundary;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UtilTest {

  @Test public void testCountNewLines() {
    assertEquals(3, countOccurrence("\nHello,\nWorld!\n", NEW_LINE));
    assertEquals(2, countOccurrence("Hello, World\n\n", NEW_LINE));
  }

  @Test public void testIsCharBoundary() {
    String s = "Löwe 老虎 Léopard";
    assertTrue(isCharBoundary(s, 0));
    assertTrue(isCharBoundary(s, 6));
    assertTrue(isCharBoundary(s, s.length()));
    // second byte of ö
    assertFalse(isCharBoundary(s, 2));
    // third byte of 老
    assertFalse(isCharBoundary(s, 8));
  }
}
