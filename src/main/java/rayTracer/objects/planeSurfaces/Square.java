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

import java.util.List;

public class Square extends BaseObject {
    private static final Vector3D localNormal = new Vector3D(0, 0, 1);
    private Vector3D realNormal;
    private double size;

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
            this.realNormal = transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
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
            Point3D localIntersection = new Point3D(
                    localRay.getPX() + s * localRay.getVX(),
                    localRay.getPY() + s * localRay.getVY(),
                    localRay.getPZ() + s * localRay.getVZ()
            );
            if(!Cutter.cut(localIntersection, cuts)) {
                if (localIntersection.getX() >= -size && localIntersection.getX() <= size && localIntersection.getY() >= -size && localIntersection.getY() <= size) {
                    Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }
    }
}
