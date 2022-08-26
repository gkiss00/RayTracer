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

public class Pipe extends BaseObject{
    protected static final Vector3D upNormal = new Vector3D(0, 0, 1);
    protected static final Vector3D downNormal = new Vector3D(0, 0, -1);
    protected  Vector3D realUpNormal;
    protected  Vector3D realDownNormal;
    private final double R;
    private final double r;
    private final double height;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Pipe(double R, double r, double height) {
        super();
        this.R = R;
        this.r = r;
        this.height = height / 2;
    }

    public Pipe(double R, double r, double height, Color color) {
        super(color);
        this.R = R;
        this.r = r;
        this.height = height / 2;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                SETTERS                *

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
        a = localRay.getVX() * localRay.getVX()+
                localRay.getVY() * localRay.getVY();

        b = 2 * localRay.getPX() * localRay.getVX() +
                2 * localRay.getPY() * localRay.getVY();

        c = localRay.getPX() * localRay.getPX() +
                localRay.getPY() * localRay.getPY() -
                R * R;

        // EXTERNAL SURFACE
        List<Double> solutions = Solver.solve(a, b, c);
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                if (localIntersection.getZ() <= height && localIntersection.getZ() >= -height && !isCut(localIntersection)) {
                    Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    Vector3D localNormal = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                        realNormal.inverse();
                    intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }

        c = localRay.getPX() * localRay.getPX() +
                localRay.getPY() * localRay.getPY() -
                r * r;

        // INTERNAL SURFACE
        solutions = Solver.solve(a, b, c);
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                if (localIntersection.getZ() <= height && localIntersection.getZ() >= -height && !isCut(localIntersection)) {
                    Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    Vector3D localNormal = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                        realNormal.inverse();
                    intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
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

                if (Math.hypot(x, y) <= R && Math.hypot(x, y) >= r) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    if(!isCut(localIntersection)){
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        Vector3D realNormal = realUpNormal;
                        if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                            realNormal = realDownNormal;
                        intersections.add(new Intersection(realIntersection, realNormal, color, dist, reflectionRatio, this));
                    }
                }
            }
            // DOWN
            t = (-height -z0) / c;
            if(t > 0) {
                x = x0 + a * t;
                y = y0 + b * t;
                z = -height;

                if (Math.hypot(x, y) <= R && Math.hypot(x, y) >= r) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    if(!isCut(localIntersection)) {
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        Vector3D realNormal = realDownNormal;
                        if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                            realNormal = realUpNormal;
                        intersections.add(new Intersection(realIntersection, realNormal, color, dist, reflectionRatio, this));
                    }
                }
            }
        }
    }
}
