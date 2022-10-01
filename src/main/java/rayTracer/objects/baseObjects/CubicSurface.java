package rayTracer.objects.baseObjects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Solver;
import rayTracer.math.Vector3D;
import rayTracer.utils.Color;
import rayTracer.utils.Intersection;

import java.util.List;

public class CubicSurface extends BaseObject{
    double A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T;

    public CubicSurface() {
        super();
    }

    public CubicSurface(Color color) {
        super(color);
    }

    public void setX3(double A) { this.A = A; }
    public void setY3(double B) { this.B = B; }
    public void setZ3(double C) { this.C = C; }
    public void setX2Y(double D) { this.D = D; }
    public void setX2Z(double E) { this.E = E; }
    public void setY2X(double F) { this.F = F; }
    public void setY2Z(double G) { this.G = G; }
    public void setZ2X(double H) { this.H = H; }
    public void setZ2Y(double I) { this.I = I; }
    public void setXYZ(double J) { this.J = J; }
    public void setX2(double K) { this.K = K; }
    public void setY2(double L) { this.L = L; }
    public void setZ2(double M) { this.M = M; }
    public void setXY(double N) { this.N = N; }
    public void setXZ(double O) { this.O = O; }
    public void setYZ(double P) { this.P = P; }
    public void setX(double Q) { this.Q = Q; }
    public void setY(double R) { this.R = R; }
    public void setZ(double S) { this.S = S; }
    public void setK(double T) { this.T = T; }

    @Override
    public void setPattern(PatternTypeEnum pattern) {

    }

    @Override
    protected Color getColor(Point3D localIntersection) {
        return colors.get(0);
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        double a = localRay.getVX();
        double b = localRay.getVY();
        double c = localRay.getVZ();
        double alpha = localRay.getPX();
        double beta = localRay.getPY();
        double gama = localRay.getPZ();

        double t3 = A * a * a * a +
                B * b * b * b +
                C * c * c * c +
                D * a * a * b +
                E * a * a * c +
                F * b * b * a +
                G * b * b * c +
                H * c * c * a +
                I * c * c * b +
                J * a * b * c;

        double t2 = A * 3 * a * a * alpha +
                B * 3 * b * b * beta +
                C * 3 * c * c * gama +
                D * ((2 * a * b * alpha) + (a * a * beta)) +
                E * ((2 * a * c * alpha) + (a * a * gama)) +
                F * ((2 * b * a * beta) + (b * b * alpha)) +
                G * ((2 * b * c * beta) + (b * b * gama)) +
                H * ((2 * c * a * gama) + (c * c * alpha)) +
                I * ((2 * c * b * beta) + (c * c * beta)) +
                J * ((a * c * beta) + (b * c * alpha) + (a * b * gama)) +
                K * a * a +
                L * b * b +
                M * c * c +
                N * a * b +
                O * a * c +
                P * b * c;

        double t1 = A * 3 * alpha * alpha * a +
                B * 3 * beta * beta * b +
                C * 3 * gama * gama * c +
                D * ((a * a * b) + (2 * a * alpha * beta)) +
                E * ((a * a * c) + (2 * a * alpha * gama)) +
                F * ((b * b * a) + (2 * b * beta * alpha)) +
                G * ((b * b * c) + (2 * b * beta * gama)) +
                H * ((c * c * a) + (2 * c * gama * alpha)) +
                I * ((c * c * b) + (2 * c * gama * beta)) +
                J * ((c * alpha * beta) + (b * alpha * gama) + (a * beta * gama)) +
                K * 2 * a * alpha +
                L * 2 * b * beta +
                M * 2 * c * gama +
                N * ((a * beta) + (b * alpha)) +
                O * ((a * gama) + (c * alpha)) +
                P * ((b * gama) + (c * beta)) +
                Q * a +
                R * b +
                S * c;

        double t0 = A * alpha * alpha * alpha +
                B * beta * beta * beta +
                C * gama * gama * gama +
                D * a * a * beta +
                E * a * a * gama +
                F * b * b * alpha +
                G * b * b * gama +
                H * c * c * alpha +
                I * c * c * beta +
                J * alpha * beta * gama +
                K * alpha * alpha +
                L * beta * beta +
                M * gama * gama +
                N * alpha * beta +
                O * alpha * gama +
                P * beta + gama +
                Q * alpha +
                R * beta +
                S * gama +
                T;

        List<Double> solutions = Solver.solve(t3, t2, t1, t0);
        System.out.println(solutions.size());
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
                intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
            }
        }
    }

    private Vector3D normalAt(Point3D localIntersection) {
        double x = localIntersection.getX();
        double y = localIntersection.getY();
        double z = localIntersection.getZ();

        double Fx = A * 3 * x * x +
                D * 2 * x * y +
                E * 2 * x * z +
                F * y * y +
                H * z * z +
                J * y * z +
                K * 2 * x +
                N * y +
                O * z +
                Q;

        double Fy = B * 3 * y * y +
                D * x * x +
                F * 2 * y * x +
                G * 2 * y * z +
                I * z * z +
                J * x * z +
                L * 2 * y +
                N * x +
                P * z +
                R;

        double Fz = C * 3 * z * z +
                E * x * x +
                G * y * y +
                H * 2 * z * x +
                I * 2 * z * y +
                J * x * y +
                M * 2 * z +
                O * x +
                P * y +
                S;
        return new Vector3D(localIntersection, new Point3D(Fx, Fy, Fz));
    }
}
