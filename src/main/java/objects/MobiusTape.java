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

public class MobiusTape extends BaseObject{
    private static final Point3D localOrigin = new Point3D(0, 0, 0);
    private static final Vector3D localNormal = new Vector3D(0, 0, 1);
    private double radius;
    private double width;

    public MobiusTape(double radius, double width) {
        super();
        this.radius = radius;
        this.width = width;
    }

    public MobiusTape(double radius, double width, Color color) {
        super(color);
        this.radius = radius;
        this.width = width;
    }

    public MobiusTape(double radius, double width, PatternTypeEnum pattern, Color... colors) {
        super(colors);
        this.radius = radius;
        this.width = width;
        setPattern(pattern);
    }

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

        double x0 = localRay.getPX();
        double y0 = localRay.getPY();
        double z0 = localRay.getPZ();
        double a = localRay.getVX();
        double b = localRay.getVY();
        double c = localRay.getVZ();

        double a2 = a * a;
        double b2 = b * b;
        double c2 = c * c;
        double x02 = x0 * x0;
        double y02 = y0 * y0;
        double z02 = z0 * z0;
        double R = radius;
        double R2 = radius * radius;

        /*double t3 = b * b * b +
                a2 * b +
                c2 * b -
                2 * (a2 * c + b2 * c);

        double t2 = 3 * b2 * y0 +
                a2 * y0 +
                2 * a * b * x0 +
                c2 * y0 +
                2 * c * b * z0 -
                2 * (a2 * z0 + 2 * a * c * x0 + b2 * z0 + 2 * b * c * y0 + R * a * b);

        double t1 = 3 * b * y02 +
                2 * a * x0 * y0 +
                b * x02 +
                2 * c * z0 * y0 +
                b * z02 -
                2 * (2 * a * x0 * z0 + c * x02 + 2 * b * y0 * z0 + c * y02 + R * a * z0 + R * c * x0) -
                R2 * b;

        double t0 = x02 * y0 +
                z02 * y0 -
                2 * (x02 * z0 + y02 * z0 + R * x0 * z0) -
                R2 * y0;*/

        double t3 = b * b * b + a2*b - 2*a2*c - 2*b2*c + b*c2;
        double t2 = 3*y0*b2 + 2*x0*a*b + a2*y0 - 4*x0*a*c - 2*a2*z0 -4*y0*b*c - 2*b2*z0 + y0*c2 + 2*b*z0*c - 2*a*c;
        double t1 = 3*y02*b + x02*b + 2*x0*y0*a -2*x02*c - 4*x0*a*z0 - 2*y02*c - 4*y0*b*z0 + 2*y0*z0*c + b*z02 - 2*x0*c - 2*a*z0 - b;
        double t0 = y0 * y0 * y0 + x02*y0 - 2*x02*z0 - 2*y02*z0 + y0*z02 - 2*x0*z0 - y0;

        List<Double> solutions = Solver.solve(t3, t2, t1, t0);
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                Vector3D tmp = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
                tmp.normalize();
                Point3D test = new Point3D(tmp.getX(), tmp.getY(), 0);
                if(Point3D.distanceBetween(test, localIntersection) < .1){
                //if(Point3D.distanceBetween(new Point3D(0, 0, 0), localIntersection) < 1 && Point3D.distanceBetween(new Point3D(0, 0, 0), localIntersection) > 0.0 &&
                //        Math.abs(localIntersection.getZ()) < 0.3) {
                //if(localIntersection.getZ() < 0.3 && localIntersection.getZ() > -0.3) {
                    Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    Vector3D localNormal = getTan(localIntersection);
                    if(Vector3D.angleBetween(localRay.getVector(), localNormal) < 90)
                        localNormal.inverse();
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio));
                }
            }
        }
    }

    private Vector3D getTan(Point3D localIntersection){
        double x = localIntersection.getX();
        double y = localIntersection.getY();
        double z = localIntersection.getZ();

        double fx = 2 * x * y - 2 * z - 4 * x * z;
        double fy = x * x + z * z + 3 * y * y - 1 - 4 * y * z;
        double fz = 2 * z * y - 2 * x - 2 * x * x - 2 * y * y;
        return new Vector3D(fx, fy, fz);
    }
}
