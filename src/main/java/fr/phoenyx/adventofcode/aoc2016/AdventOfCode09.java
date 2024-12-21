package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                LOGGER.info("PART 1: {}", getDecompressedLength(currentLine, true));
                LOGGER.info("PART 2: {}", getDecompressedLength(currentLine, false));
            }
        }
    }

    private static long getDecompressedLength(String s, boolean isPart1) {
        long length = 0;
        int index = 0;
        while (index < s.length()) {
            if (s.charAt(index) != '(') {
                length++;
                index++;
            } else {
                String marker = getMarker(s, index + 1);
                index += marker.length() + 2;
                String[] split = marker.split("x");
                int markerLength = Integer.parseInt(split[0]);
                int repetitions = Integer.parseInt(split[1]);
                if (isPart1) length += markerLength * Long.parseLong(split[1]);
                else length += repetitions * getDecompressedLength(s.substring(index, index + markerLength), false);
                index += markerLength;
            }
        }
        return length;
    }

    private static String getMarker(String s, int index) {
        StringBuilder marker = new StringBuilder();
        while (s.charAt(index) != ')') {
            marker.append(s.charAt(index));
            index++;
        }
        return marker.toString();
    }
}
