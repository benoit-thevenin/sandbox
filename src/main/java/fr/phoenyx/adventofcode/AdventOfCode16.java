package fr.phoenyx.adventofcode;

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

public class AdventOfCode16 {

    private enum Dir {
        N(0, -1),
        E(1, 0),
        S(0, 1),
        W(-1, 0);

        final int dx;
        final int dy;

        Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Dir getMirroredDir(char tileType) {
            if (tileType != '/' && tileType != '\\') throw new IllegalArgumentException("Can't get mirrored dir when not hitting a mirror");
            if (tileType == '/') {
                if (this == N) return E;
                if (this == E) return N;
                return this == S ? W : S;
            }
            if (this == N) return W;
            if (this == E) return S;
            return this == S ? E : N;
        }
    }

    private static class Beam {
        int x;
        int y;
        Dir dir;

        Beam(int x, int y, Dir dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
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
        int x;
        int y;
        char type;
        boolean isEnergized;

        Tile(int x, int y, char type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }

    private static class Contraption {
        int width;
        int height;
        Tile[][] grid;

        Contraption(List<String> lines) {
            width = lines.iterator().next().length();
            height = lines.size();
            grid = new Tile[width][height];
            for (int i = 0; i < height; i++) {
                String line = lines.get(i);
                for (int j = 0; j < width; j++) grid[j][i] = new Tile(j, i, line.charAt(j));
            }
        }

        int getBestEnergizedConfiguration() {
            Queue<Beam> beamsToTest = new LinkedList<>();
            for (int i = 0; i < width; i++) {
                beamsToTest.add(new Beam(i, 0, Dir.S));
                beamsToTest.add(new Beam(i, height - 1, Dir.N));
            }
            for (int i = 0; i < height; i++) {
                beamsToTest.add(new Beam(0, i, Dir.E));
                beamsToTest.add(new Beam(width - 1, i, Dir.W));
            }
            int bestEnergizedConfiguration = Integer.MIN_VALUE;
            while (!beamsToTest.isEmpty()) {
                Beam current = beamsToTest.remove();
                resetEnergized();
                processLight(current.x, current.y, current.dir);
                int energized = getEnergized();
                if (energized > bestEnergizedConfiguration) bestEnergizedConfiguration = energized;
            }
            return bestEnergizedConfiguration;
        }

        void processLight(int initialX, int initialY, Dir initialDir) {
            Queue<Beam> toVisit = new LinkedList<>();
            Set<Beam> visited = new HashSet<>();
            Beam initialBeam = new Beam(initialX, initialY, initialDir);
            toVisit.add(initialBeam);
            visited.add(initialBeam);
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

        private boolean isInGrid(int x, int y) {
            return x >= 0 && y >= 0 && x < width && y < height;
        }

        private boolean isBeamPassingThrough(char tileType, Beam beam) {
            return tileType == '.'
                || tileType == '|' && (beam.dir == Dir.N || beam.dir == Dir.S)
                || tileType == '-' && (beam.dir == Dir.W || beam.dir == Dir.E);
        }

        private List<Beam> getNextBeams(Beam beam) {
            char tileType = grid[beam.x][beam.y].type;
            if (isBeamPassingThrough(tileType, beam)) {
                int nextX = beam.x + beam.dir.dx;
                int nextY = beam.y + beam.dir.dy;
                return isInGrid(nextX, nextY) ? List.of(new Beam(nextX, nextY, beam.dir)) : Collections.emptyList();
            }
            if (tileType == '|' || tileType == '-') {
                List<Beam> beams = new ArrayList<>();
                List<Dir> dirs = tileType == '|' ? List.of(Dir.N, Dir.S) : List.of(Dir.W, Dir.E);
                for (Dir dir : dirs) {
                    int nextX = beam.x + dir.dx;
                    int nextY = beam.y + dir.dy;
                    if (isInGrid(nextX, nextY)) beams.add(new Beam(nextX, nextY, dir));
                }
                return beams;
            }
            Dir nextDir = beam.dir.getMirroredDir(tileType);
            int nextX = beam.x + nextDir.dx;
            int nextY = beam.y + nextDir.dy;
            return isInGrid(nextX, nextY) ? List.of(new Beam(nextX, nextY, nextDir)) : Collections.emptyList();
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
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Contraption contraption = new Contraption(lines);
            contraption.processLight(0, 0, Dir.E);
            LOGGER.info("PART 1: {}", contraption.getEnergized());
            LOGGER.info("PART 2: {}", contraption.getBestEnergizedConfiguration());
        }
    }
}
