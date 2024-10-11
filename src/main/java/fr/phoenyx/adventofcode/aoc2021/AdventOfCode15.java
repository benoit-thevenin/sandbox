package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode15 {

    private static class Node extends Coord2 {
        int cost;
        int heuristic;

        public Node(int x, int y) {
            super(x, y);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);
    private static final Node START = new Node(0, 0);
    private static Node END;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            END = new Node(grid.width - 1, grid.height - 1);
            LOGGER.info("PART 1: {}", getLowestRiskPath(grid));
            grid = extendGrid(grid);
            END = new Node(grid.width - 1, grid.height - 1);
            LOGGER.info("PART 2: {}", getLowestRiskPath(grid));
        }
    }

    private static int getLowestRiskPath(CharGrid grid) {
        Set<Node> closed = new HashSet<>();
        Set<Node> open = new HashSet<>();
        open.add(START);
        while (!open.isEmpty()) {
            Node current = open.stream().min(Comparator.comparingInt(n -> n.heuristic)).orElseThrow();
            open.remove(current);
            if (current.equals(END)) return current.cost;
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                Coord2 neighbour = current.move(dir);
                Node next = new Node(neighbour.x, neighbour.y);
                if (grid.isInGrid(next.x, next.y) && !closed.contains(next)) {
                    next.cost = current.cost + grid.grid[next.x][next.y] - '0';
                    next.heuristic = next.cost + next.manhattanDistanceTo(END);
                    if (open.stream().filter(next::equals).noneMatch(n -> n.cost < next.cost)) {
                        open.remove(next);
                        open.add(next);
                    }
                }
            }
            closed.add(current);
        }
        throw new IllegalArgumentException("Path not found");
    }

    private static CharGrid extendGrid(CharGrid grid) {
        CharGrid extendedGrid = new CharGrid(5 * grid.width, 5 * grid.height);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                for (int h = 0; h < grid.height; h++) {
                    for (int w = 0; w < grid.width; w++) {
                        int risk = grid.grid[w][h] + i + j;
                        if (risk > '0' + 9) risk -= 9;
                        extendedGrid.grid[w + i * grid.width][h + j * grid.height] = (char) risk;
                    }
                }
            }
        }
        return extendedGrid;
    }
}
