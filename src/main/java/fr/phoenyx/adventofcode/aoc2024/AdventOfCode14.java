package fr.phoenyx.adventofcode.aoc2024;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.MovingCoord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdventOfCode14 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);
    private static final int WIDTH = 101;
    private static final int HEIGHT = 103;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<MovingCoord2> robots = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" ");
                String[] pos = split[0].substring(2).split(",");
                String[] speed = split[1].substring(2).split(",");
                robots.add(new MovingCoord2(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(speed[0]), Integer.parseInt(speed[1])));
            }
            Map.Entry<Long, Integer> result = getResult(robots);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Map.Entry<Long, Integer> getResult(List<MovingCoord2> robots) {
        long safetyFactor = 1;
        List<MovingCoord2> current = new ArrayList<>(robots);
        int iteration = 0;
        while (areRobotsOverlapping(current)) {
            iteration++;
            List<MovingCoord2> next = new ArrayList<>();
            for (MovingCoord2 robot : current) {
                MovingCoord2 nextRobot = robot.move();
                next.add(new MovingCoord2((nextRobot.x + WIDTH) % WIDTH, (nextRobot.y + HEIGHT) % HEIGHT, nextRobot.vx, nextRobot.vy));
            }
            current = next;
            if (iteration == 100) {
                safetyFactor *= current.stream().filter(robot -> robot.x < WIDTH / 2 && robot.y < HEIGHT / 2).count();
                safetyFactor *= current.stream().filter(robot -> robot.x < WIDTH / 2 && robot.y > HEIGHT / 2).count();
                safetyFactor *= current.stream().filter(robot -> robot.x > WIDTH / 2 && robot.y < HEIGHT / 2).count();
                safetyFactor *= current.stream().filter(robot -> robot.x > WIDTH / 2 && robot.y > HEIGHT / 2).count();
            }
        }
        printChristmasTree(current);
        return new SimpleEntry<>(safetyFactor, iteration);
    }

    private static boolean areRobotsOverlapping(List<MovingCoord2> robots) {
        return robots.stream().map(r -> new Coord2(r.x, r.y)).distinct().count() != robots.size();
    }

    private static void printChristmasTree(List<MovingCoord2> robots) {
        for (int y = 38; y < 71; y++) {
            int finalY = y;
            for (int x = 32; x < 63; x++) {
                int finalX = x;
                if (robots.stream().anyMatch(r -> r.x == finalX && r.y == finalY)) System.out.print('#');
                else System.out.print('.');
            }
            System.out.println();
        }
    }
}
