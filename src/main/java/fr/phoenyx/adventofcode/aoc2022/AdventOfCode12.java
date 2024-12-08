package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode12 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            LOGGER.info("PART 1: {}", getShortestPathLength(grid, 'S'));
            LOGGER.info("PART 2: {}", getShortestPathLength(grid, 'a'));
        }
    }

    private static int getShortestPathLength(CharGrid grid, char start) {
        return grid.getCoordinatesMatching(start).stream()
            .map(s -> getShortestPathLength(grid, s))
            .filter(l -> l > 0)
            .min(Integer::compare).orElseThrow();
    }

    private static int getShortestPathLength(CharGrid grid, Coord2 start) {
        Map<Coord2, Integer> visited = new HashMap<>();
        Queue<Coord2> toVisit = new LinkedList<>();
        visited.put(start, 0);
        toVisit.add(start);
        while (!toVisit.isEmpty()) {
            Coord2 current = toVisit.remove();
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                Coord2 neighbour = current.move(dir);
                if (canVisit(grid, visited, current, neighbour)) {
                    if (grid.grid[neighbour.x][neighbour.y] == 'E') return visited.get(current) + 1;
                    toVisit.add(neighbour);
                    visited.put(neighbour, visited.get(current) + 1);
                }
            }
        }
        return 0;
    }

    private static boolean canVisit(CharGrid grid, Map<Coord2, Integer> visited, Coord2 current, Coord2 neighbour) {
        return grid.isInGrid(neighbour.x, neighbour.y) && !visited.containsKey(neighbour) && getHeight(grid, neighbour) <= getHeight(grid, current) + 1;
    }

    private static int getHeight(CharGrid grid, Coord2 coord) {
        if (grid.grid[coord.x][coord.y] == 'S') return 0;
        if (grid.grid[coord.x][coord.y] == 'E') return 25;
        return grid.grid[coord.x][coord.y] - 'a';
    }
}
