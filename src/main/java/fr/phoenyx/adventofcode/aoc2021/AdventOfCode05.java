package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.Coord;

public class AdventOfCode05 {

    private static class Vent {
        int x1;
        int y1;
        int x2;
        int y2;

        Vent(String line) {
            String[] split = line.split(" -> ");
            String[] start = split[0].split(",");
            String[] end = split[1].split(",");
            x1 = Integer.parseInt(start[0]);
            y1 = Integer.parseInt(start[1]);
            x2 = Integer.parseInt(end[0]);
            y2 = Integer.parseInt(end[1]);
        }

        boolean isHorizontal() {
            return y1 == y2;
        }

        boolean isVertical() {
            return x1 == x2;
        }

        List<Coord> getCoords() {
            List<Coord> coords = new ArrayList<>();
            int length = isHorizontal() ? Math.abs(x1 - x2) : Math.abs(y1 - y2);
            int offsetX = x1 < x2 ? 1 : -1;
            int offsetY = y1 < y2 ? 1 : -1;
            if (isHorizontal()) offsetY = 0;
            else if (isVertical()) offsetX = 0;
            for (int i = 0; i <= length; i++) coords.add(new Coord(x1 + i * offsetX, y1 + i * offsetY));
            return coords;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode05.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode05.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Vent> vents = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) vents.add(new Vent(currentLine));
            LOGGER.info("PART 1: {}", getOverlapCount(vents.stream().filter(v -> v.isVertical() || v.isHorizontal()).toList()));
            LOGGER.info("PART 2: {}", getOverlapCount(vents));
        }
    }

    private static long getOverlapCount(List<Vent> vents) {
        Map<Coord, Integer> coordsCount = new HashMap<>();
        for (Vent vent : vents) {
            for (Coord coord : vent.getCoords()) {
                if (coordsCount.containsKey(coord)) coordsCount.put(coord, coordsCount.get(coord) + 1);
                else coordsCount.put(coord, 1);
            }
        }
        return coordsCount.values().stream().filter(v -> v > 1).count();
    }
}
