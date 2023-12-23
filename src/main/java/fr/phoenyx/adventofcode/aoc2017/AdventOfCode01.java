package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int result1 = 0;
            int result2 = 0;
            while ((currentLine = reader.readLine()) != null) {
                int offset = currentLine.length() / 2;
                for (int i = 0; i < currentLine.length(); i++) {
                    char c = currentLine.charAt(i);
                    if (c == currentLine.charAt((i + 1) % currentLine.length())) result1 += c - '0';
                    if (c == currentLine.charAt((i + offset) % currentLine.length())) result2 += c - '0';
                }
            }
            LOGGER.info("PART 1: {}", result1);
            LOGGER.info("PART 2: {}", result2);
        }
    }
}
