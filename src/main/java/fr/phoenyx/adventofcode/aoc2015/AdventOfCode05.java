package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode05 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);
    private static final List<Character> vowels = List.of('a', 'e', 'i', 'o', 'u');
    private static final List<String> forbidden = List.of("ab", "cd", "pq", "xy");

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int resultPart1 = 0;
            int resultPart2 = 0;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (isNice1(currentLine)) resultPart1++;
                if (isNice2(currentLine)) resultPart2++;
            }
            LOGGER.info("PART 1: {}", resultPart1);
            LOGGER.info("PART 2: {}", resultPart2);
        }
    }

    private static boolean isNice1(String s) {
        for (String f : forbidden) if (s.contains(f)) return false;
        int vowelCount = 0;
        boolean hasDoubledLetter = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (vowels.contains(c)) vowelCount++;
            if (i < s.length() - 1 && c == s.charAt(i + 1)) hasDoubledLetter = true;
        }
        return vowelCount >= 3 && hasDoubledLetter;
    }

    private static boolean isNice2(String s) {
        boolean hasDoubledLetter = false;
        for (int i = 0; i < s.length() - 2; i++) {
            if (s.charAt(i) == s.charAt(i + 2)) {
                hasDoubledLetter = true;
                break;
            }
        }
        if (!hasDoubledLetter) return false;
        List<String> letterPairs = new ArrayList<>();
        for (int i = 0; i < s.length() - 1; i++) {
            String pair = s.charAt(i) + "" + s.charAt(i + 1);
            String last = null;
            if (!letterPairs.isEmpty()) last = letterPairs.remove(letterPairs.size() - 1);
            if (letterPairs.contains(pair)) return true;
            if (last != null) letterPairs.add(last);
            letterPairs.add(pair);
        }
        return false;
    }
}
