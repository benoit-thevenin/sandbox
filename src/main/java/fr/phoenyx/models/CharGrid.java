package fr.phoenyx.models;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CharGrid extends AbstractGrid {
    public final char[][] grid;

    public CharGrid(int width, int height) {
        super(width, height);
        grid = new char[width][height];
    }

    public CharGrid(List<String> lines) {
        super(lines);
        grid = new char[width][height];
        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            for (int j = 0; j < width; j++) grid[j][i] = line.charAt(j);
        }
    }

    public void copyGrid(char[][] next) {
        if (next.length != width) throw new IllegalArgumentException("Widths do not match");
        if (next[0].length != height) throw new IllegalArgumentException("Heights do not match");
        for (int i = 0; i < width; i++) System.arraycopy(next[i], 0, grid[i], 0, height);
    }

    public List<Coord2> getCoordinatesMatching(Function<Character, Boolean> matcher) {
        List<Coord2> coords = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) if (matcher.apply(grid[i][j])) coords.add(new Coord2(i, j));
        }
        return coords;
    }

    public List<Set<Coord2>> getAllRegionsMatching(Function<Character, Boolean> matcher) {
        List<Set<Coord2>> regions = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Coord2 pos = new Coord2(i, j);
                if (matcher.apply(grid[i][j]) && regions.stream().noneMatch(region -> region.contains(pos))) regions.add(getRegionMatching(pos, Character::equals));
            }
        }
        return regions;
    }

    public Set<Coord2> getRegionMatching(Coord2 start, BiFunction<Character, Character, Boolean> matcher) {
        if (!isInGrid(start)) throw new IllegalArgumentException(start + " is not within width and height bounds !");
        char c = grid[start.x][start.y];
        Queue<Coord2> toVisit = new LinkedList<>();
        toVisit.add(start);
        Set<Coord2> region = new HashSet<>(toVisit);
        while (!toVisit.isEmpty()) {
            Coord2 current = toVisit.remove();
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                Coord2 next = current.move(dir);
                if (isInGrid(next) && matcher.apply(c, grid[next.x][next.y]) && region.add(next)) toVisit.add(next);
            }
        }
        return region;
    }

    public Map<Coord2, Integer> getDistancesMatching(Coord2 start, BiFunction<Character, Character, Boolean> matcher) {
        if (!isInGrid(start)) throw new IllegalArgumentException(start + " is not within width and height bounds !");
        Map<Coord2, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        Queue<Coord2> toVisit = new LinkedList<>(distances.keySet());
        while (!toVisit.isEmpty()) {
            Coord2 current = toVisit.remove();
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                Coord2 next = current.move(dir);
                if (isInGrid(next) && matcher.apply(grid[current.x][current.y], grid[next.x][next.y]) && !distances.containsKey(next)) {
                    toVisit.add(next);
                    distances.put(next, distances.get(current) + 1);
                }
            }
        }
        return distances;
    }
}
