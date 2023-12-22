package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.Dir;

public class AdventOfCode18 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode18.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode18.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            LOGGER.info("PART 1: {}", getLagoonArea(lines, true));
            LOGGER.info("PART 2: {}", getLagoonArea(lines, false));
        }
    }

    private static long getLagoonArea(List<String> lines, boolean part1) {
        char[] dirs = "RDLU".toCharArray();
        long currentX = 0;
        long currentY = 0;
        long perimeter = 0;
        long area = 0;
        for (String line : lines) {
            String[] split = line.split(" ");
            String hexInstruction = split[2].replaceAll("[(#)]", "");
            long steps = part1 ? Long.parseLong(split[1]) : Long.parseLong(hexInstruction.substring(0, 5), 16);
            Dir dir = part1 ? Dir.fromChar(split[0].charAt(0)) : Dir.fromChar(dirs[Integer.parseInt(hexInstruction.substring(5, 6))]);
            long nextX = currentX + steps * dir.dx;
            long nextY = currentY + steps * dir.dy;
            perimeter += steps;
            area += (currentY + nextY) * (currentX - nextX);
            currentX = nextX;
            currentY = nextY;
        }
        return Math.abs(area / 2) + perimeter / 2 + 1;
    }
}
