package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode05 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Integer> ids = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) ids.add(getRow(currentLine) * 8 + getCol(currentLine));
            LOGGER.info("PART 1: {}", ids.stream().max(Integer::compare).orElseThrow());
            LOGGER.info("PART 2: {}", getMissingId(ids));
        }
    }

    private static int getRow(String line) {
        int row = 0;
        for (int i = 0; i < 7; i++) if (line.charAt(i) == 'B') row += 1 << (6 - i);
        return row;
    }

    private static int getCol(String line) {
        int col = 0;
        for (int i = 0; i < 3; i++) if (line.charAt(i + 7) == 'R') col += 1 << (2 - i);
        return col;
    }

    private static int getMissingId(List<Integer> ids) {
        Collections.sort(ids);
        for (int i = 0; i < ids.size() - 1; i++) if (ids.get(i) + 1 != ids.get(i + 1)) return ids.get(i) + 1;
        return -1;
    }
}
