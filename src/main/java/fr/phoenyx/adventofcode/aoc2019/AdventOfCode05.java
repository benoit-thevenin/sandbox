package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode05 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int[] program = new int[0];
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                program = new int[split.length];
                for (int i = 0; i < split.length; i++) program[i] = Integer.parseInt(split[i]);
            }
            LOGGER.info("PART 1: {}", run(program, 1));
            LOGGER.info("PART 2: {}", run(program, 5));
        }
    }

    private static int run(int[] program, int parameter) {
        List<Integer> outputs = new ArrayList<>();
        int[] save = new int[program.length];
        System.arraycopy(program, 0, save, 0, program.length);
        int index = 0;
        while (true) {
            if (program[index] % 100 == 99) {
                System.arraycopy(save, 0, program, 0, program.length);
                return outputs.get(outputs.size() - 1);
            }
            int opCode = program[index] % 10;
            int first = (program[index] / 100) % 10 == 0 ? program[program[index + 1]] : program[index + 1];
            int second = 0;
            if (opCode != 3 && opCode != 4) second = (program[index] / 1000) % 10 == 0 ? program[program[index + 2]] : program[index + 2];
            if (opCode == 1) {
                program[program[index + 3]] = first + second;
                index += 4;
            } else if (opCode == 2) {
                program[program[index + 3]] = first * second;
                index += 4;
            } else if (opCode == 3) {
                program[program[index + 1]] = parameter;
                index += 2;
            } else if (opCode == 4) {
                outputs.add(first);
                index += 2;
            } else if (opCode == 5) {
                if (first != 0) index = second;
                else index += 3;
            } else if (opCode == 6) {
                if (first == 0) index = second;
                else index += 3;
            } else if (opCode == 7) {
                program[program[index + 3]] = first < second ? 1 : 0;
                index += 4;
            } else if (opCode == 8) {
                program[program[index + 3]] = first == second ? 1 : 0;
                index += 4;
            } else return -1;
        }
    }
}
