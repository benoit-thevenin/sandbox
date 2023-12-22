package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode04 {

    private static class Card {
        int score;
        int matchingNumbers;
        int quantity = 1;

        Card(String line) {
            String values = line.split(": ")[1];
            String[] split = values.split(" \\| ");
            String winningNumber = split[0];
            String numbers = split[1];
            Set<Integer> winningNumbers = new HashSet<>();
            for (String number : winningNumber.split(" ")) {
                winningNumbers.add(Integer.parseInt(number));
            }
            for (String number : numbers.split(" ")) {
                int value = Integer.parseInt(number);
                if (winningNumbers.contains(value)) {
                    matchingNumbers++;
                    if (score == 0) score = 1;
                    else score *= 2;
                }
            }
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);
    private static final List<Card> CARDS = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) CARDS.add(new Card(currentLine.replace("  ", " ")));
            setCardsQuantity();
            LOGGER.info("PART 1: {}", CARDS.stream().map(c -> c.score).reduce(Integer::sum).orElse(0));
            LOGGER.info("PART 2: {}", CARDS.stream().map(c -> c.quantity).reduce(Integer::sum).orElse(0));
        }
    }

    private static void setCardsQuantity() {
        for (int i = 0; i < CARDS.size(); i++) {
            Card card = CARDS.get(i);
            if (card.matchingNumbers == 0) continue;
            for (int j = 0; j < card.matchingNumbers; j++) {
                int index = i + j + 1;
                if (index >= CARDS.size()) break;
                CARDS.get(index).quantity += card.quantity;
            }
        }
    }
}
