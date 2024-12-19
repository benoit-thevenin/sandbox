package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventOfCode19 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode19.class);
    private static final List<String> TOWELS = new ArrayList<>();
    private static final Map<String, Long> CACHE = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode19.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            CACHE.put("", 1L);
            String currentLine;
            List<String> patterns = new ArrayList<>();
            boolean isPattern = false;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isEmpty()) isPattern = true;
                else if (isPattern) patterns.add(currentLine);
                else TOWELS.addAll(Arrays.stream(currentLine.split(", ")).toList());
            }
            LOGGER.info("PART 1: {}", patterns.stream().map(AdventOfCode19::getCount).filter(c -> c > 0).count());
            LOGGER.info("PART 2: {}", patterns.stream().map(AdventOfCode19::getCount).reduce(Long::sum).orElseThrow());
        }
    }

    private static long getCount(String pattern) {
        if (CACHE.containsKey(pattern)) return CACHE.get(pattern);
        CACHE.put(pattern, TOWELS.stream().filter(pattern::startsWith).map(t -> getCount(pattern.substring(t.length()))).reduce(Long::sum).orElse(0L));
        return CACHE.get(pattern);
    }
}
