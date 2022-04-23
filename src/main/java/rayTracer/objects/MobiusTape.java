package rayTracer.objects;

import rayTracer.enums.MatrixTransformEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Solver;
import rayTracer.math.Vector3D;
import rayTracer.utils.Color;
import rayTracer.utils.Intersection;

import java.util.List;

public class MobiusTape extends BaseObject{
    private double width;

    public MobiusTape(double width) {
        super();
        this.width = width;
    }

    public MobiusTape(double width, Color color) {
        super(color);
        this.width = width;
    }

    public MobiusTape(double width, PatternTypeEnum pattern, Color... colors) {
        super(colors);
        this.width = width;
        setPattern(pattern);
    }

    @Override
    public void setPattern(PatternTypeEnum pattern) {

    }

    @Override
    protected Color getColor(Point3D localIntersection) {
        return colors.get(0);
    }

    @Override
    public void hit(Line3D ray, List<Intersection> intersections) throws Exception {
        Line3D localRay = transform.apply(ray, MatrixTransformEnum.TO_LOCAL);

        double x0 = localRay.getPX();
        double y0 = localRay.getPY();
        double z0 = localRay.getPZ();
        double a = localRay.getVX();
        double b = localRay.getVY();
        double c = localRay.getVZ();

        double a2 = a * a;
        double b2 = b * b;
        double c2 = c * c;
        double x02 = x0 * x0;
        double y02 = y0 * y0;
        double z02 = z0 * z0;

        double t3 = b * b * b + a2*b - 2*a2*c - 2*b2*c + b*c2;
        double t2 = 3*y0*b2 + 2*x0*a*b + a2*y0 - 4*x0*a*c - 2*a2*z0 -4*y0*b*c - 2*b2*z0 + y0*c2 + 2*b*z0*c - 2*a*c;
        double t1 = 3*y02*b + x02*b + 2*x0*y0*a -2*x02*c - 4*x0*a*z0 - 2*y02*c - 4*y0*b*z0 + 2*y0*z0*c + b*z02 - 2*x0*c - 2*a*z0 - b;
        double t0 = y0 * y0 * y0 + x02*y0 - 2*x02*z0 - 2*y02*z0 + y0*z02 - 2*x0*z0 - y0;

        List<Double> solutions = Solver.solve(t3, t2, t1, t0);
        for (int i = 0; i < solutions.size(); ++i) {
            if (solutions.get(i) > EPSILON) {
                Point3D localIntersection = new Point3D(
                        localRay.getPX() + localRay.getVX() * solutions.get(i),
                        localRay.getPY() + localRay.getVY() * solutions.get(i),
                        localRay.getPZ() + localRay.getVZ() * solutions.get(i)
                );
                Vector3D tmp = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
                tmp.normalize();
                Point3D pointOnRadius = new Point3D(tmp.getX(), tmp.getY(), 0);
                //if the point is in the torus and cartesian match parametric
                if(Point3D.distanceBetween(pointOnRadius, localIntersection) < .4){
                    Point3D realIntersection = this.transform.apply(localIntersection, MatrixTransformEnum.TO_REAL);
                    Vector3D localNormal = getTan(localIntersection);
                    if (Vector3D.angleBetween(localRay.getVector(), localNormal) < 90)
                        localNormal.inverse();
                    Vector3D realNormal = this.transform.apply(localNormal, MatrixTransformEnum.TO_REAL);
                    intersections.add(new Intersection(realIntersection, realNormal, getColor(localIntersection), Point3D.distanceBetween(ray.getPoint(), realIntersection), reflectionRatio, this));
                }
            }
        }
    }

    private Vector3D getTan(Point3D localIntersection){
        double x = localIntersection.getX();
        double y = localIntersection.getY();
        double z = localIntersection.getZ();

        double fx = 2 * x * y - 2 * z - 4 * x * z;
        double fy = x * x + z * z + 3 * y * y - 1 - 4 * y * z;
        double fz = 2 * z * y - 2 * x - 2 * x * x - 2 * y * y;
        return new Vector3D(fx, fy, fz);
    }

    private Point3D getNextPoint(Point3D localIntersection, double delta) {
        double x = localIntersection.getX();
        double y = localIntersection.getY();
        double hypotenuses = x * x + y * y;
        double angle = Math.toDegrees(Math.acos(y / hypotenuses));
        if(x < 0)
            angle = 360.0 - angle;
        angle += delta;
        double newX = Math.sin(Math.toRadians(angle)) * hypotenuses;
        double newY = Math.cos(Math.toRadians(angle)) * hypotenuses;
        return new Point3D(newX, newY, localIntersection.getZ());
    }

    //Cartesian equation
    //x²y + z²y + y³ - y - 2xz - 2x²z - 2y²z = 0
    //Parametric equation
    //x = (1 + (t/2)cos(v/2))cos(v)
    //y = (1 + (t/2)cos(v/2))sin(v)
    //z = (t/2)sin(v/2)
    private boolean checkPoint(Point3D localIntersection) {

        double CONST = 0.00001;
        //get values
        double x = localIntersection.getX();
        double y = -localIntersection.getY();
        double z = localIntersection.getZ();
        //get the angle for the parametric equation
        double hypotenuse = Math.sqrt(x * x + y * y);
        double angle = Math.acos(x / hypotenuse);
        if(y < 0)
            angle = Math.PI * 2 - angle;

        //get the t value
        Vector3D tmp = new Vector3D(localIntersection.getX(), localIntersection.getY(), 0);
        tmp.normalize();
        Point3D test = new Point3D(tmp.getX(), tmp.getY(), 0);
        double t = Point3D.distanceBetween(localIntersection, test);

        double newX1 = (1 + (t) * Math.cos(angle/2)) * Math.cos(angle);
        double newX2 = (1 + (-t) * Math.cos(angle/2)) * Math.cos(angle);
        double newY1 = (1 + (t) * Math.cos(angle/2)) * Math.sin(angle);
        double newY2 = (1 + (-t) * Math.cos(angle/2)) * Math.sin(angle);
        double newZ1 = (t) * Math.sin(angle / 2);
        double newZ2 = -newZ1;
        return true;

    }
}
