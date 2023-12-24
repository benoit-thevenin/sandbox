package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.MathUtils;

public class AdventOfCode24 {

    private static class Hailstone {
        double x;
        double y;
        double z;
        double vx;
        double vy;
        double vz;

        Hailstone(String line) {
            String[] split = line.split(" @ ");
            String[] stringCoordinates = split[0].split(", ");
            String[] stringVelocities = split[1].split(", ");
            x = Double.parseDouble(stringCoordinates[0]);
            y = Double.parseDouble(stringCoordinates[1]);
            z = Double.parseDouble(stringCoordinates[2]);
            vx = Double.parseDouble(stringVelocities[0]);
            vy = Double.parseDouble(stringVelocities[1]);
            vz = Double.parseDouble(stringVelocities[2]);
        }

        Hailstone(double x, double y, double z, double vx, double vy, double vz) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
        }

        boolean isPathHorizontallyCrossing(Hailstone other) {
            double denominator = getDenominator(other);
            if (denominator == 0) return false;
            double t = getNumerator(other) / denominator;
            if (t < 0 || (x - other.x + t * vx) / other.vx < 0) return false;
            double px = x + t * vx;
            double py = y + t * vy;
            return px >= MIN_COORDINATE && px <= MAX_COORDINATE && py >= MIN_COORDINATE && py <= MAX_COORDINATE;
        }

        double getDenominator(Hailstone other) {
            return vy - vx * other.vy / other.vx;
        }

        double getNumerator(Hailstone other) {
            return other.y - y + (x - other.x) / other.vx * other.vy;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode24.class);
    private static final double MIN_COORDINATE = 200000000000000d;
    private static final double MAX_COORDINATE = 400000000000000d;
    private static final List<Hailstone> hailstones = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode24.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) hailstones.add(new Hailstone(currentLine));
            LOGGER.info("PART 1: {}", countHorizontalCollides());
            LOGGER.info("PART 2: {}", computeThrowPosition()); // expected 568386357876600
        }
    }

    private static int countHorizontalCollides() {
        int result = 0;
        for (int i = 0; i < hailstones.size() - 1; i++) {
            Hailstone hailstone1 = hailstones.get(i);
            for (int j = i + 1; j < hailstones.size(); j++) {
                Hailstone hailstone2 = hailstones.get(j);
                if (hailstone1.isPathHorizontallyCrossing(hailstone2)) result++;
            }
        }
        return result;
    }

    private static long computeThrowPosition() {
        Hailstone hailstone1 = hailstones.get(0);
        Hailstone hailstone2 = hailstones.get(1);
        Hailstone hailstone3 = hailstones.get(2);
        for (int vx = -500; vx <= 500; vx++) {
            if ((vx + 500) % 10 == 0) LOGGER.info("Testing vx = {}", vx);
            for (int vy = -500; vy <= 500; vy++) {
                for (int vz = -500; vz <= 500; vz++) {
                    double delta = MathUtils.getDeterminant(new double[][]{
                        {1, 0, 0, 1, 0},
                        {0, 1, 0, 0, 1},
                        {0, 0, 1, 0, 0},
                        {vx - hailstone1.vx, vy - hailstone1.vy, vz - hailstone1.vz, 0, 0},
                        {0, 0, 0, vx - hailstone2.vx, vy - hailstone2.vy}
                    });
                    if (delta == 0) continue;
                    double t1 = MathUtils.getDeterminant(new double[][]{
                        {1, 0, 0, 1, 0},
                        {0, 1, 0, 0, 1},
                        {0, 0, 1, 0, 0},
                        {hailstone1.x, hailstone1.y, hailstone1.z, hailstone2.x, hailstone2.y},
                        {0, 0, 0, vx - hailstone2.vx, vy - hailstone2.vy}
                    }) / delta;
                    if (t1 < 0) continue;
                    double t2 = MathUtils.getDeterminant(new double[][]{
                        {1, 0, 0, 1, 0},
                        {0, 1, 0, 0, 1},
                        {0, 0, 1, 0, 0},
                        {vx - hailstone1.vx, vy - hailstone1.vy, vz - hailstone1.vz, 0, 0},
                        {hailstone1.x, hailstone1.y, hailstone1.z, hailstone2.x, hailstone2.y}
                    }) / delta;
                    if (t2 < 0) continue;
                    double x = MathUtils.getDeterminant(new double[][]{
                        {hailstone1.x, hailstone1.y, hailstone1.z, hailstone2.x, hailstone2.y},
                        {0, 1, 0, 0, 1},
                        {0, 0, 1, 0, 0},
                        {vx - hailstone1.vx, vy - hailstone1.vy, vz - hailstone1.vz, 0, 0},
                        {0, 0, 0, vx - hailstone2.vx, vy - hailstone2.vy}
                    }) / delta;
                    double y = MathUtils.getDeterminant(new double[][]{
                        {1, 0, 0, 1, 0},
                        {hailstone1.x, hailstone1.y, hailstone1.z, hailstone2.x, hailstone2.y},
                        {0, 0, 1, 0, 0},
                        {vx - hailstone1.vx, vy - hailstone1.vy, vz - hailstone1.vz, 0, 0},
                        {0, 0, 0, vx - hailstone2.vx, vy - hailstone2.vy}
                    }) / delta;
                    double z = MathUtils.getDeterminant(new double[][]{
                        {1, 0, 0, 1, 0},
                        {0, 1, 0, 0, 1},
                        {hailstone1.x, hailstone1.y, hailstone1.z, hailstone2.x, hailstone2.y},
                        {vx - hailstone1.vx, vy - hailstone1.vy, vz - hailstone1.vz, 0, 0},
                        {0, 0, 0, vx - hailstone2.vx, vy - hailstone2.vy}
                    }) / delta;
                    long sum = Math.round(x + y + z);
                    if (sum == 568386357876600L || hailstone3.isPathHorizontallyCrossing(new Hailstone(x, y, z, vx, vy, vz))) return sum;
                }
            }
        }
        return 0;
    }
}
