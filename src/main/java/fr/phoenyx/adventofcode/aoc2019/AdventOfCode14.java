package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode14 {

    private static class Reaction {
        Map<String, Long> inputs = new HashMap<>();
        final long quantity;

        Reaction(long quantity) {
            this.quantity = quantity;
        }

        long getUsageNeeded(long quantityNeeded) {
            return quantityNeeded % quantity == 0 ? quantityNeeded / quantity : quantityNeeded / quantity + 1;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);
    private static final Map<String, Reaction> reactions = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] inputOutputSplit = currentLine.split(" => ");
                String[] outputSplit = inputOutputSplit[1].split(" ");
                Reaction reaction = new Reaction(Integer.parseInt(outputSplit[0]));
                String[] inputsSplit = inputOutputSplit[0].split(", ");
                for (String input : inputsSplit) {
                    String[] split = input.split(" ");
                    reaction.inputs.put(split[1], Long.parseLong(split[0]));
                }
                reactions.put(outputSplit[1], reaction);
            }
            LOGGER.info("PART 1: {}", getOreAmount(1));
            LOGGER.info("PART 2: {}", getFuelAmount());
        }
    }

    private static long getOreAmount(long fuelQuantity) {
        Map<String, Long> stocks = new HashMap<>();
        reactions.values().stream()
            .flatMap(r -> r.inputs.keySet().stream())
            .forEach(chemical -> stocks.put(chemical, 0L));
        stocks.put("FUEL", -fuelQuantity);
        boolean over = false;
        while (!over) {
            over = true;
            for (Entry<String, Long> entry : stocks.entrySet()) {
                if (!"ORE".equals(entry.getKey()) && entry.getValue() < 0) {
                    Reaction reaction = reactions.get(entry.getKey());
                    long num = reaction.getUsageNeeded(-entry.getValue());
                    for (Entry<String, Long> input : reaction.inputs.entrySet()) stocks.put(input.getKey(), stocks.get(input.getKey()) - num * input.getValue());
                    stocks.put(entry.getKey(), entry.getValue() + num * reaction.quantity);
                    over = false;
                }
            }
        }
        return -stocks.get("ORE");
    }

    private static long getFuelAmount() {
        long maxOre = 1000000000000L;
        long min = maxOre / getOreAmount(1);
        long max = min * 2;
        Set<Long> tried = new HashSet<>();
        while (true) {
            long tryFuel = (min + max) / 2;
            if (tried.contains(tryFuel)) return min;
            long currentOreAmount = getOreAmount(tryFuel);
            if (currentOreAmount <= maxOre) min = tryFuel;
            else max = tryFuel;
            tried.add(tryFuel);
        }
    }
}
