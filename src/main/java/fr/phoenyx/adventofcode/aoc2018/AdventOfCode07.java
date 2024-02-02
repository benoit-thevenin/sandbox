package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode07 {

    private static class Step {
        final char name;
        final Set<Character> prerequisites = new HashSet<>();

        Step(char name) {
            this.name = name;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);
    private static final Map<Character, Step> steps = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                char prerequisite = currentLine.charAt(5);
                char stepName = currentLine.charAt(36);
                steps.putIfAbsent(prerequisite, new Step(prerequisite));
                steps.putIfAbsent(stepName, new Step(stepName));
                steps.get(stepName).prerequisites.add(prerequisite);
            }
            LOGGER.info("PART 1: {}", getStepOrder());
            LOGGER.info("PART 2: {}", getTimeToCompleteSteps());
        }
    }

    private static String getStepOrder() {
        List<Character> stepOrder = new ArrayList<>();
        while (stepOrder.size() != steps.size()) {
            stepOrder.add(steps.values().stream()
                .filter(s -> !stepOrder.contains(s.name))
                .filter(s -> new HashSet<>(stepOrder).containsAll(s.prerequisites))
                .sorted(Comparator.comparingInt(s -> s.name))
                .map(s -> s.name).findFirst().orElseThrow());
        }
        return String.join("", stepOrder.stream().map(c -> Character.toString(c)).toList());
    }

    private static int getTimeToCompleteSteps() {
        int time = 0;
        Set<Character> completedSteps = new HashSet<>();
        Map<Character, Integer> stepsStarted = new HashMap<>();
        while (completedSteps.size() != steps.size()) {
            Set<Character> startedOrCompleted = new HashSet<>(completedSteps);
            startedOrCompleted.addAll(stepsStarted.keySet());
            if (stepsStarted.size() < 5) {
                steps.values().stream()
                    .filter(s -> !startedOrCompleted.contains(s.name))
                    .filter(s -> completedSteps.containsAll(s.prerequisites))
                    .sorted(Comparator.comparingInt(s -> s.name))
                    .limit(5L - stepsStarted.size())
                    .forEach(s -> stepsStarted.put(s.name, 60 + s.name - 'A' + 1));
            }
            int lowestTime = stepsStarted.values().stream().min(Integer::compare).orElseThrow();
            for (Entry<Character, Integer> entry : stepsStarted.entrySet()) {
                if (entry.getValue() == lowestTime) completedSteps.add(entry.getKey());
                else stepsStarted.put(entry.getKey(), entry.getValue() - lowestTime);
            }
            time += lowestTime;
            completedSteps.forEach(stepsStarted::remove);
        }
        return time;
    }
}
