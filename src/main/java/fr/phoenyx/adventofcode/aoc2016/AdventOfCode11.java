package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode11 {

    private record State(int elevatorIndex, List<Floor> floors) {
        Set<State> getNextStates() {
            Set<State> nextStates = new HashSet<>();
            List<Integer> nextElevatorIndexes = new ArrayList<>();
            if (elevatorIndex < 3) nextElevatorIndexes.add(elevatorIndex + 1);
            if (elevatorIndex > 0) nextElevatorIndexes.add(elevatorIndex - 1);
            Floor currentFloor = floors.get(elevatorIndex);
            addMicrochipsMoveStates(nextStates, nextElevatorIndexes, currentFloor);
            addGeneratorsMoveStates(nextStates, nextElevatorIndexes, currentFloor);
            return nextStates;
        }

        private void addMicrochipsMoveStates(Set<State> nextStates, List<Integer> nextElevatorIndexes, Floor currentFloor) {
            for (String microchip : currentFloor.microchips) {
                addSoloMicrochipMoveStates(nextStates, nextElevatorIndexes, microchip);
                if (currentFloor.microchips.size() > 1) {
                    for (String secondMicrochip : currentFloor.microchips) {
                        if (secondMicrochip.equals(microchip)) continue;
                        addPairMicrochipsMoveStates(nextStates, nextElevatorIndexes, microchip, secondMicrochip);
                    }
                }
                currentFloor.generators.stream().filter(g -> g.equals(microchip)).findFirst()
                    .ifPresent(s -> addPairMicrochipGeneratorMoveStates(nextStates, nextElevatorIndexes, microchip, s));
            }
        }

        private void addGeneratorsMoveStates(Set<State> nextStates, List<Integer> nextElevatorIndexes, Floor currentFloor) {
            for (String generator : currentFloor.generators) {
                addSoloGeneratorMoveStates(nextStates, nextElevatorIndexes, generator);
                if (currentFloor.generators.size() > 1) {
                    for (String secondGenerator : currentFloor.generators) {
                        if (secondGenerator.equals(generator)) continue;
                        addPairGeneratorsMoveStates(nextStates, nextElevatorIndexes, generator, secondGenerator);
                    }
                }
            }
        }

        private void addSoloMicrochipMoveStates(Set<State> nextStates, List<Integer> nextElevatorIndexes, String microchip) {
            for (int nextElevatorIndex : nextElevatorIndexes) {
                List<Floor> nextFloors = new ArrayList<>();
                for (Floor floor : floors) nextFloors.add(new Floor(floor));
                nextFloors.get(elevatorIndex).microchips.remove(microchip);
                nextFloors.get(nextElevatorIndex).microchips.add(microchip);
                State state = new State(nextElevatorIndex, nextFloors);
                if (state.isValid()) nextStates.add(state);
            }
        }

        private void addSoloGeneratorMoveStates(Set<State> nextStates, List<Integer> nextElevatorIndexes, String generator) {
            for (int nextElevatorIndex : nextElevatorIndexes) {
                List<Floor> nextFloors = new ArrayList<>();
                for (Floor floor : floors) nextFloors.add(new Floor(floor));
                nextFloors.get(elevatorIndex).generators.remove(generator);
                nextFloors.get(nextElevatorIndex).generators.add(generator);
                State state = new State(nextElevatorIndex, nextFloors);
                if (state.isValid()) nextStates.add(state);
            }
        }

        private void addPairMicrochipsMoveStates(Set<State> nextStates, List<Integer> nextElevatorIndexes, String microchip, String secondMicrochip) {
            for (int nextElevatorIndex : nextElevatorIndexes) {
                List<Floor> nextFloors = new ArrayList<>();
                for (Floor floor : floors) nextFloors.add(new Floor(floor));
                nextFloors.get(elevatorIndex).microchips.remove(microchip);
                nextFloors.get(elevatorIndex).microchips.remove(secondMicrochip);
                nextFloors.get(nextElevatorIndex).microchips.add(microchip);
                nextFloors.get(nextElevatorIndex).microchips.add(secondMicrochip);
                State state = new State(nextElevatorIndex, nextFloors);
                if (state.isValid()) nextStates.add(state);
            }
        }

        private void addPairMicrochipGeneratorMoveStates(Set<State> nextStates, List<Integer> nextElevatorIndexes, String microchip, String generator) {
            for (int nextElevatorIndex : nextElevatorIndexes) {
                List<Floor> nextFloors = new ArrayList<>();
                for (Floor floor : floors) nextFloors.add(new Floor(floor));
                nextFloors.get(elevatorIndex).microchips.remove(microchip);
                nextFloors.get(elevatorIndex).generators.remove(generator);
                nextFloors.get(nextElevatorIndex).microchips.add(microchip);
                nextFloors.get(nextElevatorIndex).generators.add(generator);
                State state = new State(nextElevatorIndex, nextFloors);
                if (state.isValid()) nextStates.add(state);
            }
        }

        private void addPairGeneratorsMoveStates(Set<State> nextStates, List<Integer> nextElevatorIndexes, String generator, String secondGenerator) {
            for (int nextElevatorIndex : nextElevatorIndexes) {
                List<Floor> nextFloors = new ArrayList<>();
                for (Floor floor : floors) nextFloors.add(new Floor(floor));
                nextFloors.get(elevatorIndex).generators.remove(generator);
                nextFloors.get(elevatorIndex).generators.remove(secondGenerator);
                nextFloors.get(nextElevatorIndex).generators.add(generator);
                nextFloors.get(nextElevatorIndex).generators.add(secondGenerator);
                State state = new State(nextElevatorIndex, nextFloors);
                if (state.isValid()) nextStates.add(state);
            }
        }

        boolean isValid() {
            return floors.stream().allMatch(f -> f.generators.isEmpty() || f.generators.containsAll(f.microchips));
        }

        boolean isOver() {
            for (int i = 0; i < 3; i++) if (!floors.get(i).microchips.isEmpty() || !floors.get(i).generators.isEmpty()) return false;
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof State other) || elevatorIndex != other.elevatorIndex) return false;
            return floors.equals(other.floors);
        }

        @Override
        public int hashCode() {
            return Objects.hash(elevatorIndex, floors);
        }
    }

    private static class Floor {
        final Set<String> microchips = new HashSet<>();
        final Set<String> generators = new HashSet<>();

        Floor() {
            super();
        }

        Floor(Floor floor) {
            this();
            this.microchips.addAll(floor.microchips);
            this.generators.addAll(floor.generators);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Floor other)) return false;
            return microchips.size() == other.microchips.size() && generators.size() == other.generators.size();
        }

        @Override
        public int hashCode() {
            return Objects.hash(microchips.size(), generators.size());
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode11.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode11.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Floor> floors = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                Floor floor = new Floor();
                if (!currentLine.contains("nothing")) {
                    String[] content = currentLine.split(" contains a ")[1].split(",? (and )?a ");
                    for (String s : content) {
                        String[] split = s.split(" ");
                        if (split[1].startsWith("generator")) floor.generators.add(split[0]);
                        else floor.microchips.add(split[0].split("-")[0]);
                    }
                }
                floors.add(floor);
            }
            long begin = System.nanoTime();
            LOGGER.info("PART 1: {}, time elpased: {}ms", getMinStepsToLastFloor(floors), (System.nanoTime() - begin) / 1000000);
            floors.get(0).microchips.add("a");
            floors.get(0).generators.add("a");
            floors.get(0).microchips.add("b");
            floors.get(0).generators.add("b");
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elpased: {}ms", getMinStepsToLastFloor(floors), (System.nanoTime() - begin) / 1000000);
        }
    }

    private static int getMinStepsToLastFloor(List<Floor> initialFloors) {
        int steps = 0;
        Set<State> visited = new HashSet<>();
        Set<State> current = new HashSet<>();
        State initialState = new State(0, initialFloors);
        visited.add(initialState);
        current.add(initialState);
        while (!current.isEmpty()) {
            steps++;
            Set<State> next = new HashSet<>();
            for (State state : current) {
                Set<State> nextStates = state.getNextStates();
                if (nextStates.stream().anyMatch(State::isOver)) return steps;
                next.addAll(nextStates);
            }
            next.removeAll(visited);
            visited.addAll(next);
            current = next;
        }
        throw new IllegalArgumentException("Wrong dataset");
    }
}
