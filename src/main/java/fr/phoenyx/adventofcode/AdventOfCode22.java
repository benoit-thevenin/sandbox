package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode22 {

    private static class Point {
        int x;
        int y;
        int z;

        Point(String line) {
            String[] split = line.split(",");
            x = Integer.parseInt(split[0]);
            y = Integer.parseInt(split[1]);
            z = Integer.parseInt(split[2]);
        }
    }

    private static class Brick {
        Point start;
        Point end;
        Set<Brick> supports = new HashSet<>();
        Set<Brick> supportedBy = new HashSet<>();

        Brick(String line) {
            String[] split = line.split("~");
            start = new Point(split[0]);
            end = new Point(split[1]);
        }

        int getHeight() {
            return Math.min(start.z, end.z);
        }

        void fall() {
            start.z--;
            end.z--;
        }

        void up() {
            start.z++;
            end.z++;
        }

        boolean isIntersecting(Brick other) {
            return Math.max(start.x, end.x) >= Math.min(other.start.x, other.end.x) && Math.min(start.x, end.x) <= Math.max(other.start.x, other.end.x)
                && Math.max(start.y, end.y) >= Math.min(other.start.y, other.end.y) && Math.min(start.y, end.y) <= Math.max(other.start.y, other.end.y)
                && Math.max(start.z, end.z) >= Math.min(other.start.z, other.end.z) && Math.min(start.z, end.z) <= Math.max(other.start.z, other.end.z);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode22.class);
    private static final List<Brick> bricks = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode22.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) bricks.add(new Brick(currentLine));
            long begin = System.nanoTime();
            stabilizeBricks();
            LOGGER.info("PART 1: {}, time elapsed: {}ms", getDisintegrableBricks().size(), (System.nanoTime() - begin) / 1000000);
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed: {}ms", computeResult(), (System.nanoTime() - begin) / 1000000);
        }
    }

    private static void stabilizeBricks() {
        bricks.sort(Comparator.comparingInt(Brick::getHeight));
        for (Brick brick : bricks) {
            List<Brick> intersectingBricks = getIntersectingBricks(brick);
            while (intersectingBricks.isEmpty() && brick.getHeight() > 0) {
                brick.fall();
                intersectingBricks = getIntersectingBricks(brick);
            }
            if (!intersectingBricks.isEmpty()) {
                brick.up();
                for (Brick intersecting : intersectingBricks) {
                    intersecting.supports.add(brick);
                    brick.supportedBy.add(intersecting);
                }
            }
        }
    }

    private static List<Brick> getIntersectingBricks(Brick brick) {
        return bricks.stream().filter(b -> b != brick && brick.isIntersecting(b)).toList();
    }

    private static List<Brick> getDisintegrableBricks() {
        return bricks.stream()
            .filter(brick -> brick.supports.stream().allMatch(b -> b.supportedBy.size() > 1))
            .toList();
    }

    private static int computeResult() {
        int result = 0;
        for (Brick brick : bricks) {
            if (brick.supports.isEmpty()) continue;
            Set<Brick> falling = new HashSet<>();
            falling.add(brick);
            Queue<Brick> toVisit = new LinkedList<>(brick.supports);
            while (!toVisit.isEmpty()) {
                Brick current = toVisit.remove();
                if (falling.containsAll(current.supportedBy)) {
                    falling.add(current);
                    toVisit.addAll(current.supports);
                }
            }
            result += falling.size() - 1;
        }
        return result;
    }
}
