package rayTracer.objects.baseObjects.planeSurfaces;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;
import rayTracer.utils.IntersectionManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Square extends BaseObject {
    private static final Vector3D localNormal = new Vector3D(0, 0, 1);
    private final double size;
    private BufferedImage bufferedImage;
    private Vector3D upNormal;
    private Vector3D downNormal;


    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Square(double size) {
        super();
        this.size = size / 2;
    }

    public Square(double size, Color color) {
        super(color);
        this.size = size / 2;
    }

    public Square(double size, PatternTypeEnum pattern, Color... colors) {
        super(colors);
        this.size = size / 2;
        setPattern(pattern);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *               SETTERS                 *

     * * * * * * * * * * * * * * * * * * * * */

    public void setNormal() {
        try {
            this.upNormal = transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
            this.downNormal = Vector3D.inverse(upNormal);
        } catch (Exception e) {

        }
    }

    @Override
    public void setPattern(PatternTypeEnum pattern) {
        if (pattern != PatternTypeEnum.UNIFORM && this.colors.size() < 2) {
            System.err.println("Bad pattern on plane: set to UNIFORM");
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
            System.err.println("Square error: can not read texture file, set pattern to UNIFORM");
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
            case NOISE:
                return getColorFromNoise(localIntersection);
        }
        return null;
    }

    private Color getColorFromVerticalLined(Point3D localIntersection) {
        double columnWidth = 2 * size / columnValue;
        return colors.get(((int)((localIntersection.getY() + size) / columnWidth)) % 2);
    }

    private Color getColorFromHorizontalLined(Point3D localIntersection) {
        double lineWidth = 2 * size / lineValue;
        return colors.get(((int)((localIntersection.getX() + size) / lineWidth)) % 2);
    }

    private Color getColorFromGrid(Point3D localIntersection) {
        double columnWidth = 2 * size / columnValue;
        double lineWidth = 2 * size / lineValue;
        return colors.get((((int)((localIntersection.getY() + size) / columnWidth)) + ((int)((localIntersection.getX() + size) / lineWidth))) % 2);
    }

    private Color getColorFromGradient(Point3D localIntersection) {
        double xRatio = localIntersection.getX() / size;
        double gradientRatio = 1.0 / (colors.size() - 1);
        double ratio = (xRatio % gradientRatio) / gradientRatio;
        int previousColor = (int)(xRatio / gradientRatio);
        int nextColor = previousColor + 1;
        return new Color(
                colors.get(previousColor).getRed() + (colors.get(nextColor).getRed() - colors.get(previousColor).getRed()) * ratio,
                colors.get(previousColor).getGreen() + (colors.get(nextColor).getGreen() - colors.get(previousColor).getGreen()) * ratio,
                colors.get(previousColor).getBlue() + (colors.get(nextColor).getBlue() - colors.get(previousColor).getBlue()) * ratio,
                colors.get(previousColor).getAlpha() + (colors.get(nextColor).getAlpha() - colors.get(previousColor).getAlpha()) * ratio
        );
    }

    private Color getColorFromTexture(Point3D localIntersection) {
        double heightRatio = (localIntersection.getX() + size) / (2 * size);
        double widthRatio = (localIntersection.getY() + size) / (2 * size);

        int imageHeight = bufferedImage.getHeight();
        int imageWidth = bufferedImage.getWidth();

        int x = Math.min(imageWidth - 1, (int)(widthRatio * imageWidth));
        int y = Math.min(imageHeight - 1, imageHeight - (int)(heightRatio * imageHeight));

        int rgb = bufferedImage.getRGB(x, y);
        java.awt.Color color = new java.awt.Color(rgb, true);
        return new Color((double)color.getRed() / 255, (double)color.getGreen() / 255, (double)color.getBlue() / 255, (double)color.getAlpha() / 255);
    }

    private Color getColorFromNoise(Point3D localIntersection) {
        double heightRatio = (localIntersection.getX() + size) / (2 * size);
        double widthRatio = (localIntersection.getY() + size) / (2 * size);

        double tmp = noise.getValue(widthRatio, heightRatio);

        return colors.get(0).reduceOf(Math.abs(tmp));
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             INTERSECTIONS             *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        if ((localRay.getVZ() < EPSILON && localRay.getVZ() > -EPSILON) || (localRay.getPZ() < EPSILON && localRay.getPZ() > -EPSILON))
            return;
        double s = -localRay.getPZ() / localRay.getVZ();

        if(s > EPSILON) {
            List<Intersection> tmp = new ArrayList<>();
            Point3D localIntersection = new Point3D(
                    localRay.getPX() + s * localRay.getVX(),
                    localRay.getPY() + s * localRay.getVY(),
                    localRay.getPZ() + s * localRay.getVZ()
            );
            if(!Cutter.cut(localIntersection, cuts)) {
                if (localIntersection.getX() >= -size && localIntersection.getX() <= size && localIntersection.getY() >= -size && localIntersection.getY() <= size) {
                    Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    Vector3D realNormal = upNormal;
                    if (Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                        realNormal = downNormal;
                    tmp.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
            List<Intersection> blackIntersections = new ArrayList<>();
            IntersectionManager.getIntersections(localRay, blackObjects, blackIntersections);
            IntersectionManager.preProcessIntersections(tmp, blackIntersections);
            intersections.addAll(tmp);
        }
    }
}
