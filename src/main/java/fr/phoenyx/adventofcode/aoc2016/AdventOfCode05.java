package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode05 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);
    private static final Set<Character> positionDigits = Set.of('0', '1', '2', '3', '4', '5', '6', '7');

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String password1 = getPasswordPart1(currentLine);
                LOGGER.info("PART 1: {}", password1);
                String password2 = getPasswordPart2(currentLine);
                LOGGER.info("PART 2: {}", password2);
            }
        }
    }

    private static String getPasswordPart1(String key) throws NoSuchAlgorithmException {
        long begin = System.nanoTime();
        StringBuilder password = new StringBuilder();
        int iteration = 0;
        for (int i = 0; i < 8; i++) {
            String nextHash = Utils.getHexadecimalMD5Hash(key + iteration);
            while (!nextHash.startsWith("00000")) {
                iteration++;
                nextHash = Utils.getHexadecimalMD5Hash(key + iteration);
            }
            LOGGER.info("Found next hash: {}, time elpased: {}ms", nextHash, (System.nanoTime() - begin) / 1000000);
            password.append(nextHash.charAt(5));
            iteration++;
        }
        return password.toString();
    }

    private static String getPasswordPart2(String key) throws NoSuchAlgorithmException {
        long begin = System.nanoTime();
        char[] password = new char[8];
        int iteration = 0;
        for (int i = 0; i < 8; i++) {
            String nextHash = Utils.getHexadecimalMD5Hash(key + iteration);
            while (!nextHash.startsWith("00000") || !positionDigits.contains(nextHash.charAt(5)) || password[nextHash.charAt(5) - '0'] != 0) {
                iteration++;
                nextHash = Utils.getHexadecimalMD5Hash(key + iteration);
            }
            LOGGER.info("Found next hash: {}, time elpased: {}ms", nextHash, (System.nanoTime() - begin) / 1000000);
            password[nextHash.charAt(5) - '0'] = nextHash.charAt(6);
            iteration++;
        }
        return new String(password);
    }
}
