package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode05 {

    private static class Ship {
        final Map<Character, LinkedList<Character>> crates = new HashMap<>();

        Ship(List<String> lines) {
            String cols = lines.get(lines.size() - 1);
            for (int i = 1; i < cols.length(); i += 4) crates.put(cols.charAt(i), new LinkedList<>());
            for (int i = lines.size() - 2; i >= 0; i--) {
                String line = lines.get(i);
                for (int j = 1; j < line.length(); j += 4) {
                    char c = line.charAt(j);
                    if (c != ' ') crates.get(cols.charAt(j)).add(c);
                }
            }
        }

        Ship performInstructions(List<Instruction> instructions, boolean isPart2) {
            for (Instruction instruction : instructions) {
                List<Character> moving = new ArrayList<>();
                for (int i = 0; i < instruction.quantity; i++) moving.add(crates.get(instruction.from).removeLast());
                if (isPart2) Collections.reverse(moving);
                crates.get(instruction.to).addAll(moving);
            }
            return this;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Entry<Character, LinkedList<Character>> entry : crates.entrySet()) sb.append(entry.getValue().getLast());
            return sb.toString();
        }
    }

    private record Instruction(int quantity, char from, char to) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            List<Instruction> instructions = new ArrayList<>();
            boolean isInstructions = false;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) isInstructions = true;
                else if (isInstructions) {
                    int quantity = Integer.parseInt(currentLine.split(" from")[0].split(" ")[1]);
                    char from = currentLine.split("from ")[1].split(" to")[0].charAt(0);
                    char to = currentLine.split("to ")[1].charAt(0);
                    instructions.add(new Instruction(quantity, from, to));
                } else lines.add(currentLine);
            }
            LOGGER.info("PART 1: {}", new Ship(lines).performInstructions(instructions, false));
            LOGGER.info("PART 2: {}", new Ship(lines).performInstructions(instructions, true));
        }
    }
}
