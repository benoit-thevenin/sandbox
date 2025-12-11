package fr.phoenyx.adventofcode.aoc2025;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AdventOfCode11 {

    private record Path(String lastDevice, boolean containsDac, boolean containsFft) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);
    private static final Map<Path, Long> CACHE =  new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2025/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            Map<String, Set<String>> devices = new HashMap<>();
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.replace(":", "").split(" ");
                devices.put(split[0], Arrays.stream(split).filter(d -> !split[0].equals(d)).collect(Collectors.toSet()));
            }
            LOGGER.info("PART 1: {}", countPaths(devices, new Path("you", true, true)));
            LOGGER.info("PART 2: {}", countPaths(devices, new Path("svr", false, false)));
        }
    }

    private static long countPaths(Map<String, Set<String>> devices, Path path) {
        if (CACHE.containsKey(path)) return CACHE.get(path);
        if ("out".equals(path.lastDevice)) {
            long result = path.containsDac && path.containsFft ? 1 : 0;
            CACHE.put(path, result);
            return result;
        }
        long sum = devices.get(path.lastDevice).stream()
            .map(d -> countPaths(devices, new Path(d, path.containsDac || "dac".equals(d), path.containsFft || "fft".equals(d))))
            .reduce(0L, Long::sum);
        CACHE.put(path, sum);
        return sum;
    }
}
