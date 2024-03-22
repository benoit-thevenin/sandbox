package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Dir;

public class AdventOfCode14 {

    private static class Disk {
        private final boolean[][] spaces = new boolean[128][128];

        Disk(String key) {
            for (int i = 0; i < spaces.length; i++) {
                String knotHash = AdventOfCode10.getKnotHash(key + "-" + i);
                for (int j = 0; j < 4; j++) {
                    int current = Integer.parseUnsignedInt(knotHash.substring(8 * j, 8 * (j + 1)), 16);
                    for (int k = 0; k < 32; k++) spaces[32 * j + k][i] = (current & (1 << (31 - k))) != 0;
                }
            }
        }

        int getUsedSpace() {
            int count = 0;
            for (boolean[] space : spaces) {
                for (int i = 0; i < spaces.length; i++) if (space[i]) count++;
            }
            return count;
        }

        int getDistinctRegions() {
            Set<Integer> visited = new HashSet<>();
            int count = 0;
            for (int i = 0; i < spaces.length; i++) {
                for (int j = 0; j < spaces.length; j++) {
                    int hash = i + (j << 16);
                    if (!spaces[i][j] || visited.contains(hash)) continue;
                    count++;
                    visited.addAll(getRegionHashes(hash));
                }
            }
            return count;
        }

        private Set<Integer> getRegionHashes(int hash) {
            Queue<Integer> toVisit = new LinkedList<>();
            toVisit.add(hash);
            Set<Integer> regionHashes = new HashSet<>(toVisit);
            while (!toVisit.isEmpty()) {
                int current = toVisit.remove();
                int x = current & 0xffff;
                int y = current >> 16;
                for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                    int neighbourX = x + dir.dx;
                    int neighbourY = y + dir.dy;
                    int neighbourHash = neighbourX + (neighbourY << 16);
                    if (isInDisk(neighbourX, neighbourY) && !regionHashes.contains(neighbourHash) && spaces[neighbourX][neighbourY]) {
                        toVisit.add(neighbourHash);
                        regionHashes.add(neighbourHash);
                    }
                }
            }
            return regionHashes;
        }

        private boolean isInDisk(int x, int y) {
            return x >= 0 && y >= 0 && x < spaces.length && y < spaces.length;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String key;
            while ((key = reader.readLine()) != null) {
                Disk disk = new Disk(key);
                LOGGER.info("PART 1: {}", disk.getUsedSpace());
                LOGGER.info("PART 2: {}", disk.getDistinctRegions());
            }
        }
    }
}
