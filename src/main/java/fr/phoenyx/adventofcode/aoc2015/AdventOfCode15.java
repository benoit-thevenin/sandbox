package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode15 {

    private record Ingredient(long capacity, long durability, long flavor, long texture, long calories) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);
    private static final List<Ingredient> ingredients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(": ")[1].split("(capacity )|(, durability )|(, flavor )|(, texture )|(, calories )");
                ingredients.add(new Ingredient(Long.parseLong(split[1]), Long.parseLong(split[2]), Long.parseLong(split[3]), Long.parseLong(split[4]), Long.parseLong(split[5])));
            }
            LOGGER.info("PART 1: {}", getBestScore(true));
            LOGGER.info("PART 2: {}", getBestScore(false));
        }
    }

    private static long getBestScore(boolean isPart1) {
        long maxScore = 0;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100 - i; j++) {
                for (int k = 0; k < 100 - i - j; k++) {
                    int l = 100 - i - j - k;
                    long capacityScore = i * ingredients.get(0).capacity + j * ingredients.get(1).capacity + k * ingredients.get(2).capacity + l * ingredients.get(3).capacity;
                    long durabilityScore = i * ingredients.get(0).durability + j * ingredients.get(1).durability + k * ingredients.get(2).durability + l * ingredients.get(3).durability;
                    long flavorScore = i * ingredients.get(0).flavor + j * ingredients.get(1).flavor + k * ingredients.get(2).flavor + l * ingredients.get(3).flavor;
                    long textureScore = i * ingredients.get(0).texture + j * ingredients.get(1).texture + k * ingredients.get(2).texture + l * ingredients.get(3).texture;
                    long calories = i * ingredients.get(0).calories + j * ingredients.get(1).calories + k * ingredients.get(2).calories + l * ingredients.get(3).calories;
                    if (capacityScore > 0 && durabilityScore > 0 && flavorScore > 0 && textureScore > 0 && (isPart1 || calories == 500)) {
                        long score = capacityScore * durabilityScore * flavorScore * textureScore;
                        maxScore = Math.max(maxScore, score);
                    }
                }
            }
        }
        return maxScore;
    }
}
