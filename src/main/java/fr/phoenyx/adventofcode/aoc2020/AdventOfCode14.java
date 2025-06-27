package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode14 {

    private static class Program {
        String mask = "X".repeat(36);
        Map<Long, Long> memory1 = new HashMap<>();
        Map<Long, Long> memory2 = new HashMap<>();

        void apply(long address, long value) {
            apply1(address, value);
            apply2(address, value);
        }

        private void apply1(long address, long value) {
            long maskedValue = 0;
            for (int i = 0; i < mask.length(); i++) {
                char c = mask.charAt(mask.length() - 1 - i);
                if (c == '1') maskedValue += 1L << i;
                else if (c == 'X') maskedValue += value & (1L << i);
            }
            memory1.put(address, maskedValue);
        }

        private void apply2(long address, long value) {
            int addressesCount = 1 << Utils.getLetterCount(mask).get('X');
            for (int xMask = 0; xMask < addressesCount; xMask++) {
                long maskedAddress = 0;
                int xSet = 0;
                for (int i = 0; i < mask.length(); i++) {
                    char c = mask.charAt(mask.length() - 1 - i);
                    if (c == '0') maskedAddress += address & (1L << i);
                    else if (c == '1') maskedAddress += 1L << i;
                    else {
                        if ((xMask & (1L << xSet)) > 0) maskedAddress += 1L << i;
                        xSet++;
                    }
                }
                memory2.put(maskedAddress, value);
            }
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Program program = new Program();
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("mask")) program.mask = currentLine.split(" = ")[1];
                else {
                    String[] split = currentLine.split("] = ");
                    program.apply(Long.parseLong(split[0].split("mem\\[")[1]), Long.parseLong(split[1]));
                }
            }
            LOGGER.info("PART 1: {}", program.memory1.values().stream().reduce(0L, Long::sum));
            LOGGER.info("PART 2: {}", program.memory2.values().stream().reduce(0L, Long::sum));
        }
    }
}
