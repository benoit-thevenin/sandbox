package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int result1 = 0;
            int result2 = 0;
            while ((currentLine = reader.readLine()) != null) {
                int moduleMass = Integer.parseInt(currentLine);
                result1 += moduleMass / 3 - 2;
                result2 += getFuelRequirements(moduleMass);
            }
            LOGGER.info("PART 1: {}", result1);
            LOGGER.info("PART 2: {}", result2);
        }
    }

    private static int getFuelRequirements(int mass) {
        int result = 0;
        int toAdd = mass / 3 - 2;
        while (toAdd > 0) {
            result += toAdd;
            mass = toAdd;
            toAdd = mass / 3 - 2;
        }
        return result;
    }
}
