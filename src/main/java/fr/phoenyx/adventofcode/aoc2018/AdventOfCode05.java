package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode05 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                LOGGER.info("PART 1: {}", shrink(currentLine).length());
                LOGGER.info("PART 2: {}", getShortestShrinkedSuit(currentLine).length());
            }
        }
    }

    private static String shrink(String suit) {
        String current = suit;
        boolean shrinked = true;
        while (shrinked) {
            shrinked = false;
            StringBuilder next = new StringBuilder();
            for (int i = 0; i < current.length(); i++) {
                if (i == current.length() - 1) next.append(current.charAt(i));
                else {
                    char c1 = current.charAt(i);
                    char c2 = current.charAt(i + 1);
                    if (!shrink(c1, c2)) next.append(c1);
                    else i++;
                }
            }
            if (next.length() < current.length()) shrinked = true;
            current = next.toString();
        }
        return current;
    }

    private static boolean shrink(char c1, char c2) {
        return c1 != c2 && Character.toString(c1).equalsIgnoreCase(Character.toString(c2));
    }

    private static String getShortestShrinkedSuit(String suit) {
        Set<String> types = Arrays.stream(suit.split(""))
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        String shortestSuit = shrink(suit);
        for (String type : types) {
            StringBuilder next = new StringBuilder();
            for (char c : suit.toCharArray())if (!Character.toString(c).equalsIgnoreCase(type)) next.append(c);
            String shrinkedNext = shrink(next.toString());
            if (shrinkedNext.length() < shortestSuit.length()) shortestSuit = shrinkedNext;
        }
        return shortestSuit;
    }
}
