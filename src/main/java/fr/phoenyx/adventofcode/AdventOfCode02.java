package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int sumPart1 = 0;
            int sumPart2 = 0;
            int currentId = 0;
            while ((currentLine = reader.readLine()) != null) {
                currentId++;
                int lowestRed = 0;
                int lowestGreen = 0;
                int lowestBlue = 0;
                String[] sets = currentLine.split(": ")[1].split("; ");
                for (String set : sets) {
                    String[] colors = set.split(", ");
                    for (String color : colors) {
                        String[] split = color.split(" ");
                        int quantity = Integer.parseInt(split[0]);
                        if (split[1].equals("red") && quantity > lowestRed) lowestRed = quantity;
                        if (split[1].equals("green") && quantity > lowestGreen) lowestGreen = quantity;
                        if (split[1].equals("blue") && quantity > lowestBlue) lowestBlue = quantity;
                    }
                }
                if (lowestRed <= 12 && lowestGreen <= 13 && lowestBlue <= 14) sumPart1 += currentId;
                sumPart2 += lowestRed * lowestGreen * lowestBlue;
            }
            LOGGER.info("PART 1: {}", sumPart1);
            LOGGER.info("PART 2: {}", sumPart2);
        }
    }
}
