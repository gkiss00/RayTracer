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

public class Torus extends BaseObject{
    private double r; //minor radius
    private double R; //major radius

    public Torus(double R, double r) {
        super();
        this.r = r;
        this.R = R;
    }

    public Torus(double R, double r, Color color) {
        super(color);
        this.r = r;
        this.R = R;
    }
    @Override
    public void setPattern(PatternTypeEnum pattern) {

    }

    @Override
    protected Color getColor(Point3D localIntersection) {
        return colors.get(0);
        //return null;
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);
        localRay.normalize();
        //localRay.getVector().inverse();

        double x0 = localRay.getPX();
        double y0 = localRay.getPY();
        double z0 = localRay.getPZ();
        double a = localRay.getVX();
        double b = localRay.getVY();
        double c = localRay.getVZ();

        double t0 = Math.pow(x0 * x0 + y0 * y0 + z0 * z0 + R * R - r * r, 2) - (4 * R * R * (x0 * x0 + y0 * y0));
        double t2 = Math.pow(2 * x0 * a + 2 * y0 * b + 2 * z0 * c, 2) - (4 * R * R * (a * a + b * b));
        double t1 = -(4 * R * R * (2 * a * x0 + 2 * b * y0));
        double t4 = Math.pow(a * a + b * b + c * c, 2);

        List<Double> solutions = Solver.solve(t4, 0, t2, t1, t0);
        if(solutions.size() != 0)
            System.out.println(solutions.size() + " " +  ray.getVector());
        for (int i = 0; i < solutions.size(); ++i) {

            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                System.out.println(realIntersection);
                Vector3D localNormal = new Vector3D(localIntersection.getX(), localIntersection.getY(), localIntersection.getZ());
                Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio));
            }
        }
    }
}
