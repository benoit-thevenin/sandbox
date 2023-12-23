package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.Dir;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            CharGrid keyPad = new CharGrid(List.of("123", "456", "789"));
            CharGrid realKeyPad = new CharGrid(List.of("..1..", ".234.", "56789", ".ABC.", "..D.."));
            int x = 1;
            int y = 1;
            int realX = 0;
            int realY = 2;
            StringBuilder bathroomCode = new StringBuilder();
            StringBuilder realBathroomCode = new StringBuilder();
            while ((currentLine = reader.readLine()) != null) {
                for (char c : currentLine.toCharArray()) {
                    Dir dir = Dir.fromChar(c);
                    int nextX = x + dir.dx;
                    int nextY = y + dir.dy;
                    int realNextX = realX + dir.dx;
                    int realNextY = realY + dir.dy;
                    if (keyPad.isInGrid(nextX, nextY)) {
                        x = nextX;
                        y = nextY;
                    }
                    if (realKeyPad.isInGrid(realNextX, realNextY) && realKeyPad.grid[realNextX][realNextY] != '.') {
                        realX = realNextX;
                        realY = realNextY;
                    }
                }
                bathroomCode.append(keyPad.grid[x][y]);
                realBathroomCode.append(realKeyPad.grid[realX][realY]);
            }
            LOGGER.info("PART 1: {}", bathroomCode);
            LOGGER.info("PART 2: {}", realBathroomCode);
        }
    }
}
