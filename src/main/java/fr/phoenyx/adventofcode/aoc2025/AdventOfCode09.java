package fr.phoenyx.adventofcode.aoc2025;

import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class AdventOfCode09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Coord2> redTiles = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split =  currentLine.split(",");
                redTiles.add(new Coord2(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            }
            Entry<Long, Long> result = getLargestArea(redTiles);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Entry<Long, Long> getLargestArea(List<Coord2> redTiles) {
        long largestArea1 = 0;
        long largestArea2 = 0;
        for (int i = 0; i < redTiles.size() - 1; i++) {
            Coord2 a = redTiles.get(i);
            for (int j = i + 1; j < redTiles.size(); j++) {
                Coord2 b = redTiles.get(j);
                long area = (long) (Math.abs(a.x - b.x) + 1) * (Math.abs(a.y - b.y) + 1);
                if (area > largestArea1) largestArea1 = area;
                if (area > largestArea2 && isValidRectangle(redTiles, a, b)) largestArea2 = area;
            }
        }
        return new SimpleEntry<>(largestArea1, largestArea2);
    }

    private static boolean isValidRectangle(List<Coord2> redTiles, Coord2 a, Coord2 b) {
        int minX = Math.min(a.x, b.x);
        int maxX = Math.max(a.x, b.x);
        int minY = Math.min(a.y, b.y);
        int maxY = Math.max(a.y, b.y);
        for (int i = 0; i < redTiles.size(); i++) {
            Coord2 c = redTiles.get(i);
            Coord2 d = redTiles.get((i + 1) % redTiles.size());
            boolean isVertical = c.x == d.x;
            if (isVertical && (c.x <= minX || c.x >= maxX) || !isVertical && (c.y <= minY || c.y >= maxY)) continue;
            int min = isVertical ? Math.min(c.y, d.y) : Math.min(c.x, d.x);
            int max = isVertical ? Math.max(c.y, d.y) : Math.max(c.x, d.x);
            int minOverlap = isVertical ? Math.max(minY, min) : Math.max(minX, min);
            int maxOverlap = isVertical ? Math.min(maxY, max) : Math.min(maxX, max);
            if (minOverlap < maxOverlap) return false;
        }
        return true;
    }
}
