package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int result = 0;
            List<Integer> values = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                int value = Integer.parseInt(currentLine);
                result += value;
                values.add(value);
            }
            LOGGER.info("PART 1: {}", result);
            LOGGER.info("PART 2: {}", getRightFrequency(values));
        }
    }

    private static int getRightFrequency(List<Integer> values) {
        Set<Integer> seenFrequency = new HashSet<>();
        int currentFrequency = 0;
        int index = 0;
        while (seenFrequency.add(currentFrequency)) {
            currentFrequency += values.get(index % values.size());
            index++;
        }
        return currentFrequency;
    }
}
