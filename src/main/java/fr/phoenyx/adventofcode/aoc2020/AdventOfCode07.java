package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode07 {

    private static class Bag {
        final String color;
        final Set<Entry<String, Integer>> content = new HashSet<>();

        Bag(String color) {
            this.color = color;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);
    private static final Map<String, Bag> bags = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" bags contain ");
                Bag bag = new Bag(split[0]);
                bags.put(bag.color, bag);
                if (split[1].contains("no other")) continue;
                String[] content = split[1].split(", ");
                for (String c : content) {
                    String[] split2 = c.split(" ");
                    bag.content.add(new SimpleEntry<>(split2[1] + " " + split2[2], c.charAt(0) - '0'));
                }
            }
            LOGGER.info("PART 1: {}", countBagsContainingShinyGold());
            LOGGER.info("PART 2: {}", countBagsContainedByShinyGold());
        }
    }

    private static long countBagsContainingShinyGold() {
        return bags.keySet().stream()
            .map(c -> bagsContainedBy(new SimpleEntry<>(c, 1), new ArrayList<>()))
            .filter(content -> content.stream().map(Entry::getKey).anyMatch(d -> d.equals("shiny gold")))
            .count();
    }

    private static long countBagsContainedByShinyGold() {
        return bagsContainedBy(new SimpleEntry<>("shiny gold", 1), new ArrayList<>()).stream().map(Entry::getValue).reduce(Integer::sum).orElseThrow();
    }

    private static List<Entry<String, Integer>> bagsContainedBy(Entry<String, Integer> colorQuantity, List<Entry<String, Integer>> bagsContained) {
        Bag bag = bags.get(colorQuantity.getKey());
        if (bag.content.isEmpty()) return bagsContained;
        for (Entry<String, Integer> c : bag.content) {
            Entry<String, Integer> nextEntry = new SimpleEntry<>(c.getKey(), c.getValue() * colorQuantity.getValue());
            bagsContained.add(nextEntry);
            bagsContainedBy(nextEntry, bagsContained);
        }
        return bagsContained;
    }
}
