package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode08 {

    private static class Node {
        final Node ancestor;
        final List<Node> children = new ArrayList<>();
        final List<Integer> metadata = new ArrayList<>();

        Node(Node ancestor) {
            this.ancestor = ancestor;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode08.class);
    private static final Map<Node, Integer> VALUES = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode08.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Node> tree = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) buildTree(Arrays.stream(currentLine.split(" ")).map(Integer::parseInt).toList(), 0, null, tree);
            tree.stream().filter(node -> node.ancestor != null).forEach(node -> node.ancestor.children.add(node));
            LOGGER.info("PART 1: {}", tree.stream().flatMap(n -> n.metadata.stream()).reduce(0, Integer::sum));
            LOGGER.info("PART 2: {}", getValue(tree.get(0)));
        }
    }

    private static int buildTree(List<Integer> entries, int index, Node ancestor, List<Node> tree) {
        int childQuantity = entries.get(index);
        int metadataQuantity = entries.get(index + 1);
        index += 2;
        Node node = new Node(ancestor);
        tree.add(node);
        for (int i = 0; i < childQuantity; i++) index = buildTree(entries, index, node, tree);
        for (int i = 0; i < metadataQuantity; i++) node.metadata.add(entries.get(index + i));
        return index + metadataQuantity;
    }

    private static int getValue(Node node) {
        if (VALUES.containsKey(node)) return VALUES.get(node);
        int value = node.children.isEmpty() ? node.metadata.stream().reduce(0, Integer::sum) : node.metadata.stream().filter(index -> node.children.size() >= index).reduce(0, (acc, index) -> acc + getValue(node.children.get(index - 1)));
        VALUES.put(node, value);
        return value;
    }
}
