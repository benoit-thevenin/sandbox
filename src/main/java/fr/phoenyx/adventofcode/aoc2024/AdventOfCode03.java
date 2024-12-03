package fr.phoenyx.adventofcode.aoc2024;

import fr.phoenyx.models.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdventOfCode03 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode03.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode03.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            StringBuilder program = new StringBuilder();
            while ((currentLine = reader.readLine()) != null) program.append(currentLine);
            Map.Entry<Long, Long> result = getSumOfMulOps(program.toString());
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Map.Entry<Long, Long> getSumOfMulOps(String line) {
        long sum1 = 0;
        long sum2 = 0;
        List<Range> doRanges = getDoRanges(line);
        Pattern mul = Pattern.compile("(mul\\([0-9]{1,3},[0-9]{1,3}\\))");
        Matcher m =  mul.matcher(line);
        while (m.find()) {
            String mulOp = m.group();
            String[] numbers = mulOp.replace("mul(", "").replace(")", "").split(",");
            long value = Long.parseLong(numbers[0]) * Long.parseLong(numbers[1]);
            sum1 += value;
            if (doRanges.stream().anyMatch(r -> r.isInRange(line.indexOf(mulOp)))) sum2 += value;
        }
        return new AbstractMap.SimpleEntry<>(sum1, sum2);
    }

    private static List<Range> getDoRanges(String line) {
        List<Range> doRanges = new ArrayList<>();
        int doStart = 0;
        while (doStart != -1) {
            int doEnd = line.indexOf("don't()", doStart);
            if (doEnd == -1) {
                doRanges.add(Range.buildFromStartAndEnd(doStart, Long.MAX_VALUE));
                break;
            }
            doRanges.add(Range.buildFromStartAndEnd(doStart, doEnd));
            doStart = line.indexOf("do()", doEnd);
        }
        return doRanges;
    }
}
