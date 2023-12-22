package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.Dir;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);
    private static CharGrid charGrid;
    private static int[][] partNumbers;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode03.txt";
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                lines.add(currentLine);
            }
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
                for (int k = j - 1; k > j - 1 - partNumber.length(); k--) {
                    partNumbers[k][i] = Integer.parseInt(partNumber.toString());
                }
            }
        }
    }

    private static int computePart1() {
        int sum = 0;
        for (int i = 0; i < charGrid.height; i++) {
            for (int j = 0; j < charGrid.width; j++) {
                if (charGrid.grid[j][i] == '.' || Character.isDigit(charGrid.grid[j][i])) continue;
                Set<Integer> numbers = new HashSet<>();
                for (Dir dir : Dir.values()) {
                    int neighbourX = j + dir.dx;
                    int neighbourY = i + dir.dy;
                    if (charGrid.isInGrid(neighbourX, neighbourY)) numbers.add(partNumbers[neighbourX][neighbourY]);
                }
                sum += numbers.stream().reduce(Integer::sum).orElseThrow();
            }
        }
        return sum;
    }

    private static int computePart2() {
        int sum = 0;
        for (int i = 0; i < charGrid.width; i++) {
            for (int j = 0; j < charGrid.height; j++) {
                if (charGrid.grid[i][j] != '*') continue;
                Set<Integer> numbers = new HashSet<>();
                for (Dir dir : Dir.values()) {
                    int neighbourX = i + dir.dx;
                    int neighbourY = j + dir.dy;
                    if (charGrid.isInGrid(neighbourX, neighbourY) && partNumbers[neighbourX][neighbourY] != 0) numbers.add(partNumbers[neighbourX][neighbourY]);
                }
                if (numbers.size() == 2) sum += numbers.stream().reduce((a, b) -> a * b).orElseThrow();
            }
        }
        return sum;
    }
}
