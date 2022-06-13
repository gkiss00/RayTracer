package rayTracer.factories;

import rayTracer.enums.PolygonTypeEnum;
import rayTracer.math.Point3D;
import rayTracer.math.Triangle;
import rayTracer.objects.composedObjects.triangleMade.Polygon;

import java.util.ArrayList;
import java.util.List;

public class PolygonFactory {
    public static Polygon createPolygon(PolygonTypeEnum type, double... values) {
        switch (type) {
            case STAR:
                return createStar(type, values);
            case TETRAHEDRON:
                return createTetrahedron(type, values);
            case DIAMOND:
                return createDiamond(type, values);
            case TETRAHEDRON_FRACTAL:
                return createTetrahedronFractal(type, values);
            case MOBIUS_TAPE:
                return createMobiusTape(type, values);
            case TORUS:
                return createTorus(type, values);
            default:
                return null;
        }
    }

    /**
     *
     * @param values values
     * first value: number of branches
     * second value: small radius
     * third value: big radius
     * fourth value: thickness
     * @return star
     */
    private static Polygon createStar(PolygonTypeEnum type, double... values) {
        double branch = values[0];
        double r = values[1];
        double R = values[2];
        double thick = values[3];
        List<Point3D> sup_points = new ArrayList<>();
        List<Point3D> inf_points = new ArrayList<>();
        Point3D top = new Point3D(0, 0, thick / 2);
        Point3D bottom = new Point3D(0, 0, -thick / 2);

        double angle = 360.0 / branch;

        for(int i = 0; i < branch; ++i) {
            double angle1 = i * angle;
            double angle2 = angle1 + (angle / 2);
            sup_points.add(new Point3D(R * Math.cos(Math.toRadians(angle1)), R * Math.sin(Math.toRadians(angle1)), 0));
            inf_points.add(new Point3D(r * Math.cos(Math.toRadians(angle2)), r * Math.sin(Math.toRadians(angle2)), 0));
        }

        List<Triangle> triangles = new ArrayList<>();

        for(int i = 0; i < branch; ++i) {
            triangles.add(new Triangle(sup_points.get(i), inf_points.get(i), top));
            triangles.add(new Triangle(sup_points.get(i), inf_points.get(i), bottom));
            triangles.add(new Triangle(inf_points.get(i), sup_points.get((int)((i + 1) % branch)), top));
            triangles.add(new Triangle(inf_points.get(i), sup_points.get((int)((i + 1) % branch)), bottom));
        }

        return new Polygon(type, values, triangles);
    }

    /**
     *
     * @param values
     * first value: radius
     * @return tetrahedron
     */
    private static Polygon createTetrahedron(PolygonTypeEnum type, double... values) {
        double r = values[0];
        double h = r / 3.0;
        double rPrime = r * Math.sqrt(8.0 / 9.0);

        double H = r + h;
        Point3D top = new Point3D(0, 0, H / 2);
        Point3D p1 = new Point3D(rPrime * Math.cos(Math.toRadians(0.0)), rPrime * Math.sin(Math.toRadians(0.0)), -H / 2);
        Point3D p2 = new Point3D(rPrime * Math.cos(Math.toRadians(120.0)), rPrime * Math.sin(Math.toRadians(120.0)), -H / 2);
        Point3D p3 = new Point3D(rPrime * Math.cos(Math.toRadians(240.0)), rPrime * Math.sin(Math.toRadians(240.0)), -H / 2);

        List<Triangle> triangles = new ArrayList<>();

        triangles.add(new Triangle(p1, p2, p3));
        triangles.add(new Triangle(p1, p2, top));
        triangles.add(new Triangle(p1, p3, top));
        triangles.add(new Triangle(p2, p3, top));

        return new Polygon(type, values, triangles);
    }

