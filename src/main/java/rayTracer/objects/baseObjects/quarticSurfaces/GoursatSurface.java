package rayTracer.objects.baseObjects.quarticSurfaces;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Solver;
import rayTracer.math.Vector3D;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Intersection;
import rayTracer.utils.IntersectionManager;

import java.util.ArrayList;
import java.util.List;

public class GoursatSurface extends BaseObject {
    private double a, b, c;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public GoursatSurface(double a, double b, double c) {
        super();
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *               SETTERS                 *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void setPattern(PatternTypeEnum pattern) {

    }

    /* * * * * * * * * * * * * * * * * * * * *

     *               COLORS                  *

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
        Line3D localRay = this.transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        double a = localRay.getVX();
        double b = localRay.getVY();
        double c = localRay.getVZ();
        double x0 = localRay.getPX();
        double y0 = localRay.getPY();
        double z0 = localRay.getPZ();

        double a2 = a * a;
        double b2 = b * b;
        double c2 = c * c;
        double x02 = x0 * x0;
        double y02 = y0 * y0;
        double z02 = z0 * z0;

        double a3 = a * a * a;
        double b3 = b * b * b;
        double c3 = c * c * c;
        double x03 = x0 * x0 * x0;
        double y03 = y0 * y0 * y0;
        double z03 = z0 * z0 * z0;

        double a4 = a * a * a * a;
        double b4 = b * b * b * b;
        double c4 = c * c * c * c;
        double x04 = x0 * x0 * x0 * x0;
        double y04 = y0 * y0 * y0 * y0;
        double z04 = z0 * z0 * z0 * z0;

        double t4 = (this.a + 1) * (a4 + b4 + c4) + this.a * 2 * (a2 * b2 + a2 * c2 + b2 * c2);

        double t3 = (this.a + 1) * 4 * (a3 * x0 + b3 * y0 +c3 * z0) +
                this.a * 2 * (
                2 * a2 * b * y0 +
                2 * b2 * a * x0 +
                2 * a2 * c * z0 +
                2 * c2 * a * x0 +
                2 * b2 * c * z0 +
                2 * c2 * b * y0);

        double t2 = (this.a + 1) * 6 * (a2 * x02 + b2 * y02 + c2 * z02) +
                this.a * 2 * (
                        a2 * y02 +
                        4 * a * b * x0 * y0 +
                        b2 * x02 +
                        a2 * z02 +
                        4 * a * c * x0 * z0 +
                        c2 * x02 +
                        b2 * z02 +
                        4 * b * c * y0 * z0 +
                        c2 * y02) +
                this.b * (a2 + b2 + c2);

        double t1 = (this.a + 1) * 4 * (a * x03 + b * y03 + c * z03) +
                this.a * 2 * (
                        2 * a * x0 * y02 +
                        2 * b * x02 * y0 +
                        2 * a * x0 * z02 +
                        2 * c * x02 * z0 +
                        2 * b * y0 * z02 +
                        2 * c * y02 * z0) +
                this.b * (2 * a * x0 + 2 * b * y0 + 2 * c * z0);

        double t0 = (this.a + 1) * (x04 + y04 + z04) +
                this.a * 2 * (x02 * y02 + x02 * z02 + y02 * z02) +
                this.b * (x02 + y02 + z02) +
                c;

        List<Double> solutions = Solver.solve(t4, t3, t2, t1, t0);
        List<Intersection> tmp = new ArrayList<>();
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
                tmp.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
            }
        }
        List<Intersection> blackIntersections = new ArrayList<>();
        IntersectionManager.getIntersections(localRay, blackObjects, blackIntersections);
        IntersectionManager.preProcessIntersections(tmp, blackIntersections);
        intersections.addAll(tmp);
    }
}
