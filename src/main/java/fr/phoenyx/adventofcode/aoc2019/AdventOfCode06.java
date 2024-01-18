package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode06 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Map<String, String> orbits = new HashMap<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split("\\)");
                orbits.put(split[1], split[0]);
            }
            LOGGER.info("PART 1: {}", countAllOrbits(orbits));
            LOGGER.info("PART 2: {}", countOrbitalTransfers(orbits));
        }
    }

    private static int countAllOrbits(Map<String, String> orbits) {
        Map<String, Integer> orbitsCount = new HashMap<>();
        for (String key : orbits.keySet()) orbitsCount.put(key, countOrbits(key, orbits, orbitsCount));
        return orbitsCount.values().stream().reduce(Integer::sum).orElseThrow();
    }

    private static int countOrbits(String key, Map<String, String> orbits, Map<String, Integer> orbitsCount) {
        if (orbitsCount.containsKey(key)) return orbitsCount.get(key);
        if (!orbits.containsKey(key)) orbitsCount.put(key, 0);
        else orbitsCount.put(key, 1 + countOrbits(orbits.get(key), orbits, orbitsCount));
        return orbitsCount.get(key);
    }

    private static int countOrbitalTransfers(Map<String, String> orbits) {
        Set<String> toVisit = new HashSet<>();
        toVisit.add(orbits.get("YOU"));
        Set<String> visited = new HashSet<>();
        int iteration = 0;
        while (true) {
            visited.addAll(toVisit);
            Set<String> next = new HashSet<>();
            for (String current : toVisit) {
                next.add(orbits.get(current));
                next.addAll(orbits.keySet().stream().filter(o -> orbits.get(o).equals(current)).toList());
            }
            next.removeAll(visited);
            if (next.contains("SAN")) return iteration;
            toVisit = next;
            iteration++;
        }
    }
}
