package fr.phoenyx.adventofcode.aoc2017;

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
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Integer> instructions = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) instructions.add(Integer.parseInt(currentLine));
            LOGGER.info("PART 1: {}", countStepsOut1(instructions));
            LOGGER.info("PART 2: {}", countStepsOut2(instructions));
        }
    }

    private static int countStepsOut1(List<Integer> instructionList) {
        int[] instructions = new int[instructionList.size()];
        for (int i = 0; i < instructions.length; i++) instructions[i] = instructionList.get(i);
        int count = 0;
        int index = 0;
        while (true) {
            count++;
            instructions[index]++;
            index += instructions[index] - 1;
            if (index < 0 || index >= instructions.length) return count;
        }
    }

    private static int countStepsOut2(List<Integer> instructionList) {
        int[] instructions = new int[instructionList.size()];
        for (int i = 0; i < instructions.length; i++) instructions[i] = instructionList.get(i);
        int count = 0;
        int index = 0;
        while (true) {
            count++;
            int jump = instructions[index];
            if (jump > 2) instructions[index]--;
            else instructions[index]++;
            index += jump;
            if (index < 0 || index >= instructions.length) return count;
        }
    }
}
