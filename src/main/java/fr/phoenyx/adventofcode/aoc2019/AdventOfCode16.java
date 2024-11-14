package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode16 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                LOGGER.info("PART 1: {}", applyFft(currentLine));
                LOGGER.info("PART 2: {}", decodeMessage(currentLine));
            }
        }
    }

    private static String applyFft(String line) {
        int[] basePattern = new int[]{0, 1, 0, -1};
        String current = line;
        for (int phase = 0; phase < 100; phase++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= current.length(); i++) {
                int value = 0;
                for (int j = 0; j < current.length(); j++) {
                    int patternIndex = (j + 1) / i;
                    value += (current.charAt(j) - '0') * basePattern[patternIndex % basePattern.length];
                }
                sb.append(Math.abs(value) % 10);
            }
            current = sb.toString();
        }
        return current.substring(0, 8);
    }

    private static String decodeMessage(String message) {
        int offset = Integer.parseInt(message.substring(0, 7));
        String line = message.repeat(10000).substring(offset);
        for (int phase = 0; phase < 100; phase++) {
            int partialSum = Arrays.stream(line.split("")).map(c -> c.charAt(0) - '0').reduce(Integer::sum).orElseThrow();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                sb.append(Math.abs(partialSum) % 10);
                partialSum -= line.charAt(i) - '0';
            }
            line = sb.toString();
        }
        return line.substring(0, 8);
    }
}
