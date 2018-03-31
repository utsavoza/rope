package com.utsavoza.rope;

import org.junit.Before;
import org.junit.Test;

import static com.utsavoza.rope.Util.readSampleFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RopeTest {

  private String text;

  @Before public void setup() {
    this.text = readSampleFile();
  }

  @Test public void testRopeFrom() {
    Rope a = Rope.from("hello");
    assertEquals(a.toString(), "hello");

    Rope b = Rope.from("world");
    assertEquals(b.toString(), "world");

    Rope license = Rope.from(text);
    assertEquals(license.toString(), text);
  }

  @Test public void testRopeBuilder() {
    Rope rope = new Rope.Builder()
        .pushString("<<")
        .pushRope(Rope.from("hello"))
        .pushString(">>")
        .build();
    assertEquals(rope.toString(), "<<hello>>");

    Rope license = new Rope.Builder()
        .pushString(text)
        .build();
    assertEquals(license.toString(), text);
  }

  @Test public void testConcat() {
    Rope a = Rope.from("hello");
    Rope b = Rope.from(" world");
    Rope c = a.concat(b);
    assertEquals(c.toString(), "hello world");
    assertEquals(a.toString(), "hello");
    assertEquals(b.toString(), " world");

    Rope license = Rope.from(text);
    Rope newLicense = license.concat(Rope.from("=>> MIT LICENSE"));
    assertEquals(license.toString(), text);
    assertEquals(newLicense.toString(), text + "=>> MIT LICENSE");
    assertNotEquals(license, newLicense);
  }

  @Test public void testReplace() {
    Rope a = Rope.from("hello world");
    Rope b = a.replace(1, 9, "era");
    assertEquals(b.toString(), "herald");
    assertEquals(a.toString(), "hello world");
    assertNotEquals(a, b);

    Rope readme = Rope.from(text);
    Rope newLicense = readme.replace(32, 40, "UTSAVOZA");
    assertEquals(readme.toString(), text);

    String newText = text.substring(0, 32) + "UTSAVOZA" + text.substring(40, text.length());
    assertEquals(newLicense.toString(), newText);
    assertNotEquals(readme, newLicense);
  }

  @Test public void testSlice() {
    Rope a = Rope.from("hello world");
    Rope b = a.slice(1, 9);
    assertEquals(b.toString(), "ello wo");
    assertNotEquals(a, b);

    Rope readme = Rope.from(text);
    Rope title = readme.slice(0, 4);
    assertEquals(title.toString(), "Rope");
    assertNotEquals(title, readme);
  }

  @Test public void testEquals() {
    Rope a = Rope.from("hello");
    Rope b = Rope.from("world");
    Rope c = Rope.from("hello");
    assertNotEquals(a, b);
    assertEquals(a, c);
  }
}
