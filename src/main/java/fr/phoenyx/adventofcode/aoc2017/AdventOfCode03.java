package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);
    private static final Coord2 accessPort = new Coord2(0, 0);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int input = 0;
            while ((currentLine = reader.readLine()) != null) input = Integer.parseInt(currentLine);
            LOGGER.info("PART 1: {}", getCoordinates(input).manhattanDistanceTo(accessPort));
            LOGGER.info("PART 2: {}", getFirstValueGreaterThan(input));
        }
    }

    private static Coord2 getCoordinates(int value) {
        if (value < 1) throw new IllegalArgumentException("Value must be greater than 0");
        if (value == 1) return accessPort;
        int baseX = 0;
        int baseY = 0;
        int squareValue = 1;
        while ((squareValue + 2) * (squareValue + 2) <= value) {
            squareValue += 2;
            baseX++;
            baseY++;
        }
        if (squareValue * squareValue == value) return new Coord2(baseX, baseY);
        int inc = 1;
        int offsetX = 1;
        int offsetY = 0;
        while (squareValue * squareValue + inc < value) {
            if (inc < squareValue + 1) offsetY--;
            else if (inc < 2 * (squareValue + 1)) offsetX--;
            else if (inc < 3 * (squareValue + 1)) offsetY++;
            else offsetX++;
            inc++;
        }
        return new Coord2(baseX + offsetX, baseY + offsetY);
    }

    private static int getFirstValueGreaterThan(int input) {
        int value = 1;
        Map<Coord2, Integer> coordValues = new HashMap<>();
        coordValues.put(accessPort, value);
        while (value <= input) {
            Coord2 coord = getCoordinates(coordValues.size() + 1);
            value = 0;
            for (Dir dir : Dir.values()) {
                Coord2 neighbour = coord.move(dir);
                if (coordValues.containsKey(neighbour)) value += coordValues.get(neighbour);
            }
            coordValues.put(coord, value);
        }
        return value;
    }
}
