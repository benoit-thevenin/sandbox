package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import fr.phoenyx.models.CharGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            CharGrid screen = new CharGrid(50, 6);
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("rect")) processRectInstruction(currentLine, screen);
                else if (currentLine.contains("y=")) processRotateRow(currentLine, screen);
                else processRotateCol(currentLine, screen);
            }
            LOGGER.info("PART 1: {}", screen.getCoordinatesMatching(c -> c == 1).size());
            LOGGER.info("PART 2: {}", printScreen(screen));
        }
    }

    private static void processRectInstruction(String instruction, CharGrid screen) {
        String[] dimensions = instruction.split("rect ")[1].split("x");
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) screen.grid[i][j] = 1;
        }
    }

    private static void processRotateRow(String instruction, CharGrid screen) {
        String[] values = instruction.split("=")[1].split(" by ");
        int row = Integer.parseInt(values[0]);
        int shift = Integer.parseInt(values[1]);
        char[] newRow = new char[50];
        for (int i = 0; i < 50; i++) newRow[(i + shift) % 50] = screen.grid[i][row];
        for (int i = 0; i < 50; i++) screen.grid[i][row] = newRow[i];
    }

    private static void processRotateCol(String instruction, CharGrid screen) {
        String[] values = instruction.split("=")[1].split(" by ");
        int col = Integer.parseInt(values[0]);
        int shift = Integer.parseInt(values[1]);
        char[] newCol = new char[6];
        for (int i = 0; i < 6; i++) newCol[(i + shift) % 6] = screen.grid[col][i];
        System.arraycopy(newCol, 0, screen.grid[col], 0, 6);
    }

    private static String printScreen(CharGrid screen) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append('\n');
            for (int j = 0; j < 50; j++) {
                if (screen.grid[j][i] == 1) sb.append('#');
                else sb.append('.');
            }
        }
        return sb.toString();
    }
}
