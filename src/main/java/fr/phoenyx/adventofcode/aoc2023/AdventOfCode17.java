package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode17 {

    private static class City extends CharGrid {

        City(List<String> lines) {
            super(lines);
        }

        int getMinHeatLoss(int minStreak, int maxStreak) {
            Map<Node, Integer> heatLosses = new HashMap<>();
            heatLosses.put(new Node(0, 0, Dir.E, maxStreak), 0);
            heatLosses.put(new Node(0, 0, Dir.S, maxStreak), 0);
            Queue<Node> toVisit = new LinkedList<>(heatLosses.keySet());
            while (!toVisit.isEmpty()) {
                Node current = toVisit.remove();
                List<Dir> possibleDirs = current.dir.getFourNeighboursPossibleDirs(current.steps, minStreak, maxStreak);
                for (Dir dir : possibleDirs) {
                    int x = current.x + dir.dx;
                    int y = current.y + dir.dy;
                    if (isInGrid(x, y)) {
                        int steps = dir == current.dir ? current.steps + 1 : 1;
                        Node node = new Node(x, y, dir, steps);
                        int heatLoss = heatLosses.get(current) + get(x, y);
                        if (!heatLosses.containsKey(node) || heatLosses.get(node) > heatLoss) {
                            heatLosses.put(node, heatLoss);
                            toVisit.add(node);
                        }
                    }
                }
            }
            return heatLosses.entrySet().stream()
                .filter(e -> e.getKey().x == width - 1)
                .filter(e -> e.getKey().y == height - 1)
                .filter(e -> e.getKey().steps >= minStreak)
                .map(Entry::getValue)
                .reduce(Integer::min).orElseThrow();
        }

        private int get(int x, int y) {
            return grid[x][y] - '0';
        }
    }

    private static class Node extends Coord2 {
        Dir dir;
        int steps;

        Node(int x, int y, Dir dir, int steps) {
            super(x, y);
            this.dir = dir;
            this.steps = steps;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node other = (Node) o;
            return x == other.x && y == other.y && dir == other.dir && steps == other.steps;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, dir, steps);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode17.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode17.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            City city = new City(lines);
            long begin = System.nanoTime();
            LOGGER.info("PART 1: {}, time elapsed: {}ms", city.getMinHeatLoss(0, 3), (System.nanoTime() - begin) / 1000000);
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed: {}ms", city.getMinHeatLoss(4, 10), (System.nanoTime() - begin) / 1000000);
        }
    }
}
