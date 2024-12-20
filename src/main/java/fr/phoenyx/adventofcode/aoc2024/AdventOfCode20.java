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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
        Map<Coord2, Integer> posTimes = getPosTimes(grid);
        return posTimes.keySet().stream()
                .flatMap(start -> getCheatsFor(start, grid, posTimes, cheatTime).stream())
                .filter(time -> time > 99).toList();
    }

    private static Map<Coord2, Integer> getPosTimes(CharGrid grid) {
        Coord2 start = grid.getCoordinatesMatching('S').get(0);
        Queue<Coord2> toVisit = new LinkedList<>();
        toVisit.add(start);
        Map<Coord2, Integer> posTimes = new HashMap<>();
        posTimes.put(start, 0);
        while (!toVisit.isEmpty()) {
            Coord2 current = toVisit.remove();
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                Coord2 next = current.move(dir);
                if (grid.isInGrid(next.x, next.y) && grid.grid[next.x][next.y] != '#' && !posTimes.containsKey(next)) {
                    toVisit.add(next);
                    posTimes.put(next, posTimes.get(current) + 1);
                }
            }
        }
        return posTimes;
    }

    private static List<Integer> getCheatsFor(Coord2 start, CharGrid grid, Map<Coord2, Integer> posTimes, int cheatTime) {
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
                    if (grid.isInGrid(neigh.x, neigh.y) && visited.add(neigh)) {
                        next.add(neigh);
                        if (grid.grid[neigh.x][neigh.y] != '#') cheats.add(posTimes.get(neigh) - posTimes.get(start) - steps);
                    }
                }
            }
            toVisit = next;
        }
        return cheats;
    }
}
