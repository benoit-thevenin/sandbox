package fr.phoenyx.adventofcode.aoc2025;

import fr.phoenyx.models.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            long sum1 = 0;
            long sum2 = 0;
            while ((currentLine = reader.readLine()) != null) {
                for (String s : currentLine.split(",")) {
                    Range range = Range.buildFromStartAndEndInclusive(s);
                    for (long i = range.start; i < range.end; i++) {
                        if (isInvalidId1(i)) sum1 += i;
                        if (isInvalidId2(i)) sum2 += i;
                    }
                }
            }
            LOGGER.info("PART 1: {}", sum1);
            LOGGER.info("PART 2: {}", sum2);
        }
    }

    private static boolean isInvalidId1(long id) {
        String digits = Long.toString(id);
        return digits.substring(0, digits.length() / 2).equals(digits.substring(digits.length() / 2));
    }

    private static boolean isInvalidId2(long id) {
        String digits = Long.toString(id);
        for (int i = 1; i <= digits.length() / 2; i++) {
            if (digits.length() % i != 0) continue;
            String sub = digits.substring(0, i);
            boolean isInvalid = true;
            for (int j = 1; j < digits.length() / i; j++) {
                if (!sub.equals(digits.substring(i * j, i * (j + 1)))) {
                    isInvalid = false;
                    break;
                }
            }
            if (isInvalid) return isInvalid;
        }
        return false;
    }
}
