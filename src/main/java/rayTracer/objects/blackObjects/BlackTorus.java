package rayTracer.objects.blackObjects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Solver;
import rayTracer.math.Vector3D;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;

import java.util.List;

public class BlackTorus extends BlackObject{
    private final double r; //minor radius
    private final double R; //major radius

    public BlackTorus(double R, double r) {
        super();
        this.r = r;
        this.R = R;
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);
        localRay.normalize();

        double x0 = localRay.getPX();
        double y00 = localRay.getPZ();
        double z0 = localRay.getPY();

        double a = localRay.getVX();
        double b = localRay.getVZ();
        double c = localRay.getVY();

        double a2 = a*a;
        double b2 = b*b;
        double c2 = c*c;

        double x02 = x0*x0;
        double y02 = y00*y00;
        double z02 = z0*z0;

        double R2 = R*R;
        double r2 = r*r;

        double t4 = (a2 + b2 + c2) * (a2 + b2 + c2);
        double t3 = 4 * (a2 + b2 + c2) * (x0*a + y00*b + z0*c);
        double t2 = 2 * (a2 + b2 + c2) * (x02 + y02 + z02 - (r2 + R2)) + 4*(x0*a + y00*b + z0*c) * (x0*a + y00*b + z0*c)+ 4*R2*b2;
        double t1 = 4 * (x02 + y02 + z02 - (r2 + R2)) * (x0*a + y00*b + z0*c) + 8*R2*y00*b;
        double t0 = (x02 + y02 + z02 - (r2 + R2)) * (x02 + y02 + z02 - (r2 + R2)) - 4*R2*(r2 - y02);

        List<Double> solutions = Solver.solve(t4, t3, t2, t1, t0);
        for (int i = 0; i < solutions.size(); ++i) {
            Point3D localIntersection = new Point3D(
                    localRay.getPX() + localRay.getVX() * solutions.get(i),
                    localRay.getPY() + localRay.getVY() * solutions.get(i),
                    localRay.getPZ() + localRay.getVZ() * solutions.get(i)
            );
            Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
            Vector3D localNormal = getNormal(localIntersection);
            Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
            if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                realNormal.inverse();
            intersections.add(new Intersection(realIntersection, realNormal, null, Point3D.distanceBetween(ray.getPoint(), realIntersection), 0, this));
        }
    }

    public Vector3D getNormal(Point3D localIntersection) {
        Vector3D tmp = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
        tmp.normalize();
        tmp.times(R);
        return new Vector3D(localIntersection.getX() - tmp.getX(), localIntersection.getY() - tmp.getY(), localIntersection.getZ());
    }
}
