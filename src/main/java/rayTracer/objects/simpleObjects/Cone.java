package rayTracer.objects.simpleObjects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Solver;
import rayTracer.math.Vector3D;
import rayTracer.objects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;

import java.util.List;

public class Cone extends BaseObject {
    private double angle;

    public Cone(double angle) {
        super();
        this.angle = angle;
    }

    public Cone(double angle, Color color) {
        super(color);
        this.angle = angle;
    }

    public Cone(double angle, PatternTypeEnum pattern, Color... colors) {
        super(colors);
        this.angle = angle;
        setPattern(pattern);
    }

    @Override
    public void setPattern(PatternTypeEnum pattern) {
        if (pattern == PatternTypeEnum.GRADIENT) {
            System.err.println("Bad pattern on cylinder: Set to UNIFORM");
            this.pattern = PatternTypeEnum.UNIFORM;
        } else if (
                this.colors.size() < 2 &&
                        (pattern == PatternTypeEnum.VERTICAL_LINED || pattern == PatternTypeEnum.HORIZONTAL_LINED || pattern == PatternTypeEnum.GRID)
        ) {
            System.err.println("Bad pattern on cylinder: set to UNIFORM");
            this.pattern = PatternTypeEnum.UNIFORM;
        } else {
            this.pattern = pattern;
        }
    }

    @Override
    protected Color getColor(Point3D localIntersection) {
        switch (pattern) {
            case UNIFORM:
                return colors.get(0);
            case VERTICAL_LINED:
                return getColorFromVerticalLined(localIntersection);
            case HORIZONTAL_LINED:
                return getColorFromHorizontalLined(localIntersection);
            case GRID:
                return getColorFromGrid(localIntersection);
            case GRADIENT:
                return null;
        }
        return null;
    }

    private Color getColorFromVerticalLined(Point3D localIntersection) {
        double lineRadian = 360.0 / columnValue;
        double hypotenus = Math.sqrt(localIntersection.getX() * localIntersection.getX() + localIntersection.getY() * localIntersection.getY());
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenus));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;
        return colors.get(((int)(angle / lineRadian)) % 2);
    }

    private Color getColorFromHorizontalLined(Point3D localIntersection) {
        double zValue = localIntersection.getZ();
        if(zValue < 0)
            zValue = -zValue + lineValue;
        return colors.get(((int)(zValue / lineValue)) % 2);
    }

    private Color getColorFromGrid(Point3D localIntersection) {
        double lineRadian = 360.0 / columnValue;
        double hypotenus = Math.sqrt(localIntersection.getX() * localIntersection.getX() + localIntersection.getY() * localIntersection.getY());
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenus));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double zValue = localIntersection.getZ();
        if(zValue < 0)
            zValue = -zValue + lineValue;
        return colors.get(((int)(zValue / lineValue) + (int)(angle / lineRadian)) % 2);
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        double a, b, c;
        a = localRay.getVX() * localRay.getVX() +
                localRay.getVY() * localRay.getVY() -
                localRay.getVZ() * localRay.getVZ() * Math.tan(Math.toRadians(angle)) * Math.tan(Math.toRadians(angle));

        b = 2 * localRay.getPX() * localRay.getVX() +
                2 * localRay.getPY() * localRay.getVY() -
                2 * localRay.getPZ() * localRay.getVZ() * Math.tan(Math.toRadians(angle)) * Math.tan(Math.toRadians(angle));

        c = localRay.getPX() * localRay.getPX() +
                localRay.getPY() * localRay.getPY()-
                localRay.getPZ() * Math.tan(Math.toRadians(angle)) * localRay.getPZ() * Math.tan(Math.toRadians(angle));

        List<Double> solutions = Solver.solve(a, b, c);
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                if(!Cutter.cut(localIntersection, cuts)) {
                    Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    double h = Math.sqrt(localIntersection.getX() * localIntersection.getX() + localIntersection.getY() * localIntersection.getY()) / Math.tan(Math.toRadians(90 - angle));
                    if (localIntersection.getZ() < 0)
                        h = -h;
                    Vector3D localNormal = new Vector3D(localIntersection.getX(), localIntersection.getY(), -h);
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }
    }
}
