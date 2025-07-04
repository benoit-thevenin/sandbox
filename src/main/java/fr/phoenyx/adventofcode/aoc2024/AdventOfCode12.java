package fr.phoenyx.adventofcode.aoc2024;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdventOfCode12 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            List<Set<Coord2>> regions = new CharGrid(lines).getAllRegionsMatching(c -> true);
            LOGGER.info("PART 1: {}", regions.stream().map(region -> getPerimeter(region) * region.size()).reduce(0L, Long::sum));
            LOGGER.info("PART 2: {}", regions.stream().map(region -> getSides(region) * region.size()).reduce(0L, Long::sum));
        }
    }

    private static long getPerimeter(Set<Coord2> region) {
        long perimeter = 0;
        for (Coord2 coord : region) {
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) if (!region.contains(coord.move(dir))) perimeter++;
        }
        return perimeter;
    }

    private static long getSides(Set<Coord2> region) {
        long sides = 0;
        for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
            Set<Coord2> side = new HashSet<>();
            for (Coord2 coord : region) {
                Coord2 next = coord.move(dir);
                if (!region.contains(next)) side.add(next);
            }
            Set<Coord2> toRemove = new HashSet<>();
            for (Coord2 coord : side) {
                Coord2 next = coord.move(dir.fourNeighboursTurnRight());
                while (side.contains(next)) {
                    toRemove.add(next);
                    next = next.move(dir.fourNeighboursTurnRight());
                }
            }
            sides += side.size() - toRemove.size();
        }
        return sides;
    }
}
