package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AdventOfCode23 {

    private record Computer(String name, Set<String> neighbours) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode23.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode23.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Map<String, Computer> computers = new HashMap<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split("-");
                Computer computer1 = computers.getOrDefault(split[0], new Computer(split[0], new HashSet<>()));
                Computer computer2 = computers.getOrDefault(split[1], new Computer(split[1], new HashSet<>()));
                computer1.neighbours.add(split[1]);
                computer2.neighbours.add(split[0]);
                computers.put(split[0], computer1);
                computers.put(split[1], computer2);
            }
            LOGGER.info("PART 1: {}", getGroupsOfThree(computers).stream().filter(g -> g.stream().anyMatch(c -> c.name.startsWith("t"))).count());
            LOGGER.info("PART 2: {}", getPassword(computers));
        }
    }

    private static Set<Set<Computer>> getGroupsOfThree(Map<String, Computer> computers) {
        Set<Set<Computer>> groups = new HashSet<>();
        for (Computer computer : computers.values()) {
            for (String neigh : computer.neighbours) {
                Computer neighbour = computers.get(neigh);
                neighbour.neighbours.stream().map(computers::get).filter(c -> c.neighbours.contains(computer.name)).forEach(c -> groups.add(Set.of(computer, neighbour, c)));
            }
        }
        return groups;
    }

    private static String getPassword(Map<String, Computer> computers) {
        Set<Set<String>> groups = new HashSet<>();
        for (Computer computer : computers.values()) {
            Set<String> group = new HashSet<>();
            group.add(computer.name);
            Optional<Computer> next = Optional.of(computer);
            while (next.isPresent()) {
                next = computers.values().stream().filter(c -> !group.contains(c.name)).filter(c -> c.neighbours.containsAll(group)).findAny();
                next.ifPresent(c -> group.add(c.name));
            }
            groups.add(group);
        }
        return groups.stream().max(Comparator.comparingInt(Set::size)).orElseThrow().stream().sorted().collect(Collectors.joining(","));
    }
}
