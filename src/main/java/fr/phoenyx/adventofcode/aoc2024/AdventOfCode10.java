package fr.phoenyx.adventofcode.aoc2024;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class AdventOfCode10 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode10.txt";
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
        int score = 0;
        int rating = 0;
        for (int i = 0; i < grid.width; i++) {
            for (int j = 0; j < grid.height; j++) {
                if (grid.grid[i][j] == '0') {
                    Map.Entry<Integer, Integer> result = getResult(grid, i, j);
                    score += result.getKey();
                    rating += result.getValue();
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(score, rating);
    }


    private static Map.Entry<Integer, Integer> getResult(CharGrid grid, int i, int j) {
        int score = 0;
        int rating = 0;
        Queue<Coord2> toVisit = new LinkedList<>();
        toVisit.add(new Coord2(i, j));
        Set<Coord2> visited = new HashSet<>(toVisit);
        while (!toVisit.isEmpty()) {
            Coord2 current = toVisit.remove();
            int height = grid.grid[current.x][current.y] - '0';
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                Coord2 next = current.move(dir);
                if (grid.isInGrid(next.x, next.y) && grid.grid[next.x][next.y] - '0' == height + 1) {
                    boolean isNew = visited.add(next);
                    if (grid.grid[next.x][next.y] == '9') {
                        rating++;
                        if (isNew) score++;
                    } else toVisit.add(next);
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(score, rating);
    }
}
