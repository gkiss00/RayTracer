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
import java.awt.image.Raster;
import java.io.File;
import java.util.List;

public class Torus extends BaseObject {
    private double r; //minor radius
    private double R; //major radius
    private Raster image;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Torus(double R, double r) {
        super();
        this.r = r;
        this.R = R;
    }

    public Torus(double R, double r, Color color) {
        super(color);
        this.r = r;
        this.R = R;
    }

    public Torus(double R, double r, PatternTypeEnum pattern, Color... colors) {
        super(colors);
        this.setPattern(pattern);
        this.r = r;
        this.R = R;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                SETTERS                *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void setPattern(PatternTypeEnum pattern) {
        this.pattern = pattern;
        if (pattern != PatternTypeEnum.UNIFORM && pattern != PatternTypeEnum.TEXTURE)
            initMissingColors(2);
        else if (pattern == PatternTypeEnum.UNIFORM)
            initMissingColors(1);
        else
            clearColors();
    }

    public void setTexture(String filePath) {
        pattern = PatternTypeEnum.TEXTURE;
        File texture = new File(filePath);
        try {
            image = ImageIO.read(texture).getData();
        } catch (Exception e) {
            System.err.println("Torus error: can not read texture file, set pattern to UNIFORM");
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
                return getColorFromGradient(localIntersection);
            case TEXTURE:
                return getColorFromTexture(localIntersection);
        }
        return null;
    }

    private Color getColorFromVerticalLined(Point3D localIntersection) {
        double columnRadian = 360.0 / columnValue;
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;
        return colors.get(((int)(angle / columnRadian)) % 2);
    }

    private Color getColorFromHorizontalLined(Point3D localIntersection) {
        //REALY COOL BUT NOTHING SERIOUS
        /*double lineRadian = 360.0 / lineValue;
        double z = localIntersection.getZ();
        double y = localIntersection.getY();
        double hypotenuse = Math.sqrt(z * z + y * y);
        double angle = Math.toDegrees(Math.acos(y / hypotenuse));
        if(z < 0)
            angle = 360.0 - angle;
        return colors.get(((int)(angle / lineRadian)) % 2);*/

        // SOH CAH TOA
        double z = localIntersection.getZ();
        double lineRadian = 360.0 / lineValue;
        double alpha = Math.toDegrees(Math.asin(z / r));
        if(z < 0)
            alpha = 360 + alpha;
        return colors.get(((int)(alpha / lineRadian)) % 2);
    }

    private Color getColorFromGrid(Point3D localIntersection) {
        double columnRadian = 360.0 / columnValue;
        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle1 = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle1 = 360 - angle1;

        double z = localIntersection.getZ();
        double lineRadian = 360.0 / lineValue;
        double alpha = Math.toDegrees(Math.asin(z / r));
        if(z < 0)
            alpha = 360 + alpha;
        return colors.get((((int)(alpha / lineRadian)) + ((int)(angle1 / columnRadian))) % 2);
    }

    private Color getColorFromGradient(Point3D localIntersection) {
        double zValue = localIntersection.getX() + r + R;
        double diameter = 2 * (R + r);
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
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();

        double hypotenuse = Math.hypot(localIntersection.getX(), localIntersection.getY());;
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double z = localIntersection.getZ();
        Vector3D tmp = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
        tmp.normalize();
        tmp.times(R);
        Vector3D tmp2 = new Vector3D(localIntersection.getX() - tmp.getX(), localIntersection.getY() - tmp.getY(), localIntersection.getZ());
        tmp.inverse();
        double alpha = Vector3D.angleBetween(tmp, tmp2);
        if(z < 0)
            alpha = 180 + (180 - alpha);

        double[] rgb = new double[3];
        int x = Math.min(imageWidth - 1, (int)(((angle * 4 % 360) / 360) * imageWidth));
        int y = Math.min(imageHeight - 1, (int)((alpha / 360) * imageHeight));
        image.getPixel(x, y, rgb);
        return new Color(rgb[0] / 255, rgb[1] / 255, rgb[0] / 255);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             INTERSECTIONS             *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);
        localRay.normalize();

        double x0 = localRay.getPX();
        double y00 = localRay.getPZ();
        double z0 = localRay.getPY();

        double a = localRay.getVX();
        double b = localRay.getVZ();
        double c = localRay.getVY();

        double a2 = a*a;
        double b2 = b*b;
        double c2 = c*c;

        double x02 = x0*x0;
        double y02 = y00*y00;
        double z02 = z0*z0;

        double R2 = R*R;
        double r2 = r*r;

        double t4 = (a2 + b2 + c2) * (a2 + b2 + c2);
        double t3 = 4 * (a2 + b2 + c2) * (x0*a + y00*b + z0*c);
        double t2 = 2 * (a2 + b2 + c2) * (x02 + y02 + z02 - (r2 + R2)) + 4*(x0*a + y00*b + z0*c) * (x0*a + y00*b + z0*c)+ 4*R2*b2;
        double t1 = 4 * (x02 + y02 + z02 - (r2 + R2)) * (x0*a + y00*b + z0*c) + 8*R2*y00*b;
        double t0 = (x02 + y02 + z02 - (r2 + R2)) * (x02 + y02 + z02 - (r2 + R2)) - 4*R2*(r2 - y02);

        List<Double> solutions = Solver.solve(t4, t3, t2, t1, t0);
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                if(!Cutter.cut(localIntersection, cuts)) {
                    Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    Vector3D localNormal = getNormal(localIntersection);
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }
    }

    public Vector3D getNormal(Point3D localIntersection) {
        Vector3D tmp = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
        tmp.normalize();
        tmp.times(R);
        return new Vector3D(localIntersection.getX() - tmp.getX(), localIntersection.getY() - tmp.getY(), localIntersection.getZ());
    }
}
