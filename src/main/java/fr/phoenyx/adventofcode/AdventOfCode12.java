package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode12 {

    private static class SpringRow {
        String springs;
        int[] contiguousDamaged;
        long[][] cache;

        SpringRow(String line) {
            String[] split = line.split(" ");
            springs = split[0];
            String[] contiguous = split[1].split(",");
            contiguousDamaged = new int[contiguous.length];
            for (int i = 0; i < contiguousDamaged.length; i++) contiguousDamaged[i] = Integer.parseInt(contiguous[i % contiguous.length]);
        }

        void unfoldRow() {
            StringBuilder springsBuilder = new StringBuilder(springs);
            for (int i = 0; i < 4; i++) {
                springsBuilder.append('?');
                springsBuilder.append(springs);
            }
            springs = springsBuilder.toString();
            int[] newContiguousDamaged = new int[contiguousDamaged.length * 5];
            for (int i = 0; i < newContiguousDamaged.length; i++) newContiguousDamaged[i] = contiguousDamaged[i % contiguousDamaged.length];
            contiguousDamaged = newContiguousDamaged;
        }

        long springsArrangements() {
            cache = new long[springs.length()][contiguousDamaged.length + 1];
            for (int i = 0; i < springs.length(); i++) {
                for (int j = 0; j <= contiguousDamaged.length; j++) cache[i][j] = -1L;
            }
            return compute(0, 0);
        }

        long compute(int i, int j) {
            if (i == springs.length()) return j == contiguousDamaged.length ? 1 : 0;
            if (cache[i][j] != -1) return cache[i][j];
            if (springs.charAt(i) == '.') cache[i][j] = compute(i + 1, j);
            else if (springs.charAt(i) == '#') cache[i][j] = computeDamaged(i, j);
            else cache[i][j] = compute(i + 1, j) + computeDamaged(i, j);
            return cache[i][j];
        }

        long computeDamaged(int i, int j) {
            if (j == contiguousDamaged.length) return 0;
            int index = i + contiguousDamaged[j];
            if (!isContiguousDamagedPossible(i, index)) return 0;
            if (index == springs.length()) return j == contiguousDamaged.length - 1 ? 1 : 0;
            return compute(index + 1, j + 1);
        }

        boolean isContiguousDamagedPossible(int from, int to) {
            if (to > springs.length()) return false;
            if (to == springs.length()) {
                for (int i = from; i < to; i++) if (springs.charAt(i) == '.') return false;
                return true;
            }
            for (int i = from; i < to; i++) if (springs.charAt(i) == '.') return false;
            return springs.charAt(to) != '#';
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode12.class);
    private static final List<SpringRow> springRows = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode12.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) springRows.add(new SpringRow(currentLine));
            long begin = System.nanoTime();
            LOGGER.info("PART 1: {}, time elapsed: {}ms", springRows.stream().map(SpringRow::springsArrangements).reduce(Long::sum).orElseThrow(), (System.nanoTime() - begin) / 1000000);
            springRows.forEach(SpringRow::unfoldRow);
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed: {}ms", springRows.stream().map(SpringRow::springsArrangements).reduce(Long::sum).orElseThrow(), (System.nanoTime() - begin) / 1000000);
        }
    }
}
