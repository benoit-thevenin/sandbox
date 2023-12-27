package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.Coord;
import fr.phoenyx.models.Dir;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Set<Coord> visited = new HashSet<>();
            Set<Coord> visitedWithRoboSanta = new HashSet<>();
            Coord current = new Coord(0, 0);
            Coord currentSanta = current;
            Coord currentRoboSanta = current;
            visited.add(current);
            visitedWithRoboSanta.add(current);
            while ((currentLine = reader.readLine()) != null) {
                for (int i = 0; i < currentLine.length(); i++) {
                    Dir dir = Dir.fromChar(currentLine.charAt(i));
                    current = current.move(dir);
                    visited.add(current);
                    if (i % 2 == 0) {
                        currentSanta = currentSanta.move(dir);
                        visitedWithRoboSanta.add(currentSanta);
                    } else {
                        currentRoboSanta = currentRoboSanta.move(dir);
                        visitedWithRoboSanta.add(currentRoboSanta);
                    }
                }
            }
            LOGGER.info("PART 1: {}", visited.size());
            LOGGER.info("PART 2: {}", visitedWithRoboSanta.size());
        }
    }
}
