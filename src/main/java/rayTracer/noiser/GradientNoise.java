package rayTracer.noiser;

import rayTracer.enums.NoiseDimensionEnum;
import rayTracer.enums.NoiseRangeEnum;
import rayTracer.math.Vector3D;

public class GradientNoise extends Noise{
    private final Vector3D[][] grid;

    public GradientNoise(int scale) {
        super(scale, NoiseDimensionEnum.DIMENSION_2D);
        grid = new Vector3D[scale][scale];
        generateGrid();
    }

    private void generateGrid() {
        for (int y = 0; y < scale; ++y) {
            for (int x = 0; x < scale; ++x) {
                grid[y][x] = Vector3D.generateRandom2DVector();
            }
        }
    }

    private double lerp(double v1, double v2, double iPos) {
        double fade = iPos * iPos * (3 - 2 * iPos);
        return v1 + fade * (v2 - v1); // smooth
        //return (1.0 - iPos)*v1 + iPos*v2; // not smooth
    }

    @Override
    public double getValue(double x, double y) { //TODO infinite shit
        double localX = 0, localY = 0;
        int X = 0, Y = 0;

        if(range == NoiseRangeEnum.BOUNDARY) {
            localX = x * scale;
            localY = y * scale;
            X = (int)Math.floor(localX);
            Y = (int)Math.floor(localY);
        } else {
            localX = x;
            localY = y;
            X = (int)x;
            Y = (int)y;
        }
        Vector3D pa, pb, pc, pd;

        double da, db, dc, dd;

        if(range == NoiseRangeEnum.BOUNDARY) {
            pa = new Vector3D(localX - X, localY - Y, 0);
            pb = new Vector3D(localX - (X + 1), localY - Y, 0);
            pc = new Vector3D(localX - X, localY - (Y + 1), 0);
            pd = new Vector3D(localX - (X + 1), localY - (Y + 1), 0);
            da = Vector3D.dotProduct(grid[Y][X], pa);
            db = Vector3D.dotProduct(grid[Y][(X + 1) % scale], pb);
            dc = Vector3D.dotProduct(grid[(Y + 1) % scale][X], pc);
            dd = Vector3D.dotProduct(grid[(Y + 1) % scale][(X + 1) % scale], pd);
        } else {
            pa = new Vector3D(localX - X, localY - Y, 0);
            pb = new Vector3D(localX - (X + 1), localY - Y, 0);
            pc = new Vector3D(localX - X, localY - (Y + 1), 0);
            pd = new Vector3D(localX - (X + 1), localY - (Y + 1), 0);
            da = Vector3D.dotProduct(getVectorFromCoordinates(X, Y), pa);
            db = Vector3D.dotProduct(getVectorFromCoordinates(X + 1, Y), pb);
            dc = Vector3D.dotProduct(getVectorFromCoordinates(X,  + 1), pc);
            dd = Vector3D.dotProduct(getVectorFromCoordinates(X + 1, Y + 1), pd);
        }

        //return Math.abs(Math.sin(4 * Math.PI * ((localX + localY) / 2)));
        localX = localX % 1.0;
        localY = localY % 1.0;

        double t1 = lerp(da, dc, localY);
        double t2 = lerp(db, dd, localY);

        //return amplitude * lerp(t1, t2, localX) + (noise != null ? noise.getValue(x, y) : 0.0);

        return Math.abs(Math.sin(4 * Math.PI * ((localX + localY) / 2)) + amplitude * lerp(t1, t2, localX) + (noise != null ? 2.0 * noise.getValue(x, y) : 0.0));
    }

    @Override
    public double getValue(double x, double y, double z) {
        return 0;
    }
}
