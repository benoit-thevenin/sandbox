package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);
    private static final Coord2 START = new Coord2(0, 0);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Map<Coord2, Integer>> wires = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) wires.add(getWire(currentLine));
            Map<Coord2, Integer> wire1 = wires.get(0);
            Map<Coord2, Integer> wire2 = wires.get(1);
            Set<Coord2> crossingCoords = wire1.keySet().stream().filter(wire2::containsKey).collect(Collectors.toSet());
            LOGGER.info("PART 1: {}", crossingCoords.stream().map(START::manhattanDistanceTo).min(Integer::compare).orElseThrow());
            LOGGER.info("PART 2: {}", crossingCoords.stream().map(c -> wire1.get(c) + wire2.get(c)).min(Integer::compare).orElseThrow());
        }
    }

    private static Map<Coord2, Integer> getWire(String line) {
        Map<Coord2, Integer> wire = new HashMap<>();
        Coord2 current = START;
        int totalSteps = 0;
        for (String s : line.split(",")) {
            Dir dir = Dir.fromChar(s.charAt(0));
            int steps = Integer.parseInt(s.substring(1));
            for (int i = 0; i < steps; i++) {
                totalSteps++;
                Coord2 next = current.move(dir);
                wire.putIfAbsent(next, totalSteps);
                current = next;
            }
        }
        return wire;
    }
}
