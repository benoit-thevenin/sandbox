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
    private static final Coord2 START = new Coord2(0, 0);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Coord2 current = START;
            Dir dir = Dir.N;
            Set<Coord2> visited = new HashSet<>();
            visited.add(current);
            int hqDistance = -1;
            while ((currentLine = reader.readLine()) != null) {
                String[] instructions = currentLine.split(", ");
                for (String instruction : instructions) {
                    int steps = Integer.parseInt(instruction.substring(1));
                    dir = dir.fourNeighboursTurn(instruction.charAt(0));
                    for (int i = 0; i < steps; i++) {
                        current = current.move(dir);
                        if (hqDistance == -1 && !visited.add(current)) hqDistance = current.manhattanDistanceTo(START);
                    }
                }
            }
            LOGGER.info("PART 1: {}", current.manhattanDistanceTo(START));
            LOGGER.info("PART 2: {}", hqDistance);
        }
    }
}
