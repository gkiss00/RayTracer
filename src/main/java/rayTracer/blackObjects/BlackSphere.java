package rayTracer.blackObjects;

import rayTracer.math.Point3D;

public class BlackSphere implements BaseBlackObject{
    private Point3D center;
    private double radius;

    public BlackSphere(Point3D point, double radius) {
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
