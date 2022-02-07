package rayTracer.utils;

import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;

public class Intersection {
    private Point3D pointOfIntersection;
    private Vector3D normal;
    private Color color;
    private double distanceFromCamera;
    private double reflectionRatio;

    public Intersection(Point3D pointOfIntersection, Vector3D normal, Color color, double distanceFromCamera, double reflectionRatio) {
        this.pointOfIntersection = pointOfIntersection;
        this.normal = normal;
        this.color = color;
        this.distanceFromCamera = distanceFromCamera;
        this.reflectionRatio = reflectionRatio;
    }

    public Point3D getPointOfIntersection() {
        return pointOfIntersection;
    }

    public Vector3D getNormal() {
        return normal;
    }

    public Color getColor() {
        return color;
    }

    public double getDistanceFromCamera() {
        return distanceFromCamera;
    }

    public double getReflectionRatio() {
        return reflectionRatio;
    }
}
