package fr.phoenyx.models.coords;

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

    public static final List<Dir> FOUR_NEIGHBOURS_VALUES = List.of(N, E, S, W);

    public final int dx;
    public final int dy;

    Dir(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static Dir fromChar(char c) {
        if (c == 'U' || c == 'N' || c == '^') return N;
        if (c == 'R' || c == 'E' || c == '>') return E;
        if (c == 'D' || c == 'S' || c == 'v') return S;
        if (c == 'L' || c == 'W' || c == '<') return W;
        throw new IllegalArgumentException("Unknown Dir: " + c);
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

    public Dir fourNeighboursTurn(char turn) {
        if (turn == 'R') return fourNeighboursTurnRight();
        if (turn == 'L') return fourNeighboursTurnLeft();
        throw new IllegalArgumentException("Unknown turn: " + turn);
    }

    public Dir fourNeighboursTurnRight() {
        if (this == N) return E;
        if (this == E) return S;
        return this == S ? W : N;
    }

    public Dir fourNeighboursTurnLeft() {
        if (this == N) return W;
        if (this == E) return N;
        return this == S ? E : S;
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
            for (Dir dir : FOUR_NEIGHBOURS_VALUES) if (dir != this && dir != opposite) possibleDirs.add(dir);
        } else for (Dir dir : FOUR_NEIGHBOURS_VALUES) if (dir != opposite) possibleDirs.add(dir);
        return possibleDirs;
    }
}