    /**
     *
     * @param values
     * first value: top nbFaces
     * second value: top radius
     * third value: middle radius
     * fourth value: top height
     * fifth value: bottom height
     * @return diamond
     */
    private static Polygon createDiamond(PolygonTypeEnum type, double... values) {
        int nbFaces = (int)values[0];
        double topRadius = values[1];
        double middleRadius = values[2];
        double topHeight = values[3];
        double bottomHeight = values[4];
        double size = (topHeight + bottomHeight);

        List<Point3D> superiorPoints = new ArrayList<>();
        List<Point3D> inferiorPoints = new ArrayList<>();

        Point3D top = new Point3D(0, 0, size / 2);
        Point3D bottom = new Point3D(0, 0, -size / 2);

        for(int i = 0; i < nbFaces; ++i) {
            double angle = Math.toRadians(i * (360.0 / nbFaces));
            superiorPoints.add(new Point3D(topRadius * Math.cos(angle), topRadius * Math.sin(angle), size / 2));
            inferiorPoints.add(new Point3D(middleRadius * Math.cos(angle), middleRadius * Math.sin(angle), (size / 2) - topHeight));
        }
        List<Triangle> triangles = new ArrayList<>();

        for (int i = 0; i < nbFaces; ++i) {
            triangles.add(new Triangle(top, superiorPoints.get(i), superiorPoints.get((i + 1) % nbFaces)));
            triangles.add(new Triangle(bottom, inferiorPoints.get(i), inferiorPoints.get((i + 1) % nbFaces)));
            triangles.add(new Triangle(superiorPoints.get(i), superiorPoints.get((i + 1) % nbFaces), inferiorPoints.get(i)));
            triangles.add(new Triangle(inferiorPoints.get(i), inferiorPoints.get((i + 1) % nbFaces), superiorPoints.get((i + 1) % nbFaces)));
        }

        return new Polygon(type, values, triangles);
    }

    /**
     *
     * @param values
     * first value: radius
     * second value: deepness
     * @return tetrahedron
     */
    private static Polygon createTetrahedronFractal(PolygonTypeEnum type, double... values) {
        double r = values[0];

        List<Point3D> points = new ArrayList<>();
        points.add(new Point3D(0, 0, 0));

        r = addPoint(0, (int)values[1], r, points);
        double h = r / 3.0;
        double rPrime = r * Math.sqrt(8.0 / 9.0);
        double H = r + h;

        List<Triangle> triangles = new ArrayList<>();
        for (Point3D p : points) {
            Point3D top = new Point3D(p.getX(), p.getY(), p.getZ() + H / 2);
            Point3D p1 = new Point3D(p.getX() + rPrime * Math.cos(Math.toRadians(0.0)), p.getY() + rPrime * Math.sin(Math.toRadians(0.0)), p.getZ() - H / 2);
            Point3D p2 = new Point3D(p.getX() + rPrime * Math.cos(Math.toRadians(120.0)), p.getY() + rPrime * Math.sin(Math.toRadians(120.0)), p.getZ() - H / 2);
            Point3D p3 = new Point3D(p.getX() + rPrime * Math.cos(Math.toRadians(240.0)), p.getY() + rPrime * Math.sin(Math.toRadians(240.0)), p.getZ() - H / 2);
            triangles.add(new Triangle(p1, p2, p3));
            triangles.add(new Triangle(p1, p2, top));
            triangles.add(new Triangle(p1, p3, top));
            triangles.add(new Triangle(p2, p3, top));
        }

        return new Polygon(type, values, triangles);
    }

    private static double addPoint(int currentLevel, int maxLevel, double r, List<Point3D> currentPoints) {
        if (currentLevel >= maxLevel)
            return r;

        r = r / 2;

        List<Point3D> newPoints = new ArrayList<>();
        for (Point3D point : currentPoints) {
            Point3D p1 = new Point3D(point.getX(), point.getY(), point.getZ() + r);
            Point3D p2 = new Point3D(point.getX() + r * Math.cos(Math.toRadians(0.0)), point.getY() + r * Math.sin(Math.toRadians(0.0)), point.getZ() - r);
            Point3D p3 = new Point3D(point.getX() + r * Math.cos(Math.toRadians(120.0)), point.getY() + r * Math.sin(Math.toRadians(120.0)), point.getZ() - r);
            Point3D p4 = new Point3D(point.getX() + r * Math.cos(Math.toRadians(240.0)), point.getY() + r * Math.sin(Math.toRadians(240.0)), point.getZ() - r);
            newPoints.add(p1);
            newPoints.add(p2);
            newPoints.add(p3);
            newPoints.add(p4);
        }
        currentPoints.clear();
        currentPoints.addAll(newPoints);
        return addPoint(currentLevel + 1, maxLevel, r, currentPoints);
    }

