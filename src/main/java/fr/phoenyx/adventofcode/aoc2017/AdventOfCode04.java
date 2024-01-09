package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode04 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int count1 = 0;
            int count2 = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (isValid1(currentLine)) count1++;
                if (isValid2(currentLine)) count2++;
            }
            LOGGER.info("PART 1: {}", count1);
            LOGGER.info("PART 2: {}", count2);
        }
    }

    private static boolean isValid1(String passphrase) {
        String[] words = passphrase.split(" ");
        return words.length == Arrays.stream(words).distinct().count();
    }

    private static boolean isValid2(String passphrase) {
        String[] words = passphrase.split(" ");
        for (int i = 0; i < words.length - 1; i++) {
            char[] sortedChars = words[i].toCharArray();
            Arrays.sort(sortedChars);
            for (int j = i + 1; j < words.length; j++) {
                char[] toCompare = words[j].toCharArray();
                Arrays.sort(toCompare);
                if (Arrays.equals(sortedChars, toCompare)) return false;
            }
        }
        return true;
    }
}
