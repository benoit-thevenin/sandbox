package fr.phoenyx.adventofcode.aoc2024;

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
import java.util.stream.Collectors;

public class AdventOfCode15 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);
    private static final Set<Dir> VERTICAL_DIRS = Set.of(Dir.N, Dir.S);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            List<String> mapLines = lines.subList(0, lines.indexOf(""));
            CharGrid grid = new CharGrid(mapLines);
            String moves = lines.subList(lines.indexOf("") + 1, lines.size()).stream().reduce(String::concat).orElseThrow();
            applyRobotMoves(grid, moves);
            LOGGER.info("PART 1: {}", getGpsSum(grid));
            grid = getScaledGrid(mapLines);
            applyRobotMoves(grid, moves);
            LOGGER.info("PART 2: {}", getGpsSum(grid));
        }
    }

    private static void applyRobotMoves(CharGrid grid, String moves) {
        Coord2 robot = grid.getCoordinatesMatching('@').get(0);
        grid.grid[robot.x][robot.y] = '.';
        for (char c : moves.toCharArray()) {
            Dir dir = Dir.fromChar(c);
            Coord2 next = robot.move(dir);
            Set<Coord2> pushed = getPushed(grid, next, dir);
            if (pushed.stream().noneMatch(pos -> grid.grid[pos.x][pos.y] == '#')) {
                moveBoxes(grid, pushed, dir);
                robot = next;
            }
        }
    }

    private static Set<Coord2> getPushed(CharGrid grid, Coord2 start, Dir dir) {
        Queue<Coord2> toVisit = new LinkedList<>();
        toVisit.add(start);
        Set<Coord2> pushed = new HashSet<>(toVisit);
        while (pushed.stream().noneMatch(pos -> grid.grid[pos.x][pos.y] == '#') && !toVisit.isEmpty()) {
            Coord2 current = toVisit.remove();
            if (grid.grid[current.x][current.y] == '.') continue;
            if ('O' != grid.grid[current.x][current.y] && VERTICAL_DIRS.contains(dir)) {
                Coord2 neigh = grid.grid[current.x][current.y] == '[' ? current.move(Dir.E) : current.move(Dir.W);
                if (pushed.add(neigh)) toVisit.add(neigh);
            }
            Coord2 next = current.move(dir);
            if (pushed.add(next)) toVisit.add(next);
        }
        return pushed.stream().filter(pos -> grid.grid[pos.x][pos.y] != '.').collect(Collectors.toSet());
    }

    private static void moveBoxes(CharGrid grid, Set<Coord2> pushed, Dir dir) {
        Set<Coord2> toMove = getNextBoxesToMove(grid, pushed, dir);
        while (!toMove.isEmpty()) {
            for (Coord2 current : toMove) {
                move(grid, current, dir);
                pushed.remove(current);
            }
            toMove = getNextBoxesToMove(grid, pushed, dir);
        }
    }

    private static Set<Coord2> getNextBoxesToMove(CharGrid grid, Set<Coord2> pushed, Dir dir) {
        return pushed.stream().filter(pos -> {
            Coord2 next = pos.move(dir);
            return grid.grid[next.x][next.y] == '.';
        }).collect(Collectors.toSet());
    }

    private static void move(CharGrid grid, Coord2 pos, Dir dir) {
        Coord2 next = pos.move(dir);
        grid.grid[next.x][next.y] = grid.grid[pos.x][pos.y];
        grid.grid[pos.x][pos.y] = '.';
    }

    private static int getGpsSum(CharGrid grid) {
        int sum = 0;
        for (int i = 0; i < grid.width; i++) {
            for (int j = 0; j < grid.height; j++) if (grid.grid[i][j] == 'O' || grid.grid[i][j] == '[') sum += 100 * j + i;
        }
        return sum;
    }

    private static CharGrid getScaledGrid(List<String> mapLines) {
        List<String> scaledLines = new ArrayList<>();
        for (String line : mapLines) {
            StringBuilder sb = new StringBuilder();
            for (char c : line.toCharArray()) {
                if (c == '#' || c == '.') {
                    sb.append(c);
                    sb.append(c);
                } else if (c == 'O') {
                    sb.append('[');
                    sb.append(']');
                } else {
                    sb.append('@');
                    sb.append('.');
                }
            }
            scaledLines.add(sb.toString());
        }
        return new CharGrid(scaledLines);
    }
}
