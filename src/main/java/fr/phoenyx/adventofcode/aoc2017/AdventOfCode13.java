package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode13 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);
    private static final Map<Integer, Integer> firewall = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(": ");
                firewall.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }
            LOGGER.info("PART 1: {}", getSeverity(0) - 1);
            LOGGER.info("PART 2: {}", getDelayUncaught());
        }
    }

    private static int getSeverity(int delay) {
        int severity = 0;
        int lastStep = firewall.keySet().stream().max(Integer::compare).orElseThrow();
        for (int i = 0; i <= lastStep; i++) {
            if (isCaught(delay + i, i)) severity += i == 0 ? 1 : firewall.get(i) * i;
        }
        return severity;
    }

    private static boolean isCaught(int time, int layer) {
        if (!firewall.containsKey(layer)) return false;
        return time % (2 * (firewall.get(layer) - 1)) == 0;
    }

    private static int getDelayUncaught() {
        int delay = 0;
        while (true) {
            delay++;
            if (getSeverity(delay) == 0) return delay;
        }
    }
}
