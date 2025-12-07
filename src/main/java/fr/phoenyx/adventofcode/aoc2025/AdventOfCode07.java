package fr.phoenyx.adventofcode.aoc2025;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class AdventOfCode07 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            LOGGER.info("PART 1: {}", countSplits(grid));
            LOGGER.info("PART 2: {}", countTimelines(grid));
        }
    }

    private static int countSplits(CharGrid grid) {
        Set<Coord2> splittersUsed = new HashSet<>();
        Set<Coord2> tachyons = new HashSet<>(grid.getCoordinatesMatching(c -> c == 'S'));
        for (int i = 0; i < grid.height - 1; i++) {
            Set<Coord2> next = new HashSet<>();
            for (Coord2 tachyon : tachyons) {
                Coord2 fall = tachyon.move(Dir.S);
                if (grid.get(fall) == '^') {
                    next.add(fall.move(Dir.W));
                    next.add(fall.move(Dir.E));
                    splittersUsed.add(fall);
                } else next.add(fall);
            }
            tachyons = next;
        }
        return splittersUsed.size();
    }

    private static long countTimelines(CharGrid grid) {
        Map<Coord2, Long> timelines = grid.getCoordinatesMatching(c -> c == 'S').stream().collect(Collectors.toMap(c -> c, c -> 1L));
        for (int i = 0; i < grid.height - 1; i++) {
            Map<Coord2, Long> nextTimelines = new HashMap<>();
            for (Entry<Coord2, Long> timeline : timelines.entrySet()) {
                Coord2 fall = timeline.getKey().move(Dir.S);
                if (grid.get(fall) == '^') {
                    nextTimelines.merge(fall.move(Dir.W), timeline.getValue(), Long::sum);
                    nextTimelines.merge(fall.move(Dir.E), timeline.getValue(), Long::sum);
                } else nextTimelines.merge(fall, timeline.getValue(), Long::sum);
            }
            timelines = nextTimelines;
        }
        return timelines.values().stream().reduce(0L, Long::sum);
    }
}
