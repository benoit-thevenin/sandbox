package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.Dir;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Entry<Integer, Integer> result = computeResult(new CharGrid(lines));
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Entry<Integer, Integer> computeResult(CharGrid grid) {
        int visibleTrees = 0;
        int bestScenicScore = 0;
        for (int i = 0; i < grid.width; i++) {
            for (int j = 0; j < grid.height; j++) {
                Entry<Boolean, Integer> treeResult = computeTree(i, j, grid);
                if (Boolean.TRUE.equals(treeResult.getKey())) visibleTrees++;
                if (treeResult.getValue() > bestScenicScore) bestScenicScore = treeResult.getValue();
            }
        }
        return new SimpleEntry<>(visibleTrees, bestScenicScore);
    }

    private static Entry<Boolean, Integer> computeTree(int i, int j, CharGrid grid) {
        boolean isVisible = false;
        int scenicScore = 1;
        for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
            int step = 0;
            boolean end = false;
            while (!end) {
                step++;
                int x = i + step * dir.dx;
                int y = j + step * dir.dy;
                if (!grid.isInGrid(x, y)) {
                    isVisible = true;
                    end = true;
                    step--;
                } else if (grid.grid[x][y] >= grid.grid[i][j]) end = true;
            }
            scenicScore *= step;
        }
        return new SimpleEntry<>(isVisible, scenicScore);
    }
}
