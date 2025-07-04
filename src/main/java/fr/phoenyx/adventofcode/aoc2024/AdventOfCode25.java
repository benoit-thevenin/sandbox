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

public class AdventOfCode25 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode25.class);
    private static int height = -1;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode25.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<List<Long>> locks = new ArrayList<>();
            List<List<Long>> keys = new ArrayList<>();
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isEmpty()) addLockOrKey(locks, keys, lines);
                else lines.add(currentLine);
            }
            addLockOrKey(locks, keys, lines);
            LOGGER.info("SOLUTION: {}", countLockKeyPairFitting(locks, keys));
        }
    }

    private static void addLockOrKey(List<List<Long>> locks, List<List<Long>> keys, List<String> lines) {
        CharGrid grid = new CharGrid(lines);
        if (height == -1) height = grid.height;
        else if (height != grid.height) throw new IllegalArgumentException("Wrong input: heights don't match");
        if (grid.get(0, 0) == '#') locks.add(getHeights(grid));
        else keys.add(getHeights(grid));
        lines.clear();
    }

    private static List<Long> getHeights(CharGrid grid) {
        List<Long> heights = new ArrayList<>();
        List<Coord2> coords = grid.getCoordinatesMatching(c -> c == '#');
        for (int i = 0; i < grid.width; i++) {
            int x = i;
            heights.add(coords.stream().filter(c -> c.x == x).count());
        }
        return heights;
    }

    private static int countLockKeyPairFitting(List<List<Long>> locks, List<List<Long>> keys) {
        int count = 0;
        for (List<Long> lock : locks) {
            for (List<Long> key : keys) if (!isOverlapping(lock, key)) count++;
        }
        return count;
    }

    private static boolean isOverlapping(List<Long> lock, List<Long> key) {
        for (int i = 0; i < lock.size(); i++) if (lock.get(i) + key.get(i) > height) return true;
        return false;
    }
}
