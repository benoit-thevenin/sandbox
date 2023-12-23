package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.AbstractGrid;
import fr.phoenyx.models.Coord;
import fr.phoenyx.models.Dir;

public class AdventOfCode23 {

    private static class Point extends Coord {
        final char type;

        Point(int x, int y, char type) {
            super(x, y);
            this.type = type;
        }

        boolean isWalkable() {
            return type != '#';
        }

        List<Dir> getPossibleDirs() {
            try {
                return List.of(Dir.fromChar(type));
            } catch (IllegalArgumentException e) {
                return Dir.FOUR_NEIGHBOURS_VALUES;
            }
        }
    }

    private static class Graph {
        final Set<Node> nodes = new HashSet<>();
        final Node start;
        Node exit;

        Graph(Node start) {
            this.start = start;
            nodes.add(start);
        }

        int getLongestHikeLength() {
            return getLongestHikeLength(start, new HashSet<>(), 0);
        }

        int getLongestHikeLength(Node position, Set<Node> visited, int steps) {
            if (position == exit) return steps;
            visited.add(position);
            int maxLength = 0;
            for (Link link : position.links) {
                if (!visited.contains(link.target)) {
                    int length = getLongestHikeLength(link.target, new HashSet<>(visited), steps + link.weight);
                    if (length > maxLength) maxLength = length;
                }
            }
            return maxLength;
        }
    }

    private static class Node {
        final Point position;
        final List<Link> links = new ArrayList<>();

        Node(Point position) {
            this.position = position;
        }

        void addLink(Node target, int weight) {
            if (links.stream().noneMatch(l -> l.target == target)) {
                links.add(new Link(target, weight));
                target.links.add(new Link(this, weight));
            }
        }
    }

    private record Link(Node target, int weight) {}

    private static class Grid extends AbstractGrid {
        final Point[][] map;
        Point start;
        Point exit;

        Grid(List<String> lines) {
            super(lines);
            map = new Point[width][height];
            for (int i = 0; i < height; i++) {
                String line = lines.get(i);
                for (int j = 0; j < width; j++) {
                    map[j][i] = new Point(j, i, line.charAt(j));
                    if (i == 0 && map[j][i].isWalkable()) start = map[j][i];
                    if (i == height - 1 && map[j][i].isWalkable()) exit = map[j][i];
                }
            }
        }

        Graph buildGraph() {
            Graph graph = new Graph(new Node(start));
            Set<Point> visitedNodes = new HashSet<>();
            Queue<Node> toVisit = new LinkedList<>();
            toVisit.add(graph.start);
            visitedNodes.add(start);
            while (!toVisit.isEmpty()) {
                Node current = toVisit.remove();
                List<Point> neighbours = getNextPositions(current.position, new HashSet<>(), Dir.FOUR_NEIGHBOURS_VALUES);
                for (Point neighbour : neighbours) {
                    Set<Point> visited = new HashSet<>();
                    visited.add(current.position);
                    visited.add(neighbour);
                    Point target = neighbour;
                    List<Point> nextPositions = getNextPositions(neighbour, visited, Dir.FOUR_NEIGHBOURS_VALUES);
                    while (nextPositions.size() == 1) {
                        target = nextPositions.get(0);
                        visited.add(target);
                        nextPositions = getNextPositions(target, visited, Dir.FOUR_NEIGHBOURS_VALUES);
                    }
                    Node next;
                    if (visitedNodes.contains(target)) {
                        Point finalTarget = target;
                        next = graph.nodes.stream().filter(n -> n.position == finalTarget).findAny().orElseThrow();
                    } else {
                        next = new Node(target);
                        graph.nodes.add(next);
                        toVisit.add(next);
                        visitedNodes.add(target);
                        if (target == exit) graph.exit = next;
                    }
                    current.addLink(next, visited.size() - 1);
                }
            }
            return graph;
        }

        int getLongestHikeLength() {
            return getLongestHikeLength(start, new HashSet<>(), 0);
        }

        private int getLongestHikeLength(Point position, Set<Point> visited, int steps) {
            if (position == exit) return steps;
            visited.add(position);
            List<Point> nextPositions = getNextPositions(position, visited, position.getPossibleDirs());
            while (nextPositions.size() == 1) {
                Point next = nextPositions.get(0);
                steps++;
                if (next == exit) return steps;
                visited.add(next);
                nextPositions = getNextPositions(next, visited, next.getPossibleDirs());
            }
            int maxLength = 0;
            for (Point next : nextPositions) {
                int length = getLongestHikeLength(next, new HashSet<>(visited), steps + 1);
                if (length > maxLength) maxLength = length;
            }
            return maxLength;
        }

        private List<Point> getNextPositions(Point current, Set<Point> visited, List<Dir> possibleDirs) {
            List<Point> nextPositions = new ArrayList<>();
            for (Dir dir : possibleDirs) {
                int x = current.x + dir.dx;
                int y = current.y + dir.dy;
                if (isInGrid(x, y) && map[x][y].isWalkable() && !visited.contains(map[x][y])) nextPositions.add(map[x][y]);
            }
            return nextPositions;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode23.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode23.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Grid grid = new Grid(lines);
            long begin = System.nanoTime();
            LOGGER.info("PART 1: {}, time elapsed: {}ms", grid.getLongestHikeLength(), (System.nanoTime() - begin) / 1000000);
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed: {}ms", grid.buildGraph().getLongestHikeLength(), (System.nanoTime() - begin) / 1000000);
        }
    }
}
