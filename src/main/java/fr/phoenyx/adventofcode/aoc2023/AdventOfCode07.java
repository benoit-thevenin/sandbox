package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.phoenyx.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode07 {

    private enum HandType {
        FIVE(1), FOUR(2), FULL(3), THREE(4), TWO_PAIRS(5), PAIR(6), HIGH(7);

        final int index;

        HandType(int index) {
            this.index = index;
        }

        static HandType getFromHand(String hand, boolean joker) {
            Map<Character, Integer> map = Utils.getLetterCount(hand);
            boolean hasJoker = joker && map.containsKey('J');
            if (map.size() == 1) return FIVE;
            if (map.size() == 2) {
                if (hasJoker) return FIVE;
                return map.values().stream().anyMatch(v -> v == 4) ? FOUR : FULL;
            }
            if (map.size() == 3) {
                boolean hasThree = map.values().stream().anyMatch(v -> v == 3);
                if (hasJoker) return map.get('J') > 1 || hasThree ? FOUR : FULL;
                return hasThree ? THREE : TWO_PAIRS;
            }
            if (map.size() == 4) return hasJoker ? THREE : PAIR;
            return hasJoker ? PAIR : HIGH;
        }
    }

    private static class Hand implements Comparable<Hand> {

        static Map<Character, Integer> cardsValue = new HashMap<>();
        static {
            char[] chars = new char[]{'2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'};
            for (int i = 0; i < chars.length; i++) cardsValue.put(chars[i], i + 1);
        }

        String cards;
        int bid;
        HandType type;
        int handScore;

        Hand(String line, boolean joker) {
            String[] split = line.split(" ");
            cards = split[0];
            bid = Integer.parseInt(split[1]);
            type = HandType.getFromHand(cards, joker);
            for (int i = 0; i < cards.length(); i++) {
                char c = cards.charAt(i);
                if (c != 'J' || !joker) handScore += (int) Math.pow((cardsValue.size() + 1), (5 - i)) * cardsValue.get(c);
            }
        }

        @Override
        public int compareTo(Hand o) {
            int typeCompare = Integer.compare(o.type.index, type.index);
            return typeCompare != 0 ? typeCompare : Integer.compare(handScore, o.handScore);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);
    private static final List<Hand> hands = new ArrayList<>();
    private static final List<Hand> handsJoker = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                hands.add(new Hand(currentLine, false));
                handsJoker.add(new Hand(currentLine, true));
            }
            Collections.sort(hands);
            Collections.sort(handsJoker);
            int resultPart1 = 0;
            int resultPart2 = 0;
            for (int i = 0; i < hands.size(); i++) {
                Hand hand = hands.get(i);
                Hand handJoker = handsJoker.get(i);
                resultPart1 += hand.bid * (i + 1);
                resultPart2 += handJoker.bid * (i + 1);
            }
            LOGGER.info("PART 1: {}", resultPart1);
            LOGGER.info("PART 2: {}", resultPart2);
        }
    }
}
