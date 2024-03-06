package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.FlatTopHexCoord;
import fr.phoenyx.models.coords.FlatTopHexDir;

public class AdventOfCode11 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            FlatTopHexCoord start = new FlatTopHexCoord(0, 0);
            FlatTopHexCoord lastPosition = start;
            String currentLine;
            int maxDistance = 0;
            while ((currentLine = reader.readLine()) != null) {
                List<FlatTopHexDir> dirs = Arrays.stream(currentLine.toUpperCase().split(",")).map(FlatTopHexDir::valueOf).toList();
                for (FlatTopHexDir dir : dirs) {
                    lastPosition = lastPosition.neighbor(dir);
                    maxDistance = Math.max(maxDistance, lastPosition.distanceTo(start));
                }
            }
            LOGGER.info("PART 1: {}", lastPosition.distanceTo(start));
            LOGGER.info("PART 2: {}", maxDistance);
        }
    }
}
