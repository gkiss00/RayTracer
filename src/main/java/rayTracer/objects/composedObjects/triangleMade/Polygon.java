package rayTracer.objects.composedObjects.triangleMade;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Triangle;
import rayTracer.math.Vector3D;
import rayTracer.objects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Intersection;

import java.util.List;

public class Polygon extends BaseObject {
    private List<Triangle> triangles;

    public Polygon(List<Triangle> triangles) {
        super();
        this.triangles = triangles;
    }

    @Override
    protected Color getColor(Point3D localIntersection) {
        return colors.get(0);
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        for (Triangle tr: triangles) {
            Point3D localIntersection = tr.hit(localRay);
            if(localIntersection != null) {
                Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                Vector3D localNormal = tr.getNormal();
                if (Vector3D.angleBetween(localRay.getVector(), localNormal) < 90)
                    localNormal = Vector3D.inverse(localNormal);
                Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                intersections.add(new Intersection(
                        realIntersection,
                        realNormal,
                        getColor(localIntersection),
                        Point3D.distanceBetween(ray.getPoint(), realIntersection),
                        this.reflectionRatio,
                        this
                ));
            }
        }
    }
}
