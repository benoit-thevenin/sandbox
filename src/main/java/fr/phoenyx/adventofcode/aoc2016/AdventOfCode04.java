package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode04 {

    private static class Room {
        String encryptedName = "";
        int sectorId;
        String checksum;

        Room(String line) {
            checksum = line.split("\\[")[1].substring(0, 5);
            String[] split = line.split("-");
            sectorId = Integer.parseInt(split[split.length - 1].split("\\[")[0]);
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < split.length - 1; i++) name.append(split[i]);
            encryptedName = name.toString();
        }

        boolean isReal() {
            Map<Character, Integer> letterCount = new HashMap<>();
            for (int i = 0; i < encryptedName.length(); i++) {
                char c = encryptedName.charAt(i);
                if (letterCount.containsKey(c)) letterCount.put(c, letterCount.get(c) + 1);
                else letterCount.put(c, 1);
            }
            List<Character> computedChecksum = letterCount.keySet().stream()
                .sorted((c1, c2) -> {
                    int compare = Integer.compare(letterCount.get(c2), letterCount.get(c1));
                    if (compare == 0) return Character.compare(c1, c2);
                    return compare;
                }).limit(5).toList();
            for (int i = 0; i < checksum.length(); i++) {
                if (checksum.charAt(i) != computedChecksum.get(i)) return false;
            }
            return true;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Room> rooms = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) rooms.add(new Room(currentLine));
            LOGGER.info("PART 1: {}", rooms.stream().filter(Room::isReal).map(r -> r.sectorId).reduce(Integer::sum).orElseThrow());
            LOGGER.info("PART 2: {}", 0);
        }
    }
}
