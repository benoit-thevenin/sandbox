package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode05 {

    public static class IntcodeComputer {
        private long index = 0;
        private long relativeBase = 0;
        final Map<Long, Long> program = new HashMap<>();
        boolean isHalted = false;

        IntcodeComputer(long[] program) {
            for (int i = 0; i < program.length; i++) this.program.put((long) i, program[i]);
        }

        long run(long parameter) {
            boolean parameterUsed = false;
            while (true) {
                long value0 = program.getOrDefault(index, 0L);
                long value1 = program.getOrDefault(index + 1, 0L);
                long value2 = program.getOrDefault(index + 2, 0L);
                long value3 = program.getOrDefault(index + 3, 0L);
                if (value0 % 100 == 99) {
                    isHalted = true;
                    return 0;
                }
                long opCode = value0 % 10;
                long first = getParameter(value0, value1, 1);
                long second = getParameter(value0, value2, 2);
                if (opCode == 1) {
                    long result = first + second;
                    if ((value0 / 10000) != 2) program.put(value3, result);
                    else program.put(value3 + relativeBase, result);
                    index += 4;
                } else if (opCode == 2) {
                    long result = first * second;
                    if ((value0 / 10000) != 2) program.put(value3, result);
                    else program.put(value3 + relativeBase, result);
                    index += 4;
                } else if (opCode == 3) {
                    if (parameterUsed) return -2;
                    parameterUsed = true;
                    if ((value0 / 100) != 2) program.put(value1, parameter);
                    else program.put(value1 + relativeBase, parameter);
                    index += 2;
                } else if (opCode == 4) {
                    index += 2;
                    return first;
                } else if (opCode == 5) {
                    if (first != 0) index = second;
                    else index += 3;
                } else if (opCode == 6) {
                    if (first == 0) index = second;
                    else index += 3;
                } else if (opCode == 7) {
                    long result = first < second ? 1L : 0;
                    if ((value0 / 10000) != 2) program.put(value3, result);
                    else program.put(value3 + relativeBase, result);
                    index += 4;
                } else if (opCode == 8) {
                    long result = first == second ? 1L : 0;
                    if ((value0 / 10000) != 2) program.put(value3, result);
                    else program.put(value3 + relativeBase, result);
                    index += 4;
                } else if (opCode == 9) {
                    relativeBase += first;
                    index += 2;
                } else return -1;
            }
        }

        private long getParameter(long value0, long value, int index) {
            int divisor = 10;
            for (int i = 0; i < index; i++) divisor *= 10;
            long mode = (value0 / divisor) % 10;
            if (mode == 0) return program.getOrDefault(value, 0L);
            return mode == 1 ? value : program.getOrDefault(relativeBase + value, 0L);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                long[] program = new long[split.length];
                for (int i = 0; i < split.length; i++) program[i] = Long.parseLong(split[i]);
                LOGGER.info("PART 1: {}", getResult(program, 1));
                LOGGER.info("PART 2: {}", getResult(program, 5));
            }
        }
    }

    private static long getResult(long[] program, int parameter) {
        IntcodeComputer computer = new IntcodeComputer(program);
        long result = computer.run(parameter);
        while (result == 0) result = computer.run(parameter);
        return result;
    }
}
