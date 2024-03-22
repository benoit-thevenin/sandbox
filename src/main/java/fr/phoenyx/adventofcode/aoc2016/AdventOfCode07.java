package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode07 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            int countTLS = 0;
            int countSSL = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (isSupportingTLS(currentLine)) countTLS++;
                if (isSupportingSSL(currentLine)) countSSL++;
            }
            LOGGER.info("PART 1: {}", countTLS);
            LOGGER.info("PART 2: {}", countSSL);
        }
    }

    private static boolean isSupportingTLS(String ip) {
        return !ip.matches(".*\\[[a-z]*([a-z])((?!\\1)[a-z])\\2\\1[a-z]*].*")
            && ip.matches(".*([a-z])((?!\\1)[a-z])\\2\\1.*");
    }

    private static boolean isSupportingSSL(String ip) {
        return ip.matches("(?:.*])?[a-z]*([a-z])((?!\\1)[a-z])\\1.*\\[[a-z]*\\2\\1\\2[a-z]*].*")
            || ip.matches(".*\\[[a-z]*([a-z])((?!\\1)[a-z])\\1[a-z]*](?:.*])?[a-z]*\\2\\1\\2.*");
    }
}
