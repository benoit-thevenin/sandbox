package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode21 {

    private enum Dir {
        N(0, -1),
        E(1, 0),
        S(0, 1),
        W(-1, 0);

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
        boolean isWalkable;

        Point(int x, int y, boolean isWalkable) {
            this.x = x;
            this.y = y;
            this.isWalkable = isWalkable;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point other = (Point) o;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return x + 1000 * y;
        }
    }

    private static class Garden {
        int width;
        int height;
        Point[][] map;
        Point start;

        Garden(List<String> lines) {
            width = lines.iterator().next().length();
            height = lines.size();
            map = new Point[width][height];
            for (int i = 0; i < height; i++) {
                String line = lines.get(i);
                for (int j = 0; j < width; j++) {
                    map[j][i] = new Point(j, i, line.charAt(j) != '#');
                    if (line.charAt(j) == 'S') start = map[j][i];
                }
            }
        }

        long getReacheableCellsIn(int steps) {
            Set<Point> reacheableCells = new HashSet<>();
            List<Long> lengths = new ArrayList<>();
            reacheableCells.add(start);
            for (int i = 1; i <= steps; i++) {
                reacheableCells = getNextReacheableCells(reacheableCells);
                if (i % height == steps % height) lengths.add((long) reacheableCells.size());
                if (lengths.size() == 3) break;
            }
            if (lengths.size() < 3) return reacheableCells.size();
            long n = steps / height;
            long a = (lengths.get(2) + lengths.get(0) - 2 * lengths.get(1)) / 2;
            long b = lengths.get(1) - lengths.get(0) - a;
            return a * n * n + b * n + lengths.get(0);
        }

        private Set<Point> getNextReacheableCells(Set<Point> reacheableCells) {
            Set<Point> nextReacheableCells = new HashSet<>();
            for (Point point : reacheableCells) {
                for (Dir dir : Dir.values()) {
                    Point neighbour = new Point(point.x + dir.dx, point.y + dir.dy, true);
                    int x = neighbour.x % width;
                    if (x < 0) x += width;
                    int y = neighbour.y % height;
                    if (y < 0) y += height;
                    if (map[x][y].isWalkable) nextReacheableCells.add(neighbour);
                }
            }
            return nextReacheableCells;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode21.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode21.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Garden garden = new Garden(lines);
            LOGGER.info("PART 1: {}", garden.getReacheableCellsIn(64));
            LOGGER.info("PART 2: {}", garden.getReacheableCellsIn(26501365));
        }
    }
}
