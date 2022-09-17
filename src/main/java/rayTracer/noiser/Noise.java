package rayTracer.noiser;

import rayTracer.enums.NoiseDimensionEnum;
import rayTracer.enums.NoiseRangeEnum;
import rayTracer.math.Vector3D;

public abstract class Noise {
    public Noise noise;
    protected int scale;
    public double amplitude;
    public NoiseDimensionEnum dimension;

    protected Noise(int scale, NoiseDimensionEnum dimension) {
        this.scale = scale;
        this.dimension = dimension;
    }

    public NoiseRangeEnum range = NoiseRangeEnum.BOUNDARY;

    public long xorShift64(long a) {
        a ^= (a << 21);
        a ^= (a >>> 35);
        a ^= (a << 4);
        return a;
    }

    protected Vector3D getVectorFromCoordinates(int x, int y) {
        int signA = 1;
        int signB = 1;
        int a = (x) * 15485863 * (x + y);
        int b = (y) * 15485863 * (x + y);
        if(a * 15485863 % 2 == 0)
            signA = -1;
        if(b * 15485863 % 2 == 0)
            signB = -1;
        double A = (double)((a * a * a) % 2038074743) / 2038074743; //Will return in range 0 to 1 if seed >= 0 and -1 to 0 if seed < 0.
        double B = (double)((b * b * b) % 2038074743) / 2038074743; //Will return in range 0 to 1 if seed >= 0 and -1 to 0 if seed < 0.
        Vector3D v = new Vector3D(A * signA, B * signB, 0);
        v.normalize();
        return v;
    }
    abstract public double getValue(double x, double y);
    abstract public double getValue(double x, double y, double z);
}
