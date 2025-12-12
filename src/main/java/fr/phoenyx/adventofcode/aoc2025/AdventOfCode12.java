package fr.phoenyx.adventofcode.aoc2025;

import fr.phoenyx.models.CharGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdventOfCode12 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);
    private static final List<CharGrid> PRESENT_SHAPES = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> presentShapeLines = new ArrayList<>();
            int count = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (PRESENT_SHAPES.size() < 6) {
                    if (currentLine.isEmpty()) {
                        PRESENT_SHAPES.add(new CharGrid(presentShapeLines));
                        presentShapeLines.clear();
                    } else if (!Character.isDigit(currentLine.charAt(0))) presentShapeLines.add(currentLine);
                } else {
                    String[] split =  currentLine.split(": ");
                    String[] dimensions = split[0].split("x");
                    int width = Integer.parseInt(dimensions[0]);
                    int height = Integer.parseInt(dimensions[1]);
                    List<Integer> presents = Arrays.stream(split[1].split(" ")).map(Integer::parseInt).toList();
                    if (canFit(presents, width, height)) count++;
                }
            }
            LOGGER.info("SOLUTION: {}", count);
        }
    }

    private static boolean canFit(List<Integer> presents, int width, int height) {
        int area = width * height;
        int areaPresents = 0;
        for (int i = 0; i < presents.size(); i++) areaPresents += presents.get(i) * PRESENT_SHAPES.get(i).getCoordinatesMatching(c -> c == '#').size();
        return area >= areaPresents;
    }
}
