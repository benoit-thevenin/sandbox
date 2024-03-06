package fr.phoenyx.models.coords;

public enum FlatTopHexDir {
    N(0, -1, 0, -1),
    NE(1, -1, 1, 0),
    SE(1, 0, 1, 1),
    S(0, 1, 0, 1),
    SW(-1, 0, -1, 1),
    NW(-1, -1, -1, 0);

    final int evenDx;
    final int evenDy;
    final int oddDx;
    final int oddDy;

    FlatTopHexDir(int evenDx, int evenDy, int oddDx, int oddDy) {
        this.evenDx = evenDx;
        this.evenDy = evenDy;
        this.oddDx = oddDx;
        this.oddDy = oddDy;
    }
}
