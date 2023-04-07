package rayTracer.math;

import lombok.Getter;

@Getter
public class Triangle {
    private Point3D p1;
    private Point3D p2;
    private Point3D p3;
    private Vector3D normal;
    private Vector3D invertNormal;

    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        this.p1 = new Point3D(p1);
        this.p2 = new Point3D(p2);
        this.p3 = new Point3D(p3);
        this.normal = Vector3D.crossProduct(new Vector3D(p1, p2), new Vector3D(p1, p3));
        this.invertNormal = Vector3D.inverse(normal);
    }

    public Point3D hit(Line3D ray) {
        double a = this.normal.getX();
        double b = this.normal.getY();
        double c = this.normal.getZ();
        double K = ((a * (this.p1.getX() * -1)) + (b * (this.p1.getY() * -1)) + (c * (this.p1.getZ() * -1))) * -1;
        double t = a * ray.getVX() + b * ray.getVY() + c * ray.getVZ();
        double C = a * ray.getPX() + b * ray.getPY() + c * ray.getPZ();

        if (t == 0 && C != K) {

        } else if (t == 0 && C == K) {

        } else {
            double s = (K - C) / t;
            if(s > 0.00001) {
                Point3D intersection = new Point3D(
                        ray.getPX() + s * ray.getVX(),
                        ray.getPY() + s * ray.getVY(),
                        ray.getPZ() + s * ray.getVZ()
                );
                Vector3D pa = new Vector3D(intersection, this.p1);
                Vector3D pb = new Vector3D(intersection, this.p2);
                Vector3D pc = new Vector3D(intersection, this.p3);
                double area = this.normal.getMagnitude();
                double alpha, beta, gama;
                alpha = Vector3D.crossProduct(pb, pc).getMagnitude() / area;
                beta = Vector3D.crossProduct(pc, pa).getMagnitude() / area;
                gama = Vector3D.crossProduct(pa, pb).getMagnitude() / area;

                if(alpha >= 0 && alpha <= 1 &&
                        beta >= 0 && beta <= 1 &&
                        gama >= 0 && gama <= 1 &&
                        alpha + beta + gama < 1.001 && alpha + beta + gama > 0.990
                )
                    return intersection;
            }
        }
        return null;
    }
}
