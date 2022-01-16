package objects.simpleObjects;

import enums.MatrixTransformEnum;
import enums.PatternTypeEnum;
import math.Line3D;
import math.Point3D;
import math.Solver;
import math.Vector3D;
import objects.BaseObject;
import utils.Color;
import utils.Intersection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.Raster;
import java.io.File;
import java.util.List;

public class Torus extends BaseObject {
    private double r; //minor radius
    private double R; //major radius
    private Raster image;

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
            System.err.println("Sphere error: can not read texture file, set pattern to UNIFORM");
            pattern = PatternTypeEnum.UNIFORM;
        }
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
                return getColorFromGradient(localIntersection);
            case TEXTURE:
                return getColorFromGrid(localIntersection);
        }
        return null;
    }

    private Color getColorFromVerticalLined(Point3D localIntersection) {
        double columnRadian = 360.0 / columnValue;
        double hypotenuse = Math.sqrt(localIntersection.getX() * localIntersection.getX() + localIntersection.getY() * localIntersection.getY());
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
        double hypotenuse = Math.sqrt(localIntersection.getX() * localIntersection.getX() + localIntersection.getY() * localIntersection.getY());
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

        double hypotenuse = Math.sqrt(localIntersection.getX() * localIntersection.getX() + localIntersection.getY() * localIntersection.getY());
        double angle = Math.toDegrees(Math.acos(localIntersection.getY() / hypotenuse));
        if(localIntersection.getX() < 0)
            angle = 360 - angle;

        double z = localIntersection.getZ();
        double lineRadian = 360.0 / lineValue;
        double alpha = Math.toDegrees(Math.asin(z / r));
        if(z < 0)
            alpha = 360 + alpha;

        double[] rgb = new double[3];
        int x = angle / 360 > 1 ? imageWidth - 1 : (int)((angle / 360) * imageWidth);
        int y = alpha / 360 > 1 ? imageHeight - 1: (int)(((angle / 360)) * imageHeight);
        image.getPixel(x, y, rgb);
        return new Color(rgb[0], rgb[1], rgb[0]);
    }

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
                Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                Vector3D localNormal = getNormal(localIntersection);
                Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio));
            }
        }
    }

    public Vector3D getNormal(Point3D localIntersection) {
        Vector3D tmp = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
        tmp.normalize();
        tmp.times(R);
        return new Vector3D(localIntersection.getX() - tmp.getX(), localIntersection.getY() - tmp.getY(), localIntersection.getZ());
        /*double x = localIntersection.getX();
        double y = localIntersection.getY();
        double hypotenuse = Math.sqrt(x * x + y * y);
        double angle = Math.toDegrees(Math.acos(y / hypotenuse));
        if(x < 0)
            angle = 360.0 - angle;

        double distX = Math.sin(Math.toRadians(angle)) * R;
        double distY = Math.cos(Math.toRadians(angle)) * R;
        return  new Vector3D(x - distX, y -distY, localIntersection.getZ());*/
    }
}