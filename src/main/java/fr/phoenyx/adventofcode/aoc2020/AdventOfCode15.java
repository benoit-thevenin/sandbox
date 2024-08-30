package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode15 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);
    private static final List<Integer> STARTING_NUMBERS = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                for (String startingNumber : currentLine.split(",")) STARTING_NUMBERS.add(Integer.parseInt(startingNumber));
            }
            LOGGER.info("PART 1: {}", getNumberSpoken(2020));
            LOGGER.info("PART 2: {}", getNumberSpoken(30000000));
        }
    }

    private static int getNumberSpoken(int lastTurn) {
        int turn = 1;
        Map<Integer, Entry<Integer, Integer>> spoken = new HashMap<>();
        for (int number : STARTING_NUMBERS) {
            spoken.put(number, new SimpleEntry<>(0, turn));
            turn++;
        }
        int lastSpoken = STARTING_NUMBERS.get(STARTING_NUMBERS.size() - 1);
        while (turn <= lastTurn) {
            Entry<Integer, Integer> turnsSpoken = spoken.get(lastSpoken);
            if (turnsSpoken.getKey() == 0) lastSpoken = 0;
            else lastSpoken = turnsSpoken.getValue() - turnsSpoken.getKey();
            if (spoken.containsKey(lastSpoken)) spoken.put(lastSpoken, new SimpleEntry<>(spoken.get(lastSpoken).getValue(), turn));
            else spoken.put(lastSpoken, new SimpleEntry<>(0, turn));
            turn++;
        }
        return lastSpoken;
    }
}
