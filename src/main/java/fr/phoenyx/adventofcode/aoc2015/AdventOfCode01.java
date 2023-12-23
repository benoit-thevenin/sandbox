package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int result1 = 0;
            int result2 = -1;
            while ((currentLine = reader.readLine()) != null) {
                for (int i = 0; i < currentLine.length(); i++) {
                    if (currentLine.charAt(i) == '(') result1++;
                    else result1--;
                    if (result2 == -1 && result1 < 0) result2 = i + 1;
                }
            }
            LOGGER.info("PART 1: {}", result1);
            LOGGER.info("PART 2: {}", result2);
        }
    }
}
