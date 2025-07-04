package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode19 {

    private static class Workflow {
        String name;
        List<Function<Part, Optional<String>>> rules = new ArrayList<>();
        List<Function<RangePart, RangePartRuleResult>> rangeRules = new ArrayList<>();

        Workflow(String line) {
            String[] split = line.split("\\{");
            name = split[0];
            String[] rulesString = split[1].split(",");
            for (int i = 0; i < rulesString.length - 1; i++) {
                String[] ruleSplit = rulesString[i].split(":");
                int value = Integer.parseInt(ruleSplit[0].substring(2));
                String target = ruleSplit[1];
                if (ruleSplit[0].charAt(1) == '>') rules.add(part -> part.get(ruleSplit[0].charAt(0)) > value ? Optional.of(target) : Optional.empty());
                else rules.add(part -> part.get(ruleSplit[0].charAt(0)) < value ? Optional.of(target) : Optional.empty());
                rangeRules.add(rangePart -> {
                    RangePart rangePartContinuingWorkflow = rangePart.getRangePartContinuingWorkflow(ruleSplit[0].charAt(0), ruleSplit[0].charAt(1), value).orElse(null);
                    RangePart rangePartChangingWorkflow = rangePart.getRangePartChangingWorkflow(ruleSplit[0].charAt(0), ruleSplit[0].charAt(1), value).orElse(null);
                    return new RangePartRuleResult(rangePartContinuingWorkflow, rangePartChangingWorkflow, target);
                });
            }
            rules.add(part -> Optional.of(rulesString[rulesString.length - 1].replace("}", "")));
            rangeRules.add(rangePart -> new RangePartRuleResult(null, rangePart, rulesString[rulesString.length - 1].replace("}", "")));
        }
    }

    private record Part(int x, int m, int a, int s) {
        int get(char c) {
            if (c == 'x') return x;
            if (c == 'm') return m;
            return c == 'a' ? a : s;
        }

        int getValue() {
            return x + m + a + s;
        }
    }

    private static class RangePart {
        long minX = 1;
        long maxX = 4000;
        long minM = 1;
        long maxM = 4000;
        long minA = 1;
        long maxA = 4000;
        long minS = 1;
        long maxS = 4000;

        RangePart() {
            super();
        }

        RangePart(RangePart source) {
            for (char c : "xmas".toCharArray()) {
                setMin(c, source.getMin(c));
                setMax(c, source.getMax(c));
            }
        }

        Optional<RangePart> getRangePartContinuingWorkflow(char c, char comparator, int value) {
            if (comparator == '>') return getMin(c) > value ? Optional.empty() : Optional.of(new RangePart(this).setMax(c, Math.min(value, getMax(c))));
            return getMax(c) < value ? Optional.empty() : Optional.of(new RangePart(this).setMin(c, Math.max(value, getMin(c))));
        }

        Optional<RangePart> getRangePartChangingWorkflow(char c, char comparator, int value) {
            if (comparator == '>') return getMax(c) <= value ? Optional.empty() : Optional.of(new RangePart(this).setMin(c, Math.max(value + 1L, getMin(c))));
            return getMin(c) >= value ? Optional.empty() : Optional.of(new RangePart(this).setMax(c, Math.min(value - 1L, getMax(c))));
        }

        long getCombinations() {
            long result = 1;
            for (char c : "xmas".toCharArray()) result *= getRangeLength(c);
            return result;
        }

        long getMin(char c) {
            if (c == 'x') return minX;
            if (c == 'm') return minM;
            return c == 'a' ? minA : minS;
        }

        RangePart setMin(char c, long value) {
            if (c == 'x') minX = value;
            else if (c == 'm') minM = value;
            else if (c == 'a') minA = value;
            else minS = value;
            return this;
        }

        long getMax(char c) {
            if (c == 'x') return maxX;
            if (c == 'm') return maxM;
            return c == 'a' ? maxA : maxS;
        }

        RangePart setMax(char c, long value) {
            if (c == 'x') maxX = value;
            else if (c == 'm') maxM = value;
            else if (c == 'a') maxA = value;
            else maxS = value;
            return this;
        }

        private long getRangeLength(char c) {
            if (c == 'x') return maxX - minX + 1;
            if (c == 'm') return maxM - minM + 1;
            return c == 'a' ? maxA - minA + 1 : maxS - minS + 1;
        }
    }

    private record RangePartRuleResult(RangePart rangePartContinuingWorkflow, RangePart rangePartChangingWorkflow, String target) {
        Optional<RangePart> getRangePartContinuingWorkflow() {
            return Optional.ofNullable(rangePartContinuingWorkflow);
        }

        Optional<RangePart> getRangePartChangingWorkflow() {
            return Optional.ofNullable(rangePartChangingWorkflow);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode19.class);
    private static final Map<String, Workflow> workflows = new HashMap<>();
    private static final List<Part> parts = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode19.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            boolean isWorkflow = true;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) isWorkflow = false;
                else if (isWorkflow) {
                    Workflow workflow = new Workflow(currentLine);
                    workflows.put(workflow.name, workflow);
                } else {
                    String[] split = currentLine.split(",");
                    int x = Integer.parseInt(split[0].split("=")[1]);
                    int m = Integer.parseInt(split[1].split("=")[1]);
                    int a = Integer.parseInt(split[2].split("=")[1]);
                    int s = Integer.parseInt(split[3].replace("}", "").split("=")[1]);
                    parts.add(new Part(x, m, a, s));
                }
            }
            LOGGER.info("PART 1: {}", parts.stream().map(p -> getValue(p, workflows.get("in"))).reduce(0, Integer::sum));
            LOGGER.info("PART 2: {}", getValidRangeParts(new RangePart(), workflows.get("in")).stream().map(RangePart::getCombinations).reduce(0L, Long::sum));
        }
    }

    private static int getValue(Part part, Workflow workflow) {
        for (Function<Part, Optional<String>> rule : workflow.rules) {
            Optional<String> target = rule.apply(part);
            if (target.isPresent()) {
                if (target.get().equals("A")) return part.getValue();
                return target.get().equals("R") ? 0 : getValue(part, workflows.get(target.get()));
            }
        }
        throw new IllegalArgumentException("Last rule should be terminal");
    }

    private static List<RangePart> getValidRangeParts(RangePart rangePart, Workflow workflow) {
        List<RangePart> validRangeParts = new ArrayList<>();
        RangePart current = rangePart;
        for (Function<RangePart, RangePartRuleResult> rangeRule : workflow.rangeRules) {
            RangePartRuleResult result = rangeRule.apply(current);
            Optional<RangePart> rangePartChangingWorkflow = result.getRangePartChangingWorkflow();
            Optional<RangePart> rangePartContinuingWorkflow = result.getRangePartContinuingWorkflow();
            if (rangePartChangingWorkflow.isPresent()) {
                if (result.target.equals("A")) validRangeParts.add(rangePartChangingWorkflow.get());
                else if (!result.target.equals("R")) validRangeParts.addAll(getValidRangeParts(rangePartChangingWorkflow.get(), workflows.get(result.target)));
            }
            if (rangePartContinuingWorkflow.isPresent()) current = rangePartContinuingWorkflow.get();
            else break;
        }
        return validRangeParts;
    }
}
