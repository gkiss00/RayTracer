package rayTracer.objects.baseObjects.simpleObjects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Solver;
import rayTracer.math.Vector3D;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;
import rayTracer.utils.IntersectionManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Cylinder extends BaseObject {
    private double radius;
    private Raster image;
    private BufferedImage bufferedImage;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Cylinder(double radius) {
        super();
        this.radius = radius;
    }

    public Cylinder(double radius, Color color) {
        super(color);
        this.radius = radius;
    }

    public Cylinder(double radius, PatternTypeEnum pattern, Color... colors) {
        super(colors);
        this.radius = radius;
        setPattern(pattern);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                SETTERS                *

     * * * * * * * * * * * * * * * * * * * * */

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

    public void setTexture(String filePath) {
        pattern = PatternTypeEnum.TEXTURE;
        File texture = new File(filePath);
        try {
            image = ImageIO.read(texture).getData();
            bufferedImage = ImageIO.read(texture);
        } catch (Exception e) {
            System.err.println("Sphere error: can not read texture file, set pattern to UNIFORM");
            pattern = PatternTypeEnum.UNIFORM;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                 COLORS                *

     * * * * * * * * * * * * * * * * * * * * */

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
            case TEXTURE:
                return getColorFromTexture(localIntersection);
        }
        return null;
    }

    private Color getColorFromVerticalLined(Point3D localIntersection) {
        double lineRadian = 360.0 / columnValue;
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
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
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double zValue = localIntersection.getZ();
        if(zValue < 0)
            zValue = -zValue + lineValue;
        return colors.get(((int)(zValue / lineValue) + (int)(angle / lineRadian)) % 2);
    }

    private Color getColorFromTexture(Point3D localIntersection) {
        int imageHeight = bufferedImage.getHeight();
        int imageWidth = bufferedImage.getWidth();
        double circumference = 2 * Math.PI * radius;
        double textureHeight = (double)imageHeight / (double)imageWidth * circumference;
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double zRatio = (Math.abs(localIntersection.getZ()) % textureHeight) / textureHeight;

        int x = (int)(angle / 360 * imageWidth) >= imageWidth ? imageWidth - 1 : (int)(angle / 360 * imageWidth);
        int y = localIntersection.getZ() >= 0 ?
                imageHeight - (int)(zRatio * imageHeight) >= imageHeight ? imageHeight - 1: imageHeight - (int)(zRatio * imageHeight) :
                (int)(zRatio * imageHeight) >= imageHeight ? imageHeight - 1: (int)(zRatio * imageHeight);

        int rgb = bufferedImage.getRGB(x, y);
        java.awt.Color color = new java.awt.Color(rgb, true);
        return new Color((double)color.getRed() / 255, (double)color.getGreen() / 255, (double)color.getBlue() / 255, (double)color.getAlpha() / 255);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             INTERSECTIONS             *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        double a, b, c;
        a = localRay.getVX() * localRay.getVX()+
                localRay.getVY() * localRay.getVY();

        b = 2 * localRay.getPX() * localRay.getVX() +
                2 * localRay.getPY() * localRay.getVY();

        c = localRay.getPX() * localRay.getPX() +
                localRay.getPY() * localRay.getPY() -
                radius * radius;

        List<Double> solutions = Solver.solve(a, b, c);
        List<Intersection> tmp = new ArrayList<>();
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                if(!Cutter.cut(localIntersection, cuts)) {
                    Vector3D localNormal = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    if(Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                        realNormal.inverse();
                    tmp.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }
        List<Intersection> blackIntersections = new ArrayList<>();
        IntersectionManager.getIntersections(localRay, blackObjects, blackIntersections);
        IntersectionManager.preProcessIntersections(tmp, blackIntersections);
        intersections.addAll(tmp);
    }
}
