package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode10 {

    private enum Dir {
        N(0, -1), E(1, 0), S(0, 1), W(-1, 0);

        final int dx;
        final int dy;

        Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Dir getOpposite() {
            if (this == N) return S;
            if (this == E) return W;
            return this == S ? N : E;
        }

        Set<Dir> getAdjacents() {
            if (this == E || this == W) return Set.of(N, S);
            else return Set.of(E, W);
        }
    }

    private enum Pipe {
        NS, WE, NE, NW, SW, SE, START;

        static Pipe fromChar(char c) {
            if (c == '|') return NS;
            if (c == '-') return WE;
            if (c == 'L') return NE;
            if (c == 'J') return NW;
            if (c == '7') return SW;
            if (c == 'F') return SE;
            return c == 'S' ? START : null;
        }

        Set<Dir> getConnectedDirs() {
            return Arrays.stream(this.name().split("")).map(Dir::valueOf).collect(Collectors.toSet());
        }
    }

    private static class Point {
        int x;
        int y;
        Pipe pipe;
        Set<Point> neighbours = new HashSet<>();
        int distance = -1;

        Point(int x, int y, char c) {
            this.x = x;
            this.y = y;
            pipe = Pipe.fromChar(c);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);
    private static final List<String> LINES = new ArrayList<>();
    private static int width;
    private static int height;
    private static Point[][] grid;
    private static Point start;
    private static final Queue<Point> toVisit = new LinkedList<>();
    private static final Set<Integer> pairVisited = new HashSet<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) LINES.add(currentLine);
            buildGrid();
            computeAllNeighbours();
            LOGGER.info("PART 1: {}", computePart1());
            setOutsideFlags();
            LOGGER.info("PART 2: {}", computePart2());
        }
    }

    private static void buildGrid() {
        width = LINES.iterator().next().length();
        height = LINES.size();
        grid = new Point[width][height];
        for (int i = 0; i < height; i++) {
            String line = LINES.get(i);
            for (int j = 0; j < width; j++) {
                grid[j][i] = new Point(j, i, line.charAt(j));
                if (grid[j][i].pipe == Pipe.START) start = grid[j][i];
            }
        }
    }

    private static void computeAllNeighbours() {
        setStartPipe();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j].pipe == null) continue;
                for (Dir dir : grid[i][j].pipe.getConnectedDirs()) {
                    int x = i + dir.dx;
                    int y = j + dir.dy;
                    if (isInGrid(x, y) && grid[x][y].pipe != null
                    && grid[x][y].pipe.getConnectedDirs().stream().map(Dir::getOpposite).anyMatch(d -> d == dir)) grid[i][j].neighbours.add(grid[x][y]);
                }
            }
        }
    }

    private static void setStartPipe() {
        Set<Dir> startConnectedDirs = new HashSet<>();
        for (Dir dir : Dir.values()) {
            int x = start.x + dir.dx;
            int y = start.y + dir.dy;
            if (isInGrid(x, y) && grid[x][y].pipe != null && grid[x][y].pipe.getConnectedDirs().contains(dir.getOpposite()))
                startConnectedDirs.add(dir);
        }
        start.pipe = Arrays.stream(Pipe.values())
            .filter(p -> p != Pipe.START).filter(p -> p.getConnectedDirs().equals(startConnectedDirs)).findFirst().orElseThrow();
    }

    private static boolean isInGrid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private static int computePart1() {
        int maxDistance = 0;
        start.distance = 0;
        toVisit.add(start);
        while (!toVisit.isEmpty()) {
            Point current = toVisit.remove();
            for (Point neighbour : current.neighbours) {
                if (neighbour.distance != -1) continue;
                neighbour.distance = current.distance + 1;
                toVisit.add(neighbour);
                if (neighbour.distance > maxDistance) maxDistance = neighbour.distance;
            }
        }
        return maxDistance;
    }

    private static void setOutsideFlags() {
        setToVisit();
        while (!toVisit.isEmpty()) {
            Point current = toVisit.remove();
            Point squeezing = current.distance > -1 ? toVisit.remove() : null;
            if (squeezing == null) setOutsideNeighbourFlags(current);
            else {
                Set<Dir> squeezeDirs = current.x == squeezing.x ? Set.of(Dir.W, Dir.E) : Set.of(Dir.N, Dir.S);
                for (Dir dir : squeezeDirs) {
                    int x = current.x + dir.dx;
                    int y = current.y + dir.dy;
                    int sX = squeezing.x + dir.dx;
                    int sY = squeezing.y + dir.dy;
                    check(x, y);
                    check(sX, sY);
                    checkPair(x, y, sX, sY);
                    checkPair(current.x, current.y, x, y);
                    checkPair(squeezing.x, squeezing.y, sX, sY);
                }
            }
        }
    }

    private static void setOutsideNeighbourFlags(Point current) {
        for (int i = 0; i < Dir.values().length; i++) {
            Dir dir = Dir.values()[i];
            int x = current.x + dir.dx;
            int y = current.y + dir.dy;
            check(x, y);
            if (isInGrid(x, y) && grid[x][y].distance > -1) for (Dir d : dir.getAdjacents()) checkPair(x, y, x + d.dx, y + d.dy);
            Dir d = Dir.values()[(i + 1) % Dir.values().length];
            check(x + d.dx, y + d.dy);
        }
    }

    private static int computePart2() {
        int result = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[j][i].distance == -1) result++;
            }
        }
        return result;
    }

    private static void check(int x, int y) {
        if (isInGrid(x, y) && grid[x][y].distance == -1) {
            toVisit.add(grid[x][y]);
            grid[x][y].distance = -2;
        }
    }

    private static void checkPair(int x1, int y1, int x2, int y2) {
        if (!isInGrid(x1, y1) || !isInGrid(x2, y2)) return;
        int pairHash = getPairHash(grid[x1][y1], grid[x2][y2]);
        if (grid[x1][y1].distance > -1 && grid[x2][y2].distance > -1 && !pairVisited.contains(pairHash) && !grid[x1][y1].neighbours.contains(grid[x2][y2])) {
            toVisit.add(grid[x1][y1]);
            toVisit.add(grid[x2][y2]);
            pairVisited.add(pairHash);
        }
    }

    private static void setToVisit() {
        for (int i = 0; i < width; i++) {
            if (grid[i][0].distance == -1) {
                toVisit.add(grid[i][0]);
                grid[i][0].distance = -2;
            }
            if (grid[i][height - 1].distance == -1) {
                toVisit.add(grid[i][height - 1]);
                grid[i][height - 1].distance = -2;
            }
        }
        for (int i = 1; i < height - 1; i++) {
            if (grid[0][i].distance == -1) {
                toVisit.add(grid[0][i]);
                grid[0][i].distance = -2;
            }
            if (grid[width - 1][i].distance == -1) {
                toVisit.add(grid[width - 1][i]);
                grid[width - 1][i].distance = -2;
            }
        }
    }

    private static int getHash(Point point) {
        return point.x + width * point.y;
    }

    private static int getPairHash(Point a, Point b) {
        return a.x < b.x || a.x == b.x && a.y < b.y ? getHash(a) + width * height * getHash(b) : getHash(b) + width * height * getHash(a);
    }
}
