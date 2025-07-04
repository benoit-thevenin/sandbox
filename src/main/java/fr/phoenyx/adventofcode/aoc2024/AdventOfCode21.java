package fr.phoenyx.adventofcode.aoc2024;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class AdventOfCode21 {

    private record Path(Coord2 pos, String path) {}
    private record Node(Coord2 start, Coord2 end, int nbRobots) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode21.class);
    private static final Map<Node, Long> CACHE = new HashMap<>();
    private static final CharGrid NUMERICAL_ROBOT = new CharGrid(List.of("789", "456", "123", ".0A"));
    private static final CharGrid DIRECTIONAL_ROBOT = new CharGrid(List.of(".^A", "<v>"));

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode21.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String code;
            long sum1 = 0;
            long sum2 = 0;
            while ((code = reader.readLine()) != null) {
                long value = Long.parseLong(code.replace("A", ""));
                sum1 += getMinSequenceLength(NUMERICAL_ROBOT, code, 3) * value;
                sum2 += getMinSequenceLength(NUMERICAL_ROBOT, code, 26) * value;
            }
            LOGGER.info("PART 1: {}", sum1);
            LOGGER.info("PART 2: {}", sum2);
        }
    }

    private static long getMinSequenceLength(CharGrid robot, String code, int nbRobots) {
        if (nbRobots == 0) return code.length();
        long sequenceLength = 0;
        Coord2 armPosition = robot.getCoordinatesMatching(c -> c == 'A').get(0);
        for (char c : code.toCharArray()) {
            Coord2 next = robot.getCoordinatesMatching(a -> a == c).get(0);
            sequenceLength += getMinSubSequenceLength(robot, armPosition, next, nbRobots);
            armPosition = next;
        }
        return sequenceLength;
    }

    private static long getMinSubSequenceLength(CharGrid robot, Coord2 start, Coord2 end, int nbRobots) {
        Node node = new Node(start, end, nbRobots);
        if (CACHE.containsKey(node)) return CACHE.get(node);
        long minSequence = Long.MAX_VALUE;
        Queue<Path> toVisit = new LinkedList<>();
        toVisit.add(new Path(start, ""));
        while (!toVisit.isEmpty()) {
            Path current = toVisit.remove();
            if (current.pos.equals(end)) minSequence = Math.min(minSequence, getMinSequenceLength(DIRECTIONAL_ROBOT, current.path + "A", nbRobots - 1));
            else addNextPaths(toVisit, robot, current, end);
        }
        CACHE.put(node, minSequence);
        return minSequence;
    }

    private static void addNextPaths(Queue<Path> toVisit, CharGrid robot, Path current, Coord2 end) {
        for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
            Coord2 next = current.pos.move(dir);
            if (isNextPathValid(robot, current.pos, end, next)) toVisit.add(new Path(next, current.path + dirToString(dir)));
        }
    }

    private static boolean isNextPathValid(CharGrid robot, Coord2 start, Coord2 end, Coord2 next) {
        return robot.isInGrid(next) && robot.get(next) != '.' && next.manhattanDistanceTo(end) < start.manhattanDistanceTo(end);
    }

    private static String dirToString(Dir dir) {
        if (dir == Dir.N) return "^";
        if (dir == Dir.E) return ">";
        return dir == Dir.S ? "v" : "<";
    }
}
