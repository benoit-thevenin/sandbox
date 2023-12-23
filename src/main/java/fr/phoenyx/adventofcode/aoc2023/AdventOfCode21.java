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

public class AdventOfCode21 {

    private static class Point extends Coord {
        boolean isWalkable;

        Point(int x, int y, boolean isWalkable) {
            super(x, y);
            this.isWalkable = isWalkable;
        }
    }

    private static class Garden extends AbstractGrid {
        Point[][] map;
        Point start;

        Garden(List<String> lines) {
            super(lines);
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
            Set<Coord> reacheableCells = new HashSet<>();
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

        private Set<Coord> getNextReacheableCells(Set<Coord> reacheableCells) {
            Set<Coord> nextReacheableCells = new HashSet<>();
            for (Coord coord : reacheableCells) {
                for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                    Coord neighbour = coord.move(dir);
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
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode21.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Garden garden = new Garden(lines);
            long begin = System.nanoTime();
            LOGGER.info("PART 1: {}, time elapsed: {}ms", garden.getReacheableCellsIn(64), (System.nanoTime() - begin) / 1000000);
            LOGGER.info("PART 2: {}, time elapsed: {}ms", garden.getReacheableCellsIn(26501365), (System.nanoTime() - begin) / 1000000);
        }
    }
}
