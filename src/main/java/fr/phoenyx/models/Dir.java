package fr.phoenyx.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Dir {
    N(0, -1),
    NE(1, -1),
    E(1, 0),
    SE(1, 1),
    S(0, 1),
    SW(-1, 1),
    W(-1, 0),
    NW(-1, -1);

    public final int dx;
    public final int dy;

    Dir(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static Dir fromChar(char c) {
        if (c == 'U' || c == 'N') return N;
        if (c == 'R' || c == 'E') return E;
        if (c == 'D' || c == 'S') return S;
        if (c == 'L' || c == 'W') return W;
        throw new IllegalArgumentException("Unknown Dir: " + c);
    }

    public static List<Dir> fourNeighboursValues() {
        return List.of(N, E, S, W);
    }

    public Dir getOpposite() {
        if (this == N) return S;
        if (this == NE) return SW;
        if (this == E) return W;
        if (this == SE) return NW;
        if (this == S) return N;
        if (this == SW) return NE;
        return this == W ? E : SE;
    }

    public Set<Dir> getOrthogonals() {
        if (this == E || this == W) return Set.of(N, S);
        if (this == S || this == N) return Set.of(E, W);
        return this == NE || this == SW ? Set.of(NW, SE) : Set.of(NE, SW);
    }

    public Dir getMirroredDir(char tileType) {
        if (tileType != '/' && tileType != '\\') throw new IllegalArgumentException("Can't get mirrored dir when not hitting a mirror");
        if (tileType == '/') {
            if (this == N) return E;
            if (this == E) return N;
            return this == S ? W : S;
        }
        if (this == N) return W;
        if (this == E) return S;
        return this == S ? E : N;
    }

    public List<Dir> getFourNeighboursPossibleDirs(int steps, int minStreak, int maxStreak) {
        if (steps < minStreak) return List.of(this);
        Dir opposite = getOpposite();
        List<Dir> possibleDirs = new ArrayList<>();
        if (steps == maxStreak) {
            for (Dir dir : fourNeighboursValues()) if (dir != this && dir != opposite) possibleDirs.add(dir);
        } else for (Dir dir : fourNeighboursValues()) if (dir != opposite) possibleDirs.add(dir);
        return possibleDirs;
    }
}
