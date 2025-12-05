package fr.phoenyx.adventofcode.aoc2025;

import fr.phoenyx.models.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdventOfCode05 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Set<Range> ranges = new HashSet<>();
            boolean isRange = true;
            int count = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isEmpty()) isRange = false;
                else if (isRange) ranges.add(Range.buildFromStartAndEndInclusive(currentLine));
                else {
                    long id = Long.parseLong(currentLine);
                    if (ranges.stream().anyMatch(r -> r.isInRange(id))) count++;
                }
            }
            List<Range> nonOverlappingRanges = new ArrayList<>();
            while (!ranges.isEmpty()) {
                Range range = ranges.iterator().next();
                ranges.remove(range);
                while (true) {
                    Range finalRange = range;
                    Optional<Range> overlappingRange = ranges.stream().filter(r -> finalRange.intersection(r).isPresent()).findFirst();
                    if (overlappingRange.isEmpty()) break;
                    range = range.union(overlappingRange.get());
                    ranges.remove(overlappingRange.get());
                }
                nonOverlappingRanges.add(range);
            }
            LOGGER.info("PART 1: {}", count);
            LOGGER.info("PART 2: {}", nonOverlappingRanges.stream().map(r -> r.length).reduce(0L, Long::sum));
        }
    }
}
