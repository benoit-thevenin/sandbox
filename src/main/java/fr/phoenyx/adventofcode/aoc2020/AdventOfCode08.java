package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> instructions = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) instructions.add(currentLine);
            LOGGER.info("PART 1: {}", getAcc(instructions, true));
            LOGGER.info("PART 2: {}", getFixedAcc(instructions));
        }
    }

    private static int getAcc(List<String> instructions, boolean isPart1) {
        int acc = 0;
        int index = 0;
        Set<Integer> indexVisited = new HashSet<>();
        while (!indexVisited.contains(index)) {
            if (index == instructions.size()) return acc;
            String instruction = instructions.get(index);
            indexVisited.add(index);
            if (instruction.startsWith("nop")) index++;
            else if (instruction.startsWith("jmp")) index += Integer.parseInt(instruction.split(" ")[1]);
            else {
                index++;
                acc += Integer.parseInt(instruction.split(" ")[1]);
            }
        }
        return isPart1 ? acc : Integer.MIN_VALUE;
    }

    private static int getFixedAcc(List<String> instructions) {
        for (int i = 0; i < instructions.size(); i++) {
            if (instructions.get(i).startsWith("acc")) continue;
            List<String> nextInstructions = new ArrayList<>();
            for (int j = 0; j < i; j++) nextInstructions.add(instructions.get(j));
            if (instructions.get(i).startsWith("jmp")) nextInstructions.add("nop");
            else nextInstructions.add("jmp " + instructions.get(i).split(" ")[1]);
            for (int j = i + 1; j < instructions.size(); j++) nextInstructions.add(instructions.get(j));
            int acc = getAcc(nextInstructions, false);
            if (acc != Integer.MIN_VALUE) return acc;
        }
        throw new IllegalArgumentException("Wrong dataset");
    }
}
