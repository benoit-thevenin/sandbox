package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Integer> left = new ArrayList<>();
            List<Integer> right = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" {3}");
                left.add(Integer.parseInt(split[0]));
                right.add(Integer.parseInt(split[1]));
            }
            LOGGER.info("PART 1: {}", getDifferenceSum(left, right));
            LOGGER.info("PART 2: {}", getSimilarityScore(left, right));
        }
    }

    private static int getDifferenceSum(List<Integer> left, List<Integer> right) {
        Collections.sort(left);
        Collections.sort(right);
        int sum = 0;
        for (int i = 0; i < left.size(); i++) sum += Math.abs(left.get(i) - right.get(i));
        return sum;
    }

    private static long getSimilarityScore(List<Integer> left, List<Integer> right) {
        long score = 0;
        for (int id : left) score += id * right.stream().filter(i -> i == id).count();
        return score;
    }
}
