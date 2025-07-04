package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.Range;

public class AdventOfCode05 {

    private record MappingRange(Range destinationRange, Range sourceRange) {
        long getDestination(long source) {
            return source - sourceRange.start + destinationRange.start;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    private static final List<List<Long>> seedValues = new ArrayList<>();
    private static final List<List<Range>> seedRanges = new ArrayList<>();
    private static final List<List<MappingRange>> ranges = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode05.txt";
        seedValues.add(new ArrayList<>());
        seedRanges.add(new ArrayList<>());
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("seeds: ")) {
                    String[] split = currentLine.split("seeds: ")[1].split(" ");
                    seedValues.get(0).addAll(Arrays.stream(split).map(Long::parseLong).toList());
                    for (int i = 0; i < split.length; i += 2) seedRanges.get(0).add(Range.buildFromStartAndLength(Long.parseLong(split[i]), Long.parseLong(split[i + 1])));
                } else if (currentLine.contains("-")) ranges.add(new ArrayList<>());
                else if (!currentLine.isBlank()) {
                    String[] split = currentLine.split(" ");
                    long length = Long.parseLong(split[2]);
                    ranges.get(ranges.size() - 1).add(new MappingRange(
                        Range.buildFromStartAndLength(Long.parseLong(split[0]), length),
                        Range.buildFromStartAndLength(Long.parseLong(split[1]), length))
                    );
                }
            }
            setSeedValues();
            setSeedRanges();
            LOGGER.info("PART 1: {}", seedValues.get(seedValues.size() - 1).stream().sorted().findFirst().orElseThrow());
            LOGGER.info("PART 2: {}", seedRanges.get(seedRanges.size() - 1).stream().min(Comparator.comparingLong(r -> r.start)).orElseThrow().start);
        }
    }

    private static void setSeedValues() {
        for (int i = 0; i < ranges.size(); i++) {
            List<Long> sourceValues = seedValues.get(i);
            List<Long> destinationValues = new ArrayList<>();
            List<MappingRange> map = ranges.get(i);
            sourceValues.stream()
                .map(source -> map.stream().filter(r -> r.sourceRange.isInRange(source)).findFirst().map(r -> r.getDestination(source)).orElse(source))
                .forEach(destinationValues::add);
            seedValues.add(destinationValues);
        }
    }

    private static void setSeedRanges() {
        for (int i = 0; i < ranges.size(); i++) {
            List<Range> sourceRanges = seedRanges.get(i);
            List<Range> destinationRanges = new ArrayList<>();
            List<MappingRange> map = ranges.get(i);
            for (Range source : sourceRanges) {
                List<Range> destinations = map.stream()
                    .map(r -> r.sourceRange.intersection(source))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .sorted(Comparator.comparingLong(r -> r.start)).toList();
                List<Range> unmapped = getUnmapped(source, destinations);
                for (Range seedRange : destinations) {
                    MappingRange mappingRange = map.stream().filter(r -> r.sourceRange.intersection(seedRange).isPresent()).findFirst().orElseThrow();
                    destinationRanges.add(Range.buildFromStartAndLength(mappingRange.destinationRange.start, seedRange.length));
                }
                destinationRanges.addAll(unmapped);
            }
            seedRanges.add(destinationRanges);
        }
    }

    private static List<Range> getUnmapped(Range source, List<Range> destinations) {
        if (destinations.isEmpty()) return List.of(source);
        List<Range> unmapped = new ArrayList<>();
        Iterator<Range> iterator = destinations.iterator();
        Range current = iterator.next();
        if (current.start > source.start) unmapped.add(Range.buildFromStartAndLength(source.start, current.start - source.start));
        while (iterator.hasNext()) {
            Range previous = current;
            current = iterator.next();
            if (previous.start + previous.length < current.start)
                unmapped.add(Range.buildFromStartAndLength(previous.start + previous.length, current.start - previous.start - previous.length));
        }
        if (current.start + current.length < source.start + source.length)
            unmapped.add(Range.buildFromStartAndLength(current.start + current.length, source.start + source.length - current.start - current.length));
        return unmapped;
    }
}
