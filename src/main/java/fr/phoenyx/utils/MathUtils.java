package fr.phoenyx.utils;

public class MathUtils {

    private MathUtils() {
        // Not meant to be instanciated
    }

    public static long greatestCommonDivisor(long a, long b) {
        if (a == 0 && b == 0) return 1;
        return a == 0 ? b : greatestCommonDivisor(b % a, a);
    }

    public static long leastCommonMultiple(long a, long b) {
        return a * b / greatestCommonDivisor(a, b);
    }
}
