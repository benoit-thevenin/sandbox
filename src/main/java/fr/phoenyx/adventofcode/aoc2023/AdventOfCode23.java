package fr.phoenyx.adventofcode.aoc2023;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class AdventOfCode23 {

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
                if (!visited.contains(link.target))
                    maxLength = Math.max(maxLength, getLongestHikeLength(link.target, visited, steps + link.weight));
            }
            visited.remove(position);
            return maxLength;
        }
    }

    private static class Node {
        final Coord2 position;
        final List<Link> links = new ArrayList<>();

        Node(Coord2 position) {
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

    private static class Grid extends CharGrid {
        Coord2 start;
        Coord2 exit;

        Grid(List<String> lines) {
            super(lines);
            List<Coord2> walkableCoords = getCoordinatesMatching(c -> c != '#');
            start = walkableCoords.stream().filter(c -> c.y == 0).findFirst().orElseThrow();
            exit = walkableCoords.stream().filter(c -> c.y == height - 1).findFirst().orElseThrow();
        }

        Graph buildGraph() {
            Graph graph = new Graph(new Node(start));
            Set<Coord2> visitedNodes = new HashSet<>();
            Queue<Node> toVisit = new LinkedList<>();
            toVisit.add(graph.start);
            visitedNodes.add(start);
            while (!toVisit.isEmpty()) {
                Node current = toVisit.remove();
                List<Coord2> neighbours = getNextPositions(current.position, new HashSet<>(), Dir.FOUR_NEIGHBOURS_VALUES);
                for (Coord2 neighbour : neighbours) {
                    Set<Coord2> visited = new HashSet<>();
                    visited.add(current.position);
                    visited.add(neighbour);
                    Coord2 target = neighbour;
                    List<Coord2> nextPositions = getNextPositions(neighbour, visited, Dir.FOUR_NEIGHBOURS_VALUES);
                    while (nextPositions.size() == 1) {
                        target = nextPositions.get(0);
                        visited.add(target);
                        nextPositions = getNextPositions(target, visited, Dir.FOUR_NEIGHBOURS_VALUES);
                    }
                    Node next;
                    if (visitedNodes.contains(target)) {
                        Coord2 finalTarget = target;
                        next = graph.nodes.stream().filter(n -> n.position.equals(finalTarget)).findAny().orElseThrow();
                    } else {
                        next = new Node(target);
                        graph.nodes.add(next);
                        toVisit.add(next);
                        visitedNodes.add(target);
                        if (target.equals(exit)) graph.exit = next;
                    }
                    current.addLink(next, visited.size() - 1);
                }
            }
            return graph;
        }

        int getLongestHikeLength() {
            return getLongestHikeLength(start, new HashSet<>());
        }

        private int getLongestHikeLength(Coord2 position, Set<Coord2> visited) {
            Set<Coord2> added = new HashSet<>();
            added.add(position);
            visited.add(position);
            List<Coord2> nextPositions = getNextPositions(position, visited, getPossibleDirs(position));
            while (nextPositions.size() == 1) {
                Coord2 next = nextPositions.get(0);
                if (next.equals(exit)) {
                    int result = visited.size();
                    visited.removeAll(added);
                    return result;
                }
                added.add(next);
                visited.add(next);
                nextPositions = getNextPositions(next, visited, getPossibleDirs(next));
            }
            return nextPositions.stream().map(next -> getLongestHikeLength(next, visited)).max(Integer::compare).orElse(0);
        }

        private List<Coord2> getNextPositions(Coord2 current, Set<Coord2> visited, List<Dir> possibleDirs) {
            return possibleDirs.stream().map(current::move).filter(this::isInGrid).filter(next -> get(next) != '#' && !visited.contains(next)).toList();
        }

        private List<Dir> getPossibleDirs(Coord2 coord) {
            try {
                return List.of(Dir.fromChar(get(coord)));
            } catch (IllegalArgumentException e) {
                return Dir.FOUR_NEIGHBOURS_VALUES;
            }
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
