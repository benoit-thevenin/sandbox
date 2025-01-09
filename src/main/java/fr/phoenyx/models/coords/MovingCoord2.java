package fr.phoenyx.models.coords;

public class MovingCoord2 extends Coord2 {
    public final int vx;
    public final int vy;

    public MovingCoord2(int x, int y, int vx, int vy) {
        super(x, y);
        this.vx = vx;
        this.vy = vy;
    }

    public MovingCoord2 move() {
        return new MovingCoord2(x + vx, y + vy, vx, vy);
    }

    public Coord2 getCoord() {
        return new Coord2(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MovingCoord2 other)) return false;
        return x == other.x && y == other.y && vx == other.vx && vy == other.vy;
    }

    @Override
    public int hashCode() {
        return x + (1 << 8) * y + (1 << 16) * vx + (1 << 24) * vy;
    }
}
