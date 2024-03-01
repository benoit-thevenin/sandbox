package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode12 {

    private static class Computer {
        private static final String REGISTERS_NAMES = "abcd";
        final Map<Character, Integer> registers = new HashMap<>();

        Computer(List<String[]> instructions, boolean isPart2) {
            for (char register : REGISTERS_NAMES.toCharArray()) registers.put(register, 0);
            if (isPart2) registers.put('c', 1);
            int index = 0;
            while (index < instructions.size()) {
                String[] instruction = instructions.get(index);
                index++;
                if (instruction[0].charAt(0) == 'c') registers.put(instruction[2].charAt(0), getValue(instruction[1]));
                else if (instruction[0].charAt(0) == 'i') registers.put(instruction[1].charAt(0), registers.get(instruction[1].charAt(0)) + 1);
                else if (instruction[0].charAt(0) == 'd') registers.put(instruction[1].charAt(0), registers.get(instruction[1].charAt(0)) - 1);
                else if (instruction[0].charAt(0) == 'j' && getValue(instruction[1]) != 0) index += Integer.parseInt(instruction[2]) - 1;
            }
        }

        private int getValue(String value) {
            return REGISTERS_NAMES.contains(value) ? registers.get(value.charAt(0)) : Integer.parseInt(value);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String[]> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine.split(" "));
            LOGGER.info("PART 1: {}", new Computer(lines, false).registers.get('a'));
            LOGGER.info("PART 2: {}", new Computer(lines, true).registers.get('a'));
        }
    }
}
