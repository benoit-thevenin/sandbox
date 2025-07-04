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

public class AdventOfCode07 {

    private record Equation(long result, List<Long> values) {
        boolean isValid(boolean isPart2) {
            if (values.size() == 1) return result == values.get(0);
            List<BiFunction<Long, Long, Long>> operators = new ArrayList<>(List.of(Long::sum, (a, b) -> a * b));
            if (isPart2) operators.add((a, b) -> Long.parseLong(a.toString() + b));
            int possibilities = (int) Math.pow(operators.size(), values.size() - 1);
            for (int i = 0; i < possibilities; i++) {
                long subresult = values.get(0);
                for (int j = 1; j < values.size(); j++)
                    subresult = operators.get((i / (int) Math.pow(operators.size(), j - 1)) % operators.size()).apply(subresult, values.get(j));
                if (subresult == result) return true;
            }
            return false;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Equation> equations = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(": ");
                equations.add(new Equation(Long.parseLong(split[0]), Arrays.stream(split[1].split(" ")).map(Long::parseLong).toList()));
            }
            LOGGER.info("PART 1: {}", equations.stream().filter(e -> e.isValid(false)).map(e -> e.result).reduce(0L, Long::sum));
            LOGGER.info("PART 2: {}", equations.stream().filter(e -> e.isValid(true)).map(e -> e.result).reduce(0L, Long::sum));
        }
    }
}
