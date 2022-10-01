package rayTracer.blackSpaces;

import rayTracer.math.Point3D;

public interface BlackSpace {
    boolean contains(Point3D point);
    double getSize();
    Point3D getPoint();
}
