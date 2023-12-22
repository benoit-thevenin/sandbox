package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode07 {

    private enum HandType {
        FIVE(1), FOUR(2), FULL(3), THREE(4), TWO_PAIRS(5), PAIR(6), HIGH(7);

        final int index;

        HandType(int index) {
            this.index = index;
        }

        static HandType getFromHand(char[] hand, boolean joker) {
            Map<Character, Integer> map = new HashMap<>();
            for (char c : hand) {
                if (map.containsKey(c)) map.put(c, map.get(c) + 1);
                else map.put(c, 1);
            }
            if (map.entrySet().size() == 1) return FIVE;
            if (map.entrySet().size() == 5) return joker && map.containsKey('J') ? PAIR : HIGH;
            if (map.entrySet().size() == 2) {
                if (joker && map.containsKey('J')) return FIVE;
                List<Integer> values = new ArrayList<>(map.values());
                if (values.get(0) == 4 || values.get(1) == 4) return FOUR;
                else return FULL;
            }
            if (map.entrySet().size() == 3) {
                if (joker && map.containsKey('J')) {
                    int jokerCount = map.get('J');
                    map.remove('J');
                    List<Integer> values = new ArrayList<>(map.values());
                    return jokerCount > 1 || values.get(0) == 3 || values.get(1) == 3 ? FOUR : FULL;
                }
                List<Integer> values = new ArrayList<>(map.values());
                if (values.get(0) == 3 || values.get(1) == 3 || values.get(2) == 3) return THREE;
                else return TWO_PAIRS;
            }
            return joker && map.containsKey('J') ? THREE : PAIR;
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
            type = HandType.getFromHand(cards.toCharArray(), joker);
            for (int i = 0; i < cards.length(); i++) {
                char c = cards.charAt(i);
                if (c == 'J' && joker) continue;
                handScore += (int) Math.pow((cardsValue.size() + 1), (5 - i)) * cardsValue.get(c);
            }
        }

        @Override
        public int compareTo(Hand o) {
            int typeCompare = Integer.compare(o.type.index, type.index);
            if (typeCompare != 0) return typeCompare;
            return Integer.compare(handScore, o.handScore);
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
