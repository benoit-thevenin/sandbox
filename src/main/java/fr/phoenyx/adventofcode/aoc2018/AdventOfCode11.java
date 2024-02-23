package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode11 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);
    private static final int[][] fuelCellPower = new int[300][300];
    private static final Map<Integer, Integer> squarePowers = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                setFuelCellPower(Integer.parseInt(currentLine));
                long begin = System.nanoTime();
                LOGGER.info("PART 1: {}, time elapsed: {}ms", get3x3MaxPowerCoord(), (System.nanoTime() - begin) / 1000000);
                begin = System.nanoTime();
                LOGGER.info("PART 2: {}, time elapsed: {}ms", getMaxPowerCoord(), (System.nanoTime() - begin) / 1000000);
            }
        }
    }

    private static void setFuelCellPower(int serialId) {
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) fuelCellPower[i][j] = getFuelCellPower(serialId, i + 1, j + 1);
        }
    }

    private static int getFuelCellPower(int serialId, int x, int y) {
        int rackId = x + 10;
        return (((rackId * (y * rackId + serialId)) / 100) % 10) - 5;
    }

    private static String get3x3MaxPowerCoord() {
        int maxPower = Integer.MIN_VALUE;
        int x = 0;
        int y = 0;
        for (int i = 1; i < 299; i++) {
            for (int j = 1; j < 299; j++) {
                int power = getPower(i, j, 3);
                if (power > maxPower) {
                    maxPower = power;
                    x = i;
                    y = j;
                }
            }
        }
        return x + "," + y;
    }

    private static String getMaxPowerCoord() {
        int maxPower = Integer.MIN_VALUE;
        int x = 0;
        int y = 0;
        int size = 0;
        for (int squareSize = 1; squareSize <= 300; squareSize++) {
            for (int i = 1; i <= 300 - (squareSize - 1); i++) {
                for (int j = 1; j <= 300 - (squareSize - 1); j++) {
                    int power = getPower(i, j, squareSize);
                    if (power > maxPower) {
                        maxPower = power;
                        x = i;
                        y = j;
                        size = squareSize;
                    }
                }
            }
        }
        return x + "," + y + "," + size;
    }

    private static int getPower(int x, int y, int size) {
        int hash = x + 300 * y + 90000 * size;
        if (squarePowers.containsKey(hash)) return squarePowers.get(hash);
        int power;
        if (size == 1) power = fuelCellPower[x - 1][y - 1];
        else {
            power = getPower(x, y, size - 1) + getPower(x + size - 1, y + size - 1, 1);
            for (int i = 0; i < size; i++) {
                power += getPower(x + i, y + size - 1, 1);
                power += getPower(x + size - 1, y + i, 1);
            }
        }
        squarePowers.put(hash, power);
        return power;
    }
}
