package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode05 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);
    private static final Set<Character> POSITION_DIGITS = Set.of('0', '1', '2', '3', '4', '5', '6', '7');

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                long begin = System.nanoTime();
                LOGGER.info("PART 1: {}, time elapsed {}ms", getPassword(currentLine, true), (System.nanoTime() - begin) / 1000000);
                begin = System.nanoTime();
                LOGGER.info("PART 2: {}, time elapsed {}ms", getPassword(currentLine, false), (System.nanoTime() - begin) / 1000000);
            }
        }
    }

    private static String getPassword(String key, boolean isPart1) {
        char[] password = new char[8];
        int iteration = 0;
        for (int i = 0; i < 8; i++) {
            String nextHash = "";
            while (!isValid(nextHash, password, isPart1)) {
                iteration++;
                nextHash = Utils.getHexadecimalMD5Hash(key + iteration);
            }
            if (isPart1) password[i] = nextHash.charAt(5);
            else password[nextHash.charAt(5) - '0'] = nextHash.charAt(6);
        }
        return new String(password);
    }

    private static boolean isValid(String hash, char[] password, boolean isPart1) {
        return hash.startsWith("0".repeat(5)) && (isPart1 || POSITION_DIGITS.contains(hash.charAt(5)) && password[hash.charAt(5) - '0'] == 0);
    }
}
