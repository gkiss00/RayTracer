package rayTracer.objects.baseObjects.composedObjects.triangleMade;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Triangle;
import rayTracer.math.Vector3D;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Intersection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class Polygon extends BaseObject {
    private final List<Triangle> triangles;
    private BufferedImage bufferedImage;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Polygon(List<Triangle> triangles) {
        super();
        this.triangles = triangles;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *               SETTERS                 *

     * * * * * * * * * * * * * * * * * * * * */

    public void setTexture(String filePath) {
        pattern = PatternTypeEnum.TEXTURE;
        File texture = new File(filePath);
        try {
            bufferedImage = ImageIO.read(texture);
        } catch (Exception e) {
            System.err.println("Polygon error: can not read texture file, set pattern to UNIFORM");
            pattern = PatternTypeEnum.UNIFORM;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *               COLORS                  *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    protected Color getColor(Point3D localIntersection) {
        switch (pattern) {
            case GRADIENT:
                return getColorFromGradient(localIntersection);
            case TEXTURE:
                return getColorFromTexture(localIntersection);
        }
        return colors.get(0);
    }

    private Color getColorFromGradient(Point3D localIntersection) {
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double colorRatio = 360.0 / (colors.size() - 1);
        double ratio = (angle % colorRatio) / colorRatio;

        int previousColor = Math.min((int)(angle / colorRatio), colors.size() - 2);
        int nextColor = Math.min(previousColor + 1, colors.size() - 1);
        return new Color(
                colors.get(previousColor).getRed() + (colors.get(nextColor).getRed() - colors.get(previousColor).getRed()) * ratio,
                colors.get(previousColor).getGreen() + (colors.get(nextColor).getGreen() - colors.get(previousColor).getGreen()) * ratio,
                colors.get(previousColor).getBlue() + (colors.get(nextColor).getBlue() - colors.get(previousColor).getBlue()) * ratio,
                colors.get(previousColor).getAlpha() + (colors.get(nextColor).getAlpha() - colors.get(previousColor).getAlpha()) * ratio
        );
    }

    private Color getColorFromTexture(Point3D localIntersection) {
        double radius = 40;
        double width = 20;

        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        Point3D pointOnPlane = new Point3D(localIntersection.getX(), localIntersection.getY(), 0);

        Vector3D tmp = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
        tmp.normalize();
        tmp.times(radius);

        Point3D pointOnRadius = new Point3D(tmp.getX(), tmp.getY(), 0);
        Point3D oo = new Point3D(0, 0, 0);
        double distToOO = Point3D.distanceBetween(oo, pointOnPlane);
        double distToRadius = Point3D.distanceBetween(localIntersection, pointOnRadius);

        double ratio;
        if (angle < 180) {
            if (distToOO > radius)
                ratio = 0.5 + (distToRadius / width);
            else
                ratio = 0.5 - (distToRadius / width);
        } else {
            if (distToOO < radius)
                ratio = 0.5 + (distToRadius / width);
            else
                ratio = 0.5 - (distToRadius / width);
        }

        int imageHeight = bufferedImage.getHeight();
        int imageWidth = bufferedImage.getWidth();

        int x = Math.min(imageWidth - 1, (int)(((angle * 4 % 360) / 360) * imageWidth));
        int y = Math.min(imageHeight - 1, imageHeight - (int)(ratio * imageHeight));

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

        for (Triangle tr: triangles) {
            Point3D localIntersection = tr.hit(localRay);
            if(localIntersection != null) {
                Point3D realIntersection = transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                Vector3D localNormal = tr.getNormal();
                if (Vector3D.angleBetween(localRay.getVector(), localNormal) < 90)
                    localNormal = Vector3D.inverse(localNormal);
                Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                intersections.add(new Intersection(
                        realIntersection,
                        realNormal,
                        getColor(localIntersection),
                        Point3D.distanceBetween(ray.getPoint(), realIntersection),
                        this.reflectionRatio,
                        this
                ));
            }
        }
    }
}
