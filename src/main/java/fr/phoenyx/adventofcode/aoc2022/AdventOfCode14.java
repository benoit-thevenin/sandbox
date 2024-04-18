package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode14 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);
    private static final Map<Coord2, Character> cave = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" -> ");
                Coord2 last = buildCoord2FromString(split[0]);
                for (int i = 1; i < split.length; i++) {
                    Coord2 next = buildCoord2FromString(split[i]);
                    if (last.x != next.x) {
                        int xStart = Math.min(last.x, next.x);
                        int xEnd = Math.max(last.x, next.x);
                        for (int x = xStart; x <= xEnd; x++) cave.put(new Coord2(x, last.y), '#');
                    } else {
                        int yStart = Math.min(last.y, next.y);
                        int yEnd = Math.max(last.y, next.y);
                        for (int y = yStart; y <= yEnd; y++) cave.put(new Coord2(last.x, y), '#');
                    }
                    last = next;
                }
            }
            Entry<Long, Long> result = getResult();
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Coord2 buildCoord2FromString(String s) {
        String[] split = s.split(",");
        return new Coord2(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    private static Entry<Long, Long> getResult() {
        long resultPart1 = 0;
        int maxY = cave.keySet().stream().map(c -> c.y).max(Integer::compare).orElseThrow() + 1;
        while (!cave.containsKey(new Coord2(500, 0))) {
            Coord2 sand = new Coord2(500, 0);
            boolean falling = true;
            while (falling) {
                Coord2 fall = fall(sand);
                if (cave.containsKey(fall)) {
                    cave.put(sand, 'o');
                    falling = false;
                } else if (fall.y == maxY) {
                    if (resultPart1 == 0) resultPart1 = cave.values().stream().filter(c -> c == 'o').count();
                    cave.put(fall, 'o');
                    falling = false;
                } else sand = fall;
            }
        }
        return new SimpleEntry<>(resultPart1, cave.values().stream().filter(c -> c == 'o').count());
    }

    private static Coord2 fall(Coord2 sand) {
        Coord2 fall = sand.move(Dir.S);
        if (cave.containsKey(fall)) fall = sand.move(Dir.SW);
        if (cave.containsKey(fall)) fall = sand.move(Dir.SE);
        return fall;
    }
}
