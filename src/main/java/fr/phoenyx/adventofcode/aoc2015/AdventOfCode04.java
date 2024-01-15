package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode04 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String secretKey;
            while ((secretKey = reader.readLine()) != null) {
                long begin = System.nanoTime();
                int iteration = 0;
                String hash = Utils.getHexadecimalMD5Hash(secretKey + iteration);
                while (!hash.startsWith("00000")) {
                    iteration ++;
                    hash = Utils.getHexadecimalMD5Hash(secretKey + iteration);
                }
                LOGGER.info("PART 1: {} ({}), time elpased: {}ms", iteration, hash, (System.nanoTime() - begin) / 1000000);
                while (!hash.startsWith("000000")) {
                    iteration++;
                    hash = Utils.getHexadecimalMD5Hash(secretKey + iteration);
                }
                LOGGER.info("PART 2: {} ({}), time elpased: {}ms", iteration, hash, (System.nanoTime() - begin) / 1000000);
            }
        }
    }
}
