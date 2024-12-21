package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode04 {

    private record Room(String checksum, int sectorId, String encryptedName) {
        boolean isReal() {
            Map<Character, Integer> letterCount = Utils.getLetterCount(encryptedName);
            List<Character> computedChecksum = letterCount.keySet().stream()
                .sorted((c1, c2) -> {
                    int compare = Integer.compare(letterCount.get(c2), letterCount.get(c1));
                    return compare != 0 ? compare : Character.compare(c1, c2);
                }).limit(5).toList();
            for (int i = 0; i < checksum.length(); i++) if (checksum.charAt(i) != computedChecksum.get(i)) return false;
            return true;
        }

        String getDecryptedName() {
            StringBuilder name = new StringBuilder();
            for (char c : encryptedName.toCharArray()) name.append((char) (((c - 'a' + sectorId) % 26) + 'a'));
            return name.toString();
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Room> rooms = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String checksum = currentLine.split("\\[")[1].substring(0, 5);
                String[] split = currentLine.split("-");
                int sectorId = Integer.parseInt(split[split.length - 1].split("\\[")[0]);
                StringBuilder name = new StringBuilder();
                for (int i = 0; i < split.length - 1; i++) name.append(split[i]);
                rooms.add(new Room(checksum, sectorId, name.toString()));
            }
            LOGGER.info("PART 1: {}", rooms.stream().filter(Room::isReal).map(r -> r.sectorId).reduce(0, Integer::sum));
            LOGGER.info("PART 2: {}", rooms.stream().filter(r -> r.getDecryptedName().contains("northpoleobjects")).map(r -> r.sectorId).findAny().orElseThrow());
        }
    }
}
