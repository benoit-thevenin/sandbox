package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;

public class AdventOfCode13 {

    private static class Pattern extends CharGrid {
        Pattern(List<String> lines) {
            super(lines);
        }

        int getReflectionValue(int errorCount) {
            int verticalReflection = getVerticalReflection(errorCount);
            if (verticalReflection != -1) return verticalReflection;
            return 100 * getHorizontalReflection(errorCount);
        }

        private int getVerticalReflection(int errorCount) {
            for (int i = 1; i < width; i++) {
                int errors = 0;
                int length = Math.min(i, width - i);
                for (int j = 0; j < length; j++) {
                    for (int k = 0; k < height; k++) {
                        if (grid[i - j - 1][k] != grid[i + j][k]) errors++;
                    }
                }
                if (errors == errorCount) return i;
            }
            return -1;
        }

        private int getHorizontalReflection(int errorCount) {
            for (int i = 1; i < height; i++) {
                int errors = 0;
                int length = Math.min(i, height - i);
                for (int j = 0; j < length; j++) {
                    for (int k = 0; k < width; k++) {
                        if (grid[k][i - j - 1] != grid[k][i + j]) errors++;
                    }
                }
                if (errors == errorCount) return i;
            }
            return -1;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            List<Pattern> patterns = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) {
                    patterns.add(new Pattern(lines));
                    lines.clear();
                } else lines.add(currentLine);
            }
            patterns.add(new Pattern(lines));
            LOGGER.info("PART 1: {}", patterns.stream().map(p -> p.getReflectionValue(0)).reduce(Integer::sum).orElseThrow());
            LOGGER.info("PART 2: {}", patterns.stream().map(p -> p.getReflectionValue(1)).reduce(Integer::sum).orElseThrow());
        }
    }
}
