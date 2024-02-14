package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                Entry<Integer, Integer> result = getResult(currentLine);
                LOGGER.info("PART 1: {}", result.getKey());
                LOGGER.info("PART 2: {}", result.getValue());
            }
        }
    }

    private static Entry<Integer, Integer> getResult(String s) {
        int score = 0;
        int garbageLength = 0;
        int index = 0;
        int depth = 0;
        boolean isInGarbage = false;
        while (index < s.length()) {
            if (s.charAt(index) == '!') index++;
            else if (isInGarbage) {
                if (s.charAt(index) == '>') isInGarbage = false;
                else garbageLength++;
            } else {
                if (s.charAt(index) == '{') {
                    depth++;
                    score += depth;
                } else if (s.charAt(index) == '}') depth--;
                else if (s.charAt(index) == '<') isInGarbage = true;
            }
            index++;
        }
        return new SimpleEntry<>(score, garbageLength);
    }
}
