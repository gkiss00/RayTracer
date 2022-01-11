package math;

public class Line3D {
    private Point3D point;
    private Vector3D vector;

    public Line3D(Point3D point, Vector3D vector) {
        this.point = new Point3D(point);
        this.vector = new Vector3D(vector);
    }

    public Line3D(Point3D p1, Point3D p2) {
        this.point = new Point3D(p1);
        this.vector = new Vector3D(p1, p2);
    }

    public Line3D(Line3D line) {
        this.point = new Point3D(line.getPoint());
        this.vector = new Vector3D(line.getVector());
    }

    public Point3D getPoint() { return point; }

    public Vector3D getVector() {
        return vector;
    }

    public double getPX() {
        return point.getX();
    }

    public double getPY() {
        return point.getY();
    }

    public double getPZ() {
        return point.getZ();
    }

    public double getVX() {
        return vector.getX();
    }

    public double getVY() {
        return vector.getY();
    }

    public double getVZ() {
        return vector.getZ();
    }

    public void normalize() {
        vector.normalize();
    }

    public static double distanceBetween(Line3D line, Point3D point) {
        Vector3D tmp = new Vector3D(line.point, point);
        return Vector3D.crossProduct(tmp, line.vector).getMagnitude() / line.vector.getMagnitude();
    }
}
