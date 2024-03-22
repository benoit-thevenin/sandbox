package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode14 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode14.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode14.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                long begin = System.nanoTime();
                LOGGER.info("PART 1: {}, time elapsed: {}ms", getIndexOfLastKeyGenerated(currentLine, false), (System.nanoTime() - begin) / 1000000);
                begin = System.nanoTime();
                LOGGER.info("PART 2: {}, time elapsed: {}ms", getIndexOfLastKeyGenerated(currentLine, true), (System.nanoTime() - begin) / 1000000);
            }
        }
    }

    private static int getIndexOfLastKeyGenerated(String salt, boolean isPart2) {
        List<String> keys = new ArrayList<>();
        Queue<String> hashes = new LinkedList<>();
        Pattern threeConsecutiveCharacters = Pattern.compile("([0-9a-f])\\1{2}.*");
        for (int i = 0; i < 1000; i++) hashes.add(getHash(salt, i, isPart2));
        int index = 0;
        while (true) {
            String currentHash = hashes.remove();
            hashes.add(getHash(salt, index + 1000, isPart2));
            Matcher m = threeConsecutiveCharacters.matcher(currentHash);
            if (m.find() && hashes.stream().anyMatch(hash -> hash.matches(".*([" + m.group(1) + "])\\1{4}.*"))) {
                keys.add(currentHash);
                if (keys.size() == 64) return index;
            }
            index++;
        }
    }

    private static String getHash(String salt, int index, boolean isPart2) {
        String hash = Utils.getHexadecimalMD5Hash(salt + index);
        if (isPart2) for (int i = 0; i < 2016; i++) hash = Utils.getHexadecimalMD5Hash(hash);
        return hash;
    }
}
