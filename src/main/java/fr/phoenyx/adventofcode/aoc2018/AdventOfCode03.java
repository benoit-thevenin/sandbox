package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // TODO do something
            }
            LOGGER.info("PART 1: {}", 0);
            LOGGER.info("PART 2: {}", 0);
        }
    }
}
