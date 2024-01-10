package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode04 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int start = 111111;
            int end = 999999;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split("-");
                start = Integer.parseInt(split[0]);
                end = Integer.parseInt(split[1]);
            }
            LOGGER.info("PART 1: {}", countValidPasswords(start, end, true));
            LOGGER.info("PART 2: {}", countValidPasswords(start, end, false));
        }
    }

    private static int countValidPasswords(int start, int end, boolean part1) {
        int count = 0;
        for (int i = start; i < end; i++) {
            if (isValid(i, part1)) count++;
        }
        return count;
    }

    private static boolean isValid(int password, boolean part1) {
        String s = Integer.toString(password);
        boolean hasAdjacentsSameDigits = false;
        for (int i = 0; i < s.length() - 1; i++) {
            char c1 = s.charAt(i);
            char c2 = s.charAt(i + 1);
            if (c1 - c2 > 0) return false;
            if (c1 == c2 && (part1 || (i == 0 || s.charAt(i - 1) != c1) && (i == s.length() - 2 || s.charAt(i + 2) != c1))) {
                hasAdjacentsSameDigits = true;
            }
        }
        return hasAdjacentsSameDigits;
    }
}
