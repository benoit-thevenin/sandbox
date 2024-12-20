package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode13 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);
    private static final Coord2 START = new Coord2(1, 1);
    private static final Coord2 TARGET = new Coord2(31, 39);
    private static int officeDesignerFavoriteNumber;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) officeDesignerFavoriteNumber = Integer.parseInt(currentLine);
            Entry<Integer, Long> result = getResult();
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Entry<Integer, Long> getResult() {
        Map<Coord2, Integer> visited = new HashMap<>();
        visited.put(START, 0);
        Queue<Coord2> toVisit = new LinkedList<>(visited.keySet());
        while (!toVisit.isEmpty()) {
            Coord2 current = toVisit.remove();
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                Coord2 neighbour = current.move(dir);
                if (isWall(neighbour)) continue;
                if (neighbour.equals(TARGET)) return new SimpleEntry<>(visited.get(current) + 1, visited.values().stream().filter(d -> d <= 50).count());
                visited.computeIfAbsent(neighbour, n -> {
                    toVisit.add(neighbour);
                    return visited.get(current) + 1;
                });
            }
        }
        throw new IllegalArgumentException("Wrong dataset");
    }

    private static boolean isWall(Coord2 coord) {
        if (coord.x < 0 || coord.y < 0) return true;
        int value = coord.x * (coord.x + 3 + 2 * coord.y) + coord.y * (coord.y + 1) + officeDesignerFavoriteNumber;
        int bits = 0;
        for (int i = 0; i < 31; i++) if (((value >> i) & 1) == 1) bits++;
        return bits % 2 != 0;
    }
}
