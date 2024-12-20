package fr.phoenyx.adventofcode.aoc2024;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class AdventOfCode16 {

    private record Node(Coord2 pos, Dir dir) {}
    private record State(Node node, int score) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            Map.Entry<Integer, Integer> result = getResult(grid);
            LOGGER.info("PART 1: {}", result.getKey());
            LOGGER.info("PART 2: {}", result.getValue());
        }
    }

    private static Map.Entry<Integer, Integer> getResult(CharGrid grid) {
        PriorityQueue<State> toVisit = new PriorityQueue<>(Comparator.comparingInt(s -> s.score));
        toVisit.add(new State(new Node(grid.getCoordinatesMatching(c -> c == 'S').get(0), Dir.E), 0));
        Set<Node> visited = new HashSet<>();
        Map<State, Set<State>> predecessors = new HashMap<>();
        while (!toVisit.isEmpty()) {
            State current = toVisit.remove();
            if (visited.add(current.node)) {
                if (grid.grid[current.node.pos.x][current.node.pos.y] == 'E')
                    return new AbstractMap.SimpleEntry<>(current.score, getBestPathsTiles(current, predecessors).size());
                getNextStates(grid, current).forEach(next -> {
                    addBinding(predecessors, next, current);
                    toVisit.add(next);
                });
            }
        }
        throw new IllegalArgumentException("No path found");
    }

    private static Set<State> getNextStates(CharGrid grid, State current) {
        Set<State> nextStates = new HashSet<>();
        Coord2 forward = current.node.pos.move(current.node.dir);
        if (grid.isInGrid(forward.x, forward.y) && grid.grid[forward.x][forward.y] != '#')
            nextStates.add(new State(new Node(forward, current.node.dir), current.score + 1));
        nextStates.add(new State(new Node(current.node.pos, current.node.dir.fourNeighboursTurnRight()), current.score + 1000));
        nextStates.add(new State(new Node(current.node.pos, current.node.dir.fourNeighboursTurnLeft()), current.score + 1000));
        return nextStates;
    }

    private static void addBinding(Map<State, Set<State>> map, State key, State value) {
        if (map.containsKey(key)) map.get(key).add(value);
        else map.put(key, new HashSet<>(Set.of(value)));
    }

    private static Set<Coord2> getBestPathsTiles(State endState, Map<State, Set<State>> predecessors) {
        Queue<State> toVisit = new LinkedList<>();
        toVisit.add(endState);
        Set<State> visited = new HashSet<>(toVisit);
        while (!toVisit.isEmpty()) {
            State current = toVisit.remove();
            for (State state : predecessors.getOrDefault(current, Collections.emptySet())) if (visited.add(state)) toVisit.add(state);
        }
        return visited.stream().map(s -> s.node.pos).collect(Collectors.toSet());
    }
}
