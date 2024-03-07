package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode12 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);
    private static final Coord2 START_POS = new Coord2(0, 0);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Coord2 coordPart1 = START_POS;
            Dir currentDir = Dir.E;
            Coord2 coordPart2 = START_POS;
            Coord2 waypoint = new Coord2(10, -1);
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                char action = currentLine.charAt(0);
                int value = Integer.parseInt(currentLine.substring(1));
                if (action == 'R' || action == 'L') {
                    int rotations = value / 90;
                    for (int i = 0; i < rotations; i++) currentDir = currentDir.fourNeighboursTurn(action);
                    waypoint = rotateWaypoint(waypoint, action, value);
                } else if (action == 'F') {
                    coordPart1 = coordPart1.move(currentDir, value);
                    coordPart2 = new Coord2(coordPart2.x + waypoint.x * value, coordPart2.y + waypoint.y * value);
                } else {
                    Dir dir = Dir.fromChar(action);
                    coordPart1 = coordPart1.move(dir, value);
                    waypoint = waypoint.move(dir, value);
                }
            }
            LOGGER.info("PART 1: {}", coordPart1.manhattanDistanceTo(START_POS));
            LOGGER.info("PART 2: {}", coordPart2.manhattanDistanceTo(START_POS));
        }
    }

    private static Coord2 rotateWaypoint(Coord2 waypoint, char action, int value) {
        if (value == 180) return new Coord2(-waypoint.x, -waypoint.y);
        if (action == 'R' && value == 90 || action == 'L' && value == 270) return new Coord2(-waypoint.y, waypoint.x);
        return new Coord2(waypoint.y, -waypoint.x);
    }
}
