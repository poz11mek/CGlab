package CGlab;

public class Vec3i {
    public int x;
    public int y;
    public int z;


    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int get(int i) {
        if(i == 0)
            return x;
        else if(i == 1)
            return y;
        else
            return z;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}