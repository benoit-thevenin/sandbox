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

    private record File(String name, int weight, File ancestor, Map<String, File> content) {
        int getWeight() {
            return weight > 0 ? weight : content.values().stream().map(File::getWeight).reduce(0, Integer::sum);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);
    private static final List<File> directories = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            directories.add(new File("/", 0, null, new HashMap<>()));
            File current = directories.get(0);
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.equals("$ cd ..")) current = current.ancestor;
                else if (currentLine.equals("$ cd /")) current = directories.iterator().next();
                else if (currentLine.startsWith("$ cd")) current = current.content.get(currentLine.split("\\$ cd ")[1]);
                else if (currentLine.startsWith("dir")) {
                    File dir = new File(currentLine.split("dir ")[1], 0, current, new HashMap<>());
                    directories.add(dir);
                    current.content.put(dir.name, dir);
                } else if (!currentLine.equals("$ ls")) {
                    File file = new File(currentLine.split(" ")[1], Integer.parseInt(currentLine.split(" ")[0]), current, new HashMap<>());
                    current.content.put(file.name, file);
                }
            }
            LOGGER.info("PART 1: {}", directories.stream().map(File::getWeight).filter(w -> w < 100000).reduce(0, Integer::sum));
            int weightToFree = 30000000 - (70000000 - directories.get(0).getWeight());
            LOGGER.info("PART 2: {}", directories.stream().map(File::getWeight).filter(w -> w > weightToFree).min(Integer::compare).orElseThrow());
        }
    }
}
