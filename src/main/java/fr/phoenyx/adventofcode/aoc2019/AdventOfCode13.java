package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.adventofcode.aoc2019.AdventOfCode05.IntcodeComputer;

public class AdventOfCode13 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                long[] program = new long[split.length];
                for (int i = 0; i < split.length; i++) program[i] = Long.parseLong(split[i]);
                LOGGER.info("PART 1: {}", countBlocks(program));
                LOGGER.info("PART 2: {}", getHighestScore(program));
            }
        }
    }

    private static int countBlocks(long[] program) {
        int blockCount = 0;
        IntcodeComputer computer = new IntcodeComputer(program);
        while (true) {
            computer.run(0);
            computer.run(0);
            long type = computer.run(0);
            if (computer.isHalted) return blockCount;
            if (type == 2) blockCount++;
        }
    }

    private static long getHighestScore(long[] program) {
        program[0] = 2;
        long score = 0;
        IntcodeComputer computer = new IntcodeComputer(program);
        long paddleX = 0;
        long ballX = 0;
        while (true) {
            long input = 0;
            if (ballX < paddleX) input = -1;
            else if (ballX > paddleX) input = 1;
            long x = computer.run(input);
            computer.run(input);
            long type = computer.run(input);
            if (computer.isHalted) return score;
            if (x == -1) score = type;
            else if (type == 3) paddleX = x;
            else if (type == 4) ballX = x;
        }
    }
}
