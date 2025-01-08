package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;

public class AdventOfCode06 {

    private static class Point extends Coord2 {
        static int minX, maxX, minY, maxY;
        int id = -1;

        Point(int x, int y) {
            super(x, y);
        }

        boolean isOnEdge() {
            return x == minX || x == maxX || y == minY || y == maxY;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Point> points = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(", ");
                Point point = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                point.id = point.hashCode();
                points.add(point);
            }
            Set<Point> allPoints = initAllPoints(points);
            LOGGER.info("PART 1: {}", getLargestFiniteAreaSize(points, allPoints));
            LOGGER.info("PART 2: {}", allPoints.stream().filter(p -> points.stream().map(p::manhattanDistanceTo).reduce(0, Integer::sum) < 10000).count());
        }
    }

    private static Set<Point> initAllPoints(List<Point> points) {
        Point.minX = points.stream().min(Comparator.comparingInt(c -> c.x)).map(c -> c.x).orElseThrow();
        Point.maxX = points.stream().max(Comparator.comparingInt(c -> c.x)).map(c -> c.x).orElseThrow();
        Point.minY = points.stream().min(Comparator.comparingInt(c -> c.y)).map(c -> c.y).orElseThrow();
        Point.maxY = points.stream().max(Comparator.comparingInt(c -> c.y)).map(c -> c.y).orElseThrow();
        Set<Point> allPoints = new HashSet<>(points);
        for (int x = Point.minX; x <= Point.maxX; x++)
            for (int y = Point.minY; y <= Point.maxY; y++) allPoints.add(new Point(x, y));
        return allPoints;
    }

    private static int getLargestFiniteAreaSize(List<Point> points, Set<Point> allPoints) {
        setAllPointsIds(points, allPoints);
        Map<Point, Integer> finiteAreas = new HashMap<>();
        for (Point dangerous : points) {
            Set<Point> area = allPoints.stream().filter(p -> p.id == dangerous.id).collect(Collectors.toSet());
            if (area.stream().noneMatch(Point::isOnEdge)) finiteAreas.put(dangerous, area.size());
        }
        return finiteAreas.values().stream().max(Integer::compare).orElseThrow();
    }

    private static void setAllPointsIds(List<Point> points, Set<Point> allPoints) {
        for (Point point : allPoints) {
            if (point.id != -1) continue;
            Set<Point> closest = new HashSet<>();
            int minDistance = Integer.MAX_VALUE;
            for (Point dangerous : points) {
                int distance = dangerous.manhattanDistanceTo(point);
                if (distance <= minDistance) {
                    if (distance < minDistance) closest.clear();
                    minDistance = distance;
                    closest.add(dangerous);
                }
            }
            if (closest.size() == 1) point.id = closest.iterator().next().id;
        }
    }
}
