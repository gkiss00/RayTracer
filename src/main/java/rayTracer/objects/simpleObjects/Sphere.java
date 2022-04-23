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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.List;

public class Sphere extends BaseObject {
    private double radius;
    private BufferedImage bufferedImage;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Sphere(double radius) {
        super();
        this.radius = radius;
    }

    public Sphere(double radius, Color color) {
        super(color);
        this.radius = radius;
    }

    public Sphere(double radius, PatternTypeEnum pattern, Color... colors) {
        super(colors);
        this.radius = radius;
        setPattern(pattern);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                SETTERS                *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void setPattern(PatternTypeEnum pattern) {
        if(
                colors.size() < 2 &&
                (
                        pattern == PatternTypeEnum.VERTICAL_LINED ||
                        pattern == PatternTypeEnum.HORIZONTAL_LINED ||
                        pattern == PatternTypeEnum.GRID ||
                        pattern == PatternTypeEnum.GRADIENT

                )
        ) {
            System.err.println("Bad pattern on sphere: set to UNIFORM");
            this.pattern = PatternTypeEnum.UNIFORM;
        } else {
            this.pattern = pattern;
        }
    }

    public void setTexture(String filePath) {
        pattern = PatternTypeEnum.TEXTURE;
        File texture = new File(filePath);
        try {
            bufferedImage = ImageIO.read(texture);
        } catch (Exception e) {
            System.err.println("Sphere error: can not read texture file, set pattern to UNIFORM");
            pattern = PatternTypeEnum.UNIFORM;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                COLORS                 *

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
                return getColorFromGradient(localIntersection);
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
        double zValue = localIntersection.getZ() + radius;
        double diameter = 2 * radius;
        double zRatio = zValue / diameter;
        double lineRatio = 1.0 / lineValue;
        return colors.get(((int)(zRatio / lineRatio)) % 2);
    }

    private Color getColorFromGrid(Point3D localIntersection) {
        double lineRadian = 360.0 / columnValue;
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double zValue = localIntersection.getZ() + radius;
        double diameter = 2 * radius;
        double zRatio = zValue / diameter;
        double lineRatio = 1.0 / lineValue;
        return colors.get(((int)(zRatio / lineRatio) + (int)(angle / lineRadian)) % 2);
    }

    private Color getColorFromGradient(Point3D localIntersection) {
        double zValue = localIntersection.getZ() + radius;
        double diameter = 2 * radius;
        double zRatio = zValue / diameter;
        double gradientRatio = 1.0 / (colors.size() - 1);
        double ratio = (zRatio % gradientRatio) / gradientRatio;
        int previousColor = (int)(zRatio / gradientRatio);
        int nextColor = previousColor + 1;
        return new Color(
                colors.get(previousColor).getRed() + (colors.get(nextColor).getRed() - colors.get(previousColor).getRed()) * ratio,
                colors.get(previousColor).getGreen() + (colors.get(nextColor).getGreen() - colors.get(previousColor).getGreen()) * ratio,
                colors.get(previousColor).getBlue() + (colors.get(nextColor).getBlue() - colors.get(previousColor).getBlue()) * ratio,
                colors.get(previousColor).getAlpha() + (colors.get(nextColor).getAlpha() - colors.get(previousColor).getAlpha()) * ratio
        );
    }

    private Color getColorFromTexture(Point3D localIntersection) {
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double zValue = localIntersection.getZ() + radius;
        double diameter = 2 * radius;
        double zRatio = zValue / diameter;

        int imageHeight = bufferedImage.getHeight();
        int imageWidth = bufferedImage.getWidth();

        int x = (int)(angle / 360 * imageWidth) >= imageWidth ? imageWidth - 1 : (int)(angle / 360 * imageWidth);
        int y = imageHeight - (int)(zRatio * imageHeight) >= imageHeight ? imageHeight - 1: imageHeight - (int)(zRatio * imageHeight);
        int rgb = bufferedImage.getRGB(x, y);
        java.awt.Color color = new java.awt.Color(rgb, true);
        return new Color((double)color.getRed() / 255, (double)color.getGreen() / 255, (double)color.getBlue() / 255, (double)color.getAlpha() / 255);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             INTERSECTIONS             *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception{
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
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                if(!Cutter.cut(localIntersection, cuts)) {
                    Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    Vector3D localNormal = new Vector3D(localIntersection.getX(), localIntersection.getY(), localIntersection.getZ());
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }
    }
}
