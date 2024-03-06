package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

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
            LOGGER.info("PART 1: {}", getRiskScore(grid));
            LOGGER.info("PART 2: {}", getBasinsScore(grid));
        }
    }

    private static int getRiskScore(CharGrid grid) {
        int riskScore = 0;
        for (int i = 0; i < grid.width; i++) {
            for (int j = 0; j < grid.height; j++) {
                if (isLowPoint(grid, i, j)) riskScore += 1 + grid.grid[i][j] - '0';
            }
        }
        return riskScore;
    }

    private static int getBasinsScore(CharGrid grid) {
        List<Integer> basinsSize = new ArrayList<>();
        for (int i = 0; i < grid.width; i++) {
            for (int j = 0; j < grid.height; j++) {
                if (isLowPoint(grid, i, j)) basinsSize.add(getBasinSize(grid, i, j));
            }
        }
        return basinsSize.stream().sorted((a, b) -> Integer.compare(b, a)).limit(3).reduce((a, b) -> a * b).orElseThrow();
    }

    private static boolean isLowPoint(CharGrid grid, int x, int y) {
        for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
            int nextX = x + dir.dx;
            int nextY = y + dir.dy;
            if (grid.isInGrid(nextX, nextY) && grid.grid[nextX][nextY] <= grid.grid[x][y]) return false;
        }
        return true;
    }

    private static int getBasinSize(CharGrid grid, int x, int y) {
        Queue<Integer> toVisit = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        toVisit.add(getHash(grid, x, y));
        visited.add(getHash(grid, x, y));
        while (!toVisit.isEmpty()) {
            int current = toVisit.remove();
            int currentX = current % grid.width;
            int currentY = current / grid.width;
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                int nextX = currentX + dir.dx;
                int nextY = currentY + dir.dy;
                int hash = getHash(grid, nextX, nextY);
                if (!visited.contains(hash) && grid.isInGrid(nextX, nextY) && grid.grid[nextX][nextY] != '9' && grid.grid[nextX][nextY] > grid.grid[currentX][currentY]) {
                    toVisit.add(hash);
                    visited.add(hash);
                }
            }
        }
        return visited.size();
    }

    private static int getHash(CharGrid grid, int x, int y) {
        return x + grid.width * y;
    }
}
