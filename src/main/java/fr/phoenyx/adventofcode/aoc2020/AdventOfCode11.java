package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode11 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            LOGGER.info("PART 1: {}", countOccupiedSeats(new CharGrid(lines), true));
            LOGGER.info("PART 2: {}", countOccupiedSeats(new CharGrid(lines), false));
        }
    }

    private static int countOccupiedSeats(CharGrid grid, boolean isPart1) {
        processUntilStabilized(grid, isPart1);
        return grid.getCoordinatesMatching(c -> c == '#').size();
    }

    private static void processUntilStabilized(CharGrid grid, boolean isPart1) {
        int neighboursTolerance = isPart1 ? 4 : 5;
        boolean changed = true;
        while (changed) {
            changed = false;
            char[][] nextGrid = new char[grid.width][grid.height];
            for (int i = 0; i < grid.width; i++) {
                for (int j = 0; j < grid.height; j++) {
                    Coord2 pos = new Coord2(i, j);
                    int occupiedNeighbours = getOccupiedNeighbours(grid, isPart1, pos);
                    if (grid.get(pos) == 'L' && occupiedNeighbours == 0) {
                        changed = true;
                        nextGrid[i][j] = '#';
                    } else if (grid.get(pos) == '#' && occupiedNeighbours >= neighboursTolerance) {
                        changed = true;
                        nextGrid[i][j] = 'L';
                    } else nextGrid[i][j] = grid.get(pos);
                }
            }
            grid.copyGrid(nextGrid);
        }
    }

    private static int getOccupiedNeighbours(CharGrid grid, boolean isPart1, Coord2 pos) {
        int occupiedNeighbours = 0;
        for (Dir dir : Dir.values()) {
            Coord2 next = pos.move(dir);
            if (!isPart1) while (grid.isInGrid(next) && grid.get(next) == '.') next = next.move(dir);
            if (grid.isInGrid(next) && grid.get(next) == '#') occupiedNeighbours++;
        }
        return occupiedNeighbours;
    }
}
