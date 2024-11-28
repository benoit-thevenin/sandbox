package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.phoenyx.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode17 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode17.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode17.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Integer> containers = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) containers.add(Integer.parseInt(currentLine));
            Map.Entry<Integer, Integer> result = getCombinations(containers);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Map.Entry<Integer, Integer> getCombinations(List<Integer> containers) {
        int count1 = 0;
        int count2 = 0;
        int[] indexes = new int[containers.size()];
        for (int i = 0; i < indexes.length; i++) indexes[i] = i;
        int min = containers.size();
        for (int i = 1; i < containers.size(); i++) {
            boolean areAllCombinationsAboveN = true;
            List<int[]> combinations = MathUtils.getAllCombinations(indexes, i);
            for (int[] combination : combinations) {
                int sum = 0;
                for (int index : combination) sum += containers.get(index);
                if (sum <= 150) {
                    areAllCombinationsAboveN = false;
                    if (sum == 150) {
                        count1++;
                        min = Math.min(i, min);
                        if (min == i) count2++;
                    }
                }
            }
            if (areAllCombinationsAboveN) break;
        }
        return new AbstractMap.SimpleEntry<>(count1, count2);
    }
}
