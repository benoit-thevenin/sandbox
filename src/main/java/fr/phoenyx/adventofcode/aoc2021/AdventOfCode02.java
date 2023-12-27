package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int horizontalPosition = 0;
            int depth = 0;
            int trueDepth = 0;
            int aim = 0;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" ");
                int value = Integer.parseInt(split[1]);
                if ("down".equals(split[0])) {
                    aim += value;
                    depth += value;
                } else if ("up".equals(split[0])) {
                    aim -= value;
                    depth -= value;
                } else {
                    horizontalPosition += value;
                    trueDepth += aim * value;
                }
            }
            LOGGER.info("PART 1: {}", horizontalPosition * depth);
            LOGGER.info("PART 2: {}", horizontalPosition * trueDepth);
        }
    }
}
