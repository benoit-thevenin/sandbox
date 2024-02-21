package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode11 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String passwordPart1 = getNextPassword(currentLine);
                String passwordPart2 = getNextPassword(passwordPart1);
                LOGGER.info("PART 1: {}", passwordPart1);
                LOGGER.info("PART 2: {}", passwordPart2);
            }
        }
    }

    private static String getNextPassword(String password) {
        long hash = getPasswordHash(password) + 1;
        while (isNotValid(getPasswordFromHash(hash))) hash++;
        return getPasswordFromHash(hash);
    }

    private static long getPasswordHash(String password) {
        long hash = 0;
        for (int i = 0; i < password.length(); i++) hash += (long) (Math.pow(26, password.length() - 1.0 - i) * (password.charAt(i) - 'a'));
        return hash;
    }

    private static String getPasswordFromHash(long hash) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) sb.append("abcdefghijklmnopqrstuvwxyz".charAt((int) ((hash / Math.pow(26, 7.0 - i)) % 26L)));
        return sb.toString();
    }

    private static boolean isNotValid(String password) {
        boolean hasIncreasingStraight = false;
        for (int i = 0; i < password.length() - 2; i++) {
            int c1 = password.charAt(i);
            int c2 = password.charAt(i + 1);
            int c3 = password.charAt(i + 2);
            if (c1 == c2 - 1 && c2 == c3 - 1) {
                hasIncreasingStraight = true;
                break;
            }
        }
        return !hasIncreasingStraight || Pattern.matches("[a-z]*[iol][a-z]*", password)
            || !Pattern.matches("[a-z]*([a-z])\\1[a-z]*((?!\\1)[a-z])\\2[a-z]*", password);
    }
}
