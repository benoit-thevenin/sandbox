package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode14 {

    private static class RecipesSolver {
        List<Integer> scores = new ArrayList<>();
        int index1 = 0;
        int index2 = 1;

        RecipesSolver() {
            scores.add(3);
            scores.add(7);
        }

        String solve(int input) {
            while (scores.size() < input + 10) next();
            return scores.stream().skip(input).limit(10).map(s -> Integer.toString(s)).collect(Collectors.joining());
        }

        int solve(String input) {
            int offset = 0;
            while (true) {
                next();
                while (offset + input.length() < scores.size()) {
                    boolean found = true;
                    for (int i = 0; i < input.length(); i++) {
                        if (input.charAt(i) - '0' != scores.get(offset + i)) {
                            found = false;
                            break;
                        }
                    }
                    if (found) return offset;
                    offset++;
                }
            }
        }

        void next() {
            int current1 = scores.get(index1);
            int current2 = scores.get(index2);
            int sum = current1 + current2;
            if (sum < 10) scores.add(sum);
            else {
                scores.add(sum / 10);
                scores.add(sum % 10);
            }
            index1 = (index1 + current1 + 1) % scores.size();
            index2 = (index2 + current2 + 1) % scores.size();
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                LOGGER.info("PART 1: {}", new RecipesSolver().solve(Integer.parseInt(currentLine)));
                LOGGER.info("PART 2: {}", new RecipesSolver().solve(currentLine));
            }
        }
    }
}
