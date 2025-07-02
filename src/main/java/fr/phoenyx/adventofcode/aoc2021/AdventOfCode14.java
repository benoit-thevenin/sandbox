package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode14 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);
    private static final Map<String, String> rules = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            String polymer = "";
            while ((currentLine = reader.readLine()) != null) {
                if (polymer.isEmpty()) polymer = currentLine;
                else if (!currentLine.isBlank()){
                    String[] split = currentLine.split(" -> ");
                    rules.put(split[0], split[1]);
                }
            }
            LOGGER.info("PART 1: {}", polymerize(polymer, 10));
            LOGGER.info("PART 2: {}", polymerize(polymer, 40));
        }
    }

    private static long polymerize(String polymer, int steps) {
        Map<String, Long> pairs = new HashMap<>();
        Map<String, Long> singles = new HashMap<>();
        Utils.getLetterCount(polymer).forEach((key, value) -> singles.put(key.toString(), value.longValue()));
        for (int i = 0; i < polymer.length() - 1; i++) {
            String pair = polymer.charAt(i) + "" + polymer.charAt(i + 1);
            pairs.put(pair, pairs.getOrDefault(pair, 0L) + 1);
        }
        for (int i = 0; i < steps; i++) {
            Map<String, Long> nextPairs = new HashMap<>();
            for (Entry<String, Long> pair : pairs.entrySet()) {
                if (rules.containsKey(pair.getKey())) {
                    String rule = rules.get(pair.getKey());
                    singles.put(rule, singles.getOrDefault(rule, 0L) + pair.getValue());
                    String leftPair = pair.getKey().charAt(0) + rule;
                    String rightPair = rule + pair.getKey().charAt(1);
                    nextPairs.put(leftPair, nextPairs.getOrDefault(leftPair, 0L) + pair.getValue());
                    nextPairs.put(rightPair, nextPairs.getOrDefault(rightPair, 0L) + pair.getValue());
                }
            }
            pairs = nextPairs;
        }
        return singles.values().stream().max(Long::compare).orElseThrow() - singles.values().stream().min(Long::compare).orElseThrow();
    }
}
