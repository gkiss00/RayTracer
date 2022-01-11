package objects;

import enums.MatrixTransformEnum;
import enums.PatternTypeEnum;
import math.Line3D;
import math.Point3D;
import math.Solver;
import math.Vector3D;
import utils.Color;
import utils.Intersection;

import java.util.List;

// Equation:
// Ax² + By² + Cz² + Dxy + Exz + Fyz + Gx + Hy + Iz + J = 0
public class QuadraticSurface extends BaseObject{
    private double A, B, C, D, E, F, G, H, I, J;

    public QuadraticSurface() {
        super();
    }

    public QuadraticSurface(double A, double B, double C, double D, double E, double F, double G, double H, double I, double J) {
        super();
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;
        this.F = F;
        this.G = G;
        this.H = H;
        this.I = I;
        this.J = J;
    }

    public QuadraticSurface(double A, double B, double C, double D, double E, double F, double G, double H, double I, double J, Color color) {
        super(color);
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;
        this.F = F;
        this.G = G;
        this.H = H;
        this.I = I;
        this.J = J;
    }

    @Override
    public void setPattern(PatternTypeEnum pattern) {
        return;
    }

    @Override
    protected Color getColor(Point3D localIntersection) {
        switch (pattern) {
            case UNIFORM:
                return colors.get(0);
            default:
                return null;
        }
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

        t0 = A * x1 * x1 +
                B * y1 * y1 +
                C * z1 * z1 +
                D * x1 * y1 +
                E * x1 * z1 +
                F * y1 * z1 +
                G * x1 +
                H * y1 +
                I * z1 +
                J;

        t1 = A * 2 * x1 * a +
                B * 2 * y1 * b +
                C * 2 * z1 * c +
                D * x1 * b +
                D * y1 * a +
                E * x1 * c +
                E * z1 * a +
                F * y1 * c +
                F * z1 * b +
                G * a +
                H * b +
                I * c;

        t2 = A * a * a +
                B * b * b +
                C * c * c +
                D * a * b +
                E * a * c +
                F * b * c;

        List<Double> solutions = Solver.solve(t2, t1, t0);
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                Vector3D localNormal = normalAt(localIntersection);
                Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                intersections.add(new Intersection(realIntersection, realNormal, colors.get(0), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio));
            }
        }
    }

    private Vector3D normalAt(Point3D localIntersection) {
        double a, b, c, d, e, f, g, h, i;

        a = A;
        b = B;
        c = C;
        d = G;
        e = H;
        f = I;
        g = D;
        h = E;
        i = F;

        double Fx, Fy, Fz;

        Fx = 2 * a * localIntersection.getX() +
                d +
                g * localIntersection.getY() +
                h * localIntersection.getZ();
        Fy = 2 * b * localIntersection.getY() +
                e +
                g * localIntersection.getX() +
                i * localIntersection.getZ();
        Fz = 2 * c * localIntersection.getZ() +
                f +
                h * localIntersection.getX() +
                i * localIntersection.getY();

        return new Vector3D(localIntersection, new Point3D(Fx, Fy, Fz));
    }
}
