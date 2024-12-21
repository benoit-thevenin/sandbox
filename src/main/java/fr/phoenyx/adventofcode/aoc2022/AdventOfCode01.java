package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Integer> elves = new ArrayList<>();
            int calories = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) {
                    elves.add(calories);
                    calories = 0;
                } else calories += Integer.parseInt(currentLine);
            }
            LOGGER.info("PART 1: {}", elves.stream().max(Integer::compare).orElseThrow());
            LOGGER.info("PART 2: {}", elves.stream().sorted((e1, e2) -> Integer.compare(e2, e1)).limit(3).reduce(0, Integer::sum));
        }
    }
}
