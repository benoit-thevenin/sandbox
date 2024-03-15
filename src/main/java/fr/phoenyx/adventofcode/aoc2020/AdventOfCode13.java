package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.MathUtils;

public class AdventOfCode13 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            boolean isFirstLine = true;
            long timestamp = 0;
            List<Entry<Long, Long>> offsets = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                if (isFirstLine) timestamp = Long.parseLong(currentLine);
                else {
                    String[] split = currentLine.split(",");
                    for (int i = 0; i < split.length; i++) if (!"x".equals(split[i])) offsets.add(new SimpleEntry<>(Long.parseLong(split[i]), (long) i));
                }
                isFirstLine = false;
            }
            LOGGER.info("PART 1: {}", getFirstBusToAirport(timestamp, offsets));
            LOGGER.info("PART 2: {}", getFirstTimestampMatchingOffsets(offsets));
        }
    }

    private static long getFirstBusToAirport(long timestamp, List<Entry<Long, Long>> offsets) {
        long current = timestamp;
        while (true) {
            long finalCurrent = current;
            Optional<Long> bus = offsets.stream().map(Entry::getKey).filter(id -> finalCurrent % id == 0).findAny();
            if (bus.isPresent()) return (current - timestamp) * bus.get();
            current++;
        }
    }

    private static long getFirstTimestampMatchingOffsets(List<Entry<Long, Long>> offsets) {
        long timestamp = 1;
        long period = 1;
        for (Entry<Long, Long> offset : offsets) {
            while ((timestamp + offset.getValue()) % offset.getKey() != 0) timestamp += period;
            period = MathUtils.leastCommonMultiple(period, offset.getKey());
        }
        return timestamp;
    }
}
