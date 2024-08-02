package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode06 {

    private static class MemoryBanks {
        int[] banks;

        MemoryBanks(String line) {
            String[] split = line.split("\t");
            banks = new int[split.length];
            for (int i = 0; i < banks.length; i++) banks[i] = Integer.parseInt(split[i]);
        }

        MemoryBanks(MemoryBanks memoryBanks) {
            banks = new int[memoryBanks.banks.length];
            System.arraycopy(memoryBanks.banks, 0, banks, 0, banks.length);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof MemoryBanks other)) return false;
            return Arrays.equals(banks, other.banks);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(banks);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            MemoryBanks memoryBanks = new MemoryBanks("0\t2\t7\t0");
            while ((currentLine = reader.readLine()) != null) memoryBanks = new MemoryBanks(currentLine);
            Entry<Integer, Integer> result = getCycles(memoryBanks);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Entry<Integer, Integer> getCycles(MemoryBanks memoryBanks) {
        List<MemoryBanks> states = new ArrayList<>();
        MemoryBanks current = memoryBanks;
        int cycles = 0;
        while (!states.contains(current)) {
            cycles++;
            states.add(current);
            int index = 0;
            int valueToSpread = current.banks[index];
            for (int i = 1; i < memoryBanks.banks.length; i++) {
                if (current.banks[i] > valueToSpread) {
                    valueToSpread = current.banks[i];
                    index = i;
                }
            }
            MemoryBanks next = new MemoryBanks(current);
            next.banks[index] = 0;
            for (int i = 1; i <= valueToSpread; i++) {
                next.banks[(index + i) % memoryBanks.banks.length]++;
            }
            current = next;
        }
        return new SimpleEntry<>(cycles, cycles - states.indexOf(current));
    }
}
