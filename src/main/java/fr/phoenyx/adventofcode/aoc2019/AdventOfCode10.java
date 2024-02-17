package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.utils.MathUtils;

public class AdventOfCode10 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);
    private static int xStation = 0;
    private static int yStation = 0;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            LOGGER.info("PART 1: {}", getHighestDetectedAsteroids(grid));
            LOGGER.info("PART 2: {}", get200thAsteroidDestroyed(grid));
        }
    }

    private static int getHighestDetectedAsteroids(CharGrid grid) {
        int max = 0;
        for (int i = 0; i < grid.width; i++) {
            for (int j = 0; j < grid.height; j++) {
                if (grid.grid[i][j] == '#') {
                    int asteroidsDetected = getAsteroidsDetected(grid, i, j).size();
                    if (asteroidsDetected > max) {
                        max = asteroidsDetected;
                        xStation = i;
                        yStation = j;
                    }
                }
            }
        }
        return max;
    }

    private static int get200thAsteroidDestroyed(CharGrid grid) {
        List<Integer> asteroidsDetectedSorted = getAsteroidsDetected(grid, xStation, yStation).stream()
            .sorted(Comparator.comparingDouble(a -> getAngleDiff(getAngle(grid, a))))
            .toList();
        int hash = asteroidsDetectedSorted.get(199);
        return 100 * (hash % grid.width) + hash / grid.width;
    }

    private static double getAngle(CharGrid grid, int hash) {
        int x = hash % grid.width;
        int y = hash / grid.width;
        int xSlope = x - xStation;
        int ySlope = y - yStation;
        int gcd = (int) MathUtils.greatestCommonDivisor(Math.abs(xSlope), Math.abs(ySlope));
        xSlope /= gcd;
        ySlope /= gcd;
        return getAngle(xSlope, ySlope);
    }

    private static double getAngleDiff(double angle) {
        double initialAngle = Math.PI / 2;
        if (initialAngle < angle) initialAngle += 2 * Math.PI;
        return isLeft(angle) ? Math.PI + initialAngle - angle : initialAngle - angle;
    }

    private static boolean isLeft(double angle) {
        return angle > Math.PI / 2 || angle < -Math.PI / 2;
    }

    private static double getAngle(int x1, int y1) {
        double angle = Math.acos(x1 / getNorm(x1, y1));
        return y1 >= 0 ? -angle : angle;
    }

    private static double getNorm(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    private static Set<Integer> getAsteroidsDetected(CharGrid grid, int x, int y) {
        Set<Integer> asteroidsDectected = new HashSet<>();
        for (int i = 0; i < grid.width; i++) {
            for (int j = 0; j < grid.height; j++)
                if (grid.grid[i][j] == '#' && isDetected(grid, x, y, i, j)) asteroidsDectected.add(i + grid.width * j);
        }
        return asteroidsDectected;
    }

    private static boolean isDetected(CharGrid grid, int x, int y, int i, int j) {
        if (x == i && y == j) return false;
        int xSlope = i - x;
        int ySlope = j - y;
        int gcd = (int) MathUtils.greatestCommonDivisor(Math.abs(xSlope), Math.abs(ySlope));
        xSlope /= gcd;
        ySlope /= gcd;
        x += xSlope;
        y += ySlope;
        while (x != i || y != j) {
            if (grid.grid[x][y] == '#') return false;
            x += xSlope;
            y += ySlope;
        }
        return true;
    }
}
