package rayTracer.objects.baseObjects.fractals;

import rayTracer.blackSpaces.BlackSpace;
import rayTracer.blackSpaces.BlackTube;
import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.objects.baseObjects.simpleObjects.Cube;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;
import rayTracer.utils.IntersectionManager;

import java.util.ArrayList;
import java.util.List;

public class CubeFractal extends Cube {
    private int deepness;
    private static final int X = 1;
    private static final int Y = 2;
    private static final int Z = 3;
    private final List<BlackSpace> zCylinders = new ArrayList<>();
    private final List<BlackSpace> yCylinders = new ArrayList<>();
    private final List<BlackSpace> xCylinders = new ArrayList<>();

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
        this.deepness = Math.max(deepness, 0);
    }

    private void initBlackObjects(int currentDeepness, double previousCylinderRadius, double currentCylinderRadius) {
        if (currentDeepness > deepness)
            return;
        if(currentDeepness == 1) {
            // Z cylinder
            zCylinders.add(new BlackTube(new Line3D(new Point3D(0, 0, size / 2), new Vector3D(upNormal)), currentCylinderRadius));
            // Y cylinder
            yCylinders.add(new BlackTube(new Line3D(new Point3D(0, size / 2, 0), new Vector3D(rightNormal)), currentCylinderRadius));
            // X cylinder
            xCylinders.add(new BlackTube(new Line3D(new Point3D(size / 2, 0, 0), new Vector3D(backNormal)), currentCylinderRadius));
        } else {
            // Z cylinders
            int size = zCylinders.size();
            for (int i = 0; i < size; ++i) {
                if (zCylinders.get(i).getSize() < previousCylinderRadius + 0.00001 && zCylinders.get(i).getSize() > previousCylinderRadius - 0.00001) {
                    double x = zCylinders.get(i).getPoint().getX();
                    double y = zCylinders.get(i).getPoint().getY();
                    double z = zCylinders.get(i).getPoint().getZ();

                    // top left
                    zCylinders.add(new BlackTube(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y - 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // top
                    zCylinders.add(new BlackTube(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // top right
                    zCylinders.add(new BlackTube(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y + 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // left
                    zCylinders.add(new BlackTube(new Line3D(new Point3D(x, y - 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // right
                    zCylinders.add(new BlackTube(new Line3D(new Point3D(x, y + 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // bottom left
                    zCylinders.add(new BlackTube(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y - 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // bottom
                    zCylinders.add(new BlackTube(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y, z), new Vector3D(upNormal)), currentCylinderRadius));
                    // bottom right
                    zCylinders.add(new BlackTube(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y + 2 * previousCylinderRadius, z), new Vector3D(upNormal)), currentCylinderRadius));
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
                    yCylinders.add(new BlackTube(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y, z - 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // top
                    yCylinders.add(new BlackTube(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y, z), new Vector3D(rightNormal)), currentCylinderRadius));
                    // top right
                    yCylinders.add(new BlackTube(new Line3D(new Point3D(x + 2 * previousCylinderRadius, y, z + 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // left
                    yCylinders.add(new BlackTube(new Line3D(new Point3D(x, y, z - 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // right
                    yCylinders.add(new BlackTube(new Line3D(new Point3D(x, y, z + 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // bottom left
                    yCylinders.add(new BlackTube(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y, z - 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
                    // bottom
                    yCylinders.add(new BlackTube(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y, z), new Vector3D(rightNormal)), currentCylinderRadius));
                    // bottom right
                    yCylinders.add(new BlackTube(new Line3D(new Point3D(x - 2 * previousCylinderRadius, y, z + 2 * previousCylinderRadius), new Vector3D(rightNormal)), currentCylinderRadius));
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
                    xCylinders.add(new BlackTube(new Line3D(new Point3D(x, y - 2 * previousCylinderRadius, z + 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // top
                    xCylinders.add(new BlackTube(new Line3D(new Point3D(x, y, z + 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // top right
                    xCylinders.add(new BlackTube(new Line3D(new Point3D(x, y + 2 * previousCylinderRadius, z + 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // left
                    xCylinders.add(new BlackTube(new Line3D(new Point3D(x, y - 2 * previousCylinderRadius, z), new Vector3D(backNormal)), currentCylinderRadius));
                    // right
                    xCylinders.add(new BlackTube(new Line3D(new Point3D(x, y + 2 * previousCylinderRadius, z), new Vector3D(backNormal)), currentCylinderRadius));
                    // bottom left
                    xCylinders.add(new BlackTube(new Line3D(new Point3D(x, y - 2 * previousCylinderRadius, z - 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // bottom
                    xCylinders.add(new BlackTube(new Line3D(new Point3D(x, y, z - 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
                    // bottom right
                    xCylinders.add(new BlackTube(new Line3D(new Point3D(x, y + 2 * previousCylinderRadius, z - 2 * previousCylinderRadius), new Vector3D(backNormal)), currentCylinderRadius));
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
        List<Intersection> tmp = new ArrayList<>();
        if (c > EPSILON || c < -EPSILON) {
            // UP
            t = (size - z0) / c;
            if(t > 0) {
                x = x0 + a * t;
                y = y0 + b * t;
                z = size;

                if (y >= -size && y <= size && x >= -size && x <= size) {
                    Point3D localIntersection = new Point3D(x, y, z);
                    addIntersection(ray, tmp, localIntersection, realUpNormal, realDownNormal, Z);
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
                    addIntersection(ray, tmp, localIntersection, realDownNormal, realUpNormal, Z);
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
                    addIntersection(ray, tmp, localIntersection, realLeftNormal , realRightNormal, Y);
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
                    addIntersection(ray, tmp, localIntersection, realRightNormal , realLeftNormal, Y);
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
                    addIntersection(ray, tmp, localIntersection, realBackNormal , realFrontNormal, X);
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
                    addIntersection(ray, tmp, localIntersection, realFrontNormal, realBackNormal, X);
                }
            }
        }
        List<Intersection> blackIntersections = new ArrayList<>();
        IntersectionManager.getIntersections(localRay, blackObjects, blackIntersections);
        IntersectionManager.preProcessIntersections(tmp, blackIntersections);
        intersections.addAll(tmp);
    }

    private void addIntersection(Line3D ray, List<Intersection> tmp, Point3D localIntersection, Vector3D n1, Vector3D n2, int type) throws Exception {
        if(type == X && !Cutter.cut(localIntersection, cuts) && !xBlackCylindersContain(localIntersection)) {
            Color color = getColor(localIntersection);
            Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
            double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
            Vector3D realNormal = n1;
            if (Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                realNormal = n2;
            tmp.add(new Intersection(realIntersection, realNormal, color, dist, reflectionRatio, this));
        }
        if(type == Y && !Cutter.cut(localIntersection, cuts) && !yBlackCylindersContain(localIntersection)) {
            Color color = getColor(localIntersection);
            Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
            double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
            Vector3D realNormal = n1;
            if (Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                realNormal = n2;
            tmp.add(new Intersection(realIntersection, realNormal, color, dist, reflectionRatio, this));
        }
        if(type == Z && !Cutter.cut(localIntersection, cuts) && !zBlackCylindersContain(localIntersection)) {
            Color color = getColor(localIntersection);
            Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
            double dist = Point3D.distanceBetween(realIntersection, ray.getPoint());
            Vector3D realNormal = n1;
            if (Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                realNormal = n2;
            tmp.add(new Intersection(realIntersection, realNormal, color, dist, reflectionRatio, this));
        }
    }
}
