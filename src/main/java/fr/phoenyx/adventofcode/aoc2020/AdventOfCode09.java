package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            long resultPart1 = 0L;
            List<Long> values = new ArrayList<>();
            LinkedList<Long> lastInputs = new LinkedList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                long current = Long.parseLong(currentLine);
                values.add(current);
                if (lastInputs.size() < 25) lastInputs.add(Long.parseLong(currentLine));
                else {
                    if (resultPart1 == 0L && !isNextInputValid(lastInputs, current)) resultPart1 = current;
                    lastInputs.remove();
                    lastInputs.add(current);
                }
            }
            LOGGER.info("PART 1: {}", resultPart1);
            LOGGER.info("PART 2: {}", getXmasWeakness(values, resultPart1));
        }
    }

    private static boolean isNextInputValid(LinkedList<Long> lastInputs, long input) {
        for (int i = 0; i < lastInputs.size() - 1; i++) {
            for (int j = i + 1; j < lastInputs.size(); j++) if (lastInputs.get(i) + lastInputs.get(j) == input) return true;
        }
        return false;
    }

    private static long getXmasWeakness(List<Long> values, long invalid) {
        for (int i = 0; i < values.size(); i++) {
            List<Long> range = new ArrayList<>();
            long sum = values.get(i);
            range.add(values.get(i));
            for (int j = i + 1; j < values.size(); j++) {
                sum += values.get(j);
                range.add(values.get(j));
                if (sum == invalid) {
                    Collections.sort(range);
                    return range.get(0) + range.get(range.size() - 1);
                } else if (sum > invalid) break;
            }
        }
        throw new IllegalArgumentException("Wrong dataset");
    }
}
