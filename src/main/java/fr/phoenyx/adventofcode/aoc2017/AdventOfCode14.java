package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import fr.phoenyx.models.CharGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode14 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String key;
            while ((key = reader.readLine()) != null) {
                CharGrid disk = getDisk(key);
                LOGGER.info("PART 1: {}", disk.getCoordinatesMatching(c -> c == '#').size());
                LOGGER.info("PART 2: {}", disk.getAllRegionsMatching(c -> c == '#').size());
            }
        }
    }

    private static CharGrid getDisk(String key) {
        CharGrid disk = new CharGrid(128, 128);
        for (int i = 0; i < disk.height; i++) {
            String knotHash = AdventOfCode10.getKnotHash(key + "-" + i);
            for (int j = 0; j < 4; j++) {
                int current = Integer.parseUnsignedInt(knotHash.substring(8 * j, 8 * (j + 1)), 16);
                for (int k = 0; k < 32; k++) disk.grid[32 * j + k][i] = (current & (1 << (31 - k))) != 0 ? '#' : '.';
            }
        }
        return disk;
    }
}
