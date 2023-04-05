package rayTracer.objects.baseObjects.composedObjects.objectMade;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.Obj;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Intersection;
import rayTracer.utils.IntersectionManager;

import java.util.ArrayList;
import java.util.List;

public class Assembly extends BaseObject {
    private List<Obj> components;

    public Assembly() {
        super();
        this.components = new ArrayList<>();
    }

    public void add(Obj obj) {
        components.add(obj);
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);
        localRay.normalize();

        List<Intersection> tmp = new ArrayList<>();
        for (Obj component : components) {
            component.hit(localRay, tmp);
        }

        for(Intersection intersection: tmp) {
            intersection.setColor(getColor(intersection.getPointOfIntersection()));
            Point3D realIntersection = transform.apply(intersection.getPointOfIntersection(), MatrixTransformEnum.TO_REAL);
            intersection.setPointOfIntersection(realIntersection);
            Vector3D realNormal = transform.apply(intersection.getNormal(), MatrixTransformEnum.TO_REAL);
            if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                realNormal.inverse();
            intersection.setNormal(realNormal);
            intersection.setDistanceFromCamera(Point3D.distanceBetween(realIntersection, ray.getPoint()));
            intersection.setReflectionRatio(reflectionRatio);
            intersection.setObject(this);
        }
        List<Intersection> blackIntersections = new ArrayList<>();
        IntersectionManager.getIntersections(localRay, blackObjects, blackIntersections);
        IntersectionManager.preProcessIntersections(tmp, blackIntersections);
        intersections.addAll(tmp);
    }

    @Override
    protected Color getColor(Point3D localIntersection) {
        return colors.get(0);
    }
}
