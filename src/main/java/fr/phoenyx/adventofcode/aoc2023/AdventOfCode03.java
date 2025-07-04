package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);
    private static CharGrid charGrid;
    private static int[][] partNumbers;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode03.txt";
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
        }
        buildDataStructures(lines);
        LOGGER.info("PART 1: {}", computePart1());
        LOGGER.info("PART 2: {}", computePart2());
    }

    private static void buildDataStructures(List<String> lines) {
        charGrid = new CharGrid(lines);
        partNumbers = new int[charGrid.width][charGrid.height];
        for (int i = 0; i < charGrid.height; i++) {
            for (int j = 0; j < charGrid.width; j++) {
                if (!Character.isDigit(charGrid.grid[j][i])) continue;
                StringBuilder partNumber = new StringBuilder();
                while (j < charGrid.width && Character.isDigit(charGrid.grid[j][i])) {
                    partNumber.append(charGrid.grid[j][i]);
                    j++;
                }
                for (int k = j - 1; k > j - 1 - partNumber.length(); k--) partNumbers[k][i] = Integer.parseInt(partNumber.toString());
            }
        }
    }

    private static int computePart1() {
        return charGrid.getCoordinatesMatching(c -> c != '.' && !Character.isDigit(c)).stream()
            .map(c1 -> Arrays.stream(Dir.values())
                .map(c1::move)
                .filter(charGrid::isInGrid)
                .map(c2 -> partNumbers[c2.x][c2.y])
                .collect(Collectors.toSet()).stream().reduce(0, Integer::sum))
            .reduce(0, Integer::sum);
    }

    private static int computePart2() {
        return charGrid.getCoordinatesMatching(c -> c == '*').stream()
            .map(c1 -> Arrays.stream(Dir.values())
                .map(c1::move)
                .filter(charGrid::isInGrid)
                .map(c2 -> partNumbers[c2.x][c2.y])
                .filter(value -> value != 0)
                .collect(Collectors.toSet()))
                .filter(sets -> sets.size() == 2)
                .map(sets -> sets.stream().reduce(1, (a, b) -> a * b))
            .reduce(0, Integer::sum);
    }
}
