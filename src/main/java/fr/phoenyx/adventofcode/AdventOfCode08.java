package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode08 {

    private static class Node {
        String name;
        String leftNodeName;
        String rightNodeName;

        Node(String line) {
            String[] split1 = line.split(" = ");
            name = split1[0];
            split1[1] = split1[1].replace("(", "");
            split1[1] = split1[1].replace(")", "");
            String[] split2 = split1[1].split(", ");
            leftNodeName = split2[0];
            rightNodeName = split2[1];
        }

        String getNeighbourName(char instruction) {
            if (instruction == 'L') return leftNodeName;
            return rightNodeName;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    private static String instructions;
    private static final Map<String, Node> nodes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            boolean isInstructionsLine = true;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isBlank()) isInstructionsLine = false;
                else if (isInstructionsLine) instructions = currentLine;
                else {
                    Node node = new Node(currentLine);
                    nodes.put(node.name, node);
                }
            }
            LOGGER.info("PART 1: {}", computePart1());
            LOGGER.info("PART 2: {}", computePart2());
        }
    }

    private static int computePart1() {
        int count = 0;
        Node currentNode = nodes.get("AAA");
        while (!"ZZZ".equals(currentNode.name)) {
            currentNode = nodes.get(currentNode.getNeighbourName(instructions.charAt(count % instructions.length())));
            count++;
        }
        return count;
    }

    private static long computePart2() {
        List<Node> currentNodes = nodes.values().stream().filter(n -> n.name.charAt(2) == 'A').toList();
        long[] count = new long[currentNodes.size()];
        for (int i = 0; i < count.length; i++) {
            Node currentNode = currentNodes.get(i);
            while (currentNode.name.charAt(2) != 'Z') {
                currentNode = nodes.get(currentNode.getNeighbourName(instructions.charAt((int) count[i] % instructions.length())));
                count[i]++;
            }
        }
        return getPpcm(count);
    }

    private static long getPpcm(long... count) {
        long min = Long.MAX_VALUE;
        for (long l : count) if (l < min) min = l;
        List<Long> primeNumbers = getPrimeNumbers(min / 2);
        long result = 1;
        for (long prime : primeNumbers) {
            long max = 1;
            for (long l : count) {
                long value = 1;
                while ((l / value) % prime == 0) value *= prime;
                if (value > max) max = value;
            }
            result *= max;
        }
        return result;
    }

    private static List<Long> getPrimeNumbers(long max) {
        List<Long> primeNumbers = new ArrayList<>();
        for (long i = 2L; i <= max; i++) {
            long finalI = i;
            if (primeNumbers.stream().noneMatch(l -> finalI % l == 0)) primeNumbers.add(i);
        }
        return primeNumbers;
    }
}
