package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode03 {

    private static class Rucksack {
        final String content;

        Rucksack(String line) {
            content = line;
        }

        int getWronglyPlacedItemPriority() {
            Set<Character> compartment1 = new HashSet<>();
            for (int i = 0; i < content.length() / 2; i++) compartment1.add(content.charAt(i));
            for (int i = content.length() / 2; i < content.length(); i++) {
                if (compartment1.contains(content.charAt(i))) return getPriority(content.charAt(i));
            }
            throw new IllegalArgumentException("No wrongly placed item");
        }

        int getBadgePriority(Rucksack other1, Rucksack other2) {
            for (int i = 0; i < content.length(); i++) {
                String c = Character.toString(content.charAt(i));
                if (other1.content.contains(c) && other2.content.contains(c)) return getPriority(c.charAt(0));
            }
            throw new IllegalArgumentException("No badge present");
        }

        private int getPriority(char c) {
            int priority = c - 'A';
            if (priority < 26) return priority + 27;
            return c - 'a' + 1;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Rucksack> rucksacks = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) rucksacks.add(new Rucksack(currentLine));
            LOGGER.info("PART 1: {}", rucksacks.stream().map(Rucksack::getWronglyPlacedItemPriority).reduce(Integer::sum).orElseThrow());
            int result = 0;
            for (int i = 0; i < rucksacks.size(); i += 3) result += rucksacks.get(i).getBadgePriority(rucksacks.get(i + 1), rucksacks.get(i + 2));
            LOGGER.info("PART 2: {}", result);
        }
    }
}
