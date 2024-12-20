package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            Map.Entry<Integer, Integer> result = getResult(grid);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Map.Entry<Integer, Integer> getResult(CharGrid grid) {
        int riskScore = 0;
        List<Integer> basinsSize = new ArrayList<>();
        for (int i = 0; i < grid.width; i++) {
            for (int j = 0; j < grid.height; j++) {
                Coord2 pos = new Coord2(i, j);
                if (isLowPoint(grid, pos)) {
                    riskScore += 1 + grid.grid[i][j] - '0';
                    basinsSize.add(grid.getRegionMatching(pos, (c1, c2) -> c2 != '9' && c2 > c1).size());
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(riskScore, basinsSize.stream().sorted((a, b) -> Integer.compare(b, a)).limit(3).reduce((a, b) -> a * b).orElseThrow());
    }

    private static boolean isLowPoint(CharGrid grid, Coord2 pos) {
        for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
            Coord2 next = pos.move(dir);
            if (grid.isInGrid(next) && grid.grid[next.x][next.y] <= grid.grid[pos.x][pos.y]) return false;
        }
        return true;
    }
}
