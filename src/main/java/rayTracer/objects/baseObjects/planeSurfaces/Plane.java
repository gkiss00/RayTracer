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
import java.awt.image.Raster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Plane extends BaseObject {
    private BufferedImage bufferedImage;
    private Raster normalMap = null;
    private double textureWidth = 100;
    private static final Vector3D localNormal = new Vector3D(0, 0, 1);
    private Vector3D upNormal;
    private Vector3D downNormal;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Plane() {
        super();
    }

    public Plane(Color color) {
        super(color);
    }

    public Plane(PatternTypeEnum pattern, Color... colors) {
        super(colors);
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
        if (pattern == PatternTypeEnum.GRADIENT) {
            System.err.println("Bad pattern on plane: Set to UNIFORM");
            this.pattern = PatternTypeEnum.UNIFORM;
        } else if (
                this.colors.size() < 2 &&
                        (pattern == PatternTypeEnum.VERTICAL_LINED || pattern == PatternTypeEnum.HORIZONTAL_LINED || pattern == PatternTypeEnum.GRID)
        ) {
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
            System.err.println("Plane error: can not read texture file, set pattern to UNIFORM");
            pattern = PatternTypeEnum.UNIFORM;
        }
    }

    public void setNormalMap(String filePath) {
        File normals = new File(filePath);
        try {
            normalMap = ImageIO.read(normals).getData();
        } catch (Exception e) {
            System.err.println("Plane error: can not read normal file");
            System.exit(0);
            normalMap = null;
            setNormal();
        }
    }

    public void setTextureWidth(double textureWidth) {
        this.textureWidth = textureWidth;
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
                return null;
            case TEXTURE:
                return getColorFromTexture(localIntersection);
        }
        return null;
    }

    private Color getColorFromVerticalLined(Point3D localIntersection) {
        double yValue = localIntersection.getY();
        if (yValue < 0)
            yValue = Math.abs(yValue) + columnValue;
        return colors.get(((int)(yValue / columnValue)) % 2);
    }

    private Color getColorFromHorizontalLined(Point3D localIntersection) {
        double xValue = localIntersection.getX();
        if (xValue < 0)
            xValue = Math.abs(xValue) + lineValue;
        return colors.get(((int)(xValue / lineValue)) % 2);
    }

    private Color getColorFromGrid(Point3D localIntersection) {
        double yValue = localIntersection.getY();
        if (yValue < 0)
            yValue = Math.abs(yValue) + columnValue;

        double xValue = localIntersection.getX();
        if (xValue < 0)
            xValue = Math.abs(xValue) + lineValue;
        return colors.get((((int)(xValue / lineValue)) + (int)(yValue / columnValue)) % 2);
    }

    private Color getColorFromTexture(Point3D localIntersection) {
        int imageHeight = bufferedImage.getHeight();
        int imageWidth = bufferedImage.getWidth();
        double textureHeight = (double)imageHeight / (double)imageWidth * textureWidth;
        double yRatio = (Math.abs(localIntersection.getY()) % textureWidth) / textureWidth;
        double xRatio = (Math.abs(localIntersection.getX()) % textureHeight) / textureHeight;

        int y = localIntersection.getX() >= 0 ?
                imageHeight - (int)(xRatio * imageHeight) >= imageHeight ?  imageHeight - 1 : imageHeight - (int)(xRatio * imageHeight) :
                imageHeight - (int)(imageHeight - (xRatio * imageHeight)) >= imageHeight ? imageHeight - 1 : imageHeight - (int)(imageHeight - (xRatio * imageHeight));
        int x = localIntersection.getY() >= 0 ?
                (int)(yRatio * imageWidth) :
                (int)(imageWidth - (yRatio * imageWidth));

        int rgb = bufferedImage.getRGB(x, y);
        java.awt.Color color = new java.awt.Color(rgb, true);
        return new Color((double)color.getRed() / 255, (double)color.getGreen() / 255, (double)color.getBlue() / 255, (double)color.getAlpha() / 255);
    }

    private Color getColorFromNoise(Point3D localIntersection) {
        double tmp = noise.getValue(localIntersection.getX(), localIntersection.getY());

        return colors.get(0).reduceOf(tmp);
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
                Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                Vector3D normal = getNormal(localIntersection);
                Vector3D realNormal = transform.apply(normal, MatrixTransformEnum.TO_REAL);
                if (Vector3D.angleBetween(realNormal, ray.getVector()) < 90)
                    realNormal.inverse();
                realNormal.normalize();
                tmp.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
            }
            List<Intersection> blackIntersections = new ArrayList<>();
            IntersectionManager.getIntersections(localRay, blackObjects, blackIntersections);
            IntersectionManager.preProcessIntersections(tmp, blackIntersections);
            intersections.addAll(tmp);
        }
    }

    private Vector3D getNormal(Point3D localIntersection) {
        if (normalMap == null)
            return localNormal;
        int imageHeight = normalMap.getHeight();
        int imageWidth = normalMap.getWidth();
        double textureHeight = (double)imageHeight / (double)imageWidth * textureWidth;
        double yRatio = (Math.abs(localIntersection.getY()) % textureWidth) / textureWidth;
        double xRatio = (Math.abs(localIntersection.getX()) % textureHeight) / textureHeight;

        int y = localIntersection.getX() >= 0 ?
                imageHeight - (int)(xRatio * imageHeight) >= imageHeight ?  imageHeight - 1 : imageHeight - (int)(xRatio * imageHeight) :
                imageHeight - (int)(imageHeight - (xRatio * imageHeight)) >= imageHeight ? imageHeight - 1 : imageHeight - (int)(imageHeight - (xRatio * imageHeight));
        int x = localIntersection.getY() >= 0 ?
                (int)(yRatio * imageWidth) :
                (int)(imageWidth - (yRatio * imageWidth));

        double[] xyz = new double[3];
        normalMap.getPixel(x, y, xyz);
        Vector3D normal = new Vector3D(xyz[0] - 127.5, xyz[1] - 127.5, xyz[2] - 127.5);
        return normal;
    }
}
