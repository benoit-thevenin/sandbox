package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode14 {

    private record Reindeer(int speed, int flyTime, int restTime) {
        int getDistanceAfter(int time) {
            int cycles = time / (flyTime + restTime);
            int cyclesModulo = time % (flyTime + restTime);
            return cycles * speed * flyTime + Math.min(cyclesModulo, flyTime) * speed;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Reindeer> reindeers = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" ");
                reindeers.add(new Reindeer(Integer.parseInt(split[3]), Integer.parseInt(split[6]), Integer.parseInt(split[13])));
            }
            LOGGER.info("PART 1: {}", reindeers.stream().map(r -> r.getDistanceAfter(2503)).max(Integer::compare).orElseThrow());
            LOGGER.info("PART 2: {}", getMaxPointsAfter2503seconds(reindeers));
        }
    }

    private static int getMaxPointsAfter2503seconds(List<Reindeer> reindeers) {
        Map<Reindeer, Integer> points = new HashMap<>();
        for (int i = 1; i <= 2503; i++) {
            int time = i;
            int maxDistance = reindeers.stream().map(r -> r.getDistanceAfter(time)).max(Integer::compare).orElseThrow();
            reindeers.stream().filter(r -> r.getDistanceAfter(time) == maxDistance).forEach(r -> points.put(r, points.getOrDefault(r, 0) + 1));
        }
        return points.values().stream().max(Integer::compare).orElseThrow();
    }
}
