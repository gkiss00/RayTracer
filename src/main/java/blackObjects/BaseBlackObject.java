package blackObjects;

import math.Point3D;

public interface BaseBlackObject {
    public boolean contains(Point3D point);
    public double getSize();
    public Point3D getPoint();
}
