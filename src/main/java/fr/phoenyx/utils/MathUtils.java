package fr.phoenyx.utils;

import java.util.ArrayList;
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

    public static double getDeterminant(double[][] matrix) {
        if (matrix.length < 2 || matrix.length != matrix[0].length) throw new IllegalArgumentException("Only works on square matrix");
        if (matrix.length == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        if (matrix.length == 3)
            return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
            - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[2][0] * matrix[1][2])
            + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[2][0] * matrix[1][1]);
        double determinant = 0;
        for (int i = 0; i < matrix.length; i++) {
            double ai = i % 2 == 0 ? matrix[0][i] : -matrix[0][i];
            double[][] cofactor = new double[matrix.length - 1][matrix.length - 1];
            for (int k = 0; k < cofactor.length; k++) {
                for (int l = 0; l < cofactor.length; l++) {
                    cofactor[k][l] = matrix[k + 1][l < i ? l : l + 1];
                }
            }
            determinant += ai * getDeterminant(cofactor);
        }
        return determinant;
    }

    public static List<int[]> getAllPermutations(int[] values) {
        List<int[]> permutations = new ArrayList<>();
        computePermutations(values.length, values, permutations);
        return permutations;
    }

    private static void computePermutations(int n, int[] values, List<int[]> permutations) {
        if (n == 1) {
            int[] next = new int[values.length];
            System.arraycopy(values, 0, next, 0, values.length);
            permutations.add(next);
        } else {
            for (int i = 0; i < n - 1; i++) {
                computePermutations(n - 1, values, permutations);
                if (n % 2 == 0) swap(values, i, n - 1);
                else swap(values, 0, n - 1);
            }
            computePermutations(n - 1, values, permutations);
        }
    }

    private static void swap(int[] values, int index1, int index2) {
        int tmp = values[index1];
        values[index1] = values[index2];
        values[index2] = tmp;
    }
}
