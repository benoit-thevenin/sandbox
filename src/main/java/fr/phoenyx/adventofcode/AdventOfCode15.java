package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode15 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> instructions = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) instructions = Arrays.asList(currentLine.split(","));
            LOGGER.info("PART 1: {}", instructions.stream().map(AdventOfCode15::hash).reduce(Integer::sum).orElseThrow());
            LOGGER.info("PART 2: {}", 0);
        }
    }

    private static int hash(String string) {
        int result = 0;
        for (char c : string.toCharArray()) result = ((result + c) * 17) % 256;
        return result;
    }
}
