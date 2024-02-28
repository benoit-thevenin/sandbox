package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode12 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                JSONObject json = new JSONObject(currentLine);
                LOGGER.info("PART 1: {}", getSumOfNumbers(json.toMap(), true));
                LOGGER.info("PART 2: {}", getSumOfNumbers(json.toMap(), false));
            }
        }
    }

    private static int getSumOfNumbers(Map<?, ?> json, boolean isPart1) {
        if (!isPart1 && json.containsValue("red")) return 0;
        int sum = 0;
        for (Object o : json.values()) {
            if (o instanceof Integer v) sum += v;
            else if (o instanceof Map<?, ?> subJson) sum += getSumOfNumbers(subJson, isPart1);
            else if (o instanceof List<?> jsonArray) sum += getSumOfNumbers(jsonArray, isPart1);
        }
        return sum;
    }

    private static int getSumOfNumbers(List<?> jsonArray, boolean isPart1) {
        int sum = 0;
        for (Object o : jsonArray) {
            if (o instanceof Integer v) sum += v;
            else if (o instanceof Map<?, ?> subJson) sum += getSumOfNumbers(subJson, isPart1);
            else if (o instanceof List<?> subJsonArray) sum += getSumOfNumbers(subJsonArray, isPart1);
        }
        return sum;
    }
}
