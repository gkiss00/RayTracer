package rayTracer.objects.baseObjects.composedObjects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Solver;
import rayTracer.math.Vector3D;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;
import rayTracer.utils.IntersectionManager;

import java.util.ArrayList;
import java.util.List;

public class ClosedCone extends BaseObject {
    protected static final Vector3D upNormal = new Vector3D(0, 0, 1);
    protected static final Vector3D downNormal = new Vector3D(0, 0, -1);
    protected  Vector3D realUpNormal;
    protected  Vector3D realDownNormal;
    private final double angle;
    private final double height;
    private final double radius;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public ClosedCone(double angle, double height) {
        super();
        this.angle = angle;
        this.height = height / 2;
        this.radius = Math.tan(Math.toRadians(this.angle)) * this.height;
    }

    public ClosedCone(double angle, double height, Color color) {
        super(color);
        this.angle = angle;
        this.height = height / 2;
        this.radius = Math.tan(Math.toRadians(this.angle)) * this.height;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *               SETTERS                 *

     * * * * * * * * * * * * * * * * * * * * */

    public void setNormals() {
        try {
            realUpNormal = transform.apply(upNormal, MatrixTransformEnum.TO_REAL);
            realDownNormal = transform.apply(downNormal, MatrixTransformEnum.TO_REAL);
        } catch (Exception e) {

        }
    }

    @Override
    public void setPattern(PatternTypeEnum pattern) {

    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                COLORS                 *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    protected Color getColor(Point3D localIntersection) {
        return colors.get(0);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             INTERSECTIONS             *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        double a, b, c;
        a = localRay.getVX() * localRay.getVX() +
                localRay.getVY() * localRay.getVY() -
                localRay.getVZ() * localRay.getVZ() * Math.tan(Math.toRadians(angle)) * Math.tan(Math.toRadians(angle));

        b = 2 * localRay.getPX() * localRay.getVX() +
                2 * localRay.getPY() * localRay.getVY() -
                2 * localRay.getPZ() * localRay.getVZ() * Math.tan(Math.toRadians(angle)) * Math.tan(Math.toRadians(angle));

        c = localRay.getPX() * localRay.getPX() +
                localRay.getPY() * localRay.getPY()-
                localRay.getPZ() * Math.tan(Math.toRadians(angle)) * localRay.getPZ() * Math.tan(Math.toRadians(angle));

        List<Double> solutions = Solver.solve(a, b, c);
        List<Intersection> tmp = new ArrayList<>();
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                if(localIntersection.getZ() <= height && localIntersection.getZ() >= -height && !Cutter.cut(localIntersection, cuts)) {
                    Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    double h = Math.sqrt(localIntersection.getX() * localIntersection.getX() + localIntersection.getY() * localIntersection.getY()) / Math.tan(Math.toRadians(90 - angle));
                    if (localIntersection.getZ() < 0)
                        h = -h;
                    Vector3D localNormal = new Vector3D(localIntersection.getX(), localIntersection.getY(), -h);
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    tmp.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }

        double x0, y0, z0;
        x0 = localRay.getPX();
        y0 = localRay.getPY();
        z0 = localRay.getPZ();
        a = localRay.getVX();
        b = localRay.getVY();
        c = localRay.getVZ();

        double t;
        double x, y, z;
        if (c > EPSILON || c < -EPSILON) {
            // UP
            t = (height - z0) / c;
            if(t > 0) {
                x = x0 + a * t;
                y = y0 + b * t;
                z = height;

                if (Math.sqrt(x * x + y * y) <= radius) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    if(!isCut(localIntersection)){
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        Vector3D realNormal = realUpNormal;
                        if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                            realNormal = realDownNormal;
                        tmp.add(new Intersection(realIntersection, realNormal, color, dist, reflectionRatio, this));
                    }
                }
            }
            // DOWN
            t = (-height -z0) / c;
            if(t > 0) {
                x = x0 + a * t;
                y = y0 + b * t;
                z = -height;

                if (Math.sqrt(x * x + y * y) <= radius) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    if(!isCut(localIntersection)) {
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        Vector3D realNormal = realDownNormal;
                        if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                            realNormal = realUpNormal;
                        tmp.add(new Intersection(realIntersection, realNormal, color, dist, reflectionRatio, this));
                    }
                }
            }
        }
        List<Intersection> blackIntersections = new ArrayList<>();
        IntersectionManager.getIntersections(localRay, blackObjects, blackIntersections);
        IntersectionManager.preProcessIntersections(tmp, blackIntersections);
        intersections.addAll(tmp);
    }
}
