package com.utsavoza.rope;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class IntervalTest {

  @Test public void testNewParams() {
    Interval iv = Interval.ofOpenOpen(2, 42);
    assertEquals(2, iv.start());
    assertFalse(iv.isStartClosed());
    assertEquals(42, iv.end());
    assertFalse(iv.isEndClosed());

    iv = Interval.ofOpenClosed(2, 42);
    assertEquals(2, iv.start());
    assertFalse(iv.isStartClosed());
    assertEquals(42, iv.end());
    assertTrue(iv.isEndClosed());

    iv = Interval.ofClosedOpen(2, 42);
    assertEquals(2, iv.start());
    assertTrue(iv.isStartClosed());
    assertEquals(42, iv.end());
    assertFalse(iv.isEndClosed());

    iv = Interval.ofClosedClosed(2, 42);
    assertEquals(2, iv.start());
    assertTrue(iv.isStartClosed());
    assertEquals(42, iv.end());
    assertTrue(iv.isEndClosed());
  }

  @Test public void testIntervalOfOpenOpen() {
    Interval iv1 = Interval.ofOpenOpen(2, 42);

    Interval iv2 = Interval.ofClosedOpen(2, 42);
    assertNotEquals(iv1, iv2);

    iv2 = Interval.ofOpenClosed(2, 42);
    assertNotEquals(iv1, iv2);

    iv2 = Interval.ofClosedClosed(2, 42);
    assertNotEquals(iv1, iv2);
  }

  @Test public void testIntervalOpenClosed() {
    Interval iv1 = Interval.ofOpenClosed(2, 42);

    Interval iv2 = Interval.ofClosedOpen(2, 42);
    assertNotEquals(iv1, iv2);

    iv2 = Interval.ofOpenOpen(2, 42);
    assertNotEquals(iv1, iv2);

    iv2 = Interval.ofClosedClosed(2, 42);
    assertNotEquals(iv1, iv2);
  }

  @Test public void testIntervalClosedOpen() {
    Interval iv1 = Interval.ofClosedOpen(2, 42);

    Interval iv2 = Interval.ofOpenOpen(2, 42);
    assertNotEquals(iv1, iv2);

    iv2 = Interval.ofOpenClosed(2, 42);
    assertNotEquals(iv1, iv2);

    iv2 = Interval.ofClosedClosed(2, 42);
    assertNotEquals(iv1, iv2);
  }

  @Test public void testIntervalClosedClosed() {
    Interval iv1 = Interval.ofClosedClosed(2, 42);

    Interval iv2 = Interval.ofClosedOpen(2, 42);
    assertNotEquals(iv1, iv2);

    iv2 = Interval.ofOpenOpen(2, 42);
    assertNotEquals(iv1, iv2);

    iv2 = Interval.ofOpenClosed(2, 42);
    assertNotEquals(iv1, iv2);
  }

  @Test public void testContains() {
    Interval iv = Interval.ofOpenOpen(2, 42);
    assertFalse(iv.contains(1));
    assertFalse(iv.contains(2));
    assertTrue(iv.contains(3));
    assertTrue(iv.contains(41));
    assertFalse(iv.contains(42));
    assertFalse(iv.contains(43));

    iv = Interval.ofClosedClosed(2, 42);
    assertFalse(iv.contains(1));
    assertTrue(iv.contains(2));
    assertTrue(iv.contains(3));
    assertTrue(iv.contains(41));
    assertTrue(iv.contains(42));
    assertFalse(iv.contains(43));

    iv = Interval.ofOpenClosed(2, 42);
    assertFalse(iv.contains(1));
    assertFalse(iv.contains(2));
    assertTrue(iv.contains(3));
    assertTrue(iv.contains(41));
    assertTrue(iv.contains(42));
    assertFalse(iv.contains(43));

    iv = Interval.ofClosedOpen(2, 42);
    assertFalse(iv.contains(1));
    assertTrue(iv.contains(2));
    assertTrue(iv.contains(3));
    assertTrue(iv.contains(41));
    assertFalse(iv.contains(42));
    assertFalse(iv.contains(43));
  }

  @Test public void testIsBefore() {
    Interval iv = Interval.ofOpenOpen(2, 42);
    assertFalse(iv.isBefore(1));
    assertFalse(iv.isBefore(2));
    assertFalse(iv.isBefore(3));
    assertFalse(iv.isBefore(41));
    assertTrue(iv.isBefore(42));
    assertTrue(iv.isBefore(43));

    iv = Interval.ofClosedClosed(2, 42);
    assertFalse(iv.isBefore(1));
    assertFalse(iv.isBefore(2));
    assertFalse(iv.isBefore(3));
    assertFalse(iv.isBefore(41));
    assertFalse(iv.isBefore(42));
    assertTrue(iv.isBefore(43));
  }

  @Test public void testIsAfter() {
    Interval iv = Interval.ofOpenOpen(2, 42);
    assertTrue(iv.isAfter(1));
    assertTrue(iv.isAfter(2));
    assertFalse(iv.isAfter(3));
    assertFalse(iv.isAfter(41));
    assertFalse(iv.isAfter(42));
    assertFalse(iv.isAfter(43));

    iv = Interval.ofClosedClosed(2, 42);
    assertTrue(iv.isAfter(1));
    assertFalse(iv.isAfter(2));
    assertFalse(iv.isAfter(3));
    assertFalse(iv.isAfter(41));
    assertFalse(iv.isAfter(42));
    assertFalse(iv.isAfter(43));
  }

  @Test public void testTranslate() {
    Interval iv = Interval.ofOpenOpen(2, 42);
    assertEquals(Interval.ofOpenOpen(5, 45), iv.translate(3));
    assertEquals(Interval.ofOpenOpen(1, 41), iv.translateNegative(1));
  }

  @Test public void testIsEmpty() {
    assertTrue(Interval.ofOpenOpen(0, 0).isEmpty());
    assertFalse(Interval.ofClosedClosed(0, 0).isEmpty());
    assertFalse(Interval.ofClosedOpen(0, 1).isEmpty());
    assertTrue(Interval.ofOpenClosed(1, 0).isEmpty());
  }

  @Test public void testIntersect() {
    assertEquals(Interval.ofClosedOpen(2, 3),
        Interval.ofOpenOpen(1, 3).intersect(Interval.ofClosedClosed(2, 4)));
    assertTrue(Interval.ofClosedOpen(1, 2).intersect(Interval.ofClosedClosed(2, 43)).isEmpty());
  }

  @Test public void testPrefix() {
    assertEquals(Interval.ofOpenOpen(1, 2),
        Interval.ofOpenOpen(1, 4).prefix(Interval.ofClosedClosed(2, 3)));
  }

  @Test public void testSuffix() {
    assertEquals(Interval.ofOpenOpen(3, 4),
        Interval.ofOpenOpen(1, 4).suffix(Interval.ofClosedClosed(2, 3)));
  }

  @Test public void testSize() {
    assertEquals(40, Interval.ofClosedOpen(2, 42).size());
    assertEquals(0, Interval.ofClosedOpen(1, 0).size());
  }

  @Test public void testUnion() {
    assertEquals(Interval.ofClosedClosed(1, 9),
        Interval.ofClosedOpen(1, 3).union(Interval.ofOpenClosed(7, 9)));
  }
}
