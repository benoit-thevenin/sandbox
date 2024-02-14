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
                LOGGER.info("PART 1: {}", getDecompressedLength(currentLine));
                LOGGER.info("PART 2: {}", getDecompressedLengthV2(currentLine));
            }
        }
    }

    private static int getDecompressedLength(String s) {
        int length = 0;
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
                length += markerLength * Integer.parseInt(split[1]);
                index += markerLength;
            }
        }
        return length;
    }

    private static long getDecompressedLengthV2(String s) {
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
                length += repetitions * getDecompressedLengthV2(s.substring(index, index + markerLength));
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
