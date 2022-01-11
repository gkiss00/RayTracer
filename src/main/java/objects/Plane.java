package objects;

import enums.MatrixTransformEnum;
import enums.PatternTypeEnum;
import math.Line3D;
import math.Point3D;
import math.Vector3D;
import utils.Color;
import utils.Intersection;

import javax.imageio.ImageIO;
import java.awt.image.Raster;
import java.io.File;
import java.util.List;

public class Plane extends BaseObject{
    private Raster image;
    private double textureWidth = 20;
    private static final Vector3D localNormal = new Vector3D(0, 0, 1);
    private Vector3D realNormal;

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

    public void setNormal() {
        try {
            this.realNormal = transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
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
            image = ImageIO.read(texture).getData();
        } catch (Exception e) {
            System.err.println("Sphere error: can not read texture file, set pattern to UNIFORM");
            pattern = PatternTypeEnum.UNIFORM;
        }
    }

    public void setTextureWidth(double textureWidth) {
        this.textureWidth = textureWidth;
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
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        double textureHeight = (double)imageHeight / (double)imageWidth * textureWidth;
        double yRatio = (Math.abs(localIntersection.getY()) % textureWidth) / textureWidth;
        double xRatio = (Math.abs(localIntersection.getX()) % textureHeight) / textureHeight;

        int y = localIntersection.getX() >= 0 ?
                imageHeight - (int)(xRatio * imageHeight) >= imageHeight ?  imageHeight - 1 : imageHeight - (int)(xRatio * imageHeight) :
                imageHeight - (int)(imageHeight - (xRatio * imageHeight)) >= imageHeight ? imageHeight - 1 : imageHeight - (int)(imageHeight - (xRatio * imageHeight));
        int x = localIntersection.getY() >= 0 ?
                (int)(yRatio * imageWidth) :
                (int)(imageWidth - (yRatio * imageWidth));

        double[] rgb = new double[3];
        image.getPixel(x, y, rgb);
        return new Color(rgb[0] / 255, rgb[1] / 255, rgb[2] / 255);
    }

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
            Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
            intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio));
        }
    }
}
