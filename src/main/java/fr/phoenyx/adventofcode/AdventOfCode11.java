package fr.phoenyx.adventofcode;

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

public class AdventOfCode11 {

    private enum Dir {
        N(0, -1), E(1, 0), S(0, 1), W(-1, 0);

        final int dx;
        final int dy;

        Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private static class Point {
        int x;
        int y;
        boolean isGalaxy;
        boolean isExpanding;
        int distance = -1;

        Point(int x, int y, boolean isGalaxy) {
            this.x = x;
            this.y = y;
            this.isGalaxy = isGalaxy;
        }

        int distanceTo(Point other) {
            return Math.abs(x - other.x) + Math.abs(y - other.y);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);
    private static final List<String> LINES = new ArrayList<>();
    private static final List<Point> galaxies = new ArrayList<>();
    private static final Queue<Point> toVisit = new LinkedList<>();
    private static int width;
    private static int height;
    private static Point[][] grid;
    private static final Set<Integer> expansionX = new HashSet<>();
    private static final Set<Integer> expansionY = new HashSet<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) LINES.add(currentLine);
            buildGrid();
            long begin = System.nanoTime();
            LOGGER.info("PART 1: {}, time elpased: {}ms", computeWithBfs(2), (System.nanoTime() - begin) / 1000000);
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elpased: {}ms", computeWithBfs(1000000), (System.nanoTime() - begin) / 1000000);
            begin = System.nanoTime();
            LOGGER.info("PART 1: {}, time elpased: {}ms", computeWithManhattan(2), (System.nanoTime() - begin) / 1000000);
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elpased: {}ms", computeWithManhattan(1000000), (System.nanoTime() - begin) / 1000000);
        }
    }

    private static void buildGrid() {
        width = LINES.iterator().next().length();
        height = LINES.size();
        grid = new Point[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[j][i] = new Point(j, i, LINES.get(i).charAt(j) == '#');
                if (grid[j][i].isGalaxy) galaxies.add(grid[j][i]);
            }
        }
        setIsExpanding(true);
        setIsExpanding(false);
    }

    private static void setIsExpanding(boolean isLine) {
        int first = isLine ? height : width;
        int second = isLine ? width : height;
        for (int i = 0; i < first; i++) {
            Set<Point> points = new HashSet<>();
            int x = i;
            int y = i;
            boolean isExpanding = true;
            for (int j = 0; j < second; j++) {
                if (isLine) x = j;
                else y = j;
                if (grid[x][y].isGalaxy) {
                    isExpanding = false;
                    break;
                }
                points.add(grid[x][y]);
            }
            if (isExpanding) {
                points.forEach(p -> p.isExpanding = true);
                if (isLine) expansionY.add(i);
                else expansionX.add(i);
            }
        }
    }

    private static long computeWithBfs(int weightValue) {
        long result = 0;
        for (int i = 0; i < galaxies.size() - 1; i++) {
            resetDistances();
            Point galaxy = galaxies.get(i);
            galaxy.distance = 0;
            toVisit.add(galaxy);
            while (!toVisit.isEmpty()) {
                Point current = toVisit.remove();
                for (Dir dir : Dir.values()) {
                    int x = current.x + dir.dx;
                    int y = current.y + dir.dy;
                    updateDistance(current, x, y, weightValue);
                }
            }
            for (int j = i + 1; j < galaxies.size(); j++) result += galaxies.get(j).distance;
        }
        return result;
    }

    private static void updateDistance(Point current, int x, int y, int weightValue) {
        if (!isInGrid(x, y)) return;
        int weight = grid[x][y].isExpanding ? weightValue : 1;
        int distance = current.distance + weight;
        if (grid[x][y].distance == -1 || distance < grid[x][y].distance) {
            grid[x][y].distance = distance;
            toVisit.add(grid[x][y]);
        }
    }

    private static void resetDistances() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) grid[j][i].distance = -1;
        }
    }

    private static boolean isInGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    private static long computeWithManhattan(int weight) {
        long result = 0;
        for (int i = 0; i < galaxies.size() - 1; i++) {
            Point first = galaxies.get(i);
            for (int j = i + 1; j < galaxies.size(); j++) {
                Point second = galaxies.get(j);
                long countExpansionX = expansionX.stream().filter(x -> x > Math.min(first.x, second.x) && x < Math.max(first.x, second.x)).count();
                long countExpansionY = expansionY.stream().filter(x -> x > Math.min(first.y, second.y) && x < Math.max(first.y, second.y)).count();
                result += first.distanceTo(second) + (weight - 1) * (countExpansionX + countExpansionY);
            }
        }
        return result;
    }
}
