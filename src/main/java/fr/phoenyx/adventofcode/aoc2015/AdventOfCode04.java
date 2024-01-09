package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode04 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String secretKey;
            while ((secretKey = reader.readLine()) != null) {
                long begin = System.nanoTime();
                int iteration = 0;
                String hash = getHash(secretKey + iteration, md);
                while (!hash.startsWith("00000")) {
                    iteration ++;
                    hash = getHash(secretKey + iteration, md);
                }
                LOGGER.info("PART 1: {} ({}), time elpased: {}ms", iteration, hash, (System.nanoTime() - begin) / 1000000);
                while (!hash.startsWith("000000")) {
                    iteration++;
                    hash = getHash(secretKey + iteration, md);
                }
                LOGGER.info("PART 2: {} ({}), time elpased: {}ms", iteration, hash, (System.nanoTime() - begin) / 1000000);
            }
        }
    }

    private static String getHash(String key, MessageDigest md) {
        md.update(key.getBytes());
        byte[] digest = md.digest();
        BigInteger bi = new BigInteger(1, digest);
        String format = "%0" + (digest.length << 1) + "x";
        return String.format(format, bi);
    }
}
