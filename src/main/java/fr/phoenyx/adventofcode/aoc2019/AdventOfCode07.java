package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.adventofcode.aoc2019.AdventOfCode05.IntcodeComputer;
import fr.phoenyx.utils.MathUtils;

public class AdventOfCode07 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            long[] program = new long[0];
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                program = new long[split.length];
                for (int i = 0; i < split.length; i++) program[i] = Long.parseLong(split[i]);
            }
            LOGGER.info("PART 1: {}", getMaximumThrustersOutput(program));
            LOGGER.info("PART 2: {}", getMaximumThrustersOutputWithFeedbackLoop(program));
        }
    }

    private static long getMaximumThrustersOutput(long[] program) {
        IntcodeComputer[] amplifiers = new IntcodeComputer[5];
        long maxOutput = Long.MIN_VALUE;
        List<int[]> permutations = MathUtils.getAllPermutations(new int[]{0, 1, 2, 3, 4});
        for (int i = 0; i < permutations.size() - 1; i++) {
            long current = 0;
            int[] phases = permutations.get(i);
            for (int j = 0; j < 5; j++) {
                amplifiers[j] = new IntcodeComputer(program);
                amplifiers[j].run(phases[j]);
                current = amplifiers[j].run(current);
            }
            if (current > maxOutput) maxOutput = current;
        }
        return maxOutput;
    }

    private static long getMaximumThrustersOutputWithFeedbackLoop(long[] program) {
        IntcodeComputer[] amplifiers = new IntcodeComputer[5];
        long maxOutput = Long.MIN_VALUE;
        List<int[]> permutations = MathUtils.getAllPermutations(new int[]{5, 6, 7, 8, 9});
        for (int i = 0; i < permutations.size() - 1; i++) {
            int[] phases = permutations.get(i);
            for (int j = 0; j < 5; j++) {
                amplifiers[j] = new IntcodeComputer(program);
                amplifiers[j].run(phases[j]);
            }
            long current = 0;
            while (true) {
                long save = current;
                current = amplifiers[0].run(current);
                if (current == 0) {
                    current = save;
                    break;
                }
                for (int j = 1; j < 5; j++) {
                    current = amplifiers[j].run(current);
                }
            }
            if (current > maxOutput) maxOutput = current;
        }
        return maxOutput;
    }
}