    /**
     *
     * @param values
     * first value: radius
     * secondValue: width
     * third value: precision
     * @return mobius tape
     */
    private static Polygon createMobiusTape(PolygonTypeEnum type, double... values) {
        double radius = values[0];
        double width = values[1];
        int precision = (int)values[2];
        double semiWidth = width / 2;

        List<Point3D> sup_points = new ArrayList<>();
        List<Point3D> inf_points = new ArrayList<>();
        for(int i = 0; i < precision; ++i) {
            double positionAngle = (double)i * (360.0 / precision);
            double rotationAngle = positionAngle / 2.0;

            double sin = Math.sin(Math.toRadians(rotationAngle)) * (semiWidth);
            double cos = Math.cos(Math.toRadians(rotationAngle)) * (semiWidth);

            double h = sin;
            double r1 = radius + cos;
            double r2 = radius - cos;

            double x1 = r1 * Math.sin(Math.toRadians(positionAngle));
            double x2 = r2 * Math.sin(Math.toRadians(positionAngle));

            double y1 = r1 * Math.cos(Math.toRadians(positionAngle));
            double y2 = r2 * Math.cos(Math.toRadians(positionAngle));


            sup_points.add(new Point3D(x1, y1, h));
            inf_points.add(new Point3D(x2, y2, -h));
        }

        List<Triangle> triangles = new ArrayList<>();

        for(int i = 0; i < precision - 1; ++i) {
            triangles.add(new Triangle(sup_points.get(i), sup_points.get((i + 1) % precision), inf_points.get(i)));
            triangles.add(new Triangle(inf_points.get(i), inf_points.get((i + 1) % precision), sup_points.get((i + 1) % precision)));
        }
        triangles.add(new Triangle(sup_points.get(0), inf_points.get(0), inf_points.get(precision - 1)));
        triangles.add(new Triangle(inf_points.get(precision - 1), sup_points.get(precision - 1), inf_points.get(0)));

        return new Polygon(type, values, triangles);
    }

    private static Polygon createTorus(PolygonTypeEnum type, double... values) {
        double R = values[0];
        double r = values[1];
        int precision = (int)(values[2]);
        int layers = (int)(values[3]);

        List<List<Point3D>> points = new ArrayList<>();

        double angleOfLayer = 360.0 / layers;
        double angleOfPrecision = 360.0 / precision;
        for(int k = 0; k < layers; ++k) {
            List<Point3D> tmp = new ArrayList<>();
            double angle1 = (double)k * angleOfLayer;
            double h = r * Math.sin(Math.toRadians(angle1));
            double r1 = r * Math.cos(Math.toRadians(angle1));
            for (int i = 0; i < precision; ++i) {
                double angle2 = (double)i * angleOfPrecision;
                tmp.add(new Point3D(((R - r1) * (Math.cos(Math.toRadians(angle2)))), ((R - r1) * (Math.sin(Math.toRadians(angle2)))), h));
            }
            points.add(tmp);
        }

        List<Triangle> triangles = new ArrayList<>();

        for(int k = 0; k < layers; ++k){
            for(int i = 0; i < precision; ++i) {
                triangles.add(new Triangle(points.get(k).get(i), points.get(k).get((i + 1) % precision), points.get((k + 1) % layers).get(i)));
                triangles.add(new Triangle(points.get((k + 1) % layers).get(i), points.get((k + 1) % layers).get((i + 1) % precision), points.get(k).get((i + 1) % precision)));
            }
        }

        return new Polygon(type, values, triangles);
    }
}
