package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Integer> values = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) values.add(Integer.parseInt(currentLine));
            LOGGER.info("PART 1: {}", computePart1(values));
            LOGGER.info("PART 2: {}", computePart2(values));
        }
    }

    private static int computePart1(List<Integer> values) {
        int result = 0;
        for (int i = 1; i < values.size(); i++) if (values.get(i) > values.get(i - 1)) result++;
        return result;
    }

    private static int computePart2(List<Integer> values) {
        int result = 0;
        for (int i = 1; i < values.size() - 2; i++) {
            int previousWindow = values.get(i - 1) + values.get(i) + values.get(i + 1);
            int currentWindow = values.get(i) + values.get(i + 1) + values.get(i + 2);
            if (currentWindow > previousWindow) result++;
        }
        return result;
    }
}
