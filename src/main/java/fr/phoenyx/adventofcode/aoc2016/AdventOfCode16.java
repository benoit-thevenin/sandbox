package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode16 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                LOGGER.info("PART 1: {}", getResult(currentLine, 272));
                LOGGER.info("PART 2: {}", getResult(currentLine, 35651584));
            }
        }
    }

    private static String getResult(String initialState, int length) {
        String current = initialState;
        while (current.length() < length) current = dragonCurveFold(current);
        return getChecksum(current, length);
    }

    private static String dragonCurveFold(String input) {
        StringBuilder sb = new StringBuilder(input);
        sb.append('0');
        for (int i = input.length() - 1; i >= 0; i--) sb.append(input.charAt(i) == '0' ? '1' : '0');
        return sb.toString();
    }

    private static String getChecksum(String input, int length) {
        String checksum = input.substring(0, length);
        while (checksum.length() % 2 == 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < checksum.length(); i += 2) sb.append(checksum.charAt(i) == checksum.charAt(i + 1) ? '1' : '0');
            checksum = sb.toString();
        }
        return checksum;
    }
}
