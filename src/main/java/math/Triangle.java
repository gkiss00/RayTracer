package math;

public class Triangle {
    private Point3D p1;
    private Point3D p2;
    private Point3D p3;
    private Vector3D normal;

    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        this.p1 = new Point3D(p1);
        this.p2 = new Point3D(p2);
        this.p3 = new Point3D(p3);

        Vector3D p1p2 = new Vector3D(p1, p2);
        Vector3D p1p3 = new Vector3D(p1, p3);
        this.normal = Vector3D.crossProduct(p1p2, p1p3);
    }

    public void hit(Line3D ray) {
        double area = normal.getMagnitude();
    }
}
