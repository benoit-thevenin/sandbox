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

public class AdventOfCode03 {

    private enum Dir {
        N(0, -1),
        NE(1, -1),
        E(1, 0),
        SE(1, 1),
        S(0, 1),
        SW(-1, 1),
        W(-1, 0),
        NW(-1, -1);

        final int dx;
        final int dy;

        Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);
    private static int width;
    private static int height;
    private static char[][] grid;
    private static int[][] partNumbers;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode03.txt";
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                lines.add(currentLine);
            }
        }
        buildDataStructures(lines);
        LOGGER.info("PART 1: {}", computePart1());
        LOGGER.info("PART 2: {}", computePart2());
    }

    private static void buildDataStructures(List<String> lines) {
        width = lines.get(0).length();
        height = lines.size();
        grid = new char[width][height];
        partNumbers = new int[width][height];
        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            for (int j = 0; j < width; j++) grid[j][i] = line.charAt(j);
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!Character.isDigit(grid[j][i])) continue;
                StringBuilder partNumber = new StringBuilder();
                while (j < width && Character.isDigit(grid[j][i])) {
                    partNumber.append(grid[j][i]);
                    j++;
                }
                for (int k = j - 1; k > j - 1 - partNumber.length(); k--) {
                    partNumbers[k][i] = Integer.parseInt(partNumber.toString());
                }
            }
        }
    }

    private static int computePart1() {
        int sum = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[j][i] == '.' || Character.isDigit(grid[j][i])) continue;
                Set<Integer> numbers = new HashSet<>();
                for (Dir dir : Dir.values()) {
                    int neighbourX = j + dir.dx;
                    int neighbourY = i + dir.dy;
                    if (neighbourX < 0 || neighbourY < 0 || neighbourX >= width || neighbourY >= height) continue;
                    numbers.add(partNumbers[neighbourX][neighbourY]);
                }
                for (int number : numbers) sum += number;
            }
        }
        return sum;
    }

    private static int computePart2() {
        int sum = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] != '*') continue;
                Set<Integer> numbers = new HashSet<>();
                for (Dir dir : Dir.values()) {
                    int neighbourX = i + dir.dx;
                    int neighbourY = j + dir.dy;
                    if (neighbourX < 0 || neighbourY < 0 || neighbourX >= width || neighbourY >= height || partNumbers[neighbourX][neighbourY] == 0) continue;
                    numbers.add(partNumbers[neighbourX][neighbourY]);
                }
                if (numbers.size() == 2) {
                    int value = 1;
                    for (int number : numbers) value *= number;
                    sum += value;
                }
            }
        }
        return sum;
    }
}
