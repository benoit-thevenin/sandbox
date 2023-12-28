package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);
    private static int width;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            width = lines.iterator().next().length();
            LOGGER.info("PART 1: {}", getEnergyConsumption(lines));
            LOGGER.info("PART 2: {}", getOxygenGeneratorRating(lines) * getCO2ScrubberRating(lines));
        }
    }

    private static int getEnergyConsumption(List<String> lines) {
        StringBuilder gamma = new StringBuilder();
        StringBuilder epsilon = new StringBuilder();
        for (int i = 0; i < width; i++) {
            int index = i;
            long count0 = lines.stream().filter(l -> l.charAt(index) == '0').count();
            long count1 = lines.size() - count0;
            if (count1 > count0) {
                gamma.append('1');
                epsilon.append('0');
            } else {
                gamma.append('0');
                epsilon.append('1');
            }
        }
        return Integer.parseInt(gamma.toString(), 2) * Integer.parseInt(epsilon.toString(), 2);
    }

    private static int getOxygenGeneratorRating(List<String> lines) {
        List<String> candidates = new ArrayList<>(lines);
        for (int i = 0; i < width; i++) {
            int index = i;
            long count0 = candidates.stream().filter(l -> l.charAt(index) == '0').count();
            long count1 = candidates.size() - count0;
            if (count1 >= count0) candidates = candidates.stream().filter(l -> l.charAt(index) == '1').toList();
            else candidates = candidates.stream().filter(l -> l.charAt(index) == '0').toList();
            if (candidates.size() == 1) return Integer.parseInt(candidates.get(0), 2);
        }
        throw new IllegalArgumentException("Wrong data set");
    }

    private static int getCO2ScrubberRating(List<String> lines) {
        List<String> candidates = new ArrayList<>(lines);
        for (int i = 0; i < width; i++) {
            int index = i;
            long count0 = candidates.stream().filter(l -> l.charAt(index) == '0').count();
            long count1 = candidates.size() - count0;
            if (count0 <= count1) candidates = candidates.stream().filter(l -> l.charAt(index) == '0').toList();
            else candidates = candidates.stream().filter(l -> l.charAt(index) == '1').toList();
            if (candidates.size() == 1) return Integer.parseInt(candidates.get(0), 2);
        }
        throw new IllegalArgumentException("Wrong data set");
    }
}
