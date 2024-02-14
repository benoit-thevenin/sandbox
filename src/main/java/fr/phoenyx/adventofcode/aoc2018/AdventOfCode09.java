package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode09 {

    private static class Marble {
        final int id;
        Marble left;
        Marble right;

        Marble(int id) {
            this.id = id;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" ");
                int players = Integer.parseInt(split[0]);
                int marbles = Integer.parseInt(split[split.length - 2]);
                LOGGER.info("PART 1: {}", getHighestScore(players, marbles));
                LOGGER.info("PART 2: {}", getHighestScore(players, 100 * marbles));
            }
        }
    }

    private static long getHighestScore(int players, int marbles) {
        long[] scores = new long[players];
        Marble current = new Marble(0);
        current.left = current;
        current.right = current;
        for (int i = 1; i <= marbles; i++) {
            if (i % 23 == 0) {
                scores[(i - 1) % players] += i;
                for (int j = 0; j < 7; j++) current = current.left;
                scores[(i - 1) % players] += current.id;
                current.left.right = current.right;
                current.right.left = current.left;
                current = current.right;
            } else {
                current = current.right;
                Marble next = new Marble(i);
                next.left = current;
                next.right = current.right;
                next.left.right = next;
                next.right.left = next;
                current = next;
            }
        }
        Arrays.sort(scores);
        return scores[scores.length - 1];
    }
}
