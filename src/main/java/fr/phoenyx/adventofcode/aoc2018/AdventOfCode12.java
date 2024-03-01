package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.MathUtils;

public class AdventOfCode12 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);
    private static final Map<Integer, Character> pots = new HashMap<>();
    private static final Map<String, Character> rules = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) continue;
                if (currentLine.charAt(0) == 'i') {
                    String initialState = currentLine.split("initial state: ")[1];
                    for (int i = 0; i < initialState.length(); i++) pots.put(i, initialState.charAt(i));
                } else {
                    String[] split = currentLine.split(" => ");
                    rules.put(split[0], split[1].charAt(0));
                }
            }
            LOGGER.info("PART 1: {}", simulate(20));
            LOGGER.info("PART 2: {}", simulate(50000000000L - 20));
        }
    }

    private static long simulate(long generations) {
        long last = getResult();
        List<Integer> increases = new ArrayList<>();
        for (long i = 0; i < generations; i++) {
            Map<Integer, Character> next = getNextGeneration();
            pots.clear();
            pots.putAll(next);
            long result = getResult();
            int difference = (int) (result - last);
            increases.add(difference);
            if (MathUtils.getFrequency(increases).isPresent()) return result + (generations - i - 1) * difference;
            last = result;
        }
        return getResult();
    }

    private static Map<Integer, Character> getNextGeneration() {
        Map<Integer, Character> nextGeneration = new HashMap<>();
        int minIndex = pots.entrySet().stream().filter(e -> e.getValue() == '#').map(Entry::getKey).min(Integer::compare).orElseThrow() - 2;
        int maxIndex = pots.entrySet().stream().filter(e -> e.getValue() == '#').map(Entry::getKey).max(Integer::compare).orElseThrow() + 2;
        for (int i = minIndex; i <= maxIndex; i++) nextGeneration.put(i, rules.get(getPotEnvironnment(i)));
        return nextGeneration;
    }

    private static String getPotEnvironnment(int index) {
        StringBuilder sb = new StringBuilder();
        for (int i = index - 2; i <= index + 2; i++) sb.append(pots.getOrDefault(i, '.'));
        return sb.toString();
    }

    private static long getResult() {
        return pots.entrySet().stream().filter(e -> e.getValue() == '#').map(Entry::getKey).reduce(Integer::sum).orElseThrow();
    }
}
