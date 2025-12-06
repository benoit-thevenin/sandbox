package fr.phoenyx.adventofcode.aoc2025;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdventOfCode06 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Character> operators = new ArrayList<>();
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains("+")) operators = Arrays.stream(currentLine.split(" ")).filter(s -> !s.isEmpty()).map(s -> s.charAt(0)).toList();
                else lines.add(currentLine);
            }
            List<List<Long>> problems = lines.stream().map(line ->
                Arrays.stream(line.split(" ")).filter(s -> !s.isEmpty()).map(Long::parseLong).toList()
            ).toList();
            LOGGER.info("PART 1: {}", getSum(operators, problems));
            problems = buildCephalopodProblems(lines);
            LOGGER.info("PART 2: {}", getSum(operators, problems));
        }
    }

    private static List<List<Long>> buildCephalopodProblems(List<String> lines) {
        List<List<Long>> problems = new ArrayList<>();
        List<Long> currentValues = new ArrayList<>();
        for (int i = 0; i < lines.get(0).length(); i++) {
            StringBuilder sb = new StringBuilder();
            for (String line : lines) if (line.charAt(i) != ' ') sb.append(line.charAt(i));
            if (sb.isEmpty()) {
                problems.add(currentValues);
                currentValues = new ArrayList<>();
            } else currentValues.add(Long.parseLong(sb.toString()));
        }
        problems.add(currentValues);
        return problems;
    }

    private static long getSum(List<Character> operators, List<List<Long>> problems) {
        long sum = 0;
        for (int i = 0; i < operators.size(); i++) {
            List<Long> problem = new ArrayList<>();
            if (operators.size() != problems.size()) for (List<Long> value : problems) problem.add(value.get(i));
            else problem = problems.get(i);
            sum += operators.get(i) == '+' ? problem.stream().reduce(0L, Long::sum) : problem.stream().reduce(1L, (a, b) -> a * b);
        }
        return sum;
    }
}
