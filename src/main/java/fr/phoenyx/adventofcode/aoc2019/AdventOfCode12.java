package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.MovingCoord3;
import fr.phoenyx.utils.MathUtils;

public class AdventOfCode12 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);
    private static final char[] XYZ = "xyz".toCharArray();
    private static final List<MovingCoord3> moons = new ArrayList<>();
    private static final Map<Character, Map<List<Integer>, Long>> seenStates = new HashMap<>();
    private static final Map<Character, Long> stepsToInitialState = new HashMap<>();
    private static long currentStep = 0;

    static {
        for (char c : XYZ) seenStates.put(c, new HashMap<>());
    }

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                List<Integer> coords = Arrays.stream(currentLine.replaceAll("[<>xyz= ]", "").split(","))
                    .map(Integer::parseInt).toList();
                moons.add(new MovingCoord3(coords.get(0), coords.get(1), coords.get(2), 0, 0, 0));
            }
            LOGGER.info("PART 1: {}", getTotalEnergy());
            LOGGER.info("PART 2: {}", getStepsToInitialState());
        }
    }

    private static int getTotalEnergy() {
        for (int i = 0; i < 1000; i++) simulate();
        return moons.stream().map(AdventOfCode12::getTotalEnergy).reduce(0, Integer::sum);
    }

    private static void simulate() {
        applyGravity();
        List<MovingCoord3> next = moons.stream().map(MovingCoord3::move).toList();
        moons.clear();
        moons.addAll(next);
        checkSteps();
    }

    private static void applyGravity() {
        List<MovingCoord3> next = new ArrayList<>();
        for (int i = 0; i < moons.size(); i++) {
            MovingCoord3 current = moons.get(i);
            int vx = current.vx;
            int vy = current.vy;
            int vz = current.vz;
            for (MovingCoord3 other : moons) {
                if (current.x < other.x) vx++;
                else if (current.x > other.x) vx--;
                if (current.y < other.y) vy++;
                else if (current.y > other.y) vy--;
                if (current.z < other.z) vz++;
                else if (current.z > other.z) vz--;
            }
            next.add(new MovingCoord3(current.x, current.y, current.z, vx, vy, vz));
        }
        moons.clear();
        moons.addAll(next);
    }

    private static void checkSteps() {
        Map<Character, List<Integer>> keys = new HashMap<>();
        for (char c : XYZ) keys.put(c, new ArrayList<>());
        for (MovingCoord3 moon : moons) {
            keys.get('x').add(moon.x);
            keys.get('x').add(moon.vx);
            keys.get('y').add(moon.y);
            keys.get('y').add(moon.vy);
            keys.get('z').add(moon.z);
            keys.get('z').add(moon.vz);
        }
        for (char c : XYZ) {
            if (seenStates.get(c).containsKey(keys.get(c))) stepsToInitialState.putIfAbsent(c, currentStep);
            seenStates.get(c).put(keys.get(c), currentStep);
        }
        currentStep++;
    }

    private static int getTotalEnergy(MovingCoord3 moon) {
        return getPotentialEnergy(moon) * getKineticEnergy(moon);
    }

    private static int getPotentialEnergy(MovingCoord3 moon) {
        return Math.abs(moon.x) + Math.abs(moon.y) + Math.abs(moon.z);
    }

    private static int getKineticEnergy(MovingCoord3 moon) {
        return Math.abs(moon.vx) + Math.abs(moon.vy) + Math.abs(moon.vz);
    }

    private static long getStepsToInitialState() {
        while (stepsToInitialState.size() != 3) simulate();
        return stepsToInitialState.values().stream().reduce(1L, MathUtils::leastCommonMultiple);
    }
}
