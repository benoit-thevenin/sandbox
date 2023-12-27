package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        Hailstone(double x, double y, double z, double vx, double vy, double vz) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
        }

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

        boolean isPathCrossingXY(Hailstone other) {
            double denominator = getDenominator(vx, vy, other.vx, other.vy);
            if (denominator == 0) return false;
            double t = getNumerator(x, y, other.x, other.y, other.vx, other.vy) / denominator;
            if (t < 0 || (x - other.x + t * vx) / other.vx < 0) return false;
            double pa = x + t * vx;
            double pb = y + t * vy;
            return pa >= MIN_COORDINATE && pa <= MAX_COORDINATE && pb >= MIN_COORDINATE && pb <= MAX_COORDINATE;
        }

        double getDenominator(double v1, double v2, double v3, double v4) {
            return v2 - v1 * v4 / v3;
        }

        double getNumerator(double a, double b, double c, double d, double v1, double v2) {
            return d - b + (a - c) / v1 * v2;
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
            LOGGER.info("PART 2: {}", computeThrowPosition());
        }
    }

    private static int countHorizontalCollides() {
        int result = 0;
        for (int i = 0; i < hailstones.size() - 1; i++) {
            Hailstone hailstone1 = hailstones.get(i);
            for (int j = i + 1; j < hailstones.size(); j++) {
                Hailstone hailstone2 = hailstones.get(j);
                if (hailstone1.isPathCrossingXY(hailstone2)) result++;
            }
        }
        return result;
    }

    private static long computeThrowPosition() {
        // Here, we arbitrary take 3 hailstones to solve the system.
        // But as shown by the LOGGER below, there are imprecisions in the double calculations required to solve the system.
        // Therefore, using another set of hailstones might round the result differently and yield an answer considered wrong (but very close)
        Hailstone rock = buildRock(hailstones.get(0), hailstones.get(1), hailstones.get(2)).orElseThrow();
        LOGGER.info("Found rock: {}, {}, {} @ {}, {}, {}", rock.x, rock.y, rock.z, rock.vx, rock.vy, rock.vz);
        return Math.round(rock.x + rock.y + rock.z);
    }

    private static Optional<Hailstone> buildRock(Hailstone a, Hailstone b, Hailstone c) {
        double[][] matrix = new double[][]{
            {a.vy - b.vy, a.vy - c.vy, b.vz - a.vz, c.vz - a.vz, 0, 0},
            {b.vx - a.vx, c.vx - a.vx, 0, 0, a.vz - b.vz, a.vz - c.vz},
            {0, 0, a.vx - b.vx, a.vx - c.vx, b.vy - a.vy, c.vy - a.vy},
            {b.y - a.y, c.y - a.y, a.z - b.z, a.z - c.z, 0, 0},
            {a.x - b.x, a.x - c.x, 0, 0, b.z - a.z, c.z - a.z},
            {0, 0, b.x - a.x, c.x - a.x, a.y - b.y, a.y - c.y}
        };
        double delta = MathUtils.getDeterminant(matrix);
        if (delta == 0) return Optional.empty();
        double[] vector = new double[]{
            (b.y * b.vx - b.x * b.vy) - (a.y * a.vx - a.x * a.vy),
            (c.y * c.vx - c.x * c.vy) - (a.y * a.vx - a.x * a.vy),
            (b.x * b.vz - b.z * b.vx) - (a.x * a.vz - a.z * a.vx),
            (c.x * c.vz - c.z * c.vx) - (a.x * a.vz - a.z * a.vx),
            (b.z * b.vy - b.y * b.vz) - (a.z * a.vy - a.y * a.vz),
            (c.z * c.vy - c.y * c.vz) - (a.z * a.vy - a.y * a.vz)
        };
        double x = getNumerator(matrix, vector, 0) / delta;
        double y = getNumerator(matrix, vector, 1) / delta;
        double z = getNumerator(matrix, vector, 2) / delta;
        double vx = getNumerator(matrix, vector, 3) / delta;
        double vy = getNumerator(matrix, vector, 4) / delta;
        double vz = getNumerator(matrix, vector, 5) / delta;
        return Optional.of(new Hailstone(x, y, z, vx, vy, vz));
    }

    private static double getNumerator(double[][] matrix, double[] vector, int col) {
        double[] save = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            save[i] = matrix[col][i];
            matrix[col][i] = vector[i];
        }
        double numerator = MathUtils.getDeterminant(matrix);
        System.arraycopy(save, 0, matrix[col], 0, vector.length);
        return numerator;
    }
}
