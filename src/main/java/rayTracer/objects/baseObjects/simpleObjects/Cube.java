package rayTracer.objects.baseObjects.simpleObjects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class Cube extends BaseObject {
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
    private BufferedImage bufferedImage;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Cube() {
        super();
        this.size = 1.0 / 2;
    }

    public Cube(double size) {
        super();
        this.size = size / 2;
    }

    public Cube(double size, Color color) {
        super(color);
        this.size = size / 2;
    }

    public Cube(double size, PatternTypeEnum pattern, Color... colors) {
        super(colors);
        this.size = size / 2;
        setPattern(pattern);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                 SETTERS               *

     * * * * * * * * * * * * * * * * * * * * */

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
    public void setPattern(PatternTypeEnum pattern) {
        this.pattern = pattern;
    }

    public void setTexture(String filePath) {
        pattern = PatternTypeEnum.TEXTURE;
        File texture = new File(filePath);
        try {
            bufferedImage = ImageIO.read(texture);
        } catch (Exception e) {
            System.err.println("Plane error: can not read texture file, set pattern to UNIFORM");
            pattern = PatternTypeEnum.UNIFORM;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                COLORS                 *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    protected Color getColor(Point3D localIntersection) {
        switch (pattern) {
            case GRADIENT:
                return getColorFromGradient(localIntersection);
            case VERTICAL_LINED:
                return getColorFromVerticalLined(localIntersection);
            case HORIZONTAL_LINED:
                return getColorFromHorizontalLined(localIntersection);
            case GRID:
                return getColorFromGrid(localIntersection);
            case TEXTURE:
                return getColorFromTexture(localIntersection);
            default:
                return colors.get(0);
        }
    }

    private Color getColorFromGradient(Point3D localIntersection) {
        double z = localIntersection.getZ() + size;
        double cubeSize = 2 * size;
        double colorRatio = cubeSize / (colors.size() - 1);
        int previousColor = Math.min((int)(z / colorRatio), colors.size() - 2);
        int nextColor = Math.min(previousColor + 1, colors.size() - 1);
        double ratio = (z % colorRatio) / colorRatio;
        return new Color(
                colors.get(previousColor).getRed() + (colors.get(nextColor).getRed() - colors.get(previousColor).getRed()) * ratio,
                colors.get(previousColor).getGreen() + (colors.get(nextColor).getGreen() - colors.get(previousColor).getGreen()) * ratio,
                colors.get(previousColor).getBlue() + (colors.get(nextColor).getBlue() - colors.get(previousColor).getBlue()) * ratio,
                colors.get(previousColor).getAlpha() + (colors.get(nextColor).getAlpha() - colors.get(previousColor).getAlpha()) * ratio
        );
    }

    private Color getColorFromVerticalLined(Point3D localIntersection) {
        double lineRadian = 360.0 / columnValue;
        double hypotenuse = Math.sqrt(localIntersection.getX() * localIntersection.getX() + localIntersection.getY() * localIntersection.getY());
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;
        return colors.get(((int)(angle / lineRadian)) % 2);
    }

    private Color getColorFromHorizontalLined(Point3D localIntersection) {
        double zValue = localIntersection.getZ() + size;
        double cubeSize = 2 * size;
        double zRatio = zValue / cubeSize;
        double lineRatio = 1.0 / lineValue;
        return colors.get(((int)(zRatio / lineRatio)) % 2);
    }

    private Color getColorFromGrid(Point3D localIntersection) {
        double cubeSize = 2 * size;
        double lineFrequency = cubeSize / lineValue;
        double columnFrequency = cubeSize / columnValue;

        // TOP AND BOTTOM FACE
        if(
                (localIntersection.getZ() < size + EPSILON && localIntersection.getZ() > size - EPSILON) ||
                (localIntersection.getZ() < -size + EPSILON && localIntersection.getZ() > -size - EPSILON)
        ) {
            double x = localIntersection.getX() + size;
            double y = localIntersection.getY() + size;

            return colors.get(((int)(x / lineFrequency) + (int)(y / columnFrequency)) % 2);
        }
        // LEFT AND RIGHT
        else if(
                (localIntersection.getY() < size + EPSILON && localIntersection.getY() > size - EPSILON) ||
                (localIntersection.getY() < -size + EPSILON && localIntersection.getY() > -size - EPSILON)
        ) {
            double x = localIntersection.getX() + size;
            double z = localIntersection.getZ() + size;

            return colors.get(((int)(x / lineFrequency) + (int)(z / columnFrequency)) % 2);
        }
        // FRONT AND BACK
        else {
            double y = localIntersection.getY() + size;
            double z = localIntersection.getZ() + size;

            return colors.get(((int)(y / lineFrequency) + (int)(z / columnFrequency)) % 2);
        }
    }

    private Color getColorFromTexture(Point3D localIntersection) {
        double cubeSize = 2 * size;
        double x, y;
        // TOP AND BOTTOM FACE
        if(
                (localIntersection.getZ() < size + EPSILON && localIntersection.getZ() > size - EPSILON) ||
                (localIntersection.getZ() < -size + EPSILON && localIntersection.getZ() > -size - EPSILON)
        ) {
            x = localIntersection.getX() + size;
            y = localIntersection.getY() + size;
        }
        // LEFT AND RIGHT
        else if(
                (localIntersection.getY() < size + EPSILON && localIntersection.getY() > size - EPSILON) ||
                (localIntersection.getY() < -size + EPSILON && localIntersection.getY() > -size - EPSILON)
        ) {
            x = localIntersection.getX() + size;
            y = localIntersection.getZ() + size;
        }
        // FRONT AND BACK
        else {
            x = localIntersection.getY() + size;
            y = localIntersection.getZ() + size;
        }

        int imageHeight = bufferedImage.getHeight();
        int imageWidth = bufferedImage.getWidth();
        int xImage = (int)((x / cubeSize) * imageWidth);
        int yImage = (int)((y / cubeSize) * imageHeight);
        xImage = Math.min(xImage, imageWidth - 1);
        yImage = Math.min(yImage, imageHeight - 1);

        int rgb = bufferedImage.getRGB(xImage, yImage);
        java.awt.Color color = new java.awt.Color(rgb, true);
        return new Color((double)color.getRed() / 255, (double)color.getGreen() / 255, (double)color.getBlue() / 255, (double)color.getAlpha() / 255);
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
                    if(!Cutter.cut(localIntersection, cuts)) {
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
                    if(!Cutter.cut(localIntersection, cuts)) {
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
                    if(!Cutter.cut(localIntersection, cuts)) {
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
                    if(!Cutter.cut(localIntersection, cuts)) {
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
                    if(!Cutter.cut(localIntersection, cuts)) {
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
                    if(!Cutter.cut(localIntersection, cuts)) {
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
