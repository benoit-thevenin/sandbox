package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.adventofcode.aoc2019.AdventOfCode05.IntcodeComputer;

public class AdventOfCode09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                long[] program = new long[split.length];
                for (int i = 0; i < split.length; i++) program[i] = Long.parseLong(split[i]);
                LOGGER.info("PART 1: {}", new IntcodeComputer(program).run(1));
                LOGGER.info("PART 2: {}", new IntcodeComputer(program).run(2));
            }
        }
    }
}
