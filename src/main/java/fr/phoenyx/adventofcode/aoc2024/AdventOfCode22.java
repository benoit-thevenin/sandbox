package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdventOfCode22 {

    private static class Buyer {
        List<Long> secrets = new ArrayList<>();
        List<Integer> buyPrices = new ArrayList<>();
        List<Integer> diffs = new ArrayList<>();

        void generateDailySecrets() {
            buyPrices.add((int) (secrets.get(0) % 10));
            for (int i = 0; i < 2000; i++) {
                generate();
                buyPrices.add((int) (secrets.get(i + 1) % 10));
                diffs.add(buyPrices.get(i + 1) - buyPrices.get(i));
            }
        }

        private void generate() {
            long secret = secrets.get(secrets.size() - 1);
            secret ^= secret * 64 % 16777216;
            secret ^= secret / 32;
            secrets.add(secret ^ secret * 2048 % 16777216);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode22.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode22.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Buyer> buyers = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                buyers.add(new Buyer());
                buyers.get(buyers.size() - 1).secrets.add(Long.parseLong(currentLine));
            }
            buyers.forEach(Buyer::generateDailySecrets);
            LOGGER.info("PART 1: {}", buyers.stream().map(buyer -> buyer.secrets.get(buyer.secrets.size() - 1)).reduce(0L, Long::sum));
            LOGGER.info("PART 2: {}", getBestTotalPrice(buyers));
        }
    }

    private static long getBestTotalPrice(List<Buyer> buyers) {
        Map<List<Integer>, Long> cache = new HashMap<>();
        for (Buyer buyer : buyers) {
            Set<List<Integer>> visited = new HashSet<>();
            for (int i = 0; i < buyer.diffs.size() - 3; i++) {
                List<Integer> sequence = buyer.diffs.subList(i, i + 4);
                if (visited.add(sequence)) cache.put(sequence, cache.getOrDefault(sequence, 0L) + buyer.buyPrices.get(i + 4));
            }
        }
        return cache.values().stream().max(Long::compare).orElseThrow();
    }
}
