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

public class OpenCylinder extends BaseObject {
    private final double radius;
    private final double height;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public OpenCylinder(double radius, double height) {
        super();
        this.radius = radius;
        this.height = height / 2;
    }

    public OpenCylinder(double radius, double height, Color color) {
        super(color);
        this.radius = radius;
        this.height = height / 2;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                SETTERS                *

     * * * * * * * * * * * * * * * * * * * * */

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
                radius * radius;

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
    }
}
