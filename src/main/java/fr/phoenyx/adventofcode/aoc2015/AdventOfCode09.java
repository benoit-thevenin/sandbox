package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.IntBinaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode09 {

    private static class Node {
        final String name;
        final Map<Node, Integer> distances = new HashMap<>();

        Node(String name) {
            this.name = name;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);
    private static final Map<String, Node> nodes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" = ");
                String[] names = split[0].split(" to ");
                int distance = Integer.parseInt(split[1]);
                for (String name : names) nodes.putIfAbsent(name, new Node(name));
                nodes.get(names[0]).distances.put(nodes.get(names[1]), distance);
                nodes.get(names[1]).distances.put(nodes.get(names[0]), distance);
            }
            LOGGER.info("PART 1: {}", getDistance(Math::min, Integer.MAX_VALUE));
            LOGGER.info("PART 2: {}", getDistance(Math::max, Integer.MIN_VALUE));
        }
    }

    private static int getDistance(IntBinaryOperator function, int initValue) {
        int distance = initValue;
        Set<Node> visited = new HashSet<>();
        for (Node start : nodes.values()) {
            visited.add(start);
            distance = function.applyAsInt(distance, getDistance(function, initValue, start, 0, visited));
            visited.remove(start);
        }
        return distance;
    }

    private static int getDistance(IntBinaryOperator function, int initValue, Node current, int currentDistance, Set<Node> visited) {
        if (visited.containsAll(current.distances.keySet())) return currentDistance;
        int distance = initValue;
        for (Entry<Node, Integer> entry : current.distances.entrySet()) {
            if (!visited.contains(entry.getKey())) {
                visited.add(entry.getKey());
                distance = function.applyAsInt(distance, getDistance(function, initValue, entry.getKey(), currentDistance + entry.getValue(), visited));
                visited.remove(entry.getKey());
            }
        }
        return distance;
    }
}
