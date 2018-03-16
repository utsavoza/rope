package com.utsavoza.rope;

/** Junk drawer of utility methods. */
final class Util {

  static final String NEW_LINE = "\r\n|\r|\n";

  private Util() {
    /* no instances */
  }

  static int countNewLines(String s) {
    return s.split(NEW_LINE).length;
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
