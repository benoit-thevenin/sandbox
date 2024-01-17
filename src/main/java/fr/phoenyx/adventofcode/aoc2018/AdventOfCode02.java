package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> ids = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) ids.add(currentLine);
            LOGGER.info("PART 1: {}", getChecksum(ids));
            LOGGER.info("PART 2: {}", getCommonLettersFromIds(ids));
        }
    }

    private static int getChecksum(List<String> ids) {
        int twoSameLettersCount = 0;
        int threeSameLettersCount = 0;
        for (String id : ids) {
            Map<Character, Integer> letterCount = Utils.getLetterCount(id);
            if (letterCount.values().stream().anyMatch(c -> c == 2)) twoSameLettersCount++;
            if (letterCount.values().stream().anyMatch(c -> c == 3)) threeSameLettersCount++;
        }
        return twoSameLettersCount * threeSameLettersCount;
    }

    private static String getCommonLettersFromIds(List<String> ids) {
        for (int i = 0; i < ids.size() - 1; i++) {
            String id1 = ids.get(i);
            for (int j = i + 1; j < ids.size(); j++) {
                String id2 = ids.get(j);
                StringBuilder result = new StringBuilder();
                for (int k = 0; k < id1.length(); k++) if (id1.charAt(k) == id2.charAt(k)) result.append(id1.charAt(k));
                if (result.length() == id1.length() - 1) return result.toString();
            }
        }
        throw new IllegalArgumentException("Not found");
    }
}
