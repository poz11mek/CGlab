package CGlab;

public class Vec3f {
    public float x;
    public float y;
    public float z;

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float get(int i) {
        if(i == 0)
            return x;
        else if(i == 1)
            return y;
        else
            return z;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}