package fr.phoenyx.adventofcode.aoc2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode17 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode17.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2017/adventofcode17.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                int steps = Integer.parseInt(currentLine);
                Deque<Integer> stack = new ArrayDeque<>();
                stack.push(0);
                for (int i = 1; i <= 2017; i++) insertNextNode(stack, steps, i);
                LOGGER.info("PART 1: {}", stack.stream().toList().get(1));
                long begin = System.nanoTime();
                for (int i = 2018; i < 50000000; i++) insertNextNode(stack, steps, i);
                while (stack.getFirst() != 0) rotateDeque(stack, 1);
                rotateDeque(stack, 1);
                LOGGER.info("PART 2: {}, time elapsed: {}ms", stack.getFirst(), (System.nanoTime() - begin) / 1000000);
            }
        }
    }

    private static void insertNextNode(Deque<Integer> stack, int steps, int value) {
        rotateDeque(stack, steps + 1);
        stack.push(value);
    }

    private static void rotateDeque(Deque<Integer> deque, int k) {
        k = k % deque.size();
        for (int i = 0; i < k; i++) deque.addLast(deque.removeFirst());
    }
}
