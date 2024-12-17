package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdventOfCode17 {

    private static class Computer {
        long[] registers = new long[3];
        int[] program = new int[0];

        String run() {
            List<Integer> out = new ArrayList<>();
            int index = 0;
            while (index < program.length) {
                int opcode = program[index];
                int operand = program[index + 1];
                index += 2;
                if (opcode == 0) registers[0] = divide(operand);
                else if (opcode == 1) registers[1] ^= operand;
                else if (opcode == 2) registers[1] = getComboOperand(operand) % 8;
                else if (opcode == 3 && registers[0] != 0) index = operand;
                else if (opcode == 4) registers[1] ^= registers[2];
                else if (opcode == 5) out.add((int) (getComboOperand(operand) % 8));
                else if (opcode == 6) registers[1] = divide(operand);
                else if (opcode == 7) registers[2] = divide(operand);
            }
            return out.stream().map(Object::toString).collect(Collectors.joining(","));
        }

        long divide(int operand) {
            return registers[0] / (long) Math.pow(2, getComboOperand(operand));
        }

        long getComboOperand(int i) {
            return i < 4 ? i : registers[i - 4];
        }

        long getValidARegister() {
            return getValidARegister(0, program.length - 1);
        }

        long getValidARegister(long a, int i) {
            if (i < 0) return a;
            for (long test = a * 8; test < a * 8 + 8; test++) {
                registers[0] = test;
                if (run().charAt(0) - '0' == program[i]) {
                    long next = getValidARegister(test, i - 1);
                    if (next >= 0) return next;
                }
            }
            return -1;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode17.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode17.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Computer computer = new Computer();
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("Register"))
                    computer.registers[currentLine.charAt(9) - 'A'] = Long.parseLong(currentLine.split(": ")[1]);
                else if (currentLine.startsWith("Program")) {
                    String[] split = currentLine.split(": ")[1].split(",");
                    computer.program = new int[split.length];
                    for (int i = 0; i < split.length; i++) computer.program[i] = Integer.parseInt(split[i]);
                }
            }
            LOGGER.info("PART 1: {}", computer.run());
            LOGGER.info("PART 2: {}", computer.getValidARegister());
        }
    }
}
