package fr.phoenyx.adventofcode.aoc2025;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            long sum1 = 0;
            long sum2 = 0;
            while ((currentLine = reader.readLine()) != null) {
                sum1 += getMaxJoltage(currentLine, 2);
                sum2 += getMaxJoltage(currentLine, 12);
            }
            LOGGER.info("PART 1: {}", sum1);
            LOGGER.info("PART 2: {}", sum2);
        }
    }

    private static long getMaxJoltage(String bank, int quantity) {
        int highestIndexOn = -1;
        StringBuilder sb = new StringBuilder();
        while (sb.length() < quantity) {
            int bestIndex = -1;
            int max = 0;
            for (int i = bank.length() - quantity + sb.length(); i > highestIndexOn; i--) {
                int value = bank.charAt(i) - '0';
                if (value >= max) {
                    max = value;
                    bestIndex = i;
                }
            }
            highestIndexOn = bestIndex;
            sb.append(bank.charAt(bestIndex));
        }
        return Long.parseLong(sb.toString());
    }
}
