package fr.phoenyx.models;

import java.util.List;

public abstract class AbstractGrid {
    public final int width;
    public final int height;

    protected AbstractGrid(int width, int height) {
        if (width < 0 || height < 0) throw new IllegalArgumentException("Invalid width or height");
        this.width = width;
        this.height = height;
    }

    protected AbstractGrid(List<String> lines) {
        if (lines.isEmpty()) throw new IllegalArgumentException("Can't build an empty CharGrid");
        if (lines.stream().map(String::length).distinct().count() != 1) throw new IllegalArgumentException("All lines must have the same size");
        width = lines.iterator().next().length();
        if (width == 0) throw new IllegalArgumentException("Can't build an empty CharGrid");
        height = lines.size();
    }

    public boolean isInGrid(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }
}
