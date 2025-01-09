package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                int[] program = new int[split.length];
                for (int i = 0; i < split.length; i++) program[i] = Integer.parseInt(split[i]);
                LOGGER.info("PART 1: {}", run(program, 12, 2));
                LOGGER.info("PART 2: {}", getNounAndVerb(program));
            }
        }
    }

    private static int run(int[] program, int noun, int verb) {
        int[] save = new int[program.length];
        System.arraycopy(program, 0, save, 0, program.length);
        program[1] = noun;
        program[2] = verb;
        int index = 0;
        while (true) {
            if (program[index] == 99) {
                int result = program[0];
                System.arraycopy(save, 0, program, 0, program.length);
                return result;
            }
            if (program[index] == 1) program[program[index + 3]] = program[program[index + 1]] + program[program[index + 2]];
            else if (program[index] == 2) program[program[index + 3]] = program[program[index + 1]] * program[program[index + 2]];
            else return -1;
            index += 4;
        }
    }

    private static int getNounAndVerb(int[] program) {
        for (int noun = 0; noun < 100; noun++) {
            for (int verb = 0; verb < 100; verb++) {
                int result = run(program, noun, verb);
                if (result == 19690720) return 100 * noun + verb;
            }
        }
        throw new IllegalArgumentException("Not found");
    }
}
