package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode03 {

    private record Point(Set<String> claimingIds) {}

    private static class Fabric {
        static final int WIDTH = 1000;
        static final int HEIGHT = 1000;
        final Point[][] grid = new Point[WIDTH][HEIGHT];
        final Set<String> claimingIds = new HashSet<>();

        Fabric() {
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) grid[i][j] = new Point(new HashSet<>());
            }
        }

        void process(String line) {
            String[] split = line.split(" @ ");
            claimingIds.add(split[0]);
            String[] claim = split[1].split(": ");
            String[] starts = claim[0].split(",");
            String[] dimensions = claim[1].split("x");
            int xStart = Integer.parseInt(starts[0]);
            int yStart = Integer.parseInt(starts[1]);
            int xEnd = xStart + Integer.parseInt(dimensions[0]);
            int yEnd = yStart + Integer.parseInt(dimensions[1]);
            for (int i = xStart; i < xEnd; i++) {
                for (int j = yStart; j < yEnd; j++) grid[i][j].claimingIds.add(split[0]);
            }
        }

        int getOverlappingCount() {
            int count = 0;
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) if (grid[i][j].claimingIds.size() > 1) count++;
            }
            return count;
        }

        String getNonOverlappingClaimingId() {
            Set<String> nonOverlappingClaimingIds = new HashSet<>(claimingIds);
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) if (grid[i][j].claimingIds.size() > 1) nonOverlappingClaimingIds.removeAll(grid[i][j].claimingIds);
            }
            return nonOverlappingClaimingIds.iterator().next();
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Fabric fabric = new Fabric();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) fabric.process(currentLine);
            LOGGER.info("PART 1: {}", fabric.getOverlappingCount());
            LOGGER.info("PART 2: {}", fabric.getNonOverlappingClaimingId());
        }
    }
}
