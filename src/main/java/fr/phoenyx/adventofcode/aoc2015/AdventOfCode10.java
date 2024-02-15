package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode10 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                LOGGER.info("PART 1: {}", getLookAndSay(currentLine, 40).length());
                LOGGER.info("PART 2: {}", getLookAndSay(currentLine, 50).length());
            }
        }
    }

    private static String getLookAndSay(String line, int iterations) {
        String result = line;
        for (int i = 0; i < iterations; i++) result = getNextLookAndSay(result);
        return result;
    }

    private static String getNextLookAndSay(String s) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < s.length()) {
            char digit = s.charAt(index);
            int count = 0;
            while (index < s.length() && s.charAt(index) == digit) {
                index++;
                count++;
            }
            sb.append(count);
            sb.append(digit);
        }
        return sb.toString();
    }
}
