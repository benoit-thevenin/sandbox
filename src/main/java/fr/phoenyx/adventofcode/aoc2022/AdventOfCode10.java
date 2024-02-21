package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode10 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> instructions = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) instructions.add(currentLine);
            Entry<Integer, String> result = getResult(instructions);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Entry<Integer, String> getResult(List<String> instructions) {
        StringBuilder sb = new StringBuilder();
        int signalStrength = 0;
        int currentCycle = 0;
        int register = 1;
        for (String instruction : instructions) {
            if (currentCycle % 40 == 0) sb.append('\n');
            sb.append(getPixelChar(currentCycle % 40, register));
            currentCycle++;
            if ((currentCycle - 20) % 40 == 0) signalStrength += register * currentCycle;
            if (!instruction.startsWith("noop")) {
                if (currentCycle % 40 == 0) sb.append('\n');
                sb.append(getPixelChar(currentCycle % 40, register));
                currentCycle++;
                if ((currentCycle - 20) % 40 == 0) signalStrength += register * currentCycle;
                register += Integer.parseInt(instruction.split(" ")[1]);
            }
        }
        return new SimpleEntry<>(signalStrength, sb.toString());
    }

    private static char getPixelChar(int pixel, int sprite) {
        return isPixelOnSprite(pixel, sprite) ? '#' : '.';
    }

    private static boolean isPixelOnSprite(int pixel, int sprite) {
        return pixel == sprite - 1 || pixel == sprite || pixel == sprite + 1;
    }
}
