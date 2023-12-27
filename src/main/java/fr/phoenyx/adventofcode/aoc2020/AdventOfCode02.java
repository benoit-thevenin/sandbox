package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode02 {

    private static class TobogganPassword {
        final int policy1;
        final int policy2;
        final char c;
        final String password;

        TobogganPassword(String line) {
            String[] split1 = line.split(": ");
            password = split1[1];
            String[] split2 = split1[0].split(" ");
            c = split2[1].charAt(0);
            String[] split3 = split2[0].split("-");
            policy1 = Integer.parseInt(split3[0]);
            policy2 = Integer.parseInt(split3[1]);
        }

        boolean isValid() {
            int count = 0;
            for (int i = 0; i < password.length(); i++) if (password.charAt(i) == c) count++;
            return count >= policy1 && count <= policy2;
        }

        boolean isOfficialValid() {
            return password.charAt(policy1 - 1) == c && password.charAt(policy2 - 1) != c
                || password.charAt(policy1 - 1) != c && password.charAt(policy2 - 1) == c;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode02.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode02.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<TobogganPassword> passwords = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) passwords.add(new TobogganPassword(currentLine));
            LOGGER.info("PART 1: {}", passwords.stream().filter(TobogganPassword::isValid).count());
            LOGGER.info("PART 2: {}", passwords.stream().filter(TobogganPassword::isOfficialValid).count());
        }
    }
}
