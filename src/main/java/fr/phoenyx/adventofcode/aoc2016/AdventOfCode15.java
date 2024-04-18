package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode15 {

    private record Disc(int positions, int initialPosition) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);
    private static final List<Disc> discs = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" ");
                discs.add(new Disc(Integer.parseInt(split[3]), Integer.parseInt(split[split.length - 1].replace(".", ""))));
            }
            LOGGER.info("PART 1: {}", getFirstTimeRoadOk());
            discs.add(new Disc(11, 0));
            LOGGER.info("PART 2: {}", getFirstTimeRoadOk());
        }
    }

    private static int getFirstTimeRoadOk() {
        int time = 0;
        while (!isRoadOkForTime(time)) time++;
        return time;
    }

    private static boolean isRoadOkForTime(int time) {
        for (int i = 0; i < discs.size(); i++) {
            Disc disc = discs.get(i);
            if ((disc.initialPosition + time + i + 1) % disc.positions != 0) return false;
        }
        return true;
    }
}
