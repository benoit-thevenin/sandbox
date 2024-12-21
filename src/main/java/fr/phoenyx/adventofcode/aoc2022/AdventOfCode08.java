package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Entry<Integer, Integer> result = getResult(new CharGrid(lines));
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Entry<Integer, Integer> getResult(CharGrid grid) {
        int visibleTrees = 0;
        int bestScenicScore = 0;
        for (Coord2 tree : grid.getAllCoords()) {
            Entry<Boolean, Integer> treeResult = getResult(tree, grid);
            if (Boolean.TRUE.equals(treeResult.getKey())) visibleTrees++;
            bestScenicScore = Math.max(bestScenicScore, treeResult.getValue());
        }
        return new SimpleEntry<>(visibleTrees, bestScenicScore);
    }

    private static Entry<Boolean, Integer> getResult(Coord2 tree, CharGrid grid) {
        boolean isVisible = false;
        int scenicScore = 1;
        for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
            int steps = 0;
            boolean end = false;
            while (!end) {
                steps++;
                Coord2 current = tree.move(dir, steps);
                if (!grid.isInGrid(current)) {
                    isVisible = true;
                    end = true;
                    steps--;
                } else if (grid.grid[current.x][current.y] >= grid.grid[tree.x][tree.y]) end = true;
            }
            scenicScore *= steps;
        }
        return new SimpleEntry<>(isVisible, scenicScore);
    }
}
