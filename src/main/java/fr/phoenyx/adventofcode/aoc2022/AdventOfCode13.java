package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode13 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            StringBuilder sb = new StringBuilder("{");
            int i = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) continue;
                if (i != 0) sb.append(',');
                i++;
                sb.append('"');
                sb.append(i);
                sb.append("\":");
                sb.append(currentLine);
            }
            sb.append('}');
            JSONObject json = new JSONObject(sb.toString());
            LOGGER.info("PART 1: {}", getSumOfOrderedPairOfPacketsIndexes(json));
            LOGGER.info("PART 2: {}", getDecoderKey(json));
        }
    }

    private static int getSumOfOrderedPairOfPacketsIndexes(JSONObject json) {
        int sum = 0;
        for (int i = 0; i < json.length() / 2; i++) {
            JSONArray left = (JSONArray) json.get(Integer.toString(2 * i + 1));
            JSONArray right = (JSONArray) json.get(Integer.toString(2 * i + 2));
            if (compare(left, right) < 0) sum += i + 1;
        }
        return sum;
    }

    private static int compare(JSONArray left, JSONArray right) {
        for (int i = 0; i < Math.min(left.length(), right.length()); i++) {
            int compare = compare(left.get(i), right.get(i));
            if (compare != 0) return compare;
        }
        return Integer.compare(left.length(), right.length());
    }

    private static int compare(Object left, Object right) {
        if (left instanceof JSONArray leftArray && right instanceof JSONArray rightArray) return compare(leftArray, rightArray);
        if (left instanceof Integer leftValue && right instanceof Integer rightValue) return Integer.compare(leftValue, rightValue);
        return compare(getJSONArrayFromObject(left), getJSONArrayFromObject(right));
    }

    private static JSONArray getJSONArrayFromObject(Object o) {
        if (o instanceof JSONArray a) return a;
        if (o instanceof Integer) return new JSONArray("[" + o + "]");
        throw new IllegalArgumentException("Expected JSONArray or Integer");
    }

    private static int getDecoderKey(JSONObject json) {
        json.put("divider1", new JSONArray("[[2]]"));
        json.put("divider2", new JSONArray("[[6]]"));
        List<String> orderedKeys = json.toMap().keySet().stream()
            .sorted((e1, e2) -> compare(json.get(e1), json.get(e2)))
            .toList();
        return (orderedKeys.indexOf("divider1") + 1) * (orderedKeys.indexOf("divider2") + 1);
    }
}
