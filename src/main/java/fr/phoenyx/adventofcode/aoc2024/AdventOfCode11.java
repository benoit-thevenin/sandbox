package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventOfCode11 {

    private record Param(Long stone, int count) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);
    private static final Map<Param, Long> RESULTS = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                List<Long> stones = Arrays.stream(currentLine.split(" ")).map(Long::parseLong).toList();
                LOGGER.info("PART 1: {}", applyProcessIteratively(stones, 25));
                long begin = System.nanoTime();
                LOGGER.info("PART 2: {}, time elapsed {}ms", applyProcessIteratively(stones, 75), (System.nanoTime() - begin) / 1000000);
                LOGGER.info("PART 1: {}", applyProcessRecursively(stones, 25));
                begin = System.nanoTime();
                LOGGER.info("PART 2: {}, time elapsed {}ms", applyProcessRecursively(stones, 75), (System.nanoTime() - begin) / 1000000);
            }
        }
    }

    private static long applyProcessIteratively(List<Long> stones, int count) {
        Map<Long, Long> counts = new HashMap<>();
        for (Long stone: stones) addInMap(counts, stone, 1L);
        for (int i = 0; i < count; i++) {
            Map<Long, Long> next = new HashMap<>();
            for (Map.Entry<Long, Long> entry: counts.entrySet()) {
                if (entry.getKey() == 0) addInMap(next, 1L, entry.getValue());
                else if (entry.getKey().toString().length() % 2 == 0) {
                    String stoneString = entry.getKey().toString();
                    addInMap(next, Long.parseLong(stoneString.substring(0, stoneString.length() / 2)), entry.getValue());
                    addInMap(next, Long.parseLong(stoneString.substring(stoneString.length() / 2)), entry.getValue());
                } else addInMap(next, entry.getKey() * 2024, entry.getValue());
            }
            counts = next;
        }
        return counts.values().stream().reduce(0L, Long::sum);
    }

    private static void addInMap(Map<Long, Long> map, Long key, Long value) {
        if (map.containsKey(key)) map.put(key, map.get(key) + value);
        else map.put(key, value);
    }

    private static long applyProcessRecursively(List<Long> stones, int count) {
        return stones.stream().map(stone -> blink(new Param(stone, count))).reduce(0L, Long::sum);
    }

    private static long blink(Param param) {
        if (RESULTS.containsKey(param)) return RESULTS.get(param);
        long result;
        if (param.count == 0) result = 1;
        else if (param.stone == 0) result = blink(new Param(1L, param.count - 1));
        else if (param.stone.toString().length() % 2 == 0) {
            String stoneString = param.stone.toString();
            long result1 = blink(new Param(Long.parseLong(stoneString.substring(0, stoneString.length() / 2)), param.count - 1));
            long result2 = blink(new Param(Long.parseLong(stoneString.substring(stoneString.length() / 2)), param.count - 1));
            result = result1 + result2;
        } else result = blink(new Param(param.stone * 2024, param.count - 1));
        RESULTS.put(param, result);
        return result;
    }
}
