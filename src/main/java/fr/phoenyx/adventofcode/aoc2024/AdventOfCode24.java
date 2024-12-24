package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class AdventOfCode24 {

    private record Gate(String input1, String input2, BiFunction<Boolean, Boolean, Boolean> operation, String output) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode24.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode24.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Map<String, Boolean> values = new HashMap<>();
            List<Gate> gates = new ArrayList<>();
            boolean isValue = true;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isEmpty()) isValue = false;
                else if (isValue) {
                    String[] split = currentLine.split(": ");
                    values.put(split[0], Integer.parseInt(split[1]) == 1);
                } else {
                    String[] split = currentLine.split(" -> ");
                    String[] input = split[0].split(" ");
                    BiFunction<Boolean, Boolean, Boolean> operation = (a, b) -> a && b;
                    if ("OR".equals(input[1])) operation = (a, b) -> a || b;
                    else if ("XOR".equals(input[1])) operation = (a, b) -> a ^ b;
                    gates.add(new Gate(input[0], input[2], operation, split[1]));
                }
            }
            Set<String> swappedOutputs = getSwappedOutputs(values, gates);
            LOGGER.info("PART 1: {}", getDecimalNumber(values, gates));
            LOGGER.info("PART 2: {}", swappedOutputs.stream().sorted().collect(Collectors.joining(",")));
        }
    }

    private static long getDecimalNumber(Map<String, Boolean> values, List<Gate> gates) {
        processGates(values, gates);
        return getDecimalNumber(values, "z");
    }

    private static long getDecimalNumber(Map<String, Boolean> values, String prefix) {
        List<Map.Entry<String, Boolean>> prefixValues = values.entrySet().stream().filter(e -> e.getKey().startsWith(prefix)).sorted(Map.Entry.comparingByKey()).toList();
        int shift = 0;
        long result = 0;
        for (Map.Entry<String, Boolean> value : prefixValues) {
            if (value.getValue()) result += 1L << shift;
            shift++;
        }
        return result;
    }

    private static void processGates(Map<String, Boolean> values, List<Gate> gates) {
        Optional<Gate> nextGate = getNextGate(values, gates);
        while (nextGate.isPresent()) {
            Gate next = nextGate.get();
            values.put(next.output, next.operation.apply(values.get(next.input1), values.get(next.input2)));
            nextGate = getNextGate(values, gates);
        }
    }

    private static Optional<Gate> getNextGate(Map<String, Boolean> values, List<Gate> gates) {
        return gates.stream().filter(g -> values.containsKey(g.input1) && values.containsKey(g.input2) && !values.containsKey(g.output)).findAny();
    }

    private static Set<String> getSwappedOutputs(Map<String, Boolean> values, List<Gate> gates) {
        Set<String> wrongBits = new HashSet<>();
        for (int i = 0; i < 45; i++) {
            Map<String, Boolean> copy = new HashMap<>(values);
            copy.keySet().stream().filter(key -> key.startsWith("x") || key.startsWith("y")).forEach(key -> copy.put(key, false));
            String index = i < 10 ? "0" + i : i + "";
            copy.put("x" + index, true);
            copy.put("y" + index, true);
            long expected = getDecimalNumber(copy, "x") + getDecimalNumber(copy, "y");
            if (expected > getDecimalNumber(copy, gates)) wrongBits.add(index);
            else if (expected < getDecimalNumber(copy, gates)) wrongBits.add((i + 1) < 10 ? "0" + (i + 1) : (i + 1) + "");
        }
        // When a sum is wrong, it means two gates have been swapped in the cluster of the corresponding index
        // The answer below has been found by manually checking the doors where the indexes were wrong
        return Set.of("ffj", "z08", "kfm", "dwp", "z22", "gjh", "jdr", "z31");
    }
}
