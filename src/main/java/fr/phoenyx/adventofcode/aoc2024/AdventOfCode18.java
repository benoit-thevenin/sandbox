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
            LOGGER.info("PART 2: {}, time elapsed {}ms", bytes.get(getFirstByteIndexCuttingPath(grid, bytes)).toString(), (System.nanoTime() - begin) / 1000000);
        }
    }

    private static CharGrid getGrid() {
        List<String> lines = new ArrayList<>();
        String line = ".".repeat(END.x + 1);
        for (int i = 0; i <= END.y; i++) lines.add(line);
        return new CharGrid(lines);
    }

    private static int getMinSteps(CharGrid grid) {
        List<Coord2> toVisit = new ArrayList<>();
        toVisit.add(START);
        Set<Coord2> visited = new HashSet<>(toVisit);
        int steps = 0;
        while (!toVisit.isEmpty()) {
            List<Coord2> next = new ArrayList<>();
            for (Coord2 current : toVisit) {
                if (current.equals(END)) return steps;
                for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                    Coord2 neigh = current.move(dir);
                    if (grid.isInGrid(neigh.x, neigh.y) && grid.grid[neigh.x][neigh.y] == '.' && visited.add(neigh)) next.add(neigh);
                }
            }
            toVisit = next;
            steps++;
        }
        throw new IllegalArgumentException("No path found");
    }

    private static int getFirstByteIndexCuttingPath(CharGrid grid, List<Coord2> bytes) {
        for (int i = 1024; i < bytes.size(); i++) {
            grid.grid[bytes.get(i).x][bytes.get(i).y] = '#';
            try {
                getMinSteps(grid);
            } catch (IllegalArgumentException e) {
                return i;
            }
        }
        throw new IllegalArgumentException("No time cutting path found");
    }
}
