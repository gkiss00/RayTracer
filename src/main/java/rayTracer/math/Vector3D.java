package rayTracer.math;

import java.util.Random;

public class Vector3D {
    private double x;
    private double y;
    private double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Point3D p1, Point3D p2) {
        this.x = p2.getX() - p1.getX();
        this.y = p2.getY() - p1.getY();
        this.z = p2.getZ() - p1.getZ();
    }

    public Vector3D(Vector3D vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getMagnitude() {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void normalize() {
        double magnitude = getMagnitude();
        x /= magnitude;
        y /= magnitude;
        z /= magnitude;
    }

    public void add(Vector3D vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public void sub(Vector3D vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
    }

    public void times(double d) {
        x *= d;
        y *= d;
        z *= d;
    }

    public void inverse() {
        x *= -1;
        y *= -1;
        z *= -1;
    }

    public static Vector3D generateRandom2DVector() {
        Random rand = new Random();
        Vector3D v = new Vector3D(0, 0, 0);
        v.x = rand.nextDouble() - 0.5;
        v.y = rand.nextDouble() - 0.5;
        v.normalize();
        return v;
    }

    public static Vector3D generateRandom3DVector() {
        Random rand = new Random();
        Vector3D v = new Vector3D(0, 0, 0);
        v.x = rand.nextDouble() - 0.5;
        v.y = rand.nextDouble() - 0.5;
        v.z = rand.nextDouble() - 0.5;
        v.normalize();
        return v;
    }

    // return a vector perpendicular to the two others
    public static Vector3D inverse(Vector3D v) {
        return new Vector3D(
                -v.x,
                -v.y,
                -v.z
        );
    }

    public static double scalarProduct(Vector3D v1, Vector3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static double dotProduct(Vector3D v1, Vector3D v2) {
        return scalarProduct(v1, v2);
    }

    // return a vector perpendicular to the two others
    public static Vector3D crossProduct(Vector3D v1, Vector3D v2) {
        return new Vector3D(
                v1.y * v2.z - v2.y * v1.z,
                -v1.x * v2.z + v2.x * v1.z,
                v1.x * v2.y - v2.x * v1.y
        );
    }

    // return the angle between the two vector in degrees
    public static double angleBetween(Vector3D v1, Vector3D v2) {
        return Math.toDegrees(Math.acos(scalarProduct(v1, v2) / (v1.getMagnitude() * v2.getMagnitude())));
    }

    public static Vector3D reflectedRay(Vector3D vectorToReflect, Vector3D normal) {
        Vector3D newNormal = new Vector3D(normal);
        newNormal.times(2 * dotProduct(vectorToReflect, normal));
        Vector3D reflectedRay = new Vector3D(vectorToReflect);
        reflectedRay.sub(newNormal);
        return reflectedRay;
    }

    /*
        Snell-Descartes's law: n1 * sin(θ1) = n2 * sin(θ2)
        where:
            n1: indice de refraction du premier corps
            n2: indice de refraction du deuxieme corps
            θ1: angle entre la normal et le rayon incident
            θ2: angle entre la normal et le rayon refracte
     */
    public static Vector3D refractedRay(Vector3D incident, Vector3D normal, double n1, double n2) {
        Vector3D incidentTmp = new Vector3D(incident);
        incidentTmp.normalize();
        Vector3D normalTmp = new Vector3D(normal);
        normalTmp.normalize();
        double r = n1 / n2;
        double c = -Vector3D.dotProduct(normalTmp, incidentTmp);
        if(c < 0.0) {
            normalTmp = Vector3D.inverse(normalTmp);
            c = -Vector3D.dotProduct(normalTmp, incidentTmp);
        }
        incidentTmp.times(r);
        normalTmp.times(r * c - Math.sqrt(1.0 - r * r * (1.0 - c * c)));
        return Vector3D.sum(incidentTmp, normalTmp);
    }

    public static Vector3D sum(Vector3D... vectors) {
        double x = 0;
        double y = 0;
        double z = 0;
        for(int i = 0; i < vectors.length; ++i) {
            x += vectors[i].x;
            y += vectors[i].y;
            z += vectors[i].z;
        }
        return new Vector3D(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Vector3D))
            return false;
        Vector3D vector = (Vector3D) o;
        return (
                Math.abs(vector.x - x) > 0.00001 && Math.abs(vector.y - y) > 0.00001 && Math.abs(vector.z - z) > 0.00001
                );
    }

    @Override
    public String toString() {
        return String.format("Vector: {%f %f %f}", x, y, z);
    }
}
