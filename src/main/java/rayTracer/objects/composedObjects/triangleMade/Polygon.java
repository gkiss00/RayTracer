package rayTracer.objects.composedObjects.triangleMade;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Triangle;
import rayTracer.math.Vector3D;
import rayTracer.objects.BaseObject;
import rayTracer.utils.Color;
import rayTracer.utils.Intersection;

import java.util.List;

public class Polygon extends BaseObject {
    private final List<Triangle> triangles;

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

    @Override
    protected Color getColor(Point3D localIntersection) {
        switch (pattern) {
            case GRADIENT:
                return getColorFromGradient(localIntersection);
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
