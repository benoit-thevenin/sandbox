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

public class AdventOfCode06 {

    private record State(Coord2 pos, Dir dir) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);

    private static Coord2 start;
    private static Set<Coord2> visited = new HashSet<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            start = grid.getCoordinatesMatching(c -> c == '^').stream().findFirst().orElseThrow();
            visited = getVisitedPositions(grid);
            LOGGER.info("PART 1: {}", visited.size());
            long begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed: {}ms", countObstructionsPossibilities(grid), (System.nanoTime() - begin) / 1000000);
        }
    }

    private static Set<Coord2> getVisitedPositions(CharGrid grid) {
        Set<Coord2> visited = new HashSet<>();
        Set<State> visitedStates = new HashSet<>();
        Coord2 current = start;
        Dir currentDir = Dir.N;
        visited.add(current);
        visitedStates.add(new State(current, currentDir));
        while (true) {
            Coord2 next = current.move(currentDir);
            if (!grid.isInGrid(next)) return visited;
            if (grid.get(next) == '#') currentDir = currentDir.fourNeighboursTurnRight();
            else {
                current = next;
                visited.add(next);
            }
            State nextState = new State(current, currentDir);
            if (!visitedStates.add(nextState)) throw new IllegalArgumentException("Guard stuck in a loop !");
        }
    }

    private static int countObstructionsPossibilities(CharGrid grid) {
        int count = 0;
        for (Coord2 current : grid.getAllCoords()) {
            if (!start.equals(current) && visited.contains(current)) {
                grid.set(current, '#');
                try {
                    getVisitedPositions(grid);
                } catch (IllegalArgumentException e) {
                    count++;
                }
                grid.set(current, '.');
            }
        }
        return count;
    }
}
