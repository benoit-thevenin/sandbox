package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode02 {

    private record Box(int l, int w, int h) {
        int getWrappingPaperNeeded() {
            return getArea() + getSmallestSideArea();
        }

        int getRibbonLengthNeeded() {
            return getSmallestPerimeter() + getVolume();
        }

        private int getArea() {
            return 2 * l * w + 2 * w * h + 2 * h * l;
        }

        private int getSmallestSideArea() {
            return Math.min(Math.min(l * w, w * h), h * l);
        }

        private int getVolume() {
            return l * w * h;
        }

        private int getSmallestPerimeter() {
            int max = Math.max(Math.max(l, w), h);
            if (max == h) return 2 * l + 2 * w;
            return max == w ? 2 * l + 2 * h : 2 * w + 2 * h;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Box> boxes = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split("x");
                boxes.add(new Box(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
            }
            LOGGER.info("PART 1: {}", boxes.stream().map(Box::getWrappingPaperNeeded).reduce(0, Integer::sum));
            LOGGER.info("PART 2: {}", boxes.stream().map(Box::getRibbonLengthNeeded).reduce(0, Integer::sum));
        }
    }
}
