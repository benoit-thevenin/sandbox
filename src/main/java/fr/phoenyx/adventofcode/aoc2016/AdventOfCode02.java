package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            CharGrid keyPad = new CharGrid(List.of("123", "456", "789"));
            CharGrid realKeyPad = new CharGrid(List.of("..1..", ".234.", "56789", ".ABC.", "..D.."));
            Coord2 current = new Coord2(1, 1);
            Coord2 realCurrent = new Coord2(0, 2);
            StringBuilder bathroomCode = new StringBuilder();
            StringBuilder realBathroomCode = new StringBuilder();
            while ((currentLine = reader.readLine()) != null) {
                for (char c : currentLine.toCharArray()) {
                    Dir dir = Dir.fromChar(c);
                    Coord2 next = current.move(dir);
                    Coord2 realNext = realCurrent.move(dir);
                    if (keyPad.isInGrid(next)) current = next;
                    if (realKeyPad.isInGrid(realNext) && realKeyPad.grid[realNext.x][realNext.y] != '.') realCurrent = realNext;
                }
                bathroomCode.append(keyPad.grid[current.x][current.y]);
                realBathroomCode.append(realKeyPad.grid[realCurrent.x][realCurrent.y]);
            }
            LOGGER.info("PART 1: {}", bathroomCode);
            LOGGER.info("PART 2: {}", realBathroomCode);
        }
    }
}
