package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.AbstractGrid;
import fr.phoenyx.models.Coord;
import fr.phoenyx.models.Dir;

public class AdventOfCode23 {

    private static class Point extends Coord {
        char type;

        Point(int x, int y, char type) {
            super(x, y);
            this.type = type;
        }

        boolean isWalkable() {
            return type != '#';
        }

        List<Dir> getPossibleDirs() {
            try {
                return List.of(Dir.fromChar(type));
            } catch (IllegalArgumentException e) {
                return Dir.FOUR_NEIGHBOURS_VALUES;
            }
        }
    }

    private static class Grid extends AbstractGrid {
        final Point[][] map;
        Point start;
        Point exit;

        Grid(List<String> lines) {
            super(lines);
            map = new Point[width][height];
            for (int i = 0; i < height; i++) {
                String line = lines.get(i);
                for (int j = 0; j < width; j++) {
                    map[j][i] = new Point(j, i, line.charAt(j));
                    if (i == 0 && map[j][i].isWalkable()) start = map[j][i];
                    if (i == height - 1 && map[j][i].isWalkable()) exit = map[j][i];
                }
            }
        }

        int getLongestHikeLength(boolean isPart1) {
            return getLongestHikeLength(start, new HashSet<>(), 0, isPart1);
        }

        private int getLongestHikeLength(Point position, Set<Point> visited, int steps, boolean isPart1) {
            if (position == exit) return steps;
            visited.add(position);
            List<Point> nextPositions = getNextPositions(position, visited, isPart1);
            while (nextPositions.size() == 1) {
                Point next = nextPositions.iterator().next();
                steps++;
                if (next == exit) return steps;
                visited.add(next);
                nextPositions = getNextPositions(next, visited, isPart1);
            }
            int maxLength = 0;
            for (Point next : nextPositions) {
                int length = getLongestHikeLength(next, new HashSet<>(visited), steps + 1, isPart1);
                if (length > maxLength) maxLength = length;
            }
            return maxLength;
        }

        private List<Point> getNextPositions(Point current, Set<Point> visited, boolean isPart1) {
            List<Point> nextPositions = new ArrayList<>();
            List<Dir> possibleDirs = isPart1 ? current.getPossibleDirs() : Dir.FOUR_NEIGHBOURS_VALUES;
            for (Dir dir : possibleDirs) {
                int x = current.x + dir.dx;
                int y = current.y + dir.dy;
                if (isInGrid(x, y) && map[x][y].isWalkable() && !visited.contains(map[x][y])) nextPositions.add(map[x][y]);
            }
            return nextPositions;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode23.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode23.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Grid grid = new Grid(lines);
            long begin = System.nanoTime();
            LOGGER.info("PART 1: {}, time elapsed: {}ms", grid.getLongestHikeLength(true), (System.nanoTime() - begin) / 1000000);
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed: {}ms", grid.getLongestHikeLength(false), (System.nanoTime() - begin) / 1000000);
        }
    }
}
