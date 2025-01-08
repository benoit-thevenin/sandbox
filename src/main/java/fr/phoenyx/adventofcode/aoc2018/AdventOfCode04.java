package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode04 {

    private record ShiftEntry(LocalDateTime eventTime, String event) implements Comparable<ShiftEntry> {
        @Override
        public int compareTo(ShiftEntry other) {
            return eventTime.compareTo(other.eventTime);
        }
    }

    private record Guard(int id, List<ShiftEntry> shifts, Map<Integer, Integer> minutesAsleep) {
        void setMinutesAsleep() {
            for (int i = 0; i < 60; i++) minutesAsleep.put(i, 0);
            for (int i = 0; i < shifts.size() - 1; i++)
                if (shifts.get(i).event.contains("asleep"))
                    for (int j = shifts.get(i).eventTime.getMinute(); j < shifts.get(i + 1).eventTime.getMinute(); j++) minutesAsleep.put(j, minutesAsleep.get(j) + 1);
        }

        int getMinutesAsleep() {
            return minutesAsleep.values().stream().reduce(0, Integer::sum);
        }

        int getMinuteMostAsleepKey() {
            return minutesAsleep.entrySet().stream()
                .max(Comparator.comparingInt(Entry::getValue))
                .map(Entry::getKey).orElseThrow();
        }

        int getMinuteMostAsleepValue() {
            return minutesAsleep.entrySet().stream()
                .max(Comparator.comparingInt(Entry::getValue))
                .map(Entry::getValue).orElseThrow();
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode04.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode04.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<ShiftEntry> shiftEntries = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split("] ");
                shiftEntries.add(new ShiftEntry(LocalDateTime.parse(split[0].substring(1), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), split[1]));
            }
            Collections.sort(shiftEntries);
            Collection<Guard> guards = buildGuards(shiftEntries);
            LOGGER.info("PART 1: {}", guards.stream().max(Comparator.comparingInt(Guard::getMinutesAsleep)).map(g -> g.id * g.getMinuteMostAsleepKey()).orElseThrow());
            LOGGER.info("PART 2: {}", guards.stream().max(Comparator.comparingInt(Guard::getMinuteMostAsleepValue)).map(g -> g.id * g.getMinuteMostAsleepKey()).orElseThrow());
        }
    }

    private static Collection<Guard> buildGuards(List<ShiftEntry> shiftEntries) {
        Map<Integer, Guard> guards = new HashMap<>();
        Guard current = null;
        for (ShiftEntry entry : shiftEntries) {
            if (entry.event.startsWith("Guard")) {
                int id = Integer.parseInt(entry.event.split(" ")[1].substring(1));
                if (guards.containsKey(id)) current = guards.get(id);
                else {
                    current = new Guard(id, new ArrayList<>(), new HashMap<>());
                    guards.put(id, current);
                }
            }
            if (current == null) throw new IllegalArgumentException("List is not correctly ordered");
            current.shifts.add(entry);
        }
        guards.values().forEach(Guard::setMinutesAsleep);
        return guards.values();
    }
}
