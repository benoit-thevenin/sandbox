package fr.phoenyx.adventofcode.aoc2020;

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
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Integer> expenses = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) expenses.add(Integer.parseInt(currentLine));
            LOGGER.info("PART 1: {}", computePart1(expenses));
            LOGGER.info("PART 2: {}", computePart2(expenses));
        }
    }

    private static int computePart1(List<Integer> expenses) {
        for (int i = 0; i < expenses.size() - 1; i++) {
            for (int j = i + 1; j < expenses.size(); j++) {
                int expense1 = expenses.get(i);
                int expense2 = expenses.get(j);
                if (expense1 + expense2 == 2020) return expense1 * expense2;
            }
        }
        throw new IllegalArgumentException("No pair of numbers sum up to 2020");
    }

    private static int computePart2(List<Integer> expenses) {
        for (int i = 0; i < expenses.size() - 2; i++) {
            for (int j = i + 1; j < expenses.size() - 1; j++) {
                for (int k = j + 1; k < expenses.size(); k++) {
                    int expense1 = expenses.get(i);
                    int expense2 = expenses.get(j);
                    int expense3 = expenses.get(k);
                    if (expense1 + expense2 + expense3 == 2020) return expense1 * expense2 * expense3;
                }
            }
        }
        throw new IllegalArgumentException("No trio of numbers sum up to 2020");
    }
}
