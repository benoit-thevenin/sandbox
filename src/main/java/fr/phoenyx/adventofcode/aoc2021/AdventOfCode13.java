package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;

public class AdventOfCode13 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            boolean isDot = true;
            Set<Coord2> dots = new HashSet<>();
            Queue<Entry<Boolean, Integer>> foldInstructions = new LinkedList<>();
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) isDot = false;
                else if (isDot) {
                    String[] split = currentLine.split(",");
                    dots.add(new Coord2(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
                } else foldInstructions.add(new SimpleEntry<>(currentLine.contains("y"), Integer.parseInt(currentLine.split("=")[1])));
            }
            LOGGER.info("PART 1: {}", countDotsAfterFirstFold(dots, foldInstructions));
            String code = getCode(dots, foldInstructions);
            LOGGER.info("PART 2: {}", code);
        }
    }

    private static int countDotsAfterFirstFold(Set<Coord2> dots, Queue<Entry<Boolean, Integer>> foldInstructions) {
        fold(dots, foldInstructions.remove());
        return dots.size();
    }

    private static void fold(Set<Coord2> dots, Entry<Boolean, Integer> foldInstruction) {
        boolean isHorizontal = foldInstruction.getKey();
        Set<Coord2> foldedDots = new HashSet<>();
        if (isHorizontal) foldedDots.addAll(dots.stream().filter(d -> d.y > foldInstruction.getValue()).toList());
        else foldedDots.addAll(dots.stream().filter(d -> d.x > foldInstruction.getValue()).toList());
        for (Coord2 foldedDot : foldedDots) {
            dots.remove(foldedDot);
            if (isHorizontal) dots.add(new Coord2(foldedDot.x, 2 * foldInstruction.getValue() - foldedDot.y));
            else dots.add(new Coord2(2 * foldInstruction.getValue() - foldedDot.x, foldedDot.y));
        }
    }

    private static String getCode(Set<Coord2> dots, Queue<Entry<Boolean, Integer>> foldInstructions) {
        while (!foldInstructions.isEmpty()) fold(dots, foldInstructions.remove());
        int maxX = dots.stream().map(c -> c.x).max(Integer::compare).orElseThrow();
        int maxY = dots.stream().map(c -> c.y).max(Integer::compare).orElseThrow();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= maxY; i++) {
            sb.append('\n');
            for (int j = 0; j <= maxX; j++) sb.append(dots.contains(new Coord2(j, i)) ? '#' : '.');
        }
        return sb.toString();
    }
}
