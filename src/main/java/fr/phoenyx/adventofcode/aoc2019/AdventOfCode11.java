package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.adventofcode.aoc2019.AdventOfCode05.IntcodeComputer;
import fr.phoenyx.models.Coord;
import fr.phoenyx.models.Dir;

public class AdventOfCode11 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            long[] program = new long[0];
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(",");
                program = new long[split.length];
                for (int i = 0; i < split.length; i++) program[i] = Long.parseLong(split[i]);
            }
            LOGGER.info("PART 1: {}", countPaintedPanels(program));
            LOGGER.info("PART 2: {}", getRegistrationIdentifier(program));
        }
    }

    private static int countPaintedPanels(long[] program) {
        IntcodeComputer robot = new IntcodeComputer(program);
        Map<Coord, Boolean> colors = new HashMap<>();
        runRobot(robot, colors, new Coord(0, 0));
        return colors.size();
    }

    private static String getRegistrationIdentifier(long[] program) {
        IntcodeComputer robot = new IntcodeComputer(program);
        Coord currentPos = new Coord(0, 0);
        Map<Coord, Boolean> colors = new HashMap<>();
        colors.put(currentPos, true);
        runRobot(robot, colors, currentPos);
        return getRegistrationIdentifier(colors);
    }

    private static void runRobot(IntcodeComputer robot, Map<Coord, Boolean> colors, Coord currentPos) {
        Dir currentDir = Dir.N;
        while (true) {
            int nextInput = 0;
            if (colors.containsKey(currentPos) && colors.get(currentPos) == Boolean.TRUE) nextInput = 1;
            long colorOutput = robot.run(nextInput);
            if (robot.isHalted) break;
            long turnOuput = robot.run(nextInput);
            colors.put(currentPos, colorOutput == 1);
            if (turnOuput == 0) currentDir = currentDir.fourNeighboursTurnLeft();
            else currentDir = currentDir.fourNeighboursTurnRight();
            currentPos = currentPos.move(currentDir);
        }
    }

    private static String getRegistrationIdentifier(Map<Coord, Boolean> colors) {
        StringBuilder sb = new StringBuilder();
        int minX = colors.keySet().stream().map(c -> c.x).min(Integer::compare).orElseThrow();
        int maxX = colors.keySet().stream().map(c -> c.x).max(Integer::compare).orElseThrow();
        int minY = colors.keySet().stream().map(c -> c.y).min(Integer::compare).orElseThrow();
        int maxY = colors.keySet().stream().map(c -> c.y).max(Integer::compare).orElseThrow();
        for (int i = minY; i <= maxY; i++) {
            sb.append('\n');
            for (int j = minX; j <= maxX; j++) {
                Coord current = new Coord(j, i);
                if (colors.containsKey(current) && colors.get(current) == Boolean.TRUE) sb.append('#');
                else sb.append('.');
            }
        }
        return sb.toString();
    }
}
