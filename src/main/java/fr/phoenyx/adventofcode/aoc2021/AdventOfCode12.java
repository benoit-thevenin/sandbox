package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode12 {

    private static class Cave {
        final String name;
        final Set<Cave> neighbours = new HashSet<>();

        Cave(String name) {
            this.name = name;
        }

        boolean isSmallCave() {
            return name.equals(name.toLowerCase());
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);
    private static final String START = "start";
    private static final String END = "end";
    private static final Map<String, Cave> caves = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split("-");
                for (String name : split) caves.putIfAbsent(name, new Cave(name));
                caves.get(split[0]).neighbours.add(caves.get(split[1]));
                caves.get(split[1]).neighbours.add(caves.get(split[0]));
            }
            LOGGER.info("PART 1: {}", countAllPaths(caves.get(START), new HashSet<>()));
            LOGGER.info("PART 2: {}", countAllPaths(caves.get(START), new HashMap<>()));
        }
    }

    private static int countAllPaths(Cave current, Set<Cave> visitedSmallCaves) {
        if (END.equals(current.name)) return 1;
        if (current.isSmallCave()) visitedSmallCaves.add(current);
        int count = 0;
        for (Cave neighbour : current.neighbours) {
            if (neighbour.isSmallCave() && visitedSmallCaves.contains(neighbour)) continue;
            count += countAllPaths(neighbour, visitedSmallCaves);
        }
        visitedSmallCaves.remove(current);
        return count;
    }

    private static int countAllPaths(Cave current, Map<Cave, Integer> visitedSmallCaves) {
        if (END.equals(current.name)) return 1;
        if (current.isSmallCave()) visitedSmallCaves.put(current, visitedSmallCaves.containsKey(current) ? 2 : 1);
        int count = 0;
        for (Cave neighbour : current.neighbours) {
            if (START.equals(neighbour.name) || neighbour.isSmallCave() && visitedSmallCaves.containsKey(neighbour) && visitedSmallCaves.containsValue(2)) continue;
            count += countAllPaths(neighbour, visitedSmallCaves);
        }
        if (visitedSmallCaves.containsKey(current)) {
            if (visitedSmallCaves.get(current) == 2) visitedSmallCaves.put(current, 1);
            else visitedSmallCaves.remove(current);
        }
        return count;
    }
}
