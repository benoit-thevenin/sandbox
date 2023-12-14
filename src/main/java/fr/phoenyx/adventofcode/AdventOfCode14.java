package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode14 {

    private static class Platform {
        final int width;
        final int height;
        final char[][] grid;

        Platform(List<String> lines) {
            width = lines.iterator().next().length();
            height = lines.size();
            grid = new char[width][height];
            for (int i = 0; i < height; i++) {
                String line = lines.get(i);
                for (int j = 0; j < width; j++) grid[j][i] = line.charAt(j);
            }
        }

        void spin() {
            moveNorth();
            moveWest();
            moveSouth();
            moveEast();
        }

        void moveNorth() {
            for (int i = 1; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (grid[j][i] != 'O') continue;
                    for (int k = i - 1; k >= 0; k--) {
                        if (grid[j][k] != '.') break;
                        grid[j][k] = 'O';
                        grid[j][k + 1] = '.';
                    }
                }
            }
        }

        void moveWest() {
            for (int i = 1; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (grid[i][j] != 'O') continue;
                    for (int k = i - 1; k >= 0; k--) {
                        if (grid[k][j] != '.') break;
                        grid[k][j] = 'O';
                        grid[k + 1][j] = '.';
                    }
                }
            }
        }

        void moveSouth() {
            for (int i = height - 2; i >= 0; i--) {
                for (int j = 0; j < width; j++) {
                    if (grid[j][i] != 'O') continue;
                    for (int k = i + 1; k < height; k++) {
                        if (grid[j][k] != '.') break;
                        grid[j][k] = 'O';
                        grid[j][k - 1] = '.';
                    }
                }
            }
        }

        void moveEast() {
            for (int i = width - 2; i >= 0; i--) {
                for (int j = 0; j < height; j++) {
                    if (grid[i][j] != 'O') continue;
                    for (int k = i + 1; k < width; k++) {
                        if (grid[k][j] != '.') break;
                        grid[k][j] = 'O';
                        grid[k - 1][j] = '.';
                    }
                }
            }
        }

        int getNorthLoad() {
            int result = 0;
            for (int i = 0; i < height; i++) {
                int weight = height - i;
                for (int j = 0; j < width; j++) {
                    if (grid[j][i] == 'O') result += weight;
                }
            }
            return result;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Platform platform = new Platform(lines);
            platform.moveNorth();
            LOGGER.info("PART 1: {}", platform.getNorthLoad());
            platform.moveSouth(); // Resetting the first move before computing PART 2
            long begin = System.nanoTime();
            List<Integer> loads = new ArrayList<>();
            int lastIteration = 1000000000;
            for (int i = 0; i < lastIteration; i++) {
                platform.spin();
                loads.add(platform.getNorthLoad());
                Optional<Integer> frequency = getFrequency(loads);
                if (frequency.isPresent()) i += ((lastIteration - i) / frequency.get()) * frequency.get();
            }
            LOGGER.info("PART 2: {}, time elapsed: {}ms", platform.getNorthLoad(), (System.nanoTime() - begin) / 1000000);
        }
    }

    private static Optional<Integer> getFrequency(List<Integer> loads) {
        int middleIndex = loads.size() / 2; // middleIndex is the maximum frequency that can be found given loads input
        for (int i = 1; i < middleIndex; i++) {
            boolean found = true;
            for (int j = loads.size() - 1 - 2 * i; j < loads.size() - i; j++) {
                if (!loads.get(j).equals(loads.get(j + i))) {
                    found = false;
                    break;
                }
            }
            if (found) return Optional.of(i);
        }
        return Optional.empty();
    }
}
