package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.Coord;

public class AdventOfCode10 {

    private static class MovingCoord extends Coord {
        final int vx;
        final int vy;

        MovingCoord(int x, int y, int vx, int vy) {
            super(x, y);
            this.vx = vx;
            this.vy = vy;
        }

        MovingCoord move() {
            return new MovingCoord(x + vx, y + vy, vx, vy);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<MovingCoord> points = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.replaceAll("( )|(position=<)|(velocity=)|>", "");
                String[] split = currentLine.split("<");
                String[] position = split[0].split(",");
                String[] speed = split[1].split(",");
                points.add(new MovingCoord(Integer.parseInt(position[0]), Integer.parseInt(position[1]), Integer.parseInt(speed[0]), Integer.parseInt(speed[1])));
            }
            Entry<String, Integer> result = getResult(points);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Entry<String, Integer> getResult(List<MovingCoord> points) {
        int iterations = 0;
        int minHeight = Integer.MAX_VALUE;
        while (true) {
            List<MovingCoord> next = points.stream().map(MovingCoord::move).toList();
            int height = next.stream().map(p -> p.y).max(Integer::compare).orElseThrow() - next.stream().map(p -> p.y).min(Integer::compare).orElseThrow();
            if (height >= minHeight) return buildResult(points, iterations);
            minHeight = height;
            iterations++;
            points = next;
        }
    }

    private static Entry<String, Integer> buildResult(List<MovingCoord> points, int iterations) {
        int minX = points.stream().map(p -> p.x).min(Integer::compare).orElseThrow();
        int maxX = points.stream().map(p -> p.x).max(Integer::compare).orElseThrow();
        int minY = points.stream().map(p -> p.y).min(Integer::compare).orElseThrow();
        int maxY = points.stream().map(p -> p.y).max(Integer::compare).orElseThrow();
        StringBuilder sb = new StringBuilder();
        for (int y = minY; y <= maxY; y++) {
            sb.append('\n');
            for (int x = minX; x <= maxX; x++) {
                if (points.contains(new MovingCoord(x, y, 0, 0))) sb.append('#');
                else sb.append('.');
            }
        }
        return new SimpleEntry<>(sb.toString(), iterations);
    }
}
