package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.Range;

public class AdventOfCode04 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int resultPart1 = 0;
            int resultPart2 = 0;
            while ((currentLine = reader.readLine()) != null) {
                String[] pair = currentLine.split(",");
                Range first = Range.buildFromStartAndEndInclusive(pair[0]);
                Range second = Range.buildFromStartAndEndInclusive(pair[1]);
                if (first.contains(second) || first.isContainedBy(second)) resultPart1++;
                if (first.intersection(second).isPresent()) resultPart2++;
            }
            LOGGER.info("PART 1: {}", resultPart1);
            LOGGER.info("PART 2: {}", resultPart2);
        }
    }
}
