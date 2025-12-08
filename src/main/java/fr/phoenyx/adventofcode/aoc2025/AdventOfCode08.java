package fr.phoenyx.adventofcode.aoc2025;

import fr.phoenyx.models.coords.Coord3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Coord3> junctionBoxes = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                junctionBoxes.add(new Coord3(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
            }
            Entry<Integer, Long> result = getResult(junctionBoxes);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Entry<Integer, Long> getResult(List<Coord3> junctionBoxes) {
        List<Entry<Coord3, Coord3>> pairs = getSortedPairs(junctionBoxes);
        List<Set<Coord3>> circuits = new ArrayList<>();
        pairs.stream().limit(1000).forEach(pair -> connectPair(pair, circuits));
        int resultPart1 = circuits.stream().map(Set::size)
            .sorted((s1, s2) -> Integer.compare(s2, s1))
            .limit(3).reduce(1, (a, b) -> a * b);
        int i = 999;
        while (circuits.get(0).size() != junctionBoxes.size()) {
            i++;
            connectPair(pairs.get(i), circuits);
        }
        return new SimpleEntry<>(resultPart1, (long) pairs.get(i).getKey().x * pairs.get(i).getValue().x);
    }

    private static List<Entry<Coord3, Coord3>> getSortedPairs(List<Coord3> junctionBoxes) {
        Map<Entry<Coord3, Coord3>, Double> distances = new HashMap<>();
        for (int i = 0; i < junctionBoxes.size() - 1; i++) {
            for (int j = i + 1; j < junctionBoxes.size(); j++) {
                Entry<Coord3, Coord3> pair = new SimpleEntry<>(junctionBoxes.get(i), junctionBoxes.get(j));
                distances.put(pair, pair.getKey().distanceTo(pair.getValue()));
            }
        }
        return distances.entrySet().stream().sorted(Entry.comparingByValue()).map(Entry::getKey).toList();
    }

    private static void connectPair(Entry<Coord3, Coord3> pair, List<Set<Coord3>> circuits) {
        List<Set<Coord3>> existingCircuits = circuits.stream().filter(c -> c.contains(pair.getKey()) || c.contains(pair.getValue())).toList();
        if (existingCircuits.isEmpty()) {
            Set<Coord3> newCircuit = new HashSet<>();
            newCircuit.add(pair.getKey());
            newCircuit.add(pair.getValue());
            circuits.add(newCircuit);
        } else if (existingCircuits.size() == 1) {
            for (Set<Coord3> circuit : existingCircuits) {
                circuit.add(pair.getKey());
                circuit.add(pair.getValue());
            }
        } else {
            circuits.removeAll(existingCircuits);
            Set<Coord3> newCircuit = new HashSet<>();
            for (Set<Coord3> circuit : existingCircuits) newCircuit.addAll(circuit);
            circuits.add(newCircuit);
        }
    }
}
