package fr.phoenyx.models.coords;

public class Coord2 {
    public final int x;
    public final int y;

    public Coord2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coord2 move(Dir dir) {
        return move(dir, 1);
    }

    public Coord2 move(Dir dir, int steps) {
        return new Coord2(x + steps * dir.dx, y + steps * dir.dy);
    }

    public int manhattanDistanceTo(Coord2 other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Coord2 other)) return false;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return x + (1 << 16) * y;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
