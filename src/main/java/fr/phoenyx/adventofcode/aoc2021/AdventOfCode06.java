package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode06 {

    private static class Bank {
        long[] cd = new long[9];

        void simulateDay() {
            long newFish = cd[0];
            for (int i = 1; i < cd.length; i++) cd[i - 1] = cd[i];
            cd[8] = newFish;
            cd[6] += newFish;
        }

        long count() {
            long count = 0;
            for (long c : cd) count += c;
            return count;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Bank bank = new Bank();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                for (String s : currentLine.split(",")) bank.cd[Integer.parseInt(s)]++;
            }
            simulateLanternfish(bank, 80);
            LOGGER.info("PART 1: {}", bank.count());
            simulateLanternfish(bank, 256 - 80);
            LOGGER.info("PART 2: {}", bank.count());
        }
    }

    private static void simulateLanternfish(Bank bank, int days) {
        for (int i = 0; i < days; i++) bank.simulateDay();
    }
}
