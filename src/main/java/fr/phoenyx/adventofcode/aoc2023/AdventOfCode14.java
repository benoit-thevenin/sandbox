package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.utils.MathUtils;

public class AdventOfCode14 {

    private static class Platform extends CharGrid {
        Platform(List<String> lines) {
            super(lines);
        }

        void spin() {
            List.of(Dir.N, Dir.W, Dir.S, Dir.E).forEach(this::move);
        }

        void move(Dir dir) {
            Comparator<Coord2> comparator;
            if (dir == Dir.N) comparator = Comparator.comparingInt(c -> c.y);
            else if (dir == Dir.S) comparator = (c1, c2) -> Integer.compare(c2.y, c1.y);
            else if (dir == Dir.W) comparator = Comparator.comparingInt(c -> c.x);
            else comparator = (c1, c2) -> Integer.compare(c2.x, c1.x);
            getCoordinatesMatching(c -> c == 'O').stream().sorted(comparator).forEach(c -> {
                Coord2 target = c;
                Coord2 next = c.move(dir);
                while (isInGrid(next) && get(next) == '.') {
                    target = next;
                    next = target.move(dir);
                }
                set(c, '.');
                set(target, 'O');
            });
        }

        int getNorthLoad() {
            return getCoordinatesMatching(c -> c == 'O').stream().map(c -> height - c.y).reduce(0, Integer::sum);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            Platform platform = new Platform(lines);
            platform.move(Dir.N);
            LOGGER.info("PART 1: {}", platform.getNorthLoad());
            long begin = System.nanoTime();
            List<Integer> loads = new ArrayList<>();
            int lastIteration = 1000000000;
            for (int i = 0; i < lastIteration; i++) {
                platform.spin();
                loads.add(platform.getNorthLoad());
                Optional<Integer> frequency = MathUtils.getFrequency(loads);
                if (frequency.isPresent()) i += ((lastIteration - i) / frequency.get()) * frequency.get();
            }
            LOGGER.info("PART 2: {}, time elapsed: {}ms", platform.getNorthLoad(), (System.nanoTime() - begin) / 1000000);
        }
    }
}
