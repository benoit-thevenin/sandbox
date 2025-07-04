package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode15 {

    private static class Box {
        List<Lens> lenses = new ArrayList<>();

        void add(Lens lens) {
            Optional<Lens> existingLens = lenses.stream().filter(l -> l.equals(lens)).findAny();
            if (existingLens.isPresent()) existingLens.get().focalLength = lens.focalLength;
            else lenses.add(lens);
        }

        void remove(Lens lens) {
            lenses.remove(lens);
        }
    }

    private static class Lens {
        String label;
        int focalLength;

        Lens(String label) {
            this.label = label;
        }

        Lens(String label, int focalLength) {
            this(label);
            this.focalLength = focalLength;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Lens other = (Lens) o;
            return label.equals(other.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);
    private static List<String> instructions = new ArrayList<>();
    private static Box[] boxes;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) instructions = Arrays.asList(currentLine.split(","));
            LOGGER.info("PART 1: {}", instructions.stream().map(AdventOfCode15::hash).reduce(0, Integer::sum));
            processInstructions();
            LOGGER.info("PART 2: {}", computeFocusingPower());
        }
    }

    private static int hash(String string) {
        int result = 0;
        for (char c : string.toCharArray()) result = ((result + c) * 17) % 256;
        return result;
    }

    private static void processInstructions() {
        boxes = new Box[256];
        for (int i = 0; i < boxes.length; i++) boxes[i] = new Box();
        for (String instruction : instructions) {
            if (instruction.charAt(instruction.length() - 1) == '-') {
                String label = instruction.substring(0, instruction.length() - 1);
                int index = hash(label);
                boxes[index].remove(new Lens(label));
            } else {
                String label = instruction.substring(0, instruction.length() - 2);
                int focalLength = instruction.charAt(instruction.length() - 1) - '0';
                int index = hash(label);
                boxes[index].add(new Lens(label, focalLength));
            }
        }
    }

    private static int computeFocusingPower() {
        int result = 0;
        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[i].lenses.size(); j++) result += (i + 1) * (j + 1) * boxes[i].lenses.get(j).focalLength;
        }
        return result;
    }
}
