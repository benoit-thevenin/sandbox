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
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class AdventOfCode24 {

    private record Gate(String input1, String input2, BinaryOperator<Boolean> operation, String output) {
        boolean hasInputMatching(String input) {
            return input1.equals(input) || input2.equals(input);
        }

        boolean hasInputsMatching(String i1, String i2) {
            return hasInputMatching(i1) && hasInputMatching(i2);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode24.class);
    private static final Map<String, BinaryOperator<Boolean>> OPERATIONS = new HashMap<>();

    static {
        OPERATIONS.put("AND", (a, b) -> a && b);
        OPERATIONS.put("OR", (a, b) -> a || b);
        OPERATIONS.put("XOR", (a, b) -> a ^ b);
    }

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
                    BinaryOperator<Boolean> operation = OPERATIONS.get(input[1]);
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
        Set<Integer> wrongBits = new HashSet<>();
        for (int i = 0; i < 45; i++) {
            Map<String, Boolean> copy = new HashMap<>(values);
            copy.keySet().stream().filter(key -> key.startsWith("x") || key.startsWith("y")).forEach(key -> copy.put(key, false));
            String index = getIndex(i);
            copy.put("x" + index, true);
            copy.put("y" + index, true);
            long expected = getDecimalNumber(copy, "x") + getDecimalNumber(copy, "y");
            if (expected > getDecimalNumber(copy, gates)) wrongBits.add(i);
            else if (expected < getDecimalNumber(copy, gates)) wrongBits.add(i + 1);
        }
        Set<String> swappedOutputs = new HashSet<>();
        wrongBits.forEach(i -> swappedOutputs.addAll(getSwappedOutputsAtIndex(i, gates)));
        return swappedOutputs;
    }

    private static Set<String> getSwappedOutputsAtIndex(int i, List<Gate> gates) {
        String index = getIndex(i);
        String previousIndex = getIndex(i - 1);
        String zi = "z" + index;
        Gate additionGate = gates.stream().filter(g -> g.hasInputsMatching("x" + index, "y" + index) && g.operation.equals(OPERATIONS.get("XOR"))).findAny().orElseThrow();
        Optional<Gate> outputGate = gates.stream().filter(g -> g.output.equals(zi) && g.operation.equals(OPERATIONS.get("XOR"))).findAny();
        if (outputGate.isEmpty()) {
            Gate previousCarryGate = gates.stream().filter(g -> g.hasInputsMatching("x" + previousIndex, "y" + previousIndex) && g.operation.equals(OPERATIONS.get("AND"))).findAny().orElseThrow();
            Gate carryGate = gates.stream().filter(g -> g.hasInputMatching(previousCarryGate.output) && g.operation.equals(OPERATIONS.get("OR"))).findAny().orElseThrow();
            Gate swapped = gates.stream().filter(g -> g.hasInputsMatching(additionGate.output, carryGate.output) && g.operation.equals(OPERATIONS.get("XOR"))).findAny().orElseThrow();
            return Set.of(zi, swapped.output);
        }
        Gate input1 = gates.stream().filter(g -> g.output.equals(outputGate.get().input1)).findAny().orElseThrow();
        Gate input2 = gates.stream().filter(g -> g.output.equals(outputGate.get().input2)).findAny().orElseThrow();
        if (input1.operation.equals(OPERATIONS.get("AND"))) return Set.of(input1.output, additionGate.output);
        else if (input2.operation.equals(OPERATIONS.get("AND"))) return Set.of(input2.output, additionGate.output);
        else throw new IllegalArgumentException("No swapped gates found at index " + i);
    }

    private static String getIndex(int i) {
        return i < 10 ? "0" + i : i + "";
    }
}
