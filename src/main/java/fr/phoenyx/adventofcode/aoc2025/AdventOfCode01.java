package fr.phoenyx.adventofcode.aoc2025;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int dial = 50;
            int count1 = 0;
            int count2 = 0;
            while ((currentLine = reader.readLine()) != null) {
                boolean isStartZero = dial == 0;
                int rotation = Integer.parseInt(currentLine.substring(1));
                dial += currentLine.charAt(0) == 'L' ? -rotation : rotation;
                count2 += Math.abs(dial) / 100;
                if (dial <= 0 && !isStartZero) count2++;
                dial %= 100;
                if (dial < 0) dial += 100;
                if (dial == 0) count1++;
            }
            LOGGER.info("PART 1: {}", count1);
            LOGGER.info("PART 2: {}", count2);
        }
    }
}
