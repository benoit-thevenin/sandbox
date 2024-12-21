package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.phoenyx.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode03 {

    private record Rucksack(String content) {
        int getWronglyPlacedItemPriority() {
            Set<Character> compartment1 = Utils.getLetterCount(content.substring(0, content.length() / 2)).keySet();
            Set<Character> compartment2 = Utils.getLetterCount(content.substring(content.length() / 2)).keySet();
            return getPriority(compartment1.stream().filter(compartment2::contains).findAny().orElseThrow());
        }

        int getBadgePriority(Rucksack other1, Rucksack other2) {
            Set<Character> c1 = Utils.getLetterCount(content).keySet();
            Set<Character> c2 = Utils.getLetterCount(other1.content).keySet();
            Set<Character> c3 = Utils.getLetterCount(other2.content).keySet();
            return getPriority(c1.stream().filter(c2::contains).filter(c3::contains).findAny().orElseThrow());
        }

        private int getPriority(char c) {
            int priority = c - 'A';
            return priority < 26 ? priority + 27 : c - 'a' + 1;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Rucksack> rucksacks = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) rucksacks.add(new Rucksack(currentLine));
            LOGGER.info("PART 1: {}", rucksacks.stream().map(Rucksack::getWronglyPlacedItemPriority).reduce(0, Integer::sum));
            int result = 0;
            for (int i = 0; i < rucksacks.size(); i += 3) result += rucksacks.get(i).getBadgePriority(rucksacks.get(i + 1), rucksacks.get(i + 2));
            LOGGER.info("PART 2: {}", result);
        }
    }
}
