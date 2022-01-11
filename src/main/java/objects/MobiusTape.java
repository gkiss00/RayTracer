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

        double t3 = a * a * b +
                c * c * b +
                b * b * b -
                2 * a * a * c -
                2 * b * b * c;

        double t2 = a * a * y0 +
                2 * a * x0 * b +
                c * c * y0 +
                2 * c * z0 * b +
                3 * y0 * b * b -
                2 * a * a * z0 -
                2 * 2 * a * x0 * c -
                2 * b * b * z0 -
                2 * 2 * b * y0 * c -
                2 * radius * a * b;

        double t1 = x0 * x0 * b +
                2 * x0 * a * y0 +
                z0 * z0 * b +
                2 * c * z0 * y0 +
                3 * y0 * y0 * b -
                2 * x0 * x0 * c -
                2 * 2 * a * x0 * z0 -
                2 * y0 * y0 * c -
                2 * 2 * b * y0 * z0 -
                2 * radius * x0 * b -
                2 * radius * a * y0 -
                radius * radius * b;

        double t0 = x0 * x0 * y0 +
                z0 * z0 * y0 +
                y0 * y0 * y0 -
                2 * x0 * x0 * x0 -
                2 * y0 * y0 * y0 -
                2 * radius * x0 * y0 -
                radius * radius * b;

        List<Double> solutions = Solver.solve(t3, t2, t1, t0);
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                Vector3D localNormal = new Vector3D(localIntersection.getX(), localIntersection.getY(), localIntersection.getZ());
                Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio));
            }
        }
    }

    private Point3D findRealPoint(Point3D localIntersection) {
        double distFromCenter = localIntersection.getDistanceFromOrigin();
        int sign = distFromCenter < radius ? -1 : 1;
        double hypotenuse = distFromCenter;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360.0 - angle;
        double rotationAngle = angle / 2;
        double newZ = Math.sin(Math.toRadians(rotationAngle)) * distFromCenter;
        double newY = newZ / Math.tan(Math.toRadians(rotationAngle));
        double newX = Math.tan(Math.toRadians(angle)) * newY;

        Point3D res = new Point3D(localIntersection.getX(), localIntersection.getY(), newZ);
        System.out.println(localIntersection);
        System.out.println(hypotenuse);
        System.out.println(angle + " " + Math.tan(Math.toRadians(angle)));
        System.out.println(res);
        return res;
    }
}
