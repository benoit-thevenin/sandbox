package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode06 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode06.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode06.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> messages = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) messages.add(currentLine);
            LOGGER.info("PART 1: {}", getCorrectedMessage(messages, true));
            LOGGER.info("PART 2: {}", getCorrectedMessage(messages, false));
        }
    }

    private static String getCorrectedMessage(List<String> messages, boolean part1) {
        StringBuilder correctedMessage = new StringBuilder();
        int messageLength = messages.get(0).length();
        for (int i = 0; i < messageLength; i++) {
            StringBuilder rotatedMessage = new StringBuilder();
            for (String message : messages) rotatedMessage.append(message.charAt(i));
            Map<Character, Integer> letterCount = Utils.getLetterCount(rotatedMessage.toString());
            if (part1) correctedMessage.append(letterCount.entrySet().stream().max(Comparator.comparingInt(Entry::getValue)).map(Entry::getKey).orElseThrow());
            else correctedMessage.append(letterCount.entrySet().stream().min(Comparator.comparingInt(Entry::getValue)).map(Entry::getKey).orElseThrow());
        }
        return correctedMessage.toString();
    }
}
