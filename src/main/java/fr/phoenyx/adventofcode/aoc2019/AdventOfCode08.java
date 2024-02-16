package fr.phoenyx.adventofcode.aoc2019;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2019/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<List<Integer>> layers = new ArrayList<>();
            List<Integer> currentLayer = new ArrayList<>();
            layers.add(currentLayer);
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                for (int i = 0; i < currentLine.length(); i++) {
                    if (currentLayer.size() == 25 * 6) {
                        currentLayer = new ArrayList<>();
                        layers.add(currentLayer);
                    }
                    currentLayer.add(currentLine.charAt(i) - '0');
                }
            }
            LOGGER.info("PART 1: {}", getLowestZeroCountLayerValue(layers));
            LOGGER.info("PART 2: {}", printLayers(layers));
        }
    }

    private static long getLowestZeroCountLayerValue(List<List<Integer>> layers) {
        List<Integer> lowestZeroCountLayer = layers.stream()
            .min((l1, l2) -> Long.compare(l1.stream().filter(p -> p == 0).count(), l2.stream().filter(p -> p == 0).count())).orElseThrow();
        return lowestZeroCountLayer.stream().filter(p -> p == 1).count() * lowestZeroCountLayer.stream().filter(p -> p == 2).count();
    }

    private static String printLayers(List<List<Integer>> layers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append('\n');
            for (int j = 0; j < 25; j++) {
                int layerIndex = 0;
                while (layers.get(layerIndex).get(j + 25 * i) == 2) layerIndex++;
                if (layers.get(layerIndex).get(j + 25 * i) == 0) sb.append('.');
                else sb.append('#');
            }
        }
        return sb.toString();
    }
}
