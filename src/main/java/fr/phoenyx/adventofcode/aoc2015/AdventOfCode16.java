package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode16 {

    private static class Aunt {
        final int id;
        final Map<String, Integer> compounds = new HashMap<>();

        Aunt(int id) {
            this.id = id;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);
    private static final Set<Aunt> AUNTS = new HashSet<>();
    private static final Map<String, Integer> GIFTING_AUNT_COMPOUNDS = new HashMap<>();
    private static final Set<String> GREATER_THAN_KEYS = Set.of("cats", "trees");
    private static final Set<String> FEWER_THAN_KEYS = Set.of("pomeranians", "goldfish");

    static {
        GIFTING_AUNT_COMPOUNDS.put("children", 3);
        GIFTING_AUNT_COMPOUNDS.put("cats", 7);
        GIFTING_AUNT_COMPOUNDS.put("samoyeds", 2);
        GIFTING_AUNT_COMPOUNDS.put("pomeranians", 3);
        GIFTING_AUNT_COMPOUNDS.put("akitas", 0);
        GIFTING_AUNT_COMPOUNDS.put("vizslas", 0);
        GIFTING_AUNT_COMPOUNDS.put("goldfish", 5);
        GIFTING_AUNT_COMPOUNDS.put("trees", 3);
        GIFTING_AUNT_COMPOUNDS.put("cars", 2);
        GIFTING_AUNT_COMPOUNDS.put("perfumes", 1);
    }

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.substring(4);
                Aunt aunt = new Aunt(Integer.parseInt(currentLine.split(":")[0]));
                currentLine = currentLine.replace(aunt.id + ": ", "");
                String[] split = currentLine.split(", ");
                for (String compound : split) {
                    String[] keyValue = compound.split(": ");
                    aunt.compounds.put(keyValue[0], Integer.parseInt(keyValue[1]));
                }
                AUNTS.add(aunt);
            }
            LOGGER.info("PART 1: {}", getGiftingAuntId());
            LOGGER.info("PART 2: {}", getRealGiftingAuntId());
        }
    }

    private static int getGiftingAuntId() {
        return AUNTS.stream().filter(
            aunt -> GIFTING_AUNT_COMPOUNDS.entrySet().stream()
                .allMatch(e -> !aunt.compounds.containsKey(e.getKey()) || aunt.compounds.get(e.getKey()).equals(e.getValue()))
        ).findFirst().orElseThrow().id;
    }

    private static int getRealGiftingAuntId() {
        return AUNTS.stream().filter(
            aunt -> GIFTING_AUNT_COMPOUNDS.entrySet().stream()
                .allMatch(e -> {
                    if (!aunt.compounds.containsKey(e.getKey())) return true;
                    if (GREATER_THAN_KEYS.contains(e.getKey())) return aunt.compounds.get(e.getKey()) > e.getValue();
                    if (FEWER_THAN_KEYS.contains(e.getKey())) return aunt.compounds.get(e.getKey()) < e.getValue();
                    return aunt.compounds.get(e.getKey()).equals(e.getValue());
                })
        ).findFirst().orElseThrow().id;
    }
}
