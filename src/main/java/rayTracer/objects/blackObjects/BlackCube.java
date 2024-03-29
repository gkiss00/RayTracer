package rayTracer.objects.blackObjects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;

import java.util.List;

public class BlackCube extends BlackObject{
    protected static final Vector3D upNormal = new Vector3D(0, 0, 1);
    protected static final Vector3D downNormal = new Vector3D(0, 0, -1);
    protected static final Vector3D rightNormal = new Vector3D(0, 1, 0);
    protected static final Vector3D leftNormal = new Vector3D(0, -1, 0);
    protected static final Vector3D frontNormal = new Vector3D(-1, 0, 0);
    protected static final Vector3D backNormal = new Vector3D(1, 0, 0);
    protected  Vector3D realUpNormal;
    protected  Vector3D realDownNormal;
    protected  Vector3D realRightNormal;
    protected  Vector3D realLeftNormal;
    protected  Vector3D realFrontNormal;
    protected  Vector3D realBackNormal;
    protected double size;

    public BlackCube(double size) {
        super();
        this.size = size / 2;
    }

    public void setNormals() {
        try {
            realUpNormal = transform.apply(upNormal, MatrixTransformEnum.TO_REAL);
            realDownNormal = transform.apply(downNormal, MatrixTransformEnum.TO_REAL);
            realRightNormal = transform.apply(rightNormal, MatrixTransformEnum.TO_REAL);
            realLeftNormal = transform.apply(leftNormal, MatrixTransformEnum.TO_REAL);
            realBackNormal = transform.apply(backNormal, MatrixTransformEnum.TO_REAL);
            realFrontNormal = transform.apply(frontNormal, MatrixTransformEnum.TO_REAL);
        } catch (Exception e) {

        }
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        double x0, y0, z0, a, b, c;
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
            t = (size - z0) / c;
            if(t > 0) {
                x = x0 + a * t;
                y = y0 + b * t;
                z = size;

                if (y >= -size && y <= size && x >= -size && x <= size) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    addIntersection(ray, intersections, localIntersection, realUpNormal, realDownNormal);
                }
            }
            // DOWN
            t = (-size -z0) / c;
            if(t > 0) {
                x = x0 + a * t;
                y = y0 + b * t;
                z = -size;

                if (y >= -size && y <= size && x >= -size && x <= size) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    addIntersection(ray, intersections, localIntersection, realDownNormal, realUpNormal);
                }
            }
        }
        if (b > EPSILON || b < -EPSILON) {
            // LEFT
            t = (-size - y0) / b;
            if(t > 0) {
                x = x0 + a * t;
                y = -size;
                z = z0 + c * t;

                if (z >= -size && z <= size && x >= -size && x <= size) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    addIntersection(ray, intersections, localIntersection, realLeftNormal, realRightNormal);
                }
            }
            // RIGHT
            t = (size -y0) / b;
            if(t > 0) {
                x = x0 + a * t;
                y = size;
                z = z0 + c * t;

                if (z >= -size && z <= size && x >= -size && x <= size) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    addIntersection(ray, intersections, localIntersection, realRightNormal, realLeftNormal);
                }
            }
        }
        if (a > EPSILON || a < -EPSILON) {
            // BACK
            t = (size - x0) / a;
            if(t > 0) {
                x = size;
                y = y0 + b * t;
                z = z0 + c * t;

                if (z >= -size && z <= size && y >= -size && y <= size) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    addIntersection(ray, intersections, localIntersection, realBackNormal, realFrontNormal);
                }
            }
            // FRONT
            t = (-size - x0) / a;
            if(t > 0) {
                x = -size;
                y = y0 + b * t;
                z = z0 + c * t;

                if (z >= -size && z <= size && y >= -size && y <= size) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    addIntersection(ray, intersections, localIntersection, realFrontNormal, realBackNormal);
                }
            }
        }
    }

    private void addIntersection(Line3D ray, List<Intersection> intersections, Point3D localIntersection, Vector3D n1, Vector3D n2) throws Exception {
        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
        Vector3D realNormal = n1;
        if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
            realNormal = n2;
        intersections.add(new Intersection(realIntersection, realNormal, null, dist, 0, this));
    }
}
