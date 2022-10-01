package rayTracer.noiser;

import rayTracer.enums.NoiseDimensionEnum;
import rayTracer.math.Point3D;
import rayTracer.utils.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorleyNoise extends Noise{
    private final int deep;
    private final Point3D[][] grid;

    public WorleyNoise(int scale, int deep) {
        super(scale, NoiseDimensionEnum.DIMENSION_2D);
        this.deep = deep;
        grid = new Point3D[scale][scale];
        generateGrid();
    }

    private void generateGrid() {
        Random rand = new Random();
        for (int y = 0; y < scale; ++y) {
            for (int x = 0; x < scale; ++x) {
                grid[y][x] = new Point3D(x + rand.nextDouble(), y + rand.nextDouble(), 0);
            }
        }
        for (int y = 0; y < scale; ++y) {
            grid[y][scale - 1] = grid[y][0];
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
        // 0 -> 1 to 0 => scale to 0=> 360
        double localY = y * scale;

        double angleToLocalX = x * 360.0;

        double distMin = 1000000000;
        double maxDist = -1;
        List<Double> distances = new ArrayList<>();
        for (int h = 0; h < scale; ++h) {
            for (int w = 0; w < scale; ++w) {
                double angleToPointX = (grid[h % scale][w % scale].getX() / scale) * 360.0;
                double angle = Math.abs(angleToLocalX - angleToPointX);
                if(angle > 180)
                    angle = 360.0 - angle;
                double distX = (angle / 360.0) * scale;
                double dist = Math.hypot(distX, grid[h % scale][w % scale].getY() - localY);
                distances.add(dist);
                if(dist < distMin) {
                    distMin = dist;
                }
                if (dist > maxDist) {
                    maxDist = dist;
                }
            }
        }

        sortDistances(distances);

        return distances.get(deep - 1) / 2 + (noise != null ? noise.getValue(x, y) : 0.0);
    }

    @Override
    public double getValue(double x, double y, double z) {
        return 0;
    }
}
