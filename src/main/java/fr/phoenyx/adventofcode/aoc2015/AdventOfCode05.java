package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode05 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);
    private static final List<Character> vowels = List.of('a', 'e', 'i', 'o', 'u');

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
        return !Pattern.matches(".*((ab)|(cd)|(pq)|(xy)).*", s)
            && Pattern.matches("(.*[aeiou].*){3,}", s)
            && Pattern.matches(".*([a-z])\\1.*", s);
    }

    private static boolean isNice2(String s) {
        return Pattern.matches(".*([a-z]).\\1.*", s) && Pattern.matches(".*([a-z]{2}).*\\1.*", s);
    }
}
