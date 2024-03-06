package fr.phoenyx.models.coords;

public class MovingCoord3 extends Coord3 {
    public final int vx;
    public final int vy;
    public final int vz;

    public MovingCoord3(int x, int y, int z, int vx, int vy, int vz) {
        super(x, y, z);
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public MovingCoord3 move() {
        return new MovingCoord3(x + vx, y + vy, z + vz, vx, vy, vz);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MovingCoord3 other)) return false;
        return x == other.x && y == other.y && z == other.z && vx == other.vx && vy == other.vy && vz == other.vz;
    }

    @Override
    public int hashCode() {
        return x + (1 << 5) * y + (1 << 11) * z + (1 << 16) * vx + (1 << 22) * vy + (1 << 27) * vz;
    }
}
