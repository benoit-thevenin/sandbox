package fr.phoenyx.adventofcode.aoc2025;

import fr.phoenyx.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdventOfCode10 {

    private static class Machine {
        private final Map<String, Integer> CACHE = new HashMap<>();
        private final int[] BUTTON_INDEXES;

        int[] indicatorLight;
        List<int[]> buttons = new ArrayList<>();
        int[] joltageRequirements;

        Machine(String line) {
            String[] split = line.split(" ");
            indicatorLight = new int[split[0].length()];
            for (int i = 0; i < indicatorLight.length; i++) indicatorLight[i] = split[0].charAt(i) == '#' ? 1 : 0;
            for (int i = 1; i < split.length - 1; i++) buttons.add(Arrays.stream(split[i].split(",")).mapToInt(Integer::parseInt).toArray());
            joltageRequirements = Arrays.stream(split[split.length - 1].split(",")).mapToInt(Integer::parseInt).toArray();
            BUTTON_INDEXES = new int[buttons.size()];
            for (int i = 0; i < BUTTON_INDEXES.length; i++) BUTTON_INDEXES[i] = i;
        }

        int getFewerButtonPushesLights() {
            return getValidCombinationsFor(indicatorLight).stream().map(a -> a.length).min(Integer::compare).orElseThrow();
        }

        private List<int[]> getValidCombinationsFor(int[] goal) {
            List<int[]> validCombinations = new ArrayList<>();
            boolean[] lights = new boolean[goal.length];
            for (int i = 0; i < goal.length; i++) lights[i] = goal[i] % 2 == 1;
            for (int i = 0; i <= buttons.size(); i++) {
                List<int[]> combinations = MathUtils.getAllCombinations(BUTTON_INDEXES, i);
                for (int[] combination : combinations) {
                    boolean[] state = new boolean[goal.length];
                    for (int index : combination) applyPushLight(state, buttons.get(index));
                    if (Arrays.equals(state, lights)) validCombinations.add(combination);
                }
            }
            return validCombinations;
        }

        private void applyPushLight(boolean[] state, int[] button) {
            for (int index : button) state[index] = !state[index];
        }

        int getFewerButtonPushesJoltage() {
            CACHE.put(Arrays.toString(new int[joltageRequirements.length]), 0);
            return getFewerButtonPushesJoltage(joltageRequirements);
        }

        private int getFewerButtonPushesJoltage(int[] goal) {
            if (Arrays.stream(goal).anyMatch(i -> i < 0)) return Arrays.stream(joltageRequirements).reduce(0, Integer::sum);
            String key = Arrays.toString(goal);
            if (CACHE.containsKey(key)) return CACHE.get(key);
            int result = Arrays.stream(joltageRequirements).reduce(0, Integer::sum);
            List<int[]> combinations = getValidCombinationsFor(goal);
            for (int[] combination : combinations) {
                int[] newGoal = Arrays.copyOf(goal, goal.length);
                for (int buttonIndex : combination) for (int index : buttons.get(buttonIndex)) newGoal[index]--;
                for (int i = 0; i < newGoal.length; i++) newGoal[i] /= 2;
                result = Math.min(result, combination.length + 2 * getFewerButtonPushesJoltage(newGoal));
            }
            CACHE.put(key, result);
            return result;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Machine> machines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) machines.add(new Machine(currentLine.replaceAll("[(){}\\[\\]]", "")));
            LOGGER.info("PART 1: {}", machines.stream().map(Machine::getFewerButtonPushesLights).reduce(0, Integer::sum));
            long begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed: {}ms", machines.stream().map(Machine::getFewerButtonPushesJoltage).reduce(0, Integer::sum), (System.nanoTime() - begin) / 1000000);
        }
    }
}
