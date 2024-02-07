package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int totalInitialCharacters = 0;
            int totalInMemoryCharacters = 0;
            int totalEncodedCharacters = 0;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                totalInitialCharacters += currentLine.length();
                totalInMemoryCharacters += getInMemoryString(currentLine).length();
                totalEncodedCharacters += getEncodedString(currentLine).length();
            }
            LOGGER.info("PART 1: {}", totalInitialCharacters - totalInMemoryCharacters);
            LOGGER.info("PART 2: {}", totalEncodedCharacters - totalInitialCharacters);
        }
    }

    private static String getInMemoryString(String encodedString) {
        String inMemoryString = encodedString.substring(1, encodedString.length() - 1);
        inMemoryString = inMemoryString.replaceAll("\\\\x[0-9a-f]{2}", "?");
        inMemoryString = inMemoryString.replace("\\\"", "\"");
        inMemoryString = inMemoryString.replace("\\\\", "\\");
        return inMemoryString;
    }

    private static String getEncodedString(String s) {
        String encodedString = s.replace("\\", "\\\\");
        encodedString = encodedString.replace("\"", "\\\"");
        encodedString = "\"" + encodedString + "\"";
        return encodedString;
    }
}
