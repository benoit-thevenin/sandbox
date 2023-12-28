package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.Coord;
import fr.phoenyx.models.Dir;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);
    private static CharGrid grid;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            grid = new CharGrid(lines);
            LOGGER.info("PART 1: {}", countEncounteredTrees(3, 1));
            LOGGER.info("PART 2: {}", countEncounteredTrees(1, 1) * countEncounteredTrees(3, 1)
                * countEncounteredTrees(5, 1) * countEncounteredTrees(7, 1) * countEncounteredTrees(1, 2));
        }
    }

    private static long countEncounteredTrees(int rightSlope, int downSlope) {
        Coord current = new Coord(0, 0);
        long trees = 0;
        while (true) {
            for (int i = 0; i < rightSlope; i++) current = current.move(Dir.E);
            for (int i = 0; i < downSlope; i++) current = current.move(Dir.S);
            if (current.y >= grid.height) break;
            if (grid.grid[current.x % grid.width][current.y] == '#') trees++;
        }
        return trees;
    }
}
