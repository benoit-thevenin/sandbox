package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Dir;

public class AdventOfCode11 {

    private static class Octopus {
        final int x;
        final int y;
        int energy;
        boolean hasFlashed;

        Octopus(int x, int y, int energy) {
            this.x = x;
            this.y = y;
            this.energy = energy;
        }

        boolean shallFlash() {
            return !hasFlashed && energy > 9;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            LOGGER.info("PART 1: {}", countFlashes(buildGrid(lines)));
            LOGGER.info("PART 2: {}", getFirstSynchronizedFlashStep(buildGrid(lines)));
        }
    }

    private static Octopus[][] buildGrid(List<String> lines) {
        Octopus[][] grid = new Octopus[lines.get(0).length()][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) grid[j][i] = new Octopus(j, i, line.charAt(j) - '0');
        }
        return grid;
    }

    private static int countFlashes(Octopus[][] grid) {
        int flashes = 0;
        for (int i = 0; i < 100; i++) {
            increaseEnergy(grid);
            flashes += chainFlash(grid);
            resetFlashingOctopi(grid);
        }
        return flashes;
    }

    private static int getFirstSynchronizedFlashStep(Octopus[][] grid) {
        int step = 0;
        int lastStepFlashes = 0;
        while (lastStepFlashes != grid.length * grid[0].length) {
            step++;
            increaseEnergy(grid);
            lastStepFlashes = chainFlash(grid);
            resetFlashingOctopi(grid);
        }
        return step;
    }

    private static void increaseEnergy(Octopus[][] grid) {
        for (Octopus[] octopi : grid) {
            for (Octopus octopus : octopi) octopus.energy++;
        }
    }

    private static int chainFlash(Octopus[][] grid) {
        int flashes = 0;
        Set<Octopus> flashingOctopi = getFlashingOctopi(grid);
        while (!flashingOctopi.isEmpty()) {
            flashes += flashingOctopi.size();
            for (Octopus octopus : flashingOctopi) {
                octopus.hasFlashed = true;
                for (Dir dir : Dir.values()) {
                    int x = octopus.x + dir.dx;
                    int y = octopus.y + dir.dy;
                    if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) grid[x][y].energy++;
                }
            }
            flashingOctopi = getFlashingOctopi(grid);
        }
        return flashes;
    }

    private static void resetFlashingOctopi(Octopus[][] grid) {
        for (Octopus[] octopi : grid) {
            for (Octopus octopus : octopi) {
                if (octopus.hasFlashed) {
                    octopus.hasFlashed = false;
                    octopus.energy = 0;
                }
            }
        }
    }

    private static Set<Octopus> getFlashingOctopi(Octopus[][] grid) {
        Set<Octopus> flashingOctopi = new HashSet<>();
        for (Octopus[] octopi : grid) {
            for (Octopus octopus : octopi) if (octopus.shallFlash()) flashingOctopi.add(octopus);
        }
        return flashingOctopi;
    }
}
