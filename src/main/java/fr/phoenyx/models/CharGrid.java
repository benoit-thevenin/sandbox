package fr.phoenyx.models;

import fr.phoenyx.models.coords.Coord2;

import java.util.ArrayList;
import java.util.List;

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

    public List<Coord2> getCoordinatesMatching(char c) {
        List<Coord2> coords = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) if (grid[i][j] == c) coords.add(new Coord2(i, j));
        }
        return coords;
    }
}
