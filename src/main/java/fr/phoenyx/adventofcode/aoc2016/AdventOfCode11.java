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
                addNextStates(nextStates, nextElevatorIndexes, List.of(microchip), List.of(true));
                if (currentFloor.microchips.size() > 1) {
                    for (String secondMicrochip : currentFloor.microchips) {
                        if (secondMicrochip.equals(microchip)) continue;
                        addNextStates(nextStates, nextElevatorIndexes, List.of(microchip, secondMicrochip), List.of(true, true));
                    }
                }
                currentFloor.generators.stream().filter(g -> g.equals(microchip)).findFirst()
                    .ifPresent(s -> addNextStates(nextStates, nextElevatorIndexes, List.of(microchip, s), List.of(true, false)));
            }
        }

        private void addGeneratorsMoveStates(Set<State> nextStates, List<Integer> nextElevatorIndexes, Floor currentFloor) {
            for (String generator : currentFloor.generators) {
                addNextStates(nextStates, nextElevatorIndexes, List.of(generator), List.of(false));
                if (currentFloor.generators.size() > 1) {
                    for (String secondGenerator : currentFloor.generators) {
                        if (secondGenerator.equals(generator)) continue;
                        addNextStates(nextStates, nextElevatorIndexes, List.of(generator, secondGenerator), List.of(false, false));
                    }
                }
            }
        }

        private void addNextStates(Set<State> nextStates, List<Integer> nextElevatorIndexes, List<String> names, List<Boolean> isMicrochip) {
            for (int nextElevatorIndex : nextElevatorIndexes) {
                List<Floor> nextFloors = new ArrayList<>(floors.stream().map(Floor::new).toList());
                for (int i = 0; i < names.size(); i++) {
                    if (isMicrochip.get(i)) {
                        nextFloors.get(elevatorIndex).microchips.remove(names.get(i));
                        nextFloors.get(nextElevatorIndex).microchips.add(names.get(i));
                    } else {
                        nextFloors.get(elevatorIndex).generators.remove(names.get(i));
                        nextFloors.get(nextElevatorIndex).generators.add(names.get(i));
                    }
                }
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
            LOGGER.info("PART 1: {}, time elapsed: {}ms", getMinStepsToLastFloor(floors), (System.nanoTime() - begin) / 1000000);
            floors.get(0).microchips.addAll(List.of("a", "b"));
            floors.get(0).generators.addAll(List.of("a", "b"));
            begin = System.nanoTime();
            LOGGER.info("PART 2: {}, time elapsed: {}ms", getMinStepsToLastFloor(floors), (System.nanoTime() - begin) / 1000000);
        }
    }

    private static int getMinStepsToLastFloor(List<Floor> initialFloors) {
        int steps = 0;
        Set<State> current = new HashSet<>();
        current.add(new State(0, initialFloors));
        Set<State> visited = new HashSet<>(current);
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
