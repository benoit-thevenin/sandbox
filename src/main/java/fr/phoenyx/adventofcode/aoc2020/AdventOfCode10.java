package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode10 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);
    private static final Map<Integer, Long> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Integer> jolts = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) jolts.add(Integer.parseInt(currentLine));
            Collections.sort(jolts);
            jolts.add(jolts.stream().max(Integer::compare).orElseThrow() + 3);
            int oneDifference = 0;
            int threeDifference = 0;
            int current = 0;
            for (int jolt : jolts) {
                if (jolt - current == 1) oneDifference++;
                else if (jolt - current == 3) threeDifference++;
                current = jolt;
            }
            LOGGER.info("PART 1: {}", oneDifference * threeDifference);
            LOGGER.info("PART 2: {}", getValidArrangementsCount(0, jolts));
        }
    }

    private static long getValidArrangementsCount(int last, List<Integer> jolts) {
        if (cache.containsKey(last)) return cache.get(last);
        List<Integer> choices = jolts.stream().filter(j -> j > last && j <= last + 3).toList();
        if (choices.isEmpty()) cache.put(last, 1L);
        else cache.put(last, choices.stream().map(c -> getValidArrangementsCount(c, jolts)).reduce(Long::sum).orElseThrow());
        return cache.get(last);
    }
}
