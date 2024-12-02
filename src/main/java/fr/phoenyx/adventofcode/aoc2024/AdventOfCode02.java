package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class AdventOfCode02 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<List<Integer>> reports = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) reports.add(Arrays.stream(currentLine.split(" ")).map(Integer::parseInt).toList());
            LOGGER.info("PART 1: {}", reports.stream().filter(AdventOfCode02::isSafe).count());
            LOGGER.info("PART 2: {}", countSafeReportsWithProblemDampener(reports));
        }
    }

    private static boolean isSafe(List<Integer> report) {
        boolean shouldIncrease = report.get(0) < report.get(1);
        BiFunction<Integer, Integer, Boolean> comparator = shouldIncrease ? (r1, r2) -> r1 < r2 : (r1, r2) -> r1 > r2;
        for (int i = 0; i < report.size() - 1; i++)
            if (!isSafe(comparator, report.get(i), report.get(i + 1))) return false;
        return true;
    }

    private static boolean isSafe(BiFunction<Integer, Integer, Boolean> comparator, int value1, int value2) {
        return comparator.apply(value1, value2) && Math.abs(value2 - value1) <= 3;
    }

    private static int countSafeReportsWithProblemDampener(List<List<Integer>> reports) {
        int count = 0;
        for (List<Integer> report : reports) {
            if (isSafe(report)) count++;
            else {
                for (int i = 0; i < report.size(); i++) {
                    List<Integer> reducedReport = new ArrayList<>(report);
                    reducedReport.remove(i);
                    if (isSafe(reducedReport)) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }
}
