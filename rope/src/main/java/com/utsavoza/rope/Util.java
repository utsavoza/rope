package com.utsavoza.rope;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.utsavoza.rope.Node.MAX_LEAF;
import static com.utsavoza.rope.Node.MIN_LEAF;

/** Junk drawer of utility methods. */
final class Util {

  static final String NEW_LINE = "\r\n|\r|\n";

  private Util() {
    throw new AssertionError("no instances");
  }

  static int countOccurrence(String s, String pattern) {
    Matcher matcher = Pattern.compile(pattern).matcher(s);
    int newLine = 0;
    while (matcher.find()) {
      newLine++;
    }
    return newLine;
  }

  static boolean isCharBoundary(String s, int index) {
    return index == 0 || index == s.length() || s.getBytes()[index] >= -0x40;
  }

  static String readSampleFile() {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader("../README.md"))) {
      String text;
      while ((text = br.readLine()) != null) {
        sb.append(text);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  static int findLeafSplitForMerge(String s) {
    return findLeafSplit(s, Math.max(MIN_LEAF, s.length() - MAX_LEAF));
  }

  static int findLeafSplitForBulk(String s) {
    return findLeafSplit(s, MIN_LEAF);
  }

  private static int findLeafSplit(String s, int minSplit) {
    int splitPoint = Math.min(MAX_LEAF, s.length() - MIN_LEAF);
    int newlineCharIndex = s.substring(minSplit - 1, splitPoint).lastIndexOf('\n');
    if (newlineCharIndex != -1) {
      return minSplit + newlineCharIndex;
    } else {
      while (!isCharBoundary(s, splitPoint)) {
        splitPoint -= 1;
      }
      return splitPoint;
    }
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
