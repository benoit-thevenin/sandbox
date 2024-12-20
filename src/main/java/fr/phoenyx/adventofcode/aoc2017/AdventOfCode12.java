package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode12 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);
    private static final Map<Integer, Set<Integer>> pipes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" <-> ");
                pipes.put(Integer.parseInt(split[0]), Arrays.stream(split[1].split(", ")).map(Integer::parseInt).collect(Collectors.toSet()));
            }
            LOGGER.info("PART 1: {}", getGroup(0).size());
            LOGGER.info("PART 2: {}", getAllGroups().size());
        }
    }

    private static Set<Integer> getGroup(int id) {
        Queue<Integer> toVisit = new LinkedList<>();
        toVisit.add(id);
        Set<Integer> visited = new HashSet<>(toVisit);
        while (!toVisit.isEmpty()) {
            Integer current = toVisit.remove();
            for (Integer pipe : pipes.get(current)) if (visited.add(pipe)) toVisit.add(pipe);
        }
        return visited;
    }

    private static Set<Set<Integer>> getAllGroups() {
        Set<Set<Integer>> allGroups = new HashSet<>();
        for (int id : pipes.keySet()) if (allGroups.stream().noneMatch(group -> group.contains(id))) allGroups.add(getGroup(id));
        return allGroups;
    }
}
