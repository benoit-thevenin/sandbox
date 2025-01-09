package fr.phoenyx.adventofcode.aoc2019;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.MovingCoord2;
import fr.phoenyx.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdventOfCode10 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);
    private static Coord2 station;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            station = grid.getCoordinatesMatching(c -> c == '#').stream().max(Comparator.comparingInt(a -> getAsteroidsDetected(grid, a).size())).orElseThrow();
            LOGGER.info("PART 1: {}", getAsteroidsDetected(grid, station).size());
            LOGGER.info("PART 2: {}", get200thAsteroidDestroyed(grid));
        }
    }

    private static Set<Coord2> getAsteroidsDetected(CharGrid grid, Coord2 pos) {
        return grid.getCoordinatesMatching(c -> c == '#').stream().filter(asteroid -> isDetected(grid, pos, asteroid)).collect(Collectors.toSet());
    }

    private static boolean isDetected(CharGrid grid, Coord2 start, Coord2 asteroid) {
        if (start.equals(asteroid)) return false;
        Coord2 slope = getSlope(start, asteroid);
        MovingCoord2 current = new MovingCoord2(start.x, start.y, slope.x, slope.y).move();
        while (!current.getCoord().equals(asteroid)) {
            if (grid.grid[current.x][current.y] == '#') return false;
            current = current.move();
        }
        return true;
    }

    private static Coord2 getSlope(Coord2 start, Coord2 target) {
        Coord2 slope = new Coord2(target.x - start.x, target.y - start.y);
        int gcd = (int) MathUtils.greatestCommonDivisor(Math.abs(slope.x), Math.abs(slope.y));
        return new Coord2(slope.x / gcd, slope.y / gcd);
    }

    private static int get200thAsteroidDestroyed(CharGrid grid) {
        Coord2 result = getAsteroidsDetected(grid, station).stream()
            .sorted(Comparator.comparingDouble(asteroid -> getAngleDiff(getAngle(asteroid)))).toList().get(199);
        return 100 * result.x + result.y;
    }

    private static double getAngle(Coord2 pos) {
        Coord2 slope = getSlope(station, pos);
        double angle = Math.acos(slope.x / slope.getNorm());
        return slope.y >= 0 ? -angle : angle;
    }

    private static double getAngleDiff(double angle) {
        double initialAngle = Math.PI / 2;
        if (initialAngle < angle) initialAngle += 2 * Math.PI;
        return isLeft(angle) ? Math.PI + initialAngle - angle : initialAngle - angle;
    }

    private static boolean isLeft(double angle) {
        return angle > Math.PI / 2 || angle < -Math.PI / 2;
    }
}
