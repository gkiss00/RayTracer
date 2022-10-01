package rayTracer.blackSpaces;

import rayTracer.math.Line3D;
import rayTracer.math.Point3D;

public class BlackTube implements BlackSpace {
    private final Line3D line;
    private final double radius;

    public BlackTube(Line3D line, double radius) {
        this.line = new Line3D(line);
        this.radius = radius;
    }

    @Override
    public boolean contains(Point3D point) {
        return Line3D.distanceBetween(line, point) < radius;
    }

    @Override
    public double getSize() {
        return this.radius;
    }

    @Override
    public Point3D getPoint() {
        return this.line.getPoint();
    }
}
