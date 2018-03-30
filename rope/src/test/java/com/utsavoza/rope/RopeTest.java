package com.utsavoza.rope;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RopeTest {

  private String text =
            "MIT License\n"
          + "\n"
          + "Copyright (c) 2018 utsavoza\n"
          + "\n"
          + "Permission is hereby granted, free of charge, to any person obtaining a copy\n"
          + "of this software and associated documentation files (the \"Software\"), to deal\n"
          + "in the Software without restriction, including without limitation the rights\n"
          + "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"
          + "copies of the Software, and to permit persons to whom the Software is\n"
          + "furnished to do so, subject to the following conditions:\n"
          + "\n"
          + "The above copyright notice and this permission notice shall be included in all\n"
          + "copies or substantial portions of the Software.\n"
          + "\n"
          + "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"
          + "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"
          + "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"
          + "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"
          + "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"
          + "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n"
          + "SOFTWARE.";

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

    Rope license = Rope.from(text);
    Rope newLicense = license.replace(32, 40, "UTSAVOZA");
    assertEquals(license.toString(), text);

    String newText = text.substring(0, 32) + "UTSAVOZA" + text.substring(40, text.length());
    assertEquals(newLicense.toString(), newText);
    assertNotEquals(license, newLicense);
  }

  @Test public void testEquals() {
    Rope a = Rope.from("hello");
    Rope b = Rope.from("world");
    Rope c = Rope.from("hello");
    assertNotEquals(a, b);
    assertEquals(a, c);
  }

  @Test public void testSlice() {
    Rope a = Rope.from("hello world");
    Rope b = a.slice(1, 9);
    assertEquals(b.toString(), "ello wor");

    Rope license = Rope.from(text);
    Rope title = license.slice(0, 10);
    assertEquals(title.toString(), "MIT License");
  }
}
