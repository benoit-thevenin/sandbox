package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.adventofcode.aoc2019.AdventOfCode05.IntcodeComputer;

public class AdventOfCode15 {

    private static class Point extends Coord2 {
        boolean isWall;
        boolean isObjective;

        public Point(int x, int y) {
            super(x, y);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);
    private static final Point START = new Point(0, 0);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            long[] program = new long[0];
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                program = new long[split.length];
                for (int i = 0; i < split.length; i++) program[i] = Long.parseLong(split[i]);
            }
            Entry<Integer, Integer> result = getResult(program);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Entry<Integer, Integer> getResult(long[] program) {
        Set<Point> map = getMap(program);
        return new SimpleEntry<>(getStepsToTargets(START, map, Set.of(map.stream().filter(p -> p.isObjective).findAny().orElseThrow())), getTimeToFillOxygen(map));
    }

    private static Set<Point> getMap(long[] program) {
        IntcodeComputer computer = new IntcodeComputer(program);
        Set<Point> map = new HashSet<>();
        Point current = START;
        map.add(current);
        Set<Point> targets = getTargets(map);
        while (!targets.isEmpty()) {
            Set<Point> finalTargets = targets;
            int currentX = current.x;
            int currentY = current.y;
            Dir dir = Dir.FOUR_NEIGHBOURS_VALUES.stream()
                .filter(d -> {
                    int x = currentX + d.dx;
                    int y = currentY + d.dy;
                    return !map.contains(new Point(x, y)) || !map.stream().filter(p -> p.x == x && p.y == y).findAny().orElseThrow().isWall;
                })
                .min(Comparator.comparingInt(d -> getStepsToTargets(new Point(currentX + d.dx, currentY + d.dy), map, finalTargets)))
                .orElseThrow();
            Point neighbour = new Point(current.x + dir.dx, current.y + dir.dy);
            map.add(neighbour);
            long result = computer.run(getParameterFromDir(dir));
            if (result == 0) neighbour.isWall = true;
            else {
                current = neighbour;
                if (result == 2) neighbour.isObjective = true;
            }
            targets = getTargets(map);
        }
        return map;
    }

    private static Set<Point> getTargets(Set<Point> map) {
        Set<Point> toVisit = new HashSet<>();
        map.stream().filter(point -> !point.isWall).forEach(point -> {
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                Point neighbour = new Point(point.x + dir.dx, point.y + dir.dy);
                if (!map.contains(neighbour)) toVisit.add(neighbour);
            }
        });
        return toVisit;
    }

    private static int getStepsToTargets(Point start, Set<Point> map, Set<Point> targets) {
        if (targets.contains(start)) return 0;
        Set<Point> toVisit = new HashSet<>();
        toVisit.add(start);
        Set<Point> visited = new HashSet<>();
        int steps = 0;
        while (!toVisit.isEmpty()) {
            visited.addAll(toVisit);
            Set<Point> next = new HashSet<>();
            steps++;
            for (Point point : toVisit) {
                for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                    int x = point.x + dir.dx;
                    int y = point.y + dir.dy;
                    Point neighbour = map.stream().filter(p -> p.x == x && p.y == y).findAny().orElse(new Point(x, y));
                    if (targets.contains(neighbour)) return steps;
                    if (!neighbour.isWall && !visited.contains(neighbour)) next.add(neighbour);
                }
            }
            toVisit = next;
        }
        throw new IllegalStateException("The objective was not found");
    }

    private static long getParameterFromDir(Dir dir) {
        if (dir == Dir.N) return 1;
        if (dir == Dir.S) return 2;
        return dir == Dir.W ? 3 : 4;
    }

    private static int getTimeToFillOxygen(Set<Point> map) {
        Set<Point> toVisit = new HashSet<>();
        toVisit.add(map.stream().filter(point -> point.isObjective).findAny().orElseThrow());
        Set<Point> visited = new HashSet<>();
        int steps = 0;
        while (!toVisit.isEmpty()) {
            visited.addAll(toVisit);
            steps++;
            Set<Point> next = new HashSet<>();
            for (Point point : toVisit) {
                for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                    Optional<Point> neighbour = map.stream()
                        .filter(p -> !p.isWall && p.x == point.x + dir.dx && p.y == point.y + dir.dy)
                        .findAny();
                    if (neighbour.isPresent() && !visited.contains(neighbour.get())) next.add(neighbour.get());
                }
            }
            toVisit = next;
        }
        return steps - 1;
    }
}
