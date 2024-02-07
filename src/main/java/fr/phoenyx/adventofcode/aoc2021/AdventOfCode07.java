package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode07 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Integer> positions = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) positions.addAll(Arrays.stream(currentLine.split(",")).map(Integer::parseInt).toList());
            LOGGER.info("PART 1: {}", getLowestFuelCost(positions, true));
            LOGGER.info("PART 2: {}", getLowestFuelCost(positions, false));
        }
    }

    private static int getLowestFuelCost(List<Integer> positions, boolean isPart1) {
        int min = positions.stream().min(Integer::compare).orElseThrow();
        int max = positions.stream().max(Integer::compare).orElseThrow();
        int lowestFuelCost = Integer.MAX_VALUE;
        for (int i = min; i <= max; i++) {
            int finalI = i;
            int fuelCost = positions.stream().map(p -> getFuelCost(p, finalI, isPart1)).reduce(Integer::sum).orElseThrow();
            if (fuelCost < lowestFuelCost) lowestFuelCost = fuelCost;
        }
        return lowestFuelCost;
    }

    private static int getFuelCost(int a, int b, boolean isPart1) {
        int n = Math.abs(a - b);
        return isPart1 ? n : n * (n + 1) / 2;
    }
}
