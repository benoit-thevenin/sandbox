package fr.phoenyx.models;

import java.util.Objects;
import java.util.Optional;

/**
 * An object representing a range of values
 */
public class Range {

    /**
     * The start value of the range, included
     */
    public final long start;
    /**
     * The end of the range, excluded
     */
    public final long end;
    /**
     * The length of the range, positive
     */
    public final long length;

    private Range(long start, long length) {
        this.start = start;
        this.length = length;
        this.end = start + length;
    }

    public static Range buildFromStartAndLength(long start, long length) {
        if (length < 0) throw new IllegalArgumentException("Range's length can't be negative");
        return new Range(start, length);
    }

    public static Range buildFromStartAndLength(String line) {
        String[] split = line.split("[ -]");
        long start = Long.parseLong(split[0]);
        long length = Long.parseLong(split[1]);
        return buildFromStartAndLength(start, length);
    }

    public static Range buildFromStartAndEnd(long start, long end) {
        if (end < start) throw new IllegalArgumentException("Range's length can't be negative");
        return new Range(start, end - start);
    }

    public static Range buildFromStartAndEnd(String line) {
        String[] split = line.split("[ -]");
        long start = Long.parseLong(split[0]);
        long end = Long.parseLong(split[1]);
        return buildFromStartAndEnd(start, end);
    }

    public static Range buildFromStartAndEndInclusive(long start, long endInclusive) {
        return buildFromStartAndEnd(start, endInclusive + 1);
    }

    public static Range buildFromStartAndEndInclusive(String line) {
        String[] split = line.split("[ -]");
        long start = Long.parseLong(split[0]);
        long end = Long.parseLong(split[1]);
        return buildFromStartAndEndInclusive(start, end);
    }

    public boolean isInRange(long value) {
        return value >= start && value < end;
    }

    public Optional<Range> intersection(Range other) {
        if (start >= other.end || end <= other.start) return Optional.empty();
        long intersectionStart = Math.max(start, other.start);
        long intersectionEnd = Math.min(end, other.end);
        return Optional.of(Range.buildFromStartAndEnd(intersectionStart, intersectionEnd));
    }

    public Range union(Range other) {
        if (start > other.end || end < other.start) throw new IllegalArgumentException("Ranges can't be united if they don't intersect");
        return Range.buildFromStartAndEnd(Math.min(start, other.start), Math.max(end, other.end));
    }

    public boolean contains(Range other) {
        Optional<Range> intersection = intersection(other);
        return intersection.isPresent() && intersection.get().equals(other);
    }

    public boolean isContainedBy(Range other) {
        return other.contains(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Range other)) return false;
        return start == other.start && end == other.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
