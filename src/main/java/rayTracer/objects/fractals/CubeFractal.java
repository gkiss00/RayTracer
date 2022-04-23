package rayTracer.objects.fractals;

import rayTracer.blackObjects.BaseBlackObject;
import rayTracer.blackObjects.BlackCylinder;
import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.simpleObjects.Cube;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;

import java.util.ArrayList;
import java.util.List;

public class CubeFractal extends Cube {
    private int deepness;
    private final List<BaseBlackObject> zCylinders = new ArrayList<>();
    private final List<BaseBlackObject> yCylinders = new ArrayList<>();
    private final List<BaseBlackObject> xCylinders = new ArrayList<>();

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public CubeFractal(int deepness, double size) {
        super(size);
        setDeepness(deepness);
        initBlackObjects(1, size / 6.0, size / 6.0);
    }

    public CubeFractal(int deepness, double size, Color color) {
        super(size, color);
        setDeepness(deepness);
        initBlackObjects(1, size / 6.0, size / 6.0);
    }

    private void setDeepness(int deepness) {
        this.deepness = deepness <= 0 ? 0 : deepness;
    }

    private void initBlackObjects(int currentDeepness, double previousCylinderRadius, double currentCylinderRadius) {
        if (currentDeepness > deepness)
            return;
        if(currentDeepness == 1) {
            // Z cylinder
            zCylinders.add(new BlackCylinder(new Line3D(new Point3D(0, 0, size / 2), new Vector3D(upNormal)), currentCylinderRadius));
            // Y cylinder
            yCylinders.add(new BlackCylinder(new Line3D(new Point3D(0, size / 2, 0), new Vector3D(rightNormal)), currentCylinderRadius));
            // X cylinder
            xCylinders.add(new BlackCylinder(new Line3D(new Point3D(size / 2, 0, 0), new Vector3D(backNormal)), currentCylinderRadius));
        } else {
            // Z cylinders
            int size = zCylinders.size();
            for (int i = 0; i < size; ++i) {
                if (zCylinders.get(i).getSize() < previousCylinderRadius + 0.00001 && zCylinders.get(i).getSize() > previousCylinderRadius - 0.00001) {
                    double x = zCylinders.get(i).getPoint().getX();
                    double y = zCylinders.get(i).getPoint().getY();
                    double z = zCylinders.get(i).getPoint().getZ();

                    // top left
                    zCylinders.add(new BlackCylinder(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y - 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // top
                    zCylinders.add(new BlackCylinder(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // top right
                    zCylinders.add(new BlackCylinder(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y + 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // left
                    zCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y - 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // right
                    zCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y + 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // bottom left
                    zCylinders.add(new BlackCylinder(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y - 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // bottom
                    zCylinders.add(new BlackCylinder(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // bottom right
                    zCylinders.add(new BlackCylinder(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y + 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                }
            }
            // Y cylinders
            size = yCylinders.size();
            for (int i = 0; i < size; ++i) {
                if (yCylinders.get(i).getSize() < previousCylinderRadius + 0.00001 && yCylinders.get(i).getSize() > previousCylinderRadius - 0.00001) {
                    double x = yCylinders.get(i).getPoint().getX();
                    double y = yCylinders.get(i).getPoint().getY();
                    double z = yCylinders.get(i).getPoint().getZ();

                    // top left
                    yCylinders.add(new BlackCylinder(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y, z - 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // top
                    yCylinders.add(new BlackCylinder(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y, z), new Vector3D(rightNormal)), currentCylinderRadius));
                    // top right
                    yCylinders.add(new BlackCylinder(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y, z + 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // left
                    yCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y, z - 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // right
                    yCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y, z + 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // bottom left
                    yCylinders.add(new BlackCylinder(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y, z - 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // bottom
                    yCylinders.add(new BlackCylinder(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y, z), new Vector3D(rightNormal)), currentCylinderRadius));
                    // bottom right
                    yCylinders.add(new BlackCylinder(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y, z + 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                }
            }
            // X cylinders
            size = xCylinders.size();
            for (int i = 0; i < size; ++i) {
                if (xCylinders.get(i).getSize() < previousCylinderRadius + 0.00001 && xCylinders.get(i).getSize() > previousCylinderRadius - 0.00001) {
                    double x = xCylinders.get(i).getPoint().getX();
                    double y = xCylinders.get(i).getPoint().getY();
                    double z = xCylinders.get(i).getPoint().getZ();

                    // top left
                    xCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y - 2 * previousCylinderRadius, z + 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // top
                    xCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y, z + 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // top right
                    xCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y + 2 * previousCylinderRadius, z + 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // left
                    xCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y - 2 * previousCylinderRadius, z), new Vector3D(backNormal)), currentCylinderRadius));
                    // right
                    xCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y + 2 * previousCylinderRadius, z), new Vector3D(backNormal)), currentCylinderRadius));
                    // bottom left
                    xCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y - 2 * previousCylinderRadius, z - 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // bottom
                    xCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y, z - 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // bottom right
                    xCylinders.add(new BlackCylinder(new Line3D(new Point3D(x, y + 2 * previousCylinderRadius, z - 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                }
            }
        }
        initBlackObjects(currentDeepness + 1, currentCylinderRadius, currentCylinderRadius / 3);
    }

    private boolean zBlackCylindersContain(Point3D point) {
        int size = zCylinders.size();
        for (int i = 0; i < size; ++i) {
            if (zCylinders.get(i).contains(point))
                return true;
        }
        return false;
    }

    private boolean yBlackCylindersContain(Point3D point) {
        int size = yCylinders.size();
        for (int i = 0; i < size; ++i) {
            if (yCylinders.get(i).contains(point))
                return true;
        }
        return false;
    }

    private boolean xBlackCylindersContain(Point3D point) {
        int size = xCylinders.size();
        for (int i = 0; i < size; ++i) {
            if (xCylinders.get(i).contains(point))
                return true;
        }
        return false;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             INTERSECTIONS             *

     * * * * * * * * * * * * * * * * * * * * */

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
                    if (!Cutter.cut(localIntersection, cuts) && !zBlackCylindersContain(localIntersection)) {
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        intersections.add(new Intersection(realIntersection, realUpNormal, color, dist, reflectionRatio, this));
                    }
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
                    if (!Cutter.cut(localIntersection, cuts) && !zBlackCylindersContain(localIntersection)) {
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        intersections.add(new Intersection(realIntersection, realDownNormal, color, dist, reflectionRatio, this));
                    }
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
                    if (!Cutter.cut(localIntersection, cuts) && !yBlackCylindersContain(localIntersection)) {
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        intersections.add(new Intersection(realIntersection, realLeftNormal, color, dist, reflectionRatio, this));
                    }
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
                    if (!Cutter.cut(localIntersection, cuts) && !yBlackCylindersContain(localIntersection)) {
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        intersections.add(new Intersection(realIntersection, realRightNormal, color, dist, reflectionRatio, this));
                    }
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
                    if (!Cutter.cut(localIntersection, cuts) && !xBlackCylindersContain(localIntersection)) {
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        intersections.add(new Intersection(realIntersection, realBackNormal, color, dist, reflectionRatio, this));
                    }
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
                    if (!Cutter.cut(localIntersection, cuts) && !xBlackCylindersContain(localIntersection)) {
                        Color color = getColor(localIntersection);
                        Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                        double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
                        intersections.add(new Intersection(realIntersection, realFrontNormal, color, dist, reflectionRatio, this));
                    }
                }
            }
        }
    }
}
