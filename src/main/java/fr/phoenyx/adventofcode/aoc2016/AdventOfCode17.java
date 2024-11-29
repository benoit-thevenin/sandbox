package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import fr.phoenyx.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode17 {

    private record State(String passcode, Coord2 pos) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode17.class);
    private static final Coord2 START = new Coord2(0, 0);
    private static final Coord2 END = new Coord2(3, 3);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode17.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                Set<State> validPaths = getValidPaths(currentLine);
                LOGGER.info("PART 1: {}", getShortestPath(validPaths, currentLine));
                LOGGER.info("PART 2: {}", getLongestPath(validPaths, currentLine).length());
            }
        }
    }

    private static String getShortestPath(Set<State> paths, String passcode) {
        return paths.stream()
            .min(Comparator.comparingInt(s -> s.passcode.length()))
            .map(s -> s.passcode.substring(passcode.length())).orElseThrow();
    }

    private static String getLongestPath(Set<State> paths, String passcode) {
        return paths.stream()
            .max(Comparator.comparingInt(s -> s.passcode.length()))
            .map(s -> s.passcode.substring(passcode.length())).orElseThrow();
    }

    private static Set<State> getValidPaths(String passcode) {
        Set<State> validPaths = new HashSet<>();
        Queue<State> toVisit = new LinkedList<>();
        toVisit.add(new State(passcode, START));
        while (!toVisit.isEmpty()) {
            State current = toVisit.remove();
            String hash = Utils.getHexadecimalMD5Hash(current.passcode);
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                if (isDoorOpen(dir, hash)) {
                    State nextState = new State(current.passcode + dirToChar(dir), current.pos.move(dir));
                    if (END.equals(nextState.pos)) validPaths.add(nextState);
                    else if (isInGrid(nextState.pos)) toVisit.add(nextState);
                }
            }
        }
        return validPaths;
    }

    private static boolean isDoorOpen(Dir dir, String hash) {
        int charIndex = dir == Dir.N ? 0 : dir == Dir.S ? 1 : dir == Dir.W ? 2 : 3;
        return isDoorOpen(hash.charAt(charIndex));
    }

    private static boolean isDoorOpen(char c) {
        return !Character.isDigit(c) && c != 'a';
    }

    private static char dirToChar(Dir dir) {
        return dir == Dir.N ? 'U' : dir == Dir.S ? 'D' : dir == Dir.W ? 'L' : 'R';
    }

    private static boolean isInGrid(Coord2 pos) {
        return pos.x >= START.x && pos.y >= START.y && pos.x <= END.x && pos.y <= END.y;
    }
}
