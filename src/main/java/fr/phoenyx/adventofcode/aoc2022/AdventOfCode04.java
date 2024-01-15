package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode04 {

    private static class Range {
        int start;
        int end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        Range(String line) {
            String[] split = line.split("-");
            start = Integer.parseInt(split[0]);
            end = Integer.parseInt(split[1]);
        }

        boolean contains(Range other) {
            return start <= other.start && end >= other.end;
        }

        Optional<Range> intersection(Range other) {
            if (start > other.end || end < other.start) return Optional.empty();
            return Optional.of(new Range(Math.max(start, other.start), Math.min(end, other.end)));
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int resultPart1 = 0;
            int resultPart2 = 0;
            while ((currentLine = reader.readLine()) != null) {
                String[] pair = currentLine.split(",");
                Range first = new Range(pair[0]);
                Range second = new Range(pair[1]);
                if (first.contains(second) || second.contains(first)) resultPart1++;
                if (first.intersection(second).isPresent()) resultPart2++;
            }
            LOGGER.info("PART 1: {}", resultPart1);
            LOGGER.info("PART 2: {}", resultPart2);
        }
    }
}
