package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdventOfCode05 {

    private record OrderingRule(int before, int after) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            boolean isOrderingRules = true;
            List<OrderingRule> orderingRules = new ArrayList<>();
            List<List<Integer>> updates = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isEmpty()) isOrderingRules = false;
                else if (isOrderingRules) {
                    String[] split = currentLine.split("\\|");
                    orderingRules.add(new OrderingRule(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
                } else updates.add(new ArrayList<>(Arrays.stream(currentLine.split(",")).map(Integer::parseInt).toList()));
            }
            LOGGER.info("PART 1: {}", getCorrectlyOrderedUpdatesSum(orderingRules, updates));
            LOGGER.info("PART 2: {}", getIncorrectlyOrderedUpdatesSum(orderingRules, updates));
        }
    }

    private static int getCorrectlyOrderedUpdatesSum(List<OrderingRule> orderingRules, List<List<Integer>> updates) {
        return updates.stream()
            .filter(update -> isCorrectlyOrdered(orderingRules, update))
            .map(update -> update.get(update.size() / 2))
            .reduce(0, Integer::sum);
    }

    private static boolean isCorrectlyOrdered(List<OrderingRule> orderingRules, List<Integer> update) {
        for (int i = 0; i < update.size(); i++) {
            int page = update.get(i);
            List<Integer> subupdate = update.subList(i + 1, update.size());
            if (orderingRules.stream().filter(orderingRule -> orderingRule.after == page)
                .anyMatch(orderingRule -> subupdate.contains(orderingRule.before))) return false;
        }
        return true;
    }

    private static int getIncorrectlyOrderedUpdatesSum(List<OrderingRule> orderingRules, List<List<Integer>> updates) {
        List<List<Integer>> toFix = updates.stream().filter(update -> !isCorrectlyOrdered(orderingRules, update)).toList();
        for (List<Integer> update : toFix) {
            update.sort((page1, page2) -> {
                if (orderingRules.stream().anyMatch(orderingRule -> orderingRule.before == page1 && orderingRule.after == page2)) return -1;
                if (orderingRules.stream().anyMatch(orderingRule -> orderingRule.before == page2 && orderingRule.after == page1)) return 1;
                return 0;
            });
        }
        return toFix.stream().map(update -> update.get(update.size() / 2)).reduce(0, Integer::sum);
    }
}
