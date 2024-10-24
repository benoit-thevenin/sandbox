package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode16 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            char[] programs = "abcdefghijklmnop".toCharArray();
            List<String> moves = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) moves.addAll(Arrays.asList(currentLine.split(",")));
            LOGGER.info("PART 1: {}", getDanceResult(programs, moves, 1));
            LOGGER.info("PART 2: {}", getDanceResult(programs, moves, 1000000000 - 1));
        }
    }

    private static String getDanceResult(char[] programs, List<String> moves, int lastIteration) {
        List<String> configurations = new ArrayList<>();
        boolean loopFound = false;
        for (int i = 0; i < lastIteration; i++) {
            for (String move : moves) performDanceMove(programs, move);
            if (!loopFound) {
                String next = new String(programs);
                int index = configurations.indexOf(next);
                if (index != -1) {
                    loopFound = true;
                    int length = i - index;
                    i += ((lastIteration - i) / length) * length;
                } else configurations.add(next);
            }
        }
        return new String(programs);
    }

    private static void performDanceMove(char[] programs, String move) {
        if (move.charAt(0) == 's') {
            int size = Integer.parseInt(move.substring(1));
            char[] save = new char[programs.length - size];
            System.arraycopy(programs, 0, save, 0, save.length);
            System.arraycopy(programs, save.length, programs, 0, size);
            System.arraycopy(save, 0, programs, size, save.length);
        } else {
            String[] split = move.substring(1).split("/");
            if (move.charAt(0) == 'x') {
                int index1 = Integer.parseInt(split[0]);
                int index2 = Integer.parseInt(split[1]);
                char save = programs[index1];
                programs[index1] = programs[index2];
                programs[index2] = save;
            } else {
                char program1 = split[0].charAt(0);
                char program2 = split[1].charAt(0);
                for (int i = 0; i < programs.length; i++) {
                    if (programs[i] == program1) programs[i] = program2;
                    else if (programs[i] == program2) programs[i] = program1;
                }
            }
        }
    }
}
