package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode15 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Queue<Long> generators = new LinkedList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" ");
                generators.add(Long.parseLong(split[split.length - 1]));
            }
            LOGGER.info("PART 1: {}", getMatchingCount(new LinkedList<>(generators), true));
            LOGGER.info("PART 2: {}", getMatchingCount(new LinkedList<>(generators), false));
        }
    }

    private static int getMatchingCount(Queue<Long> generators, boolean isPart1) {
        long modulo = 2147483647L;
        long mask = 0xffff;
        long aFactor = 16807;
        long bFactor = 48271;
        long aCriteria = isPart1 ? 1 : 4;
        long bCriteria = isPart1 ? 1 : 8;
        int steps = isPart1 ? 40000000 : 5000000;
        int count = 0;
        for (int i = 0; i < steps; i++) {
            long a = (generators.remove() * aFactor) % modulo;
            while (a % aCriteria != 0) a = (a * aFactor) % modulo;
            long b = (generators.remove() * bFactor) % modulo;
            while (b % bCriteria != 0) b = (b * bFactor) % modulo;
            if ((a & mask) == (b & mask)) count++;
            generators.add(a);
            generators.add(b);
        }
        return count;
    }
}
