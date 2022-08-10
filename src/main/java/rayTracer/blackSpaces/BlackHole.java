package rayTracer.blackSpaces;

import rayTracer.math.Point3D;

public class BlackHole implements BlackSpace {
    private final Point3D center;
    private final double radius;

    public BlackHole(Point3D point, double radius) {
        this.center = new Point3D(point);
        this.radius = radius;
    }

    @Override
    public boolean contains(Point3D point) {
        return Point3D.distanceBetween(this.center, point) < radius;
    }

    @Override
    public double getSize() {
        return this.radius;
    }

    @Override
    public Point3D getPoint() {
        return this.center;
    }
}
