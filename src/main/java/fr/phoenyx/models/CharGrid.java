package fr.phoenyx.models;

import java.util.List;

public class CharGrid extends AbstractGrid {
    public final char[][] grid;

    public CharGrid(List<String> lines) {
        super(lines);
        grid = new char[width][height];
        for (int i = 0; i < height; i++) {
            String line = lines.get(i);
            for (int j = 0; j < width; j++) grid[j][i] = line.charAt(j);
        }
    }
}
