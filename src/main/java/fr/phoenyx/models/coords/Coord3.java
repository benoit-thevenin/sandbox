package fr.phoenyx.models.coords;

public class Coord3 {
    public final int x;
    public final int y;
    public final int z;

    public Coord3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int hexDistanceTo(Coord3 other) {
        return (Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z)) / 2;
    }

    public double distanceTo(Coord3 other) {
        return Math.sqrt(Math.pow((x - other.x), 2) + Math.pow((y - other.y), 2) + Math.pow((z - other.z), 2));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Coord3 other)) return false;
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public int hashCode() {
        return x + (1 << 10) * y + (1 << 21) * z;
    }
}
