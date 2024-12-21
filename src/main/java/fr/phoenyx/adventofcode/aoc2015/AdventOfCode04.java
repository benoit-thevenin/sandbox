package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode04 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String secretKey;
            while ((secretKey = reader.readLine()) != null) {
                long begin = System.nanoTime();
                LOGGER.info("PART 1: {}, time elapsed: {}ms", getIterations(secretKey, 5), (System.nanoTime() - begin) / 1000000);
                begin = System.nanoTime();
                LOGGER.info("PART 2: {}, time elapsed: {}ms", getIterations(secretKey, 6), (System.nanoTime() - begin) / 1000000);
            }
        }
    }

    private static int getIterations(String secretKey, int zeroCount) {
        int iteration = 0;
        String hash = "";
        while (!hash.startsWith("0".repeat(zeroCount))) {
            iteration++;
            hash = Utils.getHexadecimalMD5Hash(secretKey + iteration);
        }
        return iteration;
    }
}
