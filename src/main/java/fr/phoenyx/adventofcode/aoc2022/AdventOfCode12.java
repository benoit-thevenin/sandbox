package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;

public class AdventOfCode12 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);
    private static Coord2 EXIT;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            EXIT = grid.getCoordinatesMatching(c -> c == 'E').get(0);
            LOGGER.info("PART 1: {}", getShortestPathLength(grid, 'S'));
            LOGGER.info("PART 2: {}", getShortestPathLength(grid, 'a'));
        }
    }

    private static int getShortestPathLength(CharGrid grid, char start) {
        return grid.getCoordinatesMatching(c -> c == start).stream()
            .map(s -> grid.getDistancesMatching(s, (c1, c2) -> getHeight(c1) + 1 >= getHeight(c2)).getOrDefault(EXIT, -1))
            .filter(l -> l >= 0)
            .min(Integer::compare).orElseThrow();
    }

    private static int getHeight(char c) {
        if (c == 'S') return 0;
        return c == 'E' ? 25 : c - 'a';
    }
}
