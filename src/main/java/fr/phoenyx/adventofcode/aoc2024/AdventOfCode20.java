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
import java.util.Map;
import java.util.Set;

public class AdventOfCode20 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode20.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode20.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            LOGGER.info("PART 1: {}", getBestCheats(grid, 2).size());
            LOGGER.info("PART 2: {}", getBestCheats(grid, 20).size());
        }
    }

    private static List<Integer> getBestCheats(CharGrid grid, int cheatTime) {
        Map<Coord2, Integer> distances = grid.getDistancesMatching(grid.getCoordinatesMatching(c -> c == 'S').get(0), (c1, c2) -> c2 != '#');
        return distances.keySet().stream()
                .flatMap(start -> getCheatsFor(start, grid, distances, cheatTime).stream())
                .filter(time -> time > 99).toList();
    }

    private static List<Integer> getCheatsFor(Coord2 start, CharGrid grid, Map<Coord2, Integer> distances, int cheatTime) {
        List<Integer> cheats = new ArrayList<>();
        List<Coord2> toVisit = new ArrayList<>();
        toVisit.add(start);
        Set<Coord2> visited = new HashSet<>(toVisit);
        int steps = 0;
        while (!toVisit.isEmpty() && steps < cheatTime) {
            steps++;
            List<Coord2> next = new ArrayList<>();
            for (Coord2 current : toVisit) {
                for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                    Coord2 neigh = current.move(dir);
                    if (grid.isInGrid(neigh) && visited.add(neigh)) {
                        next.add(neigh);
                        if (grid.grid[neigh.x][neigh.y] != '#') cheats.add(distances.get(neigh) - distances.get(start) - steps);
                    }
                }
            }
            toVisit = next;
        }
        return cheats;
    }
}
