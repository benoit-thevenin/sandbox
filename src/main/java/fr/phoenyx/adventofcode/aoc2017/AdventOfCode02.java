package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int checksum = 0;
            int result = 0;
            while ((currentLine = reader.readLine()) != null) {
                List<Integer> values = Arrays.stream(currentLine.split("\t")).map(Integer::parseInt).toList();
                checksum += values.stream().max(Integer::compareTo).orElseThrow() - values.stream().min(Integer::compareTo).orElseThrow();
                for (int i = 0; i < values.size() - 1; i++) {
                    for (int j = i + 1; j < values.size(); j++) {
                        int value1 = values.get(i);
                        int value2 = values.get(j);
                        if (value1 % value2 == 0) result += value1 / value2;
                        if (value2 % value1 == 0) result += value2 / value1;
                    }
                }
            }
            LOGGER.info("PART 1: {}", checksum);
            LOGGER.info("PART 2: {}", result);
        }
    }
}
