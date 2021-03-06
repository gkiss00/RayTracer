package rayTracer.objects.planeSurfaces;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class Disk extends BaseObject {
    private static final Point3D localOrigin = new Point3D(0, 0, 0);
    private static final Vector3D localNormal = new Vector3D(0, 0, 1);
    private final double internalRadius;
    private final double externalRadius;
    private Vector3D realNormal;
    private BufferedImage bufferedImage;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Disk(double internalRadius, double externalRadius) {
        super();
        this.internalRadius = internalRadius;
        this.externalRadius = externalRadius;
    }

    public Disk(double internalRadius, double externalRadius, Color color) {
        super(color);
        this.internalRadius = internalRadius;
        this.externalRadius = externalRadius;
    }

    public Disk(double internalRadius, double externalRadius, PatternTypeEnum pattern, Color... colors) {
        super(colors);
        this.internalRadius = internalRadius;
        this.externalRadius = externalRadius;
        setPattern(pattern);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *               SETTERS                 *

     * * * * * * * * * * * * * * * * * * * * */

    public void setNormal() {
        try {
            this.realNormal = transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
        } catch (Exception e) {

        }
    }

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
            System.err.println("Bad pattern on disk: set to UNIFORM");
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
            System.err.println("Disk error: can not read texture file, set pattern to UNIFORM");
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
        double distanceFromCenter = Point3D.distanceBetween(localOrigin, localIntersection) - internalRadius;
        double radius = externalRadius - internalRadius;
        double distanceRatio = distanceFromCenter / radius;
        double lineRatio = 1.0 / lineValue;
        return colors.get(((int)(distanceRatio / lineRatio)) % 2);
    }

    private Color getColorFromGrid(Point3D localIntersection) {
        double lineRadian = 360.0 / columnValue;
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double distanceFromCenter = Point3D.distanceBetween(localOrigin, localIntersection) - internalRadius;
        double radius = externalRadius - internalRadius;
        double distanceRation = distanceFromCenter / radius;
        double lineRatio = 1.0 / lineValue;
        return colors.get((((int)(distanceRation / lineRatio)) + ((int)(angle / lineRadian))) % 2);
    }

//    private Color getColorFromGradient(Point3D localIntersection) {
//        double distanceFromCenter = Point3D.distanceBetween(localOrigin, localIntersection) - internalRadius;
//        double radius = externalRadius - internalRadius;
//        double distanceRatio = distanceFromCenter / radius;
//        double gradientRatio = 1.0 / (colors.size() - 1);
//        double ratio = (distanceRatio % gradientRatio) / gradientRatio;
//        int previousColor = (int)(distanceRatio / gradientRatio);
//        int nextColor = previousColor + 1;
//        return new Color(
//                colors.get(previousColor).getRed() + (colors.get(nextColor).getRed() - colors.get(previousColor).getRed()) * ratio,
//                colors.get(previousColor).getGreen() + (colors.get(nextColor).getGreen() - colors.get(previousColor).getGreen()) * ratio,
//                colors.get(previousColor).getBlue() + (colors.get(nextColor).getBlue() - colors.get(previousColor).getBlue()) * ratio
//        );
//    }

    private Color getColorFromGradient(Point3D localIntersection) {
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;
        double angleRatio = angle / 360;
        double gradientRatio = 1.0 / (colors.size() - 1);
        double ratio = (angleRatio % gradientRatio) / gradientRatio;
        int previousColor = (int)(angleRatio / gradientRatio);
        int nextColor = previousColor + 1;
        return new Color(
                colors.get(previousColor).getRed() + (colors.get(nextColor).getRed() - colors.get(previousColor).getRed()) * ratio,
                colors.get(previousColor).getGreen() + (colors.get(nextColor).getGreen() - colors.get(previousColor).getGreen()) * ratio,
                colors.get(previousColor).getBlue() + (colors.get(nextColor).getBlue() - colors.get(previousColor).getBlue()) * ratio,
                colors.get(previousColor).getAlpha() + (colors.get(nextColor).getAlpha() - colors.get(previousColor).getAlpha()) * ratio
        );
    }

    public Color getColorFromTexture(Point3D localIntersection) {
        int imageHeight = bufferedImage.getHeight();
        int imageWidth = bufferedImage.getWidth();

        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double width = externalRadius - internalRadius;
        double yRatio = (Math.hypot(localIntersection.getX(), localIntersection.getY()) - internalRadius) / width;

        int x = Math.min(imageWidth - 1, (int)(((angle * 4 % 360) / 360) * imageWidth));
        int y = Math.min(imageHeight - 1, imageHeight - (int)(yRatio * imageHeight));

        int rgb = bufferedImage.getRGB(x, y);
        java.awt.Color color = new java.awt.Color(rgb, true);
        return new Color((double)color.getRed() / 255, (double)color.getGreen() / 255, (double)color.getBlue() / 255, (double)color.getAlpha() / 255);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *            INTERSECTIONS              *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        if ((localRay.getVZ() < EPSILON && localRay.getVZ() > -EPSILON) || (localRay.getPZ() < EPSILON && localRay.getPZ() > -EPSILON))
            return;
        double s = -localRay.getPZ() / localRay.getVZ();
        if(s > EPSILON) {
            Point3D localIntersection = new Point3D(
                    localRay.getPX() + s * localRay.getVX(),
                    localRay.getPY() + s * localRay.getVY(),
                    localRay.getPZ() + s * localRay.getVZ()
            );
            if(!Cutter.cut(localIntersection, cuts)) {
                double distFromCenter = Point3D.distanceBetween(localOrigin, localIntersection);
                if (distFromCenter > internalRadius && distFromCenter < externalRadius) {
                    Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    //Vector3D realNormal = transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }
    }
}
