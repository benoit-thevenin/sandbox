package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode07 {

    private static class Program {
        String name;
        int weight;
        Set<String> holding;
        Program holder;

        Program(String name, int weight, String... holding) {
            this.name = name;
            this.weight = weight;
            this.holding = Set.of(holding);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Program other)) return false;
            return name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode07.class);
    private static final Map<String, Program> programs = new HashMap<>();
    private static final Map<Program, Integer> subTowerWeight = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode07.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" -> ");
                String[] programParams = split[0].split(" \\(");
                int weight = Integer.parseInt(programParams[1].split("\\)")[0]);
                if (split.length == 1) programs.put(programParams[0], new Program(programParams[0], weight));
                else programs.put(programParams[0], new Program(programParams[0], weight, split[1].split(", ")));
            }
            programs.values().forEach(program -> {
                program.holding.forEach(name -> programs.get(name).holder = program);
                subTowerWeight.putIfAbsent(program, getSubTowerWeight(program));
            });
            LOGGER.info("PART 1: {}", programs.values().stream().filter(program -> program.holder == null).findAny().orElseThrow().name);
            LOGGER.info("PART 2: {}", getBalancedWeight());
        }
    }

    private static int getSubTowerWeight(Program program) {
        if (subTowerWeight.containsKey(program)) return subTowerWeight.get(program);
        if (program.holding.isEmpty()) subTowerWeight.put(program, program.weight);
        else subTowerWeight.put(program, program.weight + programs.values().stream().filter(p -> program.holding.contains(p.name)).map(AdventOfCode07::getSubTowerWeight).reduce(0, Integer::sum));
        return subTowerWeight.get(program);
    }

    private static int getBalancedWeight() {
        for (Program program : programs.values()) {
            Set<Program> hold = program.holding.stream().map(programs::get).collect(Collectors.toSet());
            if (!isBalanced(program) && hold.stream().allMatch(AdventOfCode07::isBalanced)) {
                int expectedWeight = hold.stream().map(subTowerWeight::get).filter(w -> hold.stream().map(subTowerWeight::get).filter(w::equals).count() > 1).findAny().orElseThrow();
                Program toBalance = hold.stream().filter(p -> subTowerWeight.get(p) != expectedWeight).findAny().orElseThrow();
                return toBalance.weight + expectedWeight - subTowerWeight.get(toBalance);
            }
        }
        throw new IllegalArgumentException("Wrong dataset");
    }

    private static boolean isBalanced(Program program) {
        return program.holding.stream().map(programs::get).map(subTowerWeight::get).distinct().count() <= 1;
    }
}
