package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode11 {

    private enum Dir {
        N(0, -1, 0, -1),
        NE(1, -1, 1, 0),
        SE(1, 0, 1, 1),
        S(0, 1, 0, 1),
        SW(-1, 0, -1, 1),
        NW(-1, -1, -1, 0);

        final int evenDx;
        final int evenDy;
        final int oddDx;
        final int oddDy;

        Dir(int evenDx, int evenDy, int oddDx, int oddDy) {
            this.evenDx = evenDx;
            this.evenDy = evenDy;
            this.oddDx = oddDx;
            this.oddDy = oddDy;
        }
    }

    private record Coord(int x, int y) {
        CubeCoordinate toCubeCoordinate() {
            int xp = y - (x - (x & 1)) / 2;
            int zp = x;
            int yp = -(xp + zp);
            return new CubeCoordinate(xp, yp, zp);
        }

        Coord neighbor(Dir dir) {
            return x % 2 == 0 ? new Coord(x + dir.evenDx, y + dir.evenDy) : new Coord(x + dir.oddDx, y + dir.oddDy);
        }

        int distanceTo(Coord dst) {
            return this.toCubeCoordinate().distanceTo(dst.toCubeCoordinate());
        }
    }

    private record CubeCoordinate(int x, int y, int z) {
        int distanceTo(CubeCoordinate dst) {
            return (Math.abs(x - dst.x) + Math.abs(y - dst.y) + Math.abs(z - dst.z)) / 2;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Coord start = new Coord(0, 0);
            Coord lastPosition = start;
            String currentLine;
            int maxDistance = 0;
            while ((currentLine = reader.readLine()) != null) {
                List<Dir> dirs = Arrays.stream(currentLine.toUpperCase().split(",")).map(Dir::valueOf).toList();
                for (Dir dir : dirs) {
                    lastPosition = lastPosition.neighbor(dir);
                    maxDistance = Math.max(maxDistance, lastPosition.distanceTo(start));
                }
            }
            LOGGER.info("PART 1: {}", lastPosition.distanceTo(start));
            LOGGER.info("PART 2: {}", maxDistance);
        }
    }
}
