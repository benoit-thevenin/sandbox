package fr.phoenyx.models;

public class Coord {
    public final int x;
    public final int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord move(Dir dir) {
        return new Coord(x + dir.dx, y + dir.dy);
    }

    public int manhattanDistanceTo(Coord other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Coord other)) return false;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return x + (1 << 16) * y;
    }
}
