package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.MathUtils;

public class AdventOfCode11 {

    private record Monkey(List<Long> items, LongUnaryOperator operation, Function<Long, Integer> test) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);
    private static long leastCommonMultiple = 1;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> monkeyLines = new ArrayList<>();
            List<Monkey> monkeysPart1 = new ArrayList<>();
            List<Monkey> monkeysPart2 = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) continue;
                monkeyLines.add(currentLine);
                if (monkeyLines.size() == 6) {
                    monkeysPart1.add(buildMonkey(monkeyLines));
                    monkeysPart2.add(buildMonkey(monkeyLines));
                    monkeyLines.clear();
                }
            }
            LOGGER.info("PART 1: {}", getMonkeyBusiness(monkeysPart1, true));
            LOGGER.info("PART 2: {}", getMonkeyBusiness(monkeysPart2, false));
        }
    }

    private static Monkey buildMonkey(List<String> lines) {
        List<Long> items = new ArrayList<>();
        Arrays.stream(lines.get(1).replace(" ", "").split(":")[1].split(",")).map(Long::parseLong).forEach(items::add);
        LongUnaryOperator operation;
        if (lines.get(2).contains("+")) {
            long operationParameter = Long.parseLong(lines.get(2).split("\\+ ")[1]);
            operation = a -> a + operationParameter;
        } else {
            String token = lines.get(2).split("\\* ")[1];
            if ("old".equals(token)) operation = a -> a * a;
            else {
                long operationParameter = Long.parseLong(token);
                operation = a -> a * operationParameter;
            }
        }
        int indexIfTrue = Integer.parseInt(lines.get(4).split("monkey ")[1]);
        int indexIfFalse = Integer.parseInt(lines.get(5).split("monkey ")[1]);
        long testParameter = Long.parseLong(lines.get(3).split("by ")[1]);
        leastCommonMultiple = MathUtils.leastCommonMultiple(leastCommonMultiple, testParameter);
        Function<Long, Integer> test = a -> a % testParameter == 0 ? indexIfTrue : indexIfFalse;
        return new Monkey(items, operation, test);
    }

    private static long getMonkeyBusiness(List<Monkey> monkeys, boolean isPart1) {
        long[] inspections = new long[monkeys.size()];
        int rounds = isPart1 ? 20 : 10000;
        for (int i = 0; i < rounds; i++) {
            for (int j = 0; j < monkeys.size(); j++) {
                Monkey monkey = monkeys.get(j);
                inspections[j] += monkey.items.size();
                for (long item : monkey.items) {
                    long newItem = monkey.operation.applyAsLong(item);
                    if (isPart1) newItem /= 3;
                    else newItem %= leastCommonMultiple;
                    int target = monkey.test.apply(newItem);
                    monkeys.get(target).items.add(newItem);
                }
                monkey.items.clear();
            }
        }
        Arrays.sort(inspections);
        return inspections[inspections.length - 1] * inspections[inspections.length - 2];
    }
}
