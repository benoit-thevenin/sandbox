package fr.phoenyx.adventofcode.aoc2015;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.utils.MathUtils;

public class AdventOfCode13 {

    private record Person(String name, Map<String, Integer> affinities) {}

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);
    private static final List<Person> persons = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2015/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Map<String, Person> personsByName = new HashMap<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.replace(".", "").split(" ");
                personsByName.putIfAbsent(split[0], new Person(split[0], new HashMap<>()));
                Person current = personsByName.get(split[0]);
                int affinity = "gain".equals(split[2]) ? Integer.parseInt(split[3]) : -Integer.parseInt(split[3]);
                current.affinities.put(split[10], affinity);
            }
            persons.addAll(personsByName.values());
            LOGGER.info("PART 1: {}", getOptimalAffinities());
            addMyself();
            LOGGER.info("PART 2: {}", getOptimalAffinities());
        }
    }

    private static int getOptimalAffinities() {
        int[] indexes = new int[persons.size()];
        for (int i = 0; i < indexes.length; i++) indexes[i] = i;
        return MathUtils.getAllPermutations(indexes).stream()
            .map(AdventOfCode13::getAffinity)
            .max(Integer::compare).orElseThrow();
    }

    private static int getAffinity(int[] indexes) {
        Person[] permutation = new Person[persons.size()];
        for (int i = 0; i < indexes.length; i++) permutation[indexes[i]] = persons.get(i);
        int affinity = 0;
        for (int i = 0; i < permutation.length; i++) {
            Person current = permutation[i];
            affinity += current.affinities.get(permutation[(i + 1) % permutation.length].name);
            affinity += current.affinities.get(permutation[(i + permutation.length - 1) % permutation.length].name);
        }
        return affinity;
    }

    private static void addMyself() {
        Person me = new Person("me", new HashMap<>());
        for (Person person : persons) {
            person.affinities.put(me.name, 0);
            me.affinities.put(person.name, 0);
        }
        persons.add(me);
    }
}
