package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.CharGrid;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode13 {

    private static class Cart implements Comparable<Cart> {
        int x;
        int y;
        Dir dir;
        int turns = 0;

        Cart(int x, int y, Dir dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        void move() {
            x += dir.dx;
            y += dir.dy;
        }

        void turn(char c) {
            if (c == '/') {
                if (dir == Dir.N || dir == Dir.S) dir = dir.fourNeighboursTurnRight();
                else dir = dir.fourNeighboursTurnLeft();
            } else if (c == '\\') {
                if (dir == Dir.N || dir == Dir.S) dir = dir.fourNeighboursTurnLeft();
                else dir = dir.fourNeighboursTurnRight();
            } else if (c == '+') {
                if (turns % 3 == 0) dir = dir.fourNeighboursTurnLeft();
                else if (turns % 3 == 2) dir = dir.fourNeighboursTurnRight();
                turns++;
            }
        }

        @Override
        public int compareTo(Cart o) {
            if (y < o.y) return -1;
            if (y > o.y) return 1;
            if (x < o.x) return -1;
            return x > o.x ? 1 : 0;
        }

        @Override
        public String toString() {
            return String.join(",", Integer.toString(x), Integer.toString(y));
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);
    private static final Set<Character> DIR_CHARS = Set.of('^', '>', 'v', '<');

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String currentLine;
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            CharGrid grid = new CharGrid(lines);
            List<Cart> carts = updateAndExtractCarts(grid);
            List<String> crashes = computeCrashes(grid, carts);
            String firstCrash = crashes.get(0);
            String lastCrash = crashes.get(crashes.size() - 1);
            LOGGER.info("PART 1: {}", firstCrash);
            LOGGER.info("PART 2: {}", lastCrash);
        }
    }

    private static List<Cart> updateAndExtractCarts(CharGrid grid) {
        List<Cart> carts = new ArrayList<>();
        for (int i = 0; i < grid.width; i++) {
            for (int j = 0; j < grid.height; j++) {
                if (DIR_CHARS.contains(grid.grid[i][j])) {
                    Cart cart = new Cart(i, j, Dir.fromChar(grid.grid[i][j]));
                    carts.add(cart);
                    grid.grid[i][j] = cart.dir == Dir.E || cart.dir == Dir.W ? '-' : '|';
                }
            }
        }
        return carts;
    }

    private static List<String> computeCrashes(CharGrid grid, List<Cart> carts) {
        List<String> crashes = new ArrayList<>();
        while (carts.size() != 1) {
            Collections.sort(carts);
            Set<Cart> cartsToRemove = new HashSet<>();
            for (Cart cart : carts) {
                cart.move();
                cart.turn(grid.grid[cart.x][cart.y]);
                if (carts.stream().filter(c -> c.x == cart.x && c.y == cart.y).count() > 1) {
                    crashes.add(cart.toString());
                    cartsToRemove.addAll(carts.stream().filter(c -> c.x == cart.x && c.y == cart.y).toList());
                }
            }
            carts.removeAll(cartsToRemove);
        }
        Cart lastCart = carts.iterator().next();
        crashes.add(lastCart.toString());
        return crashes;
    }
}
