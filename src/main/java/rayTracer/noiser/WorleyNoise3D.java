package rayTracer.noiser;

import rayTracer.enums.NoiseDimensionEnum;
import rayTracer.math.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorleyNoise3D extends Noise{
    private final int deep;
    private final Point3D[][][] grid;

    public WorleyNoise3D(int scale, int deep) {
        super(scale, NoiseDimensionEnum.DIMENSION_3D);
        this.deep = deep;
        this.grid = new Point3D[scale][scale][scale];
        generateGrid();
    }

    private void generateGrid() {
        Random rand = new Random();
        for(int z = 0; z < scale ; ++z) {
            for (int y = 0; y < scale; ++y) {
                for (int x = 0; x < scale; ++x) {
                    grid[z][y][x] = new Point3D(x + rand.nextDouble(), y + rand.nextDouble(), z + rand.nextDouble());
                }
            }
        }
    }

    private void sortDistances(List<Double> distances) {
        int size = distances.size();
        for (int i = 0; i < size - 1; ++i) {
            boolean swapped = false;
            for (int j = 0; j < size - i - 1; ++j) {
                if (distances.get(j) > distances.get(j + 1)) {
                    double tmp = distances.get(j);
                    distances.set(j, distances.get(j + 1));
                    distances.set(j + 1, tmp);
                    swapped = true;
                }
            }
            if(!swapped)
                break;
        }
    }

    @Override
    public double getValue(double x, double y) {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
        // 0 -> 1 to 0 => scale to 0=> 360
        double localX = x * scale;
        double localY = y * scale;
        double localZ = z * scale;

        Point3D point = new Point3D(localX, localY, localZ);

        double distMin = 1000000000;
        double maxDist = -1;
        List<Double> distances = new ArrayList<>();
        for(int p = 0; p < scale; ++p) {
            for (int h = 0; h < scale; ++h) {
                for (int w = 0; w < scale; ++w) {
                    double dist = Point3D.distanceBetween(point, grid[p][h][w]);
                    distances.add(dist);
                    if(dist < distMin) {
                        distMin = dist;
                    }
                    if (dist > maxDist) {
                        maxDist = dist;
                    }
                }
            }
        }

        sortDistances(distances);

        return distances.get(deep - 1) + (noise != null ? noise.getValue(x, y) : 0.0);
    }
}
