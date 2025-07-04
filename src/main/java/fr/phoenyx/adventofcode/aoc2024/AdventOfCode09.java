package fr.phoenyx.adventofcode.aoc2024;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class AdventOfCode09 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode09.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode09.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                int length = Arrays.stream(currentLine.split("")).map(Integer::parseInt).reduce(0, Integer::sum);
                int[] disk = new int[length];
                int index = 0;
                int currentFileId = 0;
                boolean isFile = true;
                for (char c : currentLine.toCharArray()) {
                    for (int i = 0; i < c - '0'; i++) {
                        if (isFile) disk[index] = currentFileId;
                        else disk[index] = -1;
                        index++;
                    }
                    if (isFile) currentFileId++;
                    isFile = !isFile;
                }
                LOGGER.info("PART 1: {}", getChecksum(compact1(disk)));
                LOGGER.info("PART 2: {}", getChecksum(compact2(disk)));
            }
        }
    }

    private static int[] compact1(int[] disk) {
        int[] compactedDisk = new int[disk.length];
        System.arraycopy(disk, 0, compactedDisk, 0, disk.length);
        int frontIndex = 0;
        int backIndex = disk.length - 1;
        while (frontIndex < backIndex) {
            if (compactedDisk[frontIndex] != -1) frontIndex++;
            else if (compactedDisk[backIndex] == -1) backIndex--;
            else {
                compactedDisk[frontIndex] = compactedDisk[backIndex];
                compactedDisk[backIndex] = -1;
            }
        }
        return compactedDisk;
    }

    private static long getChecksum(int[] disk) {
        long checksum = 0;
        for (int i = 0; i < disk.length; i++) {
            if (disk[i] == -1) continue;
            checksum += (long) i * disk[i];
        }
        return checksum;
    }

    private static int[] compact2(int[] disk) {
        int[] compactedDisk = new int[disk.length];
        System.arraycopy(disk, 0, compactedDisk, 0, disk.length);
        int currentFileId = -1;
        int endIndex = -1;
        for (int i = disk.length - 1; i >= 0; i--) {
            if (compactedDisk[i] == currentFileId) continue;
            if (currentFileId != -1) moveFile(compactedDisk, i + 1, endIndex);
            currentFileId = compactedDisk[i];
            endIndex = i;
        }
        return compactedDisk;
    }

    private static void moveFile(int[] disk, int startIndex, int endIndex) {
        int movePosition = getMovePosition(disk, startIndex, endIndex);
        if (movePosition == -1) return;
        int length = endIndex - startIndex + 1;
        for (int i = 0; i < length; i++) {
            disk[movePosition + i] = disk[startIndex + i];
            disk[startIndex + i] = -1;
        }
    }

    private static int getMovePosition(int[] disk, int startIndex, int endIndex) {
        int length = endIndex - startIndex + 1;
        int currentLength = 0;
        int movePosition = -1;
        for (int i = 0; i < startIndex; i++) {
            if (disk[i] == -1) {
                if (currentLength == 0) movePosition = i;
                currentLength++;
                if (currentLength == length) return movePosition;
            } else {
                currentLength = 0;
                movePosition = -1;
            }
        }
        return -1;
    }
}
