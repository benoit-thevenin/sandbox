package fr.phoenyx.adventofcode.aoc2025;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdventOfCode04 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            LOGGER.info("PART 1: {}", getAccessibleRolls(grid).size());
            LOGGER.info("PART 2: {}", countRemoveableRolls(grid));
        }
    }

    private static List<Coord2> getAccessibleRolls(CharGrid grid) {
        return grid.getCoordinatesMatching(c -> c == '@').stream().filter(c -> isAccessible(grid, c)).toList();
    }

    private static int countRemoveableRolls(CharGrid grid) {
        List<Coord2> accessibleRolls = getAccessibleRolls(grid);
        int initialCount = grid.getCoordinatesMatching(c -> c == '@').size();
        while (!accessibleRolls.isEmpty()) {
            for (Coord2 roll : accessibleRolls) grid.set(roll, '.');
            accessibleRolls = getAccessibleRolls(grid);
        }
        return initialCount - grid.getCoordinatesMatching(c -> c == '@').size();
    }

    private static boolean isAccessible(CharGrid grid, Coord2 coord) {
        int count = 0;
        for (Dir dir : Dir.values()) {
            Coord2 neighbour = coord.move(dir);
            if (grid.isInGrid(neighbour) && grid.get(neighbour) == '@') count++;
        }
        return count < 4;
    }
}
