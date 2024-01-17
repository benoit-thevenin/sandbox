package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.Range;

public class AdventOfCode06 {

    private enum Action {
        TURN_ON, TURN_OFF, TOGGLE
    }

    private static class Instruction {
        Action action;
        Range xRange;
        Range yRange;

        Instruction(String line) {
            action = Action.TOGGLE;
            if (line.contains(" on")) action = Action.TURN_ON;
            else if (line.contains(" off")) action = Action.TURN_OFF;
            String[] split = line.split(" through ");
            split[0] = split[0].split("( off )|( on )|(gle )")[1];
            String[] first = split[0].split(",");
            String[] second = split[1].split(",");
            xRange = Range.buildFromStartAndEndInclusive(Integer.parseInt(first[0]), Integer.parseInt(second[0]));
            yRange = Range.buildFromStartAndEndInclusive(Integer.parseInt(first[1]), Integer.parseInt(second[1]));
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);
    private static final boolean[][] lightGrid1 = new boolean[1000][1000];
    private static final int[][] lightGrid2 = new int[1000][1000];

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) perform(new Instruction(currentLine));
            LOGGER.info("PART 1: {}", countLightsOn());
            LOGGER.info("PART 2: {}", getBrightness());
        }
    }

    private static void perform(Instruction instruction) {
        for (int x = (int) instruction.xRange.start; x < instruction.xRange.end; x++) {
            for (int y = (int) instruction.yRange.start; y < instruction.yRange.end; y++) {
                if (instruction.action == Action.TURN_ON) {
                    lightGrid1[x][y] = true;
                    lightGrid2[x][y]++;
                } else if (instruction.action == Action.TURN_OFF) {
                    lightGrid1[x][y] = false;
                    if (lightGrid2[x][y] > 0) lightGrid2[x][y]--;
                } else {
                    lightGrid1[x][y] = !lightGrid1[x][y];
                    lightGrid2[x][y] += 2;
                }
            }
        }
    }

    private static int countLightsOn() {
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) if (lightGrid1[i][j]) count++;
        }
        return count;
    }

    private static int getBrightness() {
        int brightness = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) brightness += lightGrid2[i][j];
        }
        return brightness;
    }
}
