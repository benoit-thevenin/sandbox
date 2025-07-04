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
import java.util.List;

public class AdventOfCode04 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            LOGGER.info("PART 1: {}", countXmas(grid));
            LOGGER.info("PART 2: {}", countX_mas(grid));
        }
    }

    private static int countXmas(CharGrid grid) {
        int count = 0;
        for (Coord2 pos : grid.getAllCoords()) {
            for (Dir dir : Dir.values()) if (isWordFound(grid, pos, dir, "XMAS")) count++;
        }
        return count;
    }

    private static boolean isWordFound(CharGrid grid, Coord2 pos, Dir dir, String word) {
        if (grid.get(pos) != word.charAt(0)) return false;
        Coord2 current = pos;
        for (int i = 1; i < word.length(); i++) {
            current = current.move(dir);
            if (!grid.isInGrid(current) || grid.get(current) != word.charAt(i)) return false;
        }
        return true;
    }

    private static int countX_mas(CharGrid grid) {
        int count = 0;
        for (Coord2 pos : grid.getAllCoords()) if (grid.get(pos) == 'A' && isValidX_mas(grid, pos)) count++;
        return count;
    }

    private static boolean isValidX_mas(CharGrid grid, Coord2 pos) {
        for (Dir dir : List.of(Dir.NE, Dir.SE)) {
            Coord2 next = pos.move(dir);
            if (!grid.isInGrid(next) || !isWordFound(grid, next, dir.getOpposite(), "MAS") && !isWordFound(grid, next, dir.getOpposite(), "SAM")) return false;
        }
        return true;
    }
}
