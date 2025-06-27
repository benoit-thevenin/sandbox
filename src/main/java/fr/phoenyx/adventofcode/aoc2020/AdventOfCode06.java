package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode06 {

    private record Group(List<String> answers) {
        int getAnyoneCount() {
            return Utils.getLetterCount(String.join("", answers)).size();
        }

        long getEveryoneCount() {
            return Utils.getLetterCount(String.join("", answers)).values().stream().filter(v -> v == answers.size()).count();
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Group> groups = new ArrayList<>();
            Group current = new Group(new ArrayList<>());
            groups.add(current);
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) {
                    current = new Group(new ArrayList<>());
                    groups.add(current);
                } else current.answers.add(currentLine);
            }
            LOGGER.info("PART 1: {}", groups.stream().map(Group::getAnyoneCount).reduce(0, Integer::sum));
            LOGGER.info("PART 2: {}", groups.stream().map(Group::getEveryoneCount).reduce(0L, Long::sum));
        }
    }
}
