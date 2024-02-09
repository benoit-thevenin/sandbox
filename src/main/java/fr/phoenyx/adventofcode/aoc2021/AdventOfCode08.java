package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.Utils;

public class AdventOfCode08 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int resultPart1 = 0;
            int resultPart2 = 0;
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" \\| ");
                Set<String> inputs = Arrays.stream(split[0].split(" ")).collect(Collectors.toSet());
                List<String> outputs = Arrays.asList(split[1].split(" "));
                for (String output : outputs) if (Set.of(2, 3, 4, 7).contains(output.length())) resultPart1++;
                resultPart2 += decode(inputs, outputs);
            }
            LOGGER.info("PART 1: {}", resultPart1);
            LOGGER.info("PART 2: {}", resultPart2);
        }
    }

    private static int decode(Set<String> inputs, List<String> outputs) {
        Map<Integer, String> digitToSegments = getDigitToSegments(inputs);
        StringBuilder sb = new StringBuilder();
        for (String output : outputs) {
            Set<Character> letterCount = Utils.getLetterCount(output).keySet();
            int digit = digitToSegments.entrySet().stream()
                .filter(e -> e.getValue().length() == output.length())
                .filter(e -> letterCount.equals(Utils.getLetterCount(e.getValue()).keySet())).findFirst().orElseThrow().getKey();
            sb.append(digit);
        }
        return Integer.parseInt(sb.toString());
    }

    private static Map<Integer, String> getDigitToSegments(Set<String> inputs) {
        Map<Integer, String> digitToSegments = new HashMap<>();
        // 1, 4, 7, 8 are obvious because they have unique length
        digitToSegments.put(1, inputs.stream().filter(i -> i.length() == 2).findFirst().orElseThrow());
        digitToSegments.put(4, inputs.stream().filter(i -> i.length() == 4).findFirst().orElseThrow());
        digitToSegments.put(7, inputs.stream().filter(i -> i.length() == 3).findFirst().orElseThrow());
        digitToSegments.put(8, inputs.stream().filter(i -> i.length() == 7).findFirst().orElseThrow());

        Set<Character> letters4 = Utils.getLetterCount(digitToSegments.get(4)).keySet();
        Set<Character> letters7 = Utils.getLetterCount(digitToSegments.get(7)).keySet();

        // Digit 6 is the only one of length 6 that does not fully contain segments of digit 7
        digitToSegments.put(6, inputs.stream().filter(i -> i.length() == 6)
            .filter(i -> !Utils.getLetterCount(i).keySet().containsAll(letters7)).findFirst().orElseThrow());
        // Digit 9 fully contains segments of digit 4, while 0 doesn't
        digitToSegments.put(9, inputs.stream().filter(i -> i.length() == 6 && !digitToSegments.containsValue(i))
            .filter(i -> Utils.getLetterCount(i).keySet().containsAll(letters4)).findFirst().orElseThrow());
        // Digit 0 is the last of length 6
        digitToSegments.put(0, inputs.stream().filter(i -> i.length() == 6 && !digitToSegments.containsValue(i)).findFirst().orElseThrow());

        Set<Character> letters6 = Utils.getLetterCount(digitToSegments.get(6)).keySet();
        Set<Character> letters9 = Utils.getLetterCount(digitToSegments.get(9)).keySet();

        // Digit 2 is the only one of length 5 that is not fully contained by segments of digit 9
        digitToSegments.put(2, inputs.stream().filter(i -> i.length() == 5)
            .filter(i -> !letters9.containsAll(Utils.getLetterCount(i).keySet())).findFirst().orElseThrow());
        // Digit 5 is fully contained by segments of digit 6, while 3 doesn't
        digitToSegments.put(5, inputs.stream().filter(i -> i.length() == 5 && !digitToSegments.containsValue(i))
            .filter(i -> letters6.containsAll(Utils.getLetterCount(i).keySet())).findFirst().orElseThrow());
        // Digit 3 is the last digit
        digitToSegments.put(3, inputs.stream().filter(i -> !digitToSegments.containsValue(i)).findFirst().orElseThrow());
        return digitToSegments;
    }
}
