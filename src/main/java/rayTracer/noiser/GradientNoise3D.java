package rayTracer.noiser;

import rayTracer.enums.NoiseDimensionEnum;
import rayTracer.enums.NoiseRangeEnum;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;

public class GradientNoise3D extends Noise{
    private final Vector3D[][][] grid;

    public GradientNoise3D(int scale) {
        super(scale, NoiseDimensionEnum.DIMENSION_3D);
        grid = new Vector3D[scale + 1][scale + 1][scale + 1];
        generateGrid();
    }

    private void generateGrid() {
        for(int z = 0; z <= scale; ++z) {
            for (int y = 0; y <= scale; ++y) {
                for (int x = 0; x <= scale; ++x) {
                    grid[z][y][x] = Vector3D.generateRandom3DVector();
                }
            }
        }
    }

    private double lerp(double v1, double v2, double iPos) {
        double fade = iPos * iPos * (3 - 2 * iPos);
        return v1 + fade * (v2 - v1); // smooth
        //return (1.0 - iPos)*v1 + iPos*v2; // not smooth
    }

    @Override
    public double getValue(double x, double y) {
        return 0;
    }

    public double getValue(double x, double y, double z) {
        double localX, localY, localZ;
        int X, Y, Z;

        localX = x * scale;
        localY = y * scale;
        localZ = z * scale;
        X = (int)Math.floor(localX);
        Y = (int)Math.floor(localY);
        Z = (int)Math.floor(localZ);

        Vector3D pa, pb, pc, pd, pe, pf, pg, ph;

        double da, db, dc, dd, de, df, dg, dh;

        pa = new Vector3D(localX - X, localY - Y, localZ - Z);
        pb = new Vector3D(localX - (X + 1), localY - Y, localZ - Z);
        pc = new Vector3D(localX - X, localY - (Y + 1), localZ - Z);
        pd = new Vector3D(localX - (X + 1), localY - (Y + 1), localZ - Z);
        pe = new Vector3D(localX - X, localY - Y, localZ - (Z + 1));
        pf = new Vector3D(localX - (X + 1), localY - Y, localZ - (Z + 1));
        pg = new Vector3D(localX - X, localY - (Y + 1), localZ - (Z + 1));
        ph = new Vector3D(localX - (X + 1), localY - (Y + 1), localZ - (Z + 1));

        da = Vector3D.dotProduct(grid[Z][Y][X], pa);
        db = Vector3D.dotProduct(grid[Z][Y][X + 1], pb);
        dc = Vector3D.dotProduct(grid[Z][Y + 1][X], pc);
        dd = Vector3D.dotProduct(grid[Z][Y + 1][X + 1], pd);
        de = Vector3D.dotProduct(grid[Z + 1][Y][X], pe);
        df = Vector3D.dotProduct(grid[Z + 1][Y][X + 1], pf);
        dg = Vector3D.dotProduct(grid[Z + 1][Y + 1][X], pg);
        dh = Vector3D.dotProduct(grid[Z + 1][Y + 1][X + 1], ph);

        //return Math.abs(Math.sin(4 * Math.PI * ((localX + localY) / 2)));
        localX = localX % 1.0;
        localY = localY % 1.0;
        localZ = localZ % 1.0;

        double t1 = lerp(da, de, localZ);
        double t2 = lerp(db, df, localZ);
        double t3 = lerp(dc, dg, localZ);
        double t4 = lerp(dd, dh, localZ);

        double t5 = lerp(t1, t3, localY);
        double t6 = lerp(t2, t4, localY);

        return amplitude * lerp(t5, t6, localX) + (noise != null ? noise.getValue(x, y, z) : 0.0);
    }
}
