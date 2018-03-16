package com.utsavoza.rope;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Junk drawer of utility methods. */
final class Util {

  static final String NEW_LINE = "\r\n|\r|\n";

  private Util() {
    /* no instances */
  }

  static int countNewLines(String s) {
    Matcher matcher = Pattern.compile(NEW_LINE).matcher(s);
    int newLine = 0;
    while (matcher.find()) {
      newLine++;
    }
    return newLine;
  }

  static boolean isCharBoundary(String s, int index) {
    return index == 0 || index == s.length() || s.getBytes()[index] >= -0x40;
  }

  static Ordering compare(int num1, int num2) {
    if (num1 < num2) {
      return Ordering.LESS;
    } else if (num1 == num2) {
      return Ordering.EQUAL;
    } else {
      return Ordering.GREATER;
    }
  }

  enum Ordering {
    LESS,
    EQUAL,
    GREATER,
  }
}
