package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntBinaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Map<String, Integer> registers = new HashMap<>();
            int highestValue = Integer.MIN_VALUE;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" ");
                int checkValue = registers.getOrDefault(split[4], 0);
                int conditionValue = Integer.parseInt(split[6]);
                if (isConditionMet(checkValue, split[5], conditionValue)) {
                    IntBinaryOperator operation = "inc".equals(split[1]) ? Integer::sum : (a, b) -> a - b;
                    registers.put(split[0], operation.applyAsInt(registers.getOrDefault(split[0], 0), Integer.parseInt(split[2])));
                    if (registers.get(split[0]) > highestValue) highestValue = registers.get(split[0]);
                }
            }
            LOGGER.info("PART 1: {}", registers.values().stream().max(Integer::compare).orElseThrow());
            LOGGER.info("PART 2: {}", highestValue);
        }
    }

    private static boolean isConditionMet(int checkValue, String operator, int conditionValue) {
        return "<".equals(operator) && checkValue < conditionValue
            || "<=".equals(operator) && checkValue <= conditionValue
            || ">".equals(operator) && checkValue > conditionValue
            || ">=".equals(operator) && checkValue >= conditionValue
            || "==".equals(operator) && checkValue == conditionValue
            || "!=".equals(operator) && checkValue != conditionValue;
    }
}
