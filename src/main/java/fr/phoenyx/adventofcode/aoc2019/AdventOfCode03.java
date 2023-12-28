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

import fr.phoenyx.models.Coord;
import fr.phoenyx.models.Dir;

public class AdventOfCode03 {

    private static class Wire {
        Map<Coord, Integer> stepsByCoord = new HashMap<>();

        Wire(String line) {
            Coord current = START;
            int totalSteps = 0;
            for (String s : line.split(",")) {
                Dir dir = Dir.fromChar(s.charAt(0));
                int steps = Integer.parseInt(s.substring(1));
                for (int i = 0; i < steps; i++) {
                    totalSteps++;
                    Coord next = current.move(dir);
                    stepsByCoord.putIfAbsent(next, totalSteps);
                    current = next;
                }
            }
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);
    private static final Coord START = new Coord(0, 0);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Wire> wires = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) wires.add(new Wire(currentLine));
            Wire wire1 = wires.get(0);
            Wire wire2 = wires.get(1);
            Set<Coord> crossingCoords = wire1.stepsByCoord.keySet().stream().filter(c -> wire2.stepsByCoord.containsKey(c)).collect(Collectors.toSet());
            int minDistance = Integer.MAX_VALUE;
            int minSteps = Integer.MAX_VALUE;
            for (Coord coord : crossingCoords) {
                int distance = coord.manhattanDistanceTo(START);
                int steps = wire1.stepsByCoord.get(coord) + wire2.stepsByCoord.get(coord);
                if (distance < minDistance) minDistance = distance;
                if (steps < minSteps) minSteps = steps;
            }
            LOGGER.info("PART 1: {}", minDistance);
            LOGGER.info("PART 2: {}", minSteps);
        }
    }
}
