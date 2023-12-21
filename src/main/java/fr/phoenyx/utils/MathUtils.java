package fr.phoenyx.utils;

import java.util.List;
import java.util.Optional;

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

    /**
     * @param inputs a list of integers to analyze
     * @return an Optional containing an integer (if found) that represents the least size of a sequence that repeats inside inputs from the end
     * examples:
     *  - [1, 2, 3, 4] -> Optional.empty() (no repetition at all)
     *  - [1, 2, 3, 3] -> Optional.of(1) (the last integer repeats)
     *  - [1, 2, 1, 2] -> Optional.of(2) (the two last integers repeats)
     */
    public static Optional<Integer> getFrequency(List<Integer> inputs) {
        int middleIndex = inputs.size() / 2 + 1; // middleIndex is the maximum frequency that can be found given inputs
        for (int i = 1; i < middleIndex; i++) {
            boolean found = true;
            for (int j = inputs.size() - 1; j >= inputs.size() - i; j--) {
                if (!inputs.get(j).equals(inputs.get(j - i))) {
                    found = false;
                    break;
                }
            }
            if (found) return Optional.of(i);
        }
        return Optional.empty();
    }
}
