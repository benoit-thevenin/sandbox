package fr.phoenyx.adventofcode.aoc2024;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdventOfCode18 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode18.class);
    private static final Coord2 START = new Coord2(0, 0);
    private static final Coord2 END = new Coord2(70, 70);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode18.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Coord2> bytes = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                bytes.add(new Coord2(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            }
            CharGrid grid = getGrid();
            bytes.subList(0, 1024).forEach(b -> grid.grid[b.x][b.y] = '#');
            LOGGER.info("PART 1: {}", getMinSteps(grid));
            long begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed {}ms", getFirstByteCuttingPath(grid, bytes).toString(), (System.nanoTime() - begin) / 1000000);
        }
    }

    private static CharGrid getGrid() {
        List<String> lines = new ArrayList<>();
        String line = ".".repeat(END.x + 1);
        for (int i = 0; i <= END.y; i++) lines.add(line);
        return new CharGrid(lines);
    }

    private static int getMinSteps(CharGrid grid) {
        return grid.getDistancesMatching(START, Character::equals).getOrDefault(END, -1);
    }

    private static Coord2 getFirstByteCuttingPath(CharGrid grid, List<Coord2> bytes) {
        int index = 1023;
        int steps = getMinSteps(grid);
        while (steps != -1) {
            index++;
            grid.set(bytes.get(index), '#');
            steps = getMinSteps(grid);
        }
        return bytes.get(index);
    }
}
