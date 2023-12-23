package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static class Elf {
        int calories;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Elf> elves = new ArrayList<>();
            Elf currentElf = new Elf();
            elves.add(currentElf);
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) {
                    currentElf = new Elf();
                    elves.add(currentElf);
                }
                else currentElf.calories += Integer.parseInt(currentLine);
            }
            LOGGER.info("PART 1: {}", elves.stream().max(Comparator.comparingInt(e -> e.calories)).orElseThrow().calories);
            LOGGER.info("PART 2: {}", elves.stream().sorted((e1, e2) -> Integer.compare(e2.calories, e1.calories)).limit(3).map(e -> e.calories).reduce(Integer::sum).orElseThrow());
        }
    }
}
