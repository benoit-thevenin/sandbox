package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode07 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);
    private static final Map<String, String> circuit = new HashMap<>();
    private static final Map<String, Integer> values = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            values.put("1", 1);
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" -> ");
                circuit.put(split[1], split[0]);
            }
            LOGGER.info("PART 1: {}", getWireValue("a"));
            circuit.put("b", Integer.toString(getWireValue("a")));
            values.clear();
            values.put("1", 1);
            LOGGER.info("PART 2: {}", getWireValue("a"));
        }
    }

    private static int getWireValue(String key) {
        if (values.containsKey(key)) return values.get(key);
        String source = circuit.get(key);
        if (source.startsWith("NOT ")) {
            values.put(key, 0xffff - getWireValue(source.split("NOT ")[1]));
        } else if (source.contains(" RSHIFT ")) {
            String[] split = source.split(" RSHIFT ");
            values.put(key, getWireValue(split[0]) >> Integer.parseInt(split[1]));
        } else if (source.contains(" LSHIFT ")) {
            String[] split = source.split(" LSHIFT ");
            values.put(key, (getWireValue(split[0]) << Integer.parseInt(split[1])) & 0xffff);
        } else if (source.contains(" OR ")) {
            String[] split = source.split(" OR ");
            values.put(key, getWireValue(split[0]) | getWireValue(split[1]));
        } else if (source.contains(" AND ")) {
            String[] split = source.split(" AND ");
            values.put(key, getWireValue(split[0]) & getWireValue(split[1]));
        } else {
            try {
                values.put(key, Integer.parseInt(source));
            } catch (NumberFormatException e) {
                values.put(key, getWireValue(source));
            }
        }
        return values.get(key);
    }
}
