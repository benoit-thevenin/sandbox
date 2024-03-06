package fr.phoenyx.models.coords;

public record FlatTopHexCoord(int x, int y) {
    Coord3 toCoord3() {
        int xp = y - (x - (x & 1)) / 2;
        int zp = x;
        int yp = -(xp + zp);
        return new Coord3(xp, yp, zp);
    }

    public FlatTopHexCoord neighbor(FlatTopHexDir dir) {
        return x % 2 == 0 ? new FlatTopHexCoord(x + dir.evenDx, y + dir.evenDy) : new FlatTopHexCoord(x + dir.oddDx, y + dir.oddDy);
    }

    public int distanceTo(FlatTopHexCoord other) {
        return toCoord3().hexDistanceTo(other.toCoord3());
    }
}
