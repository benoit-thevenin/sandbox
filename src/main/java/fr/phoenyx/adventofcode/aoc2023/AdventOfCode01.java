package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);
    private static final Map<String, Character> WORD_TO_DIGIT_MAP = new HashMap<>();

    static {
        WORD_TO_DIGIT_MAP.put("one", '1');
        WORD_TO_DIGIT_MAP.put("two", '2');
        WORD_TO_DIGIT_MAP.put("three", '3');
        WORD_TO_DIGIT_MAP.put("four", '4');
        WORD_TO_DIGIT_MAP.put("five", '5');
        WORD_TO_DIGIT_MAP.put("six", '6');
        WORD_TO_DIGIT_MAP.put("seven", '7');
        WORD_TO_DIGIT_MAP.put("eight", '8');
        WORD_TO_DIGIT_MAP.put("nine", '9');
    }

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            LOGGER.info("PART 1: {}", lines.stream().map(line -> computeSubResult(line, true)).reduce(0, Integer::sum));
            LOGGER.info("PART 2: {}", lines.stream().map(line -> computeSubResult(line, false)).reduce(0, Integer::sum));
        }
    }

    private static int computeSubResult(String line, boolean part1) {
        return Integer.parseInt("" + getFirstDigit(line, part1) + getLastDigit(line, part1));
    }

    private static char getFirstDigit(String line, boolean part1) {
        for (int i = 0; i < line.length(); i++) {
            if (Character.isDigit(line.charAt(i))) return line.charAt(i);
            if (part1) continue;
            for (Map.Entry<String, Character> entry : WORD_TO_DIGIT_MAP.entrySet()) {
                int wordLength = entry.getKey().length();
                if (i + wordLength <= line.length() && line.substring(i, i + wordLength).equals(entry.getKey())) return entry.getValue();
            }
        }
        throw new IllegalArgumentException("The line must contain at least one digit");
    }

    private static char getLastDigit(String line, boolean part1) {
        for (int i = line.length() - 1; i >= 0; i--) {
            if (Character.isDigit(line.charAt(i))) return line.charAt(i);
            if (part1) continue;
            for (Map.Entry<String, Character> entry : WORD_TO_DIGIT_MAP.entrySet()) {
                int wordLength = entry.getKey().length();
                if (i - wordLength + 1 >= 0 && line.substring(i - wordLength + 1, i + 1).equals(entry.getKey())) return entry.getValue();
            }
        }
        throw new IllegalArgumentException("The line must contain at least one digit");
    }
}
