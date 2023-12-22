package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
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

        boolean isHorizontallyCrossing(Brick other) {
            int minX = Math.min(start.x, end.x);
            int maxX = Math.max(start.x, end.x);
            int otherMinX = Math.min(other.start.x, other.end.x);
            int otherMaxX = Math.max(other.start.x, other.end.x);
            if (maxX < otherMinX || minX > otherMaxX) return false;
            int minY = Math.min(start.y, end.y);
            int maxY = Math.max(start.y, end.y);
            int otherMinY = Math.min(other.start.y, other.end.y);
            int otherMaxY = Math.max(other.start.y, other.end.y);
            return maxY >= otherMinY && minY <= otherMaxY;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode22.class);
    private static final List<Brick> bricks = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode22.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String test = """
                1,0,1~1,2,1
                0,0,2~2,0,2
                0,2,3~2,2,3
                0,0,4~0,2,4
                2,0,5~2,2,5
                0,1,6~2,1,6
                1,1,8~1,1,9
                """;
            for (String s : test.split("\n")) bricks.add(new Brick(s));
            stabilizeBricks();
            LOGGER.info("TEST 1: {}", getDisintegrableBricks().size());
            bricks.clear();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) bricks.add(new Brick(currentLine));
            stabilizeBricks();
            LOGGER.info("PART 1: {}", getDisintegrableBricks().size());
        }
    }

    private static void stabilizeBricks() {
        bricks.sort(Comparator.comparingInt(Brick::getHeight));
        for (Brick brick : bricks) {
            setSupportedBy(brick);
            while (brick.supportedBy.isEmpty() && brick.getHeight() > 0) {
                brick.fall();
                setSupportedBy(brick);
            }
        }
    }

    private static void setSupportedBy(Brick brick) {
        bricks.stream().filter(b -> b.getHeight() == brick.getHeight() - 1 && brick.isHorizontallyCrossing(b))
            .forEach(support -> {
                support.supports.add(brick);
                brick.supportedBy.add(support);
            });
    }

    private static List<Brick> getDisintegrableBricks() {
        return bricks.stream()
            .filter(brick -> brick.supports.stream().allMatch(b -> b.supportedBy.size() > 1))
            .toList();
    }
}
