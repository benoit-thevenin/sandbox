package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String[] allColors = new String[]{"red", "green", "blue"};
            String currentLine;
            int sumPart1 = 0;
            int sumPart2 = 0;
            int currentId = 0;
            while ((currentLine = reader.readLine()) != null) {
                currentId++;
                Map<String, Integer> lowestPerColor = Arrays.stream(allColors).collect(Collectors.toMap(a -> a, a -> 0));
                String[] sets = currentLine.split(": ")[1].split("; ");
                for (String set : sets) {
                    String[] colors = set.split(", ");
                    for (String color : colors) {
                        String[] split = color.split(" ");
                        int quantity = Integer.parseInt(split[0]);
                        if (quantity > lowestPerColor.get(split[1])) lowestPerColor.put(split[1], quantity);
                    }
                }
                if (lowestPerColor.get("red") <= 12 && lowestPerColor.get("green") <= 13 && lowestPerColor.get("blue") <= 14) sumPart1 += currentId;
                sumPart2 += lowestPerColor.values().stream().reduce((a, b) -> a * b).orElseThrow();
            }
            LOGGER.info("PART 1: {}", sumPart1);
            LOGGER.info("PART 2: {}", sumPart2);
        }
    }
}
