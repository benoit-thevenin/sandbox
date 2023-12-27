package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode02 {

    private enum Hand {
        ROCK(1), PAPER(2), SCISSORS(3);

        final int value;

        Hand(int value) {
            this.value = value;
        }

        static Hand fromChar(char c) {
            if (c == 'A' || c == 'X') return ROCK;
            return c == 'B' || c == 'Y' ? PAPER : SCISSORS;
        }

        int getScoreAgainst(Hand other) {
            if (this == ROCK) {
                if (other == ROCK) return value + 3;
                return other == PAPER ? value : value + 6;
            }
            if (this == PAPER) {
                if (other == ROCK) return value + 6;
                return other == PAPER ? value + 3 : value;
            }
            if (other == ROCK) return value;
            return other == PAPER ? value + 6 : value + 3;
        }

        Hand getHandToPlay(char c) {
            if (this == ROCK) {
                if (c == 'X') return SCISSORS;
                return c == 'Y' ? ROCK : PAPER;
            }
            if (this == PAPER) {
                if (c == 'X') return ROCK;
                return c == 'Y' ? PAPER : SCISSORS;
            }
            if (c == 'X') return PAPER;
            return c == 'Y' ? SCISSORS : ROCK;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int result1 = 0;
            int result2 = 0;
            while ((currentLine = reader.readLine()) != null) {
                Hand opponentHand = Hand.fromChar(currentLine.charAt(0));
                Hand myHand = Hand.fromChar(currentLine.charAt(2));
                result1 += myHand.getScoreAgainst(opponentHand);
                Hand trueHand = opponentHand.getHandToPlay(currentLine.charAt(2));
                result2 += trueHand.getScoreAgainst(opponentHand);
            }
            LOGGER.info("PART 1: {}", result1);
            LOGGER.info("PART 2: {}", result2);
        }
    }
}
