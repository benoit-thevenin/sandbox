package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int x = 0;
            int y = 0;
            Dir dir = Dir.N;
            Set<Coord2> visited = new HashSet<>();
            visited.add(new Coord2(x, y));
            int hqDistance = -1;
            while ((currentLine = reader.readLine()) != null) {
                String[] instructions = currentLine.split(", ");
                for (String instruction : instructions) {
                    int steps = Integer.parseInt(instruction.substring(1));
                    dir = dir.fourNeighboursTurn(instruction.charAt(0));
                    for (int i = 0; i < steps; i++) {
                        x += dir.dx;
                        y += dir.dy;
                        if (hqDistance == -1) {
                            Coord2 next = new Coord2(x, y);
                            if (visited.contains(next)) hqDistance = Math.abs(x) + Math.abs(y);
                            else visited.add(next);
                        }
                    }
                }
            }
            LOGGER.info("PART 1: {}", Math.abs(x) + Math.abs(y));
            LOGGER.info("PART 2: {}", hqDistance);
        }
    }
}
