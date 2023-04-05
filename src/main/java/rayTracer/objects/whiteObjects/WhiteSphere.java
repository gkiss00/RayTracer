package rayTracer.objects.whiteObjects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Solver;
import rayTracer.math.Vector3D;
import rayTracer.utils.Intersection;

import java.util.List;

public class WhiteSphere extends WhiteObject {
    private final double radius;

    public WhiteSphere(double radius) {
        super();
        this.radius = radius;
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);
        localRay.normalize();

        double a = 1;
        double b = 2 * localRay.getPX() * localRay.getVX() +
                2 * localRay.getPY() * localRay.getVY() +
                2 * localRay.getPZ() * localRay.getVZ();

        double c = localRay.getPX() * localRay.getPX() +
                localRay.getPY() * localRay.getPY() +
                localRay.getPZ() * localRay.getPZ() -
                radius * radius;

        List<Double> solutions = Solver.solve(a, b, c);
        for (int i = 0; i < solutions.size(); ++i) {
            Point3D localIntersection = new Point3D(
                    localRay.getPX() + localRay.getVX() * solutions.get(i),
                    localRay.getPY() + localRay.getVY() * solutions.get(i),
                    localRay.getPZ() + localRay.getVZ() * solutions.get(i)
            );
            Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
            Vector3D localNormal = new Vector3D(localIntersection.getX(), localIntersection.getY(), localIntersection.getZ());
            Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
            if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                realNormal.inverse();
            intersections.add(new Intersection(realIntersection, realNormal, null, Point3D.distanceBetween(ray.getPoint(), realIntersection), 0, this));
        }
    }
}
