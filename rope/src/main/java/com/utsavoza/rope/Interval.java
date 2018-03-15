package com.utsavoza.rope;

public final class Interval {

  private final int start;
  private final int end;

  // start = 2 * actualValue + 1 if open:
  // end   = 2 * actualValue + 1 if closed
  private Interval(int start, boolean startInclusive, int end, boolean endInclusive) {
    this.start = start * 2 + (startInclusive ? 0 : 1);
    this.end = Math.max(end * 2 + (endInclusive ? 1 : 0), this.start);
  }

  /**
   * Obtains the instance of {@code Interval} from start and end.
   * <br>Symbolically: (start, end)
   *
   * @param start the start instant, exclusive
   * @param end the end instant, exclusive
   * @return the open interval, not null.
   */
  public static Interval ofOpenOpen(int start, int end) {
    return new Interval(start, false, end, false);
  }

  /**
   * Obtains the instance of {@code Interval} from start and end.
   * <br>Symbolically: (start, end]
   *
   * @param start the start instant, exclusive
   * @param end the end instant, inclusive
   * @return the half-closed interval, not null.
   */
  public static Interval ofOpenClosed(int start, int end) {
    return new Interval(start, false, end, true);
  }

  /**
   * Obtains the instance of {@code Interval} from start and end.
   * <br>Symbolically: [start, end)
   *
   * @param start the start instant, inclusive by default.
   * @param end the end instant, exclusive by default.
   * @return the half-closed interval, not null.
   */
  public static Interval ofClosedOpen(int start, int end) {
    return new Interval(start, true, end, false);
  }

  /**
   * Obtains the instance of {@code Interval} from start and end.
   * <br>Symbolically: [start, end]
   *
   * @param start the start instant, inclusive by default.
   * @param end the end instant, exclusive by default.
   * @return the closed interval, not null.
   */
  public static Interval ofClosedClosed(int start, int end) {
    return new Interval(start, true, end, true);
  }

  public boolean isStartClosed() {
    return (this.start & 1) == 0;
  }

  public boolean isEndClosed() {
    return (this.end & 1) != 0;
  }

  public boolean isEmpty() {
    return this.start >= this.end;
  }

  public boolean isBefore(int point) {
    int value = point * 2;
    return this.end <= value;
  }

  public boolean contains(int point) {
    int value = point * 2;
    return this.start <= value && value < this.end;
  }

  public boolean isAfter(int point) {
    int value = point * 2;
    return this.start > value;
  }

  public Interval intersect(Interval other) {
    int start = Math.max(this.start(), other.start());
    int end = Math.min(this.end(), other.end());
    int actualStart = Math.max(this.start, other.start);
    int actualEnd = Math.min(this.end, other.end);
    return new Interval(start, (actualStart & 1) == 0, Math.max(start, end), (actualEnd & 1) != 0);
  }

  public Interval union(Interval other) {
    if (this.isEmpty()) return other;
    if (other.isEmpty()) return this;
    int start = Math.min(this.start(), other.start());
    int end = Math.max(this.end(), other.end());
    int actualStart = Math.min(this.start, other.start);
    int actualEnd = Math.max(this.end, other.end);
    return new Interval(start, (actualStart & 1) == 0, end, (actualEnd & 1) != 0);
  }

  /* The first half of this - other */
  public Interval prefix(Interval other) {
    int start = Math.min(this.start(), other.start());
    int end = Math.min(this.end(), other.start());
    int actualStart = Math.min(this.start, other.start);
    int actualEnd = Math.min(this.end, other.start);
    return new Interval(start, (actualStart & 1) == 0, end, (actualEnd & 1) != 0);
  }

  /* The second half of this - other */
  public Interval suffix(Interval other) {
    int start = Math.max(this.start(), other.end());
    int end = Math.max(this.end(), other.end());
    int actualStart = Math.max(this.start, other.end);
    int actualEnd = Math.max(this.end, other.end);
    return new Interval(start, (actualStart & 1) == 0, end, (actualEnd & 1) != 0);
  }

  public Interval translate(int amount) {
    int start = this.start() + amount;
    int end = this.end() + amount;
    return new Interval(start, this.isStartClosed(), end, this.isEndClosed());
  }

  public Interval translateNegative(int amount) {
    int start = this.start() - amount;
    int end = this.end() - amount;
    return new Interval(start, this.isStartClosed(), end, this.isEndClosed());
  }

  /* Insensitive to open or closed ends, just the size of the interior. */
  public int size() {
    return this.end() - this.start();
  }

  public int start() {
    return this.start / 2;
  }

  public int end() {
    return this.end / 2;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Interval)) {
      return false;
    }
    Interval otherInterval = (Interval) o;
    return otherInterval.start == this.start
        && otherInterval.end == this.end;
  }

  @Override public int hashCode() {
    int hash = 17;
    hash = 31 * hash + this.start;
    hash = 31 * hash + this.end;
    return hash;
  }

  @Override public String toString() {
    StringBuilder sb = new StringBuilder("Interval: ");
    if (this.isStartClosed()) {
      sb.append("[");
    } else {
      sb.append("(");
    }
    sb.append(this.start());
    sb.append(", ");
    sb.append(this.end());
    if (this.isEndClosed()) {
      sb.append("]");
    } else {
      sb.append(")");
    }
    return sb.toString();
  }
}