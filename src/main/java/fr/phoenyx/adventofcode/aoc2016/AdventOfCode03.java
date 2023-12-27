package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode03 {

    private static class Grid {
        static final int WIDTH = 3;
        final int height;
        final int[][] map;

        Grid(List<String> lines) {
            this.height = lines.size();
            map = new int[WIDTH][height];
            for (int i = 0; i < height; i++) {
                List<Integer> sides = Arrays.stream(lines.get(i).split(" ")).filter(s -> !s.isBlank()).map(Integer::parseInt).toList();
                for (int j = 0; j < WIDTH; j++) map[j][i] = sides.get(j);
            }
        }

        int getValidCount() {
            int result = 0;
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < height; j += 3) {
                    if (map[i][j] + map[i][j + 1] > map[i][j + 2] && map[i][j] + map[i][j + 2] > map[i][j + 1] && map[i][j + 1] + map[i][j + 2] > map[i][j]) result++;
                }
            }
            return result;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            int count = 0;
            while ((currentLine = reader.readLine()) != null) {
                lines.add(currentLine);
                List<Integer> sides = Arrays.stream(currentLine.split(" ")).filter(s -> !s.isBlank()).map(Integer::parseInt).toList();
                if (sides.get(0) + sides.get(1) > sides.get(2) && sides.get(0) + sides.get(2) > sides.get(1) && sides.get(1) + sides.get(2) > sides.get(0)) count++;
            }
            LOGGER.info("PART 1: {}", count);
            LOGGER.info("PART 2: {}", new Grid(lines).getValidCount());
        }
    }
}
