package fr.phoenyx.adventofcode.aoc2024;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            Map.Entry<Integer, Integer> result = getResult(grid);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Map.Entry<Integer, Integer> getResult(CharGrid grid) {
        Set<Coord2> antinodes1 = new HashSet<>();
        Set<Coord2> antinodes2 = new HashSet<>();
        Set<Character> visited = new HashSet<>();
        visited.add('.');
        for (Coord2 coord : grid.getAllCoords()) {
            char currentChar = grid.get(coord);
            if (!visited.add(currentChar)) continue;
            computeAllAntinodes(grid, grid.getCoordinatesMatching(c -> c == currentChar), antinodes1, antinodes2);
        }
        return new SimpleEntry<>(antinodes1.size(), antinodes2.size());
    }

    private static void computeAllAntinodes(CharGrid grid, List<Coord2> antennas, Set<Coord2> antinodes1, Set<Coord2> antinodes2) {
        antinodes2.addAll(antennas);
        for (int i = 0; i < antennas.size() - 1; i++) {
            for (int j = i + 1; j < antennas.size(); j++) computeAntinodes(grid, antennas.get(i), antennas.get(j), antinodes1, antinodes2);
        }
    }

    private static void computeAntinodes(CharGrid grid, Coord2 first, Coord2 second, Set<Coord2> antinodes1, Set<Coord2> antinodes2) {
        Coord2 vect = new Coord2(first.x - second.x, first.y - second.y);
        int iteration = 0;
        boolean found = true;
        while (found) {
            found = false;
            iteration++;
            Coord2 antinode1 = new Coord2(first.x + iteration * vect.x, first.y + iteration * vect.y);
            Coord2 antinode2 = new Coord2(second.x - iteration * vect.x, second.y - iteration * vect.y);
            for (Coord2 antinode : List.of(antinode1, antinode2)) {
                if (grid.isInGrid(antinode)) {
                    if (iteration == 1) antinodes1.add(antinode);
                    antinodes2.add(antinode);
                    found = true;
                }
            }
        }
    }
}
