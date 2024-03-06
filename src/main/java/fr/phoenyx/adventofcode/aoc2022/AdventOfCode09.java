package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> instructions = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) instructions.add(currentLine);
            LOGGER.info("PART 1: {}", simulateRope(2, instructions));
            LOGGER.info("PART 2: {}", simulateRope(10, instructions));
        }
    }

    private static int simulateRope(int ropeLength, List<String> instructions) {
        Set<Coord2> visitedByTail = new HashSet<>();
        Coord2[] rope = new Coord2[ropeLength];
        for (int i = 0; i < ropeLength; i++) rope[i] = new Coord2(0, 0);
        visitedByTail.add(rope[ropeLength - 1]);
        for (String instruction : instructions) {
            Dir dir = Dir.fromChar(instruction.charAt(0));
            int steps = Integer.parseInt(instruction.split(" ")[1]);
            for (int i = 0; i < steps; i++) {
                rope[0] = rope[0].move(dir);
                for (int j = 1; j < ropeLength; j++) {
                    Optional<Dir> tailDir = getTailDir(rope[j - 1], rope[j]);
                    if (tailDir.isPresent()) rope[j] = rope[j].move(tailDir.get());
                }
                visitedByTail.add(rope[ropeLength - 1]);
            }
        }
        return visitedByTail.size();
    }

    private static Optional<Dir> getTailDir(Coord2 head, Coord2 tail) {
        if (head.manhattanDistanceTo(tail) < 2 || head.manhattanDistanceTo(tail) == 2 && head.x != tail.x && head.y != tail.y) return Optional.empty();
        if (head.x == tail.x) return head.y < tail.y ? Optional.of(Dir.N) : Optional.of(Dir.S);
        if (head.y == tail.y) return head.x < tail.x ? Optional.of(Dir.W) : Optional.of(Dir.E);
        if (head.x < tail.x && head.y < tail.y) return Optional.of(Dir.NW);
        if (head.x < tail.x) return Optional.of(Dir.SW);
        return head.y < tail.y ? Optional.of(Dir.NE) : Optional.of(Dir.SE);
    }
}
