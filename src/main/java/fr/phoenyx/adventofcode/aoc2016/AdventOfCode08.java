package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            boolean[][] screen = new boolean[50][6];
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("rect")) processRectInstruction(currentLine, screen);
                else if (currentLine.contains("y=")) processRotateRow(currentLine, screen);
                else processRotateCol(currentLine, screen);
            }
            LOGGER.info("PART 1: {}", countLitPixels(screen));
            LOGGER.info("PART 2: {}", printScreen(screen));
        }
    }

    private static void processRectInstruction(String instruction, boolean[][] screen) {
        String[] dimensions = instruction.split("rect ")[1].split("x");
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) screen[i][j] = true;
        }
    }

    private static void processRotateRow(String instruction, boolean[][] screen) {
        String[] values = instruction.split("=")[1].split(" by ");
        int row = Integer.parseInt(values[0]);
        int shift = Integer.parseInt(values[1]);
        boolean[] newRow = new boolean[50];
        for (int i = 0; i < 50; i++) newRow[(i + shift) % 50] = screen[i][row];
        for (int i = 0; i < 50; i++) screen[i][row] = newRow[i];
    }

    private static void processRotateCol(String instruction, boolean[][] screen) {
        String[] values = instruction.split("=")[1].split(" by ");
        int col = Integer.parseInt(values[0]);
        int shift = Integer.parseInt(values[1]);
        boolean[] newCol = new boolean[6];
        for (int i = 0; i < 6; i++) newCol[(i + shift) % 6] = screen[col][i];
        System.arraycopy(newCol, 0, screen[col], 0, 6);
    }

    private static int countLitPixels(boolean[][] screen) {
        int litPixels = 0;
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 6; j++) if (screen[i][j]) litPixels++;
        }
        return litPixels;
    }

    private static String printScreen(boolean[][] screen) {
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 50; j++) {
                if (screen[j][i]) sb.append('#');
                else sb.append('.');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
