package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode04 {

    private static class Bingo {
        final int[][] grid = new int[5][5];
        final boolean[][] marked = new boolean[5][5];

        void addLine(String line, int row) {
            String[] split = line.split(" ");
            int col = 0;
            for (String s : split) {
                try {
                    int value = Integer.parseInt(s);
                    grid[col][row] = value;
                    col++;
                } catch (NumberFormatException e) {
                    // skipping blank lines
                }
            }
        }

        int getIndexCompletion(List<Integer> draws) {
            reset();
            for (int i = 0; i < draws.size(); i++) {
                play(draws.get(i));
                if (isCompleted()) return i;
            }
            return Integer.MAX_VALUE;
        }

        private void reset() {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) marked[i][j] = false;
            }
        }

        private void play(int value) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) if (grid[i][j] == value) marked[i][j] = true;
            }
        }

        private boolean isCompleted() {
            for (int i = 0; i < 5; i++) {
                boolean completedRow = true;
                boolean completedCol = true;
                for (int j = 0; j < 5; j++) {
                    if (!marked[i][j]) completedCol = false;
                    if (!marked[j][i]) completedRow = false;
                }
                if (completedRow || completedCol) return true;
            }
            return false;
        }

        int getScore(List<Integer> draws) {
            int indexCompletion = getIndexCompletion(draws);
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) if (!marked[i][j]) sum += grid[i][j];
            }
            return draws.get(indexCompletion) * sum;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Integer> draws = new ArrayList<>();
            List<Bingo> bingos = new ArrayList<>();
            Bingo current = new Bingo();
            int currentRow = 0;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (draws.isEmpty()) draws.addAll(Arrays.stream(currentLine.split(",")).map(Integer::parseInt).toList());
                else if (currentLine.isBlank()) {
                    current = new Bingo();
                    bingos.add(current);
                    currentRow = 0;
                } else current.addLine(currentLine, currentRow++);
            }
            LOGGER.info("PART 1: {}", bingos.stream().min(Comparator.comparingInt(b -> b.getIndexCompletion(draws))).map(b -> b.getScore(draws)).orElseThrow());
            LOGGER.info("PART 2: {}", bingos.stream().max(Comparator.comparingInt(b -> b.getIndexCompletion(draws))).map(b -> b.getScore(draws)).orElseThrow());
        }
    }
}
