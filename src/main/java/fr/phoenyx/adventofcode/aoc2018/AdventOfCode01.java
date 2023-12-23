package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode01 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode01.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode01.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int result = 0;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.charAt(0) == '+') result += Integer.parseInt(currentLine.substring(1));
                else result -= Integer.parseInt(currentLine.substring(1));
                lines.add(currentLine);
            }
            LOGGER.info("PART 1: {}", result);
            LOGGER.info("PART 2: {}", getRightFrequency(lines));
        }
    }

    private static int getRightFrequency(List<String> lines) {
        Set<Integer> seenFrequency = new HashSet<>();
        int currentFrequency = 0;
        seenFrequency.add(0);
        int index = 0;
        while (true) {
            String line = lines.get(index % lines.size());
            if (line.charAt(0) == '+') currentFrequency += Integer.parseInt(line.substring(1));
            else currentFrequency -= Integer.parseInt(line.substring(1));
            if (seenFrequency.contains(currentFrequency)) return currentFrequency;
            seenFrequency.add(currentFrequency);
            index++;
        }
    }
}
