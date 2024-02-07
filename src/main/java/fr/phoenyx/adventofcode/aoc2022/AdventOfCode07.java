package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode07 {

    private static class File {
        final String name;
        private int weight;
        final File ancestor;
        Map<String, File> content = new HashMap<>();

        File(String name, int weight, File ancestor) {
            this.name = name;
            this.weight = weight;
            this.ancestor = ancestor;
        }

        int getWeight() {
            if (weight > 0) return weight;
            weight = content.values().stream().map(File::getWeight).reduce(Integer::sum).orElse(0);
            return weight;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);
    private static final List<File> directories = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            directories.add(new File("/", 0, null));
            File current = directories.iterator().next();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals("$ cd ..")) current = current.ancestor;
                else if (currentLine.equals("$ cd /")) current = directories.iterator().next();
                else if (currentLine.startsWith("$ cd")) current = current.content.get(currentLine.split("\\$ cd ")[1]);
                else if (currentLine.startsWith("dir")) {
                    File dir = new File(currentLine.split("dir ")[1], 0, current);
                    directories.add(dir);
                    current.content.put(dir.name, dir);
                } else if (!currentLine.equals("$ ls")) {
                    File file = new File(currentLine.split(" ")[1], Integer.parseInt(currentLine.split(" ")[0]), current);
                    current.content.put(file.name, file);
                }
            }
            LOGGER.info("PART 1: {}", directories.stream().map(File::getWeight).filter(w -> w < 100000).reduce(Integer::sum).orElseThrow());
            int weightToFree = 30000000 - (70000000 - directories.iterator().next().getWeight());
            LOGGER.info("PART 2: {}", directories.stream().map(File::getWeight).filter(w -> w > weightToFree).min(Integer::compare).orElseThrow());
        }
    }
}
