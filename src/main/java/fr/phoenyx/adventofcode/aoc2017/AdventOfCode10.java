package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode10 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                LOGGER.info("PART 1: {}", getHashCheck(currentLine));
                LOGGER.info("PART 2: {}", getKnotHash(currentLine));
            }
        }
    }

    private static int getHashCheck(String line) {
        List<Integer> lengths = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
        int[] values = new int[256];
        for (int i = 0; i < values.length; i++) values[i] = i;
        int skip = 0;
        int index = 0;
        for (int length : lengths) {
            values = knot(values, index, length);
            index += length + skip;
            index = index % values.length;
            skip++;
        }
        return values[0] * values[1];
    }

    private static String getKnotHash(String line) {
        List<Integer> lengths = new ArrayList<>();
        for (char c : line.toCharArray()) lengths.add((int) c);
        lengths.addAll(List.of(17, 31, 73, 47, 23));
        int[] values = new int[256];
        for (int i = 0; i < values.length; i++) values[i] = i;
        int skip = 0;
        int index = 0;
        for (int iteration = 0; iteration < 64; iteration++) {
            for (int length : lengths) {
                values = knot(values, index, length);
                index += length + skip;
                index = index % values.length;
                skip++;
            }
        }
        return getHexRepresentation(values);
    }

    private static int[] knot(int[] values, int index, int length) {
        int[] next = new int[values.length];
        System.arraycopy(values, 0, next, 0, values.length);
        for (int i = 0; i < length; i++) {
            next[(index + i) % values.length] = values[(index + length - i - 1) % values.length];
        }
        return next;
    }

    private static String getHexRepresentation(int[] values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int value = values[16 * i];
            for (int j = 1; j < 16; j++) value ^= values[16 * i + j];
            String hexString = Integer.toHexString(value);
            if (hexString.length() == 1) sb.append("0");
            sb.append(hexString);
        }
        return sb.toString();
    }
}
