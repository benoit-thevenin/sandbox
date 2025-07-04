package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.MathUtils;

public class AdventOfCode08 {

    private record Node(String name, String leftNodeName, String rightNodeName) {
        String getNeighbourName(char instruction) {
            return instruction == 'L' ? leftNodeName : rightNodeName;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);

    private static String instructions = null;
    private static final Map<String, Node> nodes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (instructions == null) instructions = currentLine;
                else if (!currentLine.isBlank()) {
                    String[] split1 = currentLine.split(" = ");
                    split1[1] = split1[1].replace("(", "");
                    split1[1] = split1[1].replace(")", "");
                    String[] split2 = split1[1].split(", ");
                    Node node = new Node(split1[0], split2[0], split2[1]);
                    nodes.put(node.name, node);
                }
            }
            Map<String, Long> count = getStepCounts();
            LOGGER.info("PART 1: {}", count.get("AAA"));
            LOGGER.info("PART 2: {}", count.values().stream().reduce(1L, MathUtils::leastCommonMultiple));
        }
    }

    private static Map<String, Long> getStepCounts() {
        List<Node> currentNodes = nodes.values().stream().filter(n -> n.name.charAt(2) == 'A').toList();
        Map<String, Long> count = new HashMap<>();
        for (Node currentNode : currentNodes) {
            String origin = currentNode.name;
            count.put(origin, 0L);
            while (currentNode.name.charAt(2) != 'Z') {
                currentNode = nodes.get(currentNode.getNeighbourName(instructions.charAt(count.get(origin).intValue() % instructions.length())));
                count.put(origin, count.get(origin) + 1);
            }
        }
        return count;
    }
}
