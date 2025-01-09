package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.adventofcode.aoc2019.AdventOfCode05.IntcodeComputer;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode11 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);
    private static final Coord2 START = new Coord2(0, 0);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                long[] program = new long[split.length];
                for (int i = 0; i < split.length; i++) program[i] = Long.parseLong(split[i]);
                Map.Entry<Integer, String> result = getResult(program);
                LOGGER.info("PART 1: {}", result.getKey());
                LOGGER.info("PART 2: {}", result.getValue());
            }
        }
    }

    private static Map.Entry<Integer, String> getResult(long[] program) {
        Map<Coord2, Boolean> colors = new HashMap<>();
        runRobot(new IntcodeComputer(program), colors);
        int paintedPanels = colors.size();
        colors.clear();
        colors.put(START, true);
        runRobot(new IntcodeComputer(program), colors);
        return new AbstractMap.SimpleEntry<>(paintedPanels, getRegistrationIdentifier(colors));
    }

    private static void runRobot(IntcodeComputer robot, Map<Coord2, Boolean> colors) {
        Coord2 currentPos = START;
        Dir currentDir = Dir.N;
        while (true) {
            int nextInput = colors.containsKey(currentPos) && colors.get(currentPos) ? 1 : 0;
            long colorOutput = robot.run(nextInput);
            if (robot.isHalted) break;
            long turnOuput = robot.run(nextInput);
            colors.put(currentPos, colorOutput == 1);
            currentDir = turnOuput == 0 ? currentDir.fourNeighboursTurnLeft() : currentDir.fourNeighboursTurnRight();
            currentPos = currentPos.move(currentDir);
        }
    }

    private static String getRegistrationIdentifier(Map<Coord2, Boolean> colors) {
        StringBuilder sb = new StringBuilder();
        int minX = colors.keySet().stream().map(c -> c.x).min(Integer::compare).orElseThrow();
        int maxX = colors.keySet().stream().map(c -> c.x).max(Integer::compare).orElseThrow();
        int minY = colors.keySet().stream().map(c -> c.y).min(Integer::compare).orElseThrow();
        int maxY = colors.keySet().stream().map(c -> c.y).max(Integer::compare).orElseThrow();
        for (int i = minY; i <= maxY; i++) {
            sb.append('\n');
            for (int j = minX; j <= maxX; j++) {
                Coord2 current = new Coord2(j, i);
                sb.append(colors.containsKey(current) && colors.get(current) ? '#' : '.');
            }
        }
        return sb.toString();
    }
}
