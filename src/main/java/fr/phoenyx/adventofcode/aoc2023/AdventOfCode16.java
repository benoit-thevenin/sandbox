package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.AbstractGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode16 {

    private static class Beam extends Coord2 {
        final Dir dir;

        Beam(int x, int y, Dir dir) {
            super(x, y);
            this.dir = dir;
        }

        Beam(Coord2 coord, Dir dir) {
            this(coord.x, coord.y, dir);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Beam other = (Beam) o;
            return x == other.x && y == other.y && dir == other.dir;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, dir);
        }
    }

    private static class Tile {
        final char type;
        boolean isEnergized;

        Tile(char type) {
            this.type = type;
        }
    }

    private static class Contraption extends AbstractGrid {
        Tile[][] grid;

        Contraption(List<String> lines) {
            super(lines);
            grid = new Tile[width][height];
            for (int i = 0; i < height; i++) {
                String line = lines.get(i);
                for (int j = 0; j < width; j++) grid[j][i] = new Tile(line.charAt(j));
            }
        }

        int getBestEnergizedConfiguration() {
            List<Beam> beamsToTest = new ArrayList<>();
            for (int i = 0; i < width; i++) {
                beamsToTest.add(new Beam(i, 0, Dir.S));
                beamsToTest.add(new Beam(i, height - 1, Dir.N));
            }
            for (int i = 0; i < height; i++) {
                beamsToTest.add(new Beam(0, i, Dir.E));
                beamsToTest.add(new Beam(width - 1, i, Dir.W));
            }
            int bestEnergizedConfiguration = Integer.MIN_VALUE;
            for (Beam current : beamsToTest) {
                resetEnergized();
                processLight(current);
                bestEnergizedConfiguration = Math.max(bestEnergizedConfiguration, getEnergized());
            }
            return bestEnergizedConfiguration;
        }

        void processLight(Beam beam) {
            Queue<Beam> toVisit = new LinkedList<>();
            Set<Beam> visited = new HashSet<>();
            toVisit.add(beam);
            visited.add(beam);
            while (!toVisit.isEmpty()) {
                Beam current = toVisit.remove();
                grid[current.x][current.y].isEnergized = true;
                List<Beam> nextBeams = getNextBeams(current);
                for (Beam nextBeam : nextBeams) {
                    if (!visited.contains(nextBeam)) {
                        visited.add(nextBeam);
                        toVisit.add(nextBeam);
                    }
                }
            }
        }

        private boolean isBeamPassingThrough(char tileType, Beam beam) {
            return tileType == '.'
                || tileType == '|' && (beam.dir == Dir.N || beam.dir == Dir.S)
                || tileType == '-' && (beam.dir == Dir.W || beam.dir == Dir.E);
        }

        private List<Beam> getNextBeams(Beam beam) {
            char tileType = grid[beam.x][beam.y].type;
            if (isBeamPassingThrough(tileType, beam)) {
                Coord2 next = beam.move(beam.dir);
                return isInGrid(next) ? List.of(new Beam(next, beam.dir)) : Collections.emptyList();
            }
            if (tileType == '|' || tileType == '-') {
                List<Beam> beams = new ArrayList<>();
                List<Dir> dirs = tileType == '|' ? List.of(Dir.N, Dir.S) : List.of(Dir.W, Dir.E);
                for (Dir dir : dirs) {
                    Coord2 next = beam.move(dir);
                    if (isInGrid(next)) beams.add(new Beam(next, dir));
                }
                return beams;
            }
            Dir nextDir = beam.dir.getMirroredDir(tileType);
            Coord2 next = beam.move(nextDir);
            return isInGrid(next) ? List.of(new Beam(next, nextDir)) : Collections.emptyList();
        }

        int getEnergized() {
            int result = 0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) if (grid[i][j].isEnergized) result++;
            }
            return result;
        }

        void resetEnergized() {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) grid[i][j].isEnergized = false;
            }
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Contraption contraption = new Contraption(lines);
            contraption.processLight(new Beam(0, 0, Dir.E));
            LOGGER.info("PART 1: {}", contraption.getEnergized());
            LOGGER.info("PART 2: {}", contraption.getBestEnergizedConfiguration());
        }
    }
}
