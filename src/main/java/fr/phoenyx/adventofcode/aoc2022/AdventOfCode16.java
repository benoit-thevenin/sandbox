package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode16 {

    private static class Valve {
        static final String STARTING_VALVE_LABEL = "AA";

        String label;
        int flowRate;
        Map<Valve, Integer> neighboursByDistance = new HashMap<>();

        Valve(String label, int flowRate) {
            this.label = label;
            this.flowRate = flowRate;
        }

        boolean isUseful() {
            return flowRate > 0 || STARTING_VALVE_LABEL.equals(label);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Map<String, Valve> valves = new HashMap<>();
            Map<Valve, Set<String>> neighboursByValve = new HashMap<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String label = currentLine.split(" ")[1];
                int flowRate = Integer.parseInt(currentLine.split("=")[1].split(";")[0]);
                Valve valve = new Valve(label, flowRate);
                String[] neighbours = currentLine.split("to valves? ")[1].split(", ");
                neighboursByValve.put(valve, new HashSet<>(Arrays.asList(neighbours)));
                valves.put(valve.label, valve);
            }
            optimizeGraph(valves, neighboursByValve);
            long begin = System.nanoTime();
            Valve start = valves.get(Valve.STARTING_VALVE_LABEL);
            LOGGER.info("PART 1: {}, time elapsed {}ms", getMaxPressureReleased(start, 30, 0), (System.nanoTime() - begin) / 1000000);
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed {}ms", getMaxPressureReleased(start, start, 0, 0, 26, 0), (System.nanoTime() - begin) / 1000000);
        }
    }

    private static void optimizeGraph(Map<String, Valve> valves, Map<Valve, Set<String>> neighboursByValve) {
        valves.values().stream().filter(Valve::isUseful).forEach(v -> computeNeighbours(v, valves, neighboursByValve));
        Set<Valve> uselessValves = valves.values().stream().filter(v -> !v.isUseful()).collect(Collectors.toSet());
        uselessValves.stream().map(v -> v.label).forEach(valves::remove);
        valves.values().forEach(valve -> uselessValves.forEach(v -> valve.neighboursByDistance.remove(v)));
    }

    private static void computeNeighbours(Valve valve, Map<String, Valve> valves, Map<Valve, Set<String>> neighboursByValve) {
        Queue<Valve> toVisit = new LinkedList<>();
        toVisit.add(valve);
        Set<Valve> visited = new HashSet<>(toVisit);
        while (!toVisit.isEmpty()) {
            Valve current = toVisit.remove();
            for (String neighbourLabel : neighboursByValve.get(current)) {
                Valve neighbour = valves.get(neighbourLabel);
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    toVisit.add(neighbour);
                    current.neighboursByDistance.put(neighbour, 1);
                    neighbour.neighboursByDistance.put(current, 1);
                    if (!valve.neighboursByDistance.containsKey(neighbour)) {
                        valve.neighboursByDistance.put(neighbour, valve.neighboursByDistance.get(current) + 1);
                        neighbour.neighboursByDistance.put(valve, valve.neighboursByDistance.get(neighbour));
                    }
                }
            }
        }
    }

    private static int getMaxPressureReleased(Valve valve, int timeLeft, int pressureReleased) {
        if (timeLeft == 0) return pressureReleased;
        Set<Valve> toVisit = valve.neighboursByDistance.entrySet().stream()
            .filter(e -> e.getKey().flowRate > 0 && e.getValue() < timeLeft)
            .map(Map.Entry::getKey).collect(Collectors.toSet());
        int max = pressureReleased;
        for (Valve neighbour : toVisit) {
            int timeConsumed = valve.neighboursByDistance.get(neighbour) + 1;
            int flowRate = neighbour.flowRate;
            neighbour.flowRate = 0;
            int nextTimeLeft = timeLeft - timeConsumed;
            max = Math.max(max, getMaxPressureReleased(neighbour, nextTimeLeft, pressureReleased + nextTimeLeft * flowRate));
            neighbour.flowRate = flowRate;
        }
        return max;
    }

    private static int getMaxPressureReleased(Valve me, Valve elephant, int meDelta, int elephantDelta, int timeLeft, int pressureReleased) {
        if (timeLeft == 0 && meDelta == 0 && elephantDelta == 0) return pressureReleased;
        int max = pressureReleased;
        Valve start = elephantDelta == 0 ? me : elephant;
        int delta = start == me ? meDelta : elephantDelta;
        int otherDelta = start == me ? elephantDelta : meDelta;
        int timeConstraint = timeLeft + delta;
        Set<Valve> toVisit = start.neighboursByDistance.entrySet().stream()
            .filter(e -> e.getKey().flowRate > 0 && e.getValue() < timeConstraint)
            .map(Map.Entry::getKey).collect(Collectors.toSet());
        for (Valve neighbour : toVisit) {
            int timeConsumed = start.neighboursByDistance.get(neighbour) - delta + 1;
            int flowRate = neighbour.flowRate;
            neighbour.flowRate = 0;
            int nextTimeLeft = timeConsumed > 0 ? timeLeft - timeConsumed : timeLeft;
            int nextDelta = timeConsumed > 0 ? 0 : -timeConsumed;
            int nextOtherDelta = timeConsumed > 0 ? otherDelta + timeConsumed : otherDelta;
            int nextPressureReleased = pressureReleased + (timeLeft - timeConsumed) * flowRate;
            if (start == me) max = Math.max(max, getMaxPressureReleased(neighbour, elephant, nextDelta, nextOtherDelta, nextTimeLeft, nextPressureReleased));
            else max = Math.max(max, getMaxPressureReleased(me, neighbour, nextOtherDelta, nextDelta, nextTimeLeft, nextPressureReleased));
            neighbour.flowRate = flowRate;
        }
        return max;
    }
}
