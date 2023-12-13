package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode05 {

    private static class Range {
        long destinationStart;
        long sourceStart;
        long length;

        Range(long destinationStart, long sourceStart, long length) {
            this.destinationStart = destinationStart;
            this.sourceStart = sourceStart;
            this.length = length;
        }

        Range(String line) {
            this(Long.parseLong(line.split(" ")[0]), Long.parseLong(line.split(" ")[1]), Long.parseLong(line.split(" ")[2]));
        }

        boolean isSourceInRange(long source) {
            return source >= sourceStart && sourceStart + length > source;
        }

        long getDestination(long source) {
            return source - sourceStart + destinationStart;
        }

        Optional<SeedRange> intersection(SeedRange seedRange) {
            if (seedRange.start + seedRange.length <= sourceStart
             || sourceStart + length <= seedRange.start) return Optional.empty();
            long start = Math.max(seedRange.start, sourceStart);
            long end = Math.min(seedRange.start + seedRange.length, sourceStart + length);
            return Optional.of(new SeedRange(start, end - start));
        }
    }

    private static class SeedRange implements Comparable<SeedRange> {
        long start;
        long length;

        SeedRange(long start, long length) {
            this.start = start;
            this.length = length;
        }

        @Override
        public int compareTo(SeedRange o) {
            return Long.compare(start, o.start);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    private static final List<List<Long>> seedValues = new ArrayList<>();
    private static final List<List<SeedRange>> seedRanges = new ArrayList<>();

    private static final List<List<Range>> ranges = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode05.txt";
        seedValues.add(new ArrayList<>());
        seedRanges.add(new ArrayList<>());
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) continue;
                if (currentLine.startsWith("seeds: ")) {
                    String[] split = currentLine.split("seeds: ")[1].split(" ");
                    seedValues.get(0).addAll(Arrays.stream(split).map(Long::parseLong).toList());
                    for (int i = 0; i < split.length; i += 2) {
                        seedRanges.get(0).add(new SeedRange(Long.parseLong(split[i]), Long.parseLong(split[i + 1])));
                    }
                }
                else if (currentLine.contains("-")) ranges.add(new ArrayList<>());
                else ranges.get(ranges.size() - 1).add(new Range(currentLine));
            }
            setSeedValues();
            setSeedRanges();
            LOGGER.info("PART 1: {}", seedValues.get(seedValues.size() - 1).stream().sorted().findFirst().orElseThrow());
            LOGGER.info("PART 2: {}", seedRanges.get(seedRanges.size() - 1).stream().sorted().findFirst().orElseThrow().start);
        }
    }

    private static void setSeedValues() {
        for (int i = 0; i < ranges.size(); i++) {
            List<Long> sourceValues = seedValues.get(i);
            List<Long> destinationValues = new ArrayList<>();
            List<Range> map = ranges.get(i);
            for (long source : sourceValues) {
                long destination = map.stream().filter(r -> r.isSourceInRange(source)).findFirst().map(r -> r.getDestination(source)).orElse(source);
                destinationValues.add(destination);
            }
            seedValues.add(destinationValues);
        }
    }

    private static void setSeedRanges() {
        for (int i = 0; i < ranges.size(); i++) {
            List<SeedRange> sourceRanges = seedRanges.get(i);
            List<SeedRange> destinationRanges = new ArrayList<>();
            List<Range> map = ranges.get(i);
            for (SeedRange source : sourceRanges) {
                List<SeedRange> destinations = map.stream().map(r -> r.intersection(source)).filter(Optional::isPresent).map(Optional::get).sorted().toList();
                List<SeedRange> unmapped = new ArrayList<>();
                if (destinations.isEmpty()) unmapped.add(source);
                else {
                    Iterator<SeedRange> iterator = destinations.iterator();
                    SeedRange current = iterator.next();
                    if (current.start > source.start)
                        unmapped.add(new SeedRange(source.start, current.start - source.start));
                    while (iterator.hasNext()) {
                        SeedRange previous = current;
                        current = iterator.next();
                        if (previous.start + previous.length < current.start)
                            unmapped.add(new SeedRange(previous.start + previous.length, current.start - previous.start - previous.length));
                    }
                    if (current.start + current.length < source.start + source.length)
                        unmapped.add(new SeedRange(current.start + current.length, source.start + source.length - current.start - current.length));
                }
                for (SeedRange seedRange : destinations) {
                    Range range = map.stream().filter(r -> r.intersection(seedRange).isPresent()).findFirst().orElseThrow();
                    destinationRanges.add(new SeedRange(range.destinationStart, seedRange.length));
                }
                destinationRanges.addAll(unmapped);
            }
            seedRanges.add(destinationRanges);
        }
    }
}
