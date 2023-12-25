package fr.phoenyx.adventofcode.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode25 {

    private static class Node {
        final String name;
        List<Link> links = new ArrayList<>();

        Node(String name) {
            this.name = name;
        }
    }

    private record Link(Node node1, Node node2) {
        Node getNeighbour(Node node) {
            return node == node1 ? node2 : node1;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode25.class);
    private static final Map<String, Node> nodes = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2023/adventofcode25.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) processInputLine(currentLine);
            LOGGER.info("PART 1: {}", cutThreeLinks());
            LOGGER.info("PART 2: {}", 0);
        }
    }

    private static void processInputLine(String line) {
        String[] split = line.split(": ");
        Node node = buildOrGetNode(split[0]);
        String[] neighbours = split[1].split(" ");
        for (String s : neighbours) {
            Node neighbour = buildOrGetNode(s);
            Link link = new Link(node, neighbour);
            node.links.add(link);
            neighbour.links.add(link);
        }
    }

    private static Node buildOrGetNode(String name) {
        if (nodes.containsKey(name)) return nodes.get(name);
        Node node = new Node(name);
        nodes.put(name, node);
        return node;
    }

    private static int cutThreeLinks() {
        Set<Link> deletedLinks = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            Link criticalLink = getCriticalLink();
            deletedLinks.add(criticalLink);
            nodes.get(criticalLink.node1.name).links.remove(criticalLink);
            nodes.get(criticalLink.node2.name).links.remove(criticalLink);
        }
        int result = getSubgraphsSize();
        for (Link link : deletedLinks) {
            nodes.get(link.node1.name).links.add(link);
            nodes.get(link.node2.name).links.add(link);
        }
        return result;
    }

    private static Link getCriticalLink() {
        Map<Link, Integer> linksUsage = new HashMap<>();
        for (Node node : nodes.values()) {
            Queue<Node> toVisit = new LinkedList<>();
            Set<Node> visited = new HashSet<>();
            toVisit.add(node);
            visited.add(node);
            while (!toVisit.isEmpty()) {
                Node current = toVisit.remove();
                for (Link link : current.links) {
                    Node neighbour = link.getNeighbour(current);
                    if (!visited.contains(neighbour)) {
                        toVisit.add(neighbour);
                        visited.add(neighbour);
                        if (linksUsage.containsKey(link)) linksUsage.put(link, linksUsage.get(link) + 1);
                        else linksUsage.put(link, 1);
                    }
                }
            }
        }
        return linksUsage.keySet().stream().max(Comparator.comparingInt(linksUsage::get)).orElseThrow();
    }

    private static int getSubgraphsSize() {
        Set<Integer> sizes = new HashSet<>();
        for (Node node : nodes.values()) {
            Queue<Node> toVisit = new LinkedList<>();
            Set<Node> visited = new HashSet<>();
            toVisit.add(node);
            visited.add(node);
            while (!toVisit.isEmpty()) {
                Node current = toVisit.remove();
                for (Link link : current.links) {
                    Node neighbour = link.getNeighbour(current);
                    if (!visited.contains(neighbour)) {
                        toVisit.add(neighbour);
                        visited.add(neighbour);
                    }
                }
            }
            sizes.add(visited.size());
        }
        return sizes.stream().reduce((s1, s2) -> s1 * s2).orElseThrow();
    }
}
