package fr.phoenyx.adventofcode.aoc2023;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdventOfCode11 {

    private static class Universe extends CharGrid {
        final List<Coord2> galaxies = new ArrayList<>();
        final Set<Integer> expansionX = new HashSet<>();
        final Set<Integer> expansionY = new HashSet<>();

        Universe(List<String> lines) {
            super(lines);
            galaxies.addAll(getCoordinatesMatching(c -> c == '#'));
            setExpansions(true);
            setExpansions(false);
        }

        private void setExpansions(boolean isLine) {
            int first = isLine ? height : width;
            int second = isLine ? width : height;
            for (int i = 0; i < first; i++) {
                int x = i;
                int y = i;
                boolean isExpanding = true;
                for (int j = 0; j < second; j++) {
                    if (isLine) x = j;
                    else y = j;
                    if (get(x, y) == '#') {
                        isExpanding = false;
                        break;
                    }
                }
                if (isExpanding) {
                    if (isLine) expansionY.add(i);
                    else expansionX.add(i);
                }
            }
        }

        long getGalaxiesDistancesSum(int weight) {
            long result = 0;
            for (int i = 0; i < galaxies.size() - 1; i++) {
                Coord2 first = galaxies.get(i);
                for (int j = i + 1; j < galaxies.size(); j++) {
                    Coord2 second = galaxies.get(j);
                    long countExpansionX = expansionX.stream().filter(x -> x > Math.min(first.x, second.x) && x < Math.max(first.x, second.x)).count();
                    long countExpansionY = expansionY.stream().filter(x -> x > Math.min(first.y, second.y) && x < Math.max(first.y, second.y)).count();
                    result += first.manhattanDistanceTo(second) + (weight - 1) * (countExpansionX + countExpansionY);
                }
            }
            return result;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Universe universe = new Universe(lines);
            LOGGER.info("PART 1: {}", universe.getGalaxiesDistancesSum(2));
            LOGGER.info("PART 2: {}", universe.getGalaxiesDistancesSum(1000000));
        }
    }
}
