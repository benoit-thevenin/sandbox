package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode06 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String message = "";
            String currentLine;
            while ((currentLine = reader.readLine()) != null) message = currentLine;
            LOGGER.info("PART 1: {}", getMarkerPosition(message, 4));
            LOGGER.info("PART 2: {}", getMarkerPosition(message, 14));
        }
    }

    private static int getMarkerPosition(String message, int length) {
        for (int i = 0; i < message.length() - length; i++) {
            if (Utils.getLetterCount(message.substring(i, i + length)).values().stream().allMatch(v -> v == 1)) return i + length;
        }
        throw new IllegalArgumentException("Invalid message");
    }
}
