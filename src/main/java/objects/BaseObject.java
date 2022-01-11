package objects;

import enums.PatternTypeEnum;
import math.Line3D;
import math.Point3D;
import utils.Color;
import utils.Intersection;
import utils.Transform;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseObject {
    protected final static double EPSILON = 0.0001;
    protected List<Color> colors = new ArrayList<>();
    protected PatternTypeEnum pattern;
    protected int lineValue = 10;
    protected int columnValue = 10;
    protected Transform transform;
    protected double reflectionRatio = 0;

    public BaseObject() {
        this.colors.add(new Color());
        init();
    }

    public BaseObject(Color color) {
        this.colors.add(new Color(color));
        init();
    }

    public BaseObject(Color... colors) {
        for (int i = 0; i < colors.length; ++i)
            this.colors.add(new Color(colors[i]));
        init();
    }

    private void init() {
        this.pattern = PatternTypeEnum.UNIFORM;
        try {
            this.transform = new Transform();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public double getReflectionRatio() {
        return this.reflectionRatio;
    }

    public void setReflectionRatio(double reflectionRatio) {
        this.reflectionRatio = reflectionRatio < 0 ? 0 : reflectionRatio > 1 ? 1 : reflectionRatio;
    }

    public void updateMatrices(
            double alpha,
            double beta,
            double gama,
            double scalingX,
            double scalingY,
            double scalingZ,
            double translationX,
            double translationY,
            double translationZ
    ) {
        try {
            transform.updateMatrices(alpha, beta, gama, scalingX, scalingY, scalingZ, translationX, translationY, translationZ);
        } catch (Exception e) {}
    }

    abstract public void setPattern(PatternTypeEnum pattern);
    abstract protected Color getColor(Point3D localIntersection);
    abstract public void hit(Line3D ray, List<Intersection> intersections) throws Exception;
}
