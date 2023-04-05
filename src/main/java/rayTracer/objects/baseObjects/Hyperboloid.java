package rayTracer.objects.baseObjects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Solver;
import rayTracer.math.Vector3D;
import rayTracer.utils.Color;
import rayTracer.utils.Intersection;

import java.util.List;

public class Hyperboloid extends BaseObject{
    private double A, B, C, D;
    public boolean isLimited = false;
    public double upperLimit = 2000000000, lowerLimit = 2000000000;

    public Hyperboloid(double a, double b, double c) {
        super();
        this.A = a * a;
        this.B = b * b;
        this.C = c * c;
        this.D = 1;
    }

    public Hyperboloid(double a, double b, double c, double d) {
        super();
        this.A = a * a;
        this.B = b * b;
        this.C = c * c;
        this.D = d;
    }

    @Override
    protected Color getColor(Point3D localIntersection) {
        return colors.get(0);
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);
        double x1, y1, z1, a, b, c, t0, t1, t2;
        x1 = localRay.getPX();
        y1 = localRay.getPY();
        z1 = localRay.getPZ();
        a = localRay.getVX();
        b = localRay.getVY();
        c = localRay.getVZ();

        t2 = (a * a) / A +
                (b * b) / B -
                (c * c) / C;

        t1 = (2 * x1 * a) / A +
                (2 * y1 * b) / B -
                (2 * z1 * c) / C;

        t0 = (x1 * x1 / A) +
                (y1 * y1) / B -
                (z1 * z1) / C -
                D;

        List<Double> solutions = Solver.solve(t2, t1, t0);
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                if(!isLimited || (isLimited && localIntersection.getZ() < upperLimit && localIntersection.getZ() > lowerLimit)) {
                    Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    Vector3D localNormal = normalAt(localIntersection);
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    intersections.add(new Intersection(realIntersection, realNormal, colors.get(0), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }
    }

    private Vector3D normalAt(Point3D localIntersection) {
        double Fx, Fy, Fz;

        Fx = (2 * localIntersection.getX()) / A;
        Fy = (2 * localIntersection.getY()) / B;
        Fz = -(2 * localIntersection.getZ()) / C;

        return new Vector3D(localIntersection, new Point3D(Fx, Fy, Fz));
    }
}
