package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode06 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);
    private static final List<Integer> times = new ArrayList<>();
    private static final List<Integer> distances = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                boolean isTimes = currentLine.startsWith("Time");
                for (String s : currentLine.split(" ")) {
                    try {
                        int value = Integer.parseInt(s);
                        if (isTimes) times.add(value);
                        else distances.add(value);
                    } catch (NumberFormatException e) {
                        // skipping non numbers
                    }
                }
            }
            LOGGER.info("PART 1: {}", computePart1());
            LOGGER.info("PART 2: {}", computePart2());
        }
    }

    private static int computePart1() {
        int result = 1;
        for (int i = 0; i < times.size(); i++) {
            int waysToWin = 0;
            int timeAllowed = times.get(i);
            int distanceToBeat = distances.get(i);
            for (int j = 1; j < timeAllowed; j++) {
                int distance = j * (timeAllowed - j);
                if (distance > distanceToBeat) waysToWin++;
            }
            result *= waysToWin;
        }
        return result;
    }

    private static int computePart2() {
        int timeAllowed = Integer.parseInt(times.stream().map(Object::toString).reduce(String::concat).orElseThrow());
        long distanceToBeat = Long.parseLong(distances.stream().map(Object::toString).reduce(String::concat).orElseThrow());
        int result = 0;
        for (long i = 1; i < timeAllowed; i++) {
            long distance = i * (timeAllowed - i);
            if (distance > distanceToBeat) result++;
            else if (result > 0) break;
        }
        return result;
    }
}
