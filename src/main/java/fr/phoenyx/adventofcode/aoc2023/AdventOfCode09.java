package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);
    private static final List<int[]> values = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" ");
                int[] history = new int[split.length];
                for (int i = 0; i < split.length; i++) history[i] = Integer.parseInt(split[i]);
                values.add(history);
            }
            Map.Entry<Integer, Integer> result = computeResult();
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Map.Entry<Integer, Integer> computeResult() {
        int resultPart1 = 0;
        int resultPart2 = 0;
        for (int[] history : values) {
            int[] current = history;
            List<int[]> sequences = new ArrayList<>();
            sequences.add(history);
            boolean allZeroes = false;
            while (!allZeroes) {
                allZeroes = true;
                int[] sequence = new int[current.length - 1];
                for (int i = 0; i < sequence.length; i++) {
                    sequence[i] = current[i + 1] - current[i];
                    if (sequence[i] != 0) allZeroes = false;
                }
                current = sequence;
                sequences.add(sequence);
            }
            int nextValue = 0;
            int previousValue = 0;
            for (int i = sequences.size() - 2; i >= 0; i--) {
                int[] sequence = sequences.get(i);
                nextValue += sequence[sequence.length - 1];
                previousValue = sequence[0] - previousValue;
            }
            resultPart1 += nextValue;
            resultPart2 += previousValue;
        }
        return new SimpleEntry<>(resultPart1, resultPart2);
    }
}
