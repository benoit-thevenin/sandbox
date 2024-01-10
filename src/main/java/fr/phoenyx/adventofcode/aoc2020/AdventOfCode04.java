package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode04 {

    private static class Passport {
        private final Map<String, String> values = new HashMap<>();
        private static final Set<String> VALID_HEIGHT_UNITS = Set.of("cm", "in");
        private static final Set<String> VALID_EYES_COLOR = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

        void addFields(String line) {
            String[] split = line.split(" ");
            for (String s : split) {
                String[] keyValue = s.split(":");
                values.put(keyValue[0], keyValue[1]);
            }
        }

        boolean isValid1() {
            return values.size() == 8 || values.size() == 7 && !values.containsKey("cid");
        }

        boolean isValid2() {
            if (!isValid1()) return false;
            try {
                int byr = Integer.parseInt(values.get("byr"));
                int iyr = Integer.parseInt(values.get("iyr"));
                int eyr = Integer.parseInt(values.get("eyr"));
                String stringHeight = values.get("hgt");
                String unit = stringHeight.substring(stringHeight.length() - 2);
                int hgt = Integer.parseInt(stringHeight.substring(0, stringHeight.length() - 2));
                if (byr < 1920 || byr > 2002
                 || iyr < 2010 || iyr > 2020
                 || eyr < 2020 || eyr > 2030
                 || !VALID_HEIGHT_UNITS.contains(unit)
                 || "cm".equals(unit) && (hgt < 150 || hgt > 193)
                 || "in".equals(unit) && (hgt < 59 || hgt > 76)
                 || !Pattern.matches("#[0-9a-f]{6}", values.get("hcl"))
                 || !VALID_EYES_COLOR.contains(values.get("ecl"))
                 || !Pattern.matches("\\d{9}", values.get("pid"))) return false;
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Passport> passports = new ArrayList<>();
            String currentLine;
            Passport current = new Passport();
            passports.add(current);
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) {
                    current = new Passport();
                    passports.add(current);
                } else current.addFields(currentLine);
            }
            LOGGER.info("PART 1: {}", passports.stream().filter(Passport::isValid1).count());
            LOGGER.info("PART 2: {}", passports.stream().filter(Passport::isValid2).count());
        }
    }
}
