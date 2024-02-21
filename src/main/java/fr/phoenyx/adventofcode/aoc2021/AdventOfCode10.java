package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode10 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);
    private static final Map<Character, Character> CLOSING_TO_OPENING = new HashMap<>();
    private static final Map<Character, Integer> ILLEGAL_CHARACTER_SCORES = new HashMap<>();

    static {
        CLOSING_TO_OPENING.put(')', '(');
        CLOSING_TO_OPENING.put(']', '[');
        CLOSING_TO_OPENING.put('}', '{');
        CLOSING_TO_OPENING.put('>', '<');
        ILLEGAL_CHARACTER_SCORES.put(')', 3);
        ILLEGAL_CHARACTER_SCORES.put(']', 57);
        ILLEGAL_CHARACTER_SCORES.put('}', 1197);
        ILLEGAL_CHARACTER_SCORES.put('>', 25137);
    }

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Long> autocompleteScores = new ArrayList<>();
            int totalSyntaxErrorScore = 0;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                Entry<Optional<Character>, Long> analysis = analyze(currentLine);
                Optional<Character> illegalCharacter = analysis.getKey();
                if (illegalCharacter.isPresent()) totalSyntaxErrorScore += ILLEGAL_CHARACTER_SCORES.get(illegalCharacter.get());
                else autocompleteScores.add(analysis.getValue());
            }
            Collections.sort(autocompleteScores);
            LOGGER.info("PART 1: {}", totalSyntaxErrorScore);
            LOGGER.info("PART 2: {}", autocompleteScores.get(autocompleteScores.size() / 2));
        }
    }

    private static Entry<Optional<Character>, Long> analyze(String line) {
        Deque<Character> openedChunks = new ArrayDeque<>();
        for (char c : line.toCharArray()) {
            if (CLOSING_TO_OPENING.containsValue(c)) openedChunks.push(c);
            else if (CLOSING_TO_OPENING.get(c).equals(openedChunks.peek())) openedChunks.pop();
            else return new SimpleEntry<>(Optional.of(c), 0L);
        }
        return new SimpleEntry<>(Optional.empty(), getAutocompleteScore(openedChunks));
    }

    private static long getAutocompleteScore(Deque<Character> opened) {
        long score = 0;
        while (!opened.isEmpty()) {
            score *= 5;
            score += "0([{<".indexOf((opened.pop()));
        }
        return score;
    }
}
