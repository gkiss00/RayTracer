package rayTracer.utils;

import lombok.Setter;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.Obj;
import rayTracer.objects.baseObjects.BaseObject;

@Setter()
public class Intersection{
    private Point3D pointOfIntersection;
    private Vector3D normal;
    private Color color;
    private double distanceFromCamera;
    private double reflectionRatio;
    private Obj object;

    public Intersection(Point3D pointOfIntersection, Vector3D normal, Color color, double distanceFromCamera, double reflectionRatio, Obj object) {
        this.pointOfIntersection = pointOfIntersection;
        this.normal = normal;
        this.color = color;
        this.distanceFromCamera = distanceFromCamera;
        this.reflectionRatio = reflectionRatio;
        this.object = object;
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

    public Obj getObject() {
        return object;
    }
}
