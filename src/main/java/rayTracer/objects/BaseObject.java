package rayTracer.objects;

import rayTracer.enums.CutTypeEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;
import rayTracer.utils.Intersection;
import rayTracer.utils.Transform;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseObject {
    protected final static double EPSILON = 0.0001;
    protected List<Color> colors = new ArrayList<>();
    protected List<CutTypeEnum> cuts = new ArrayList<>();
    protected PatternTypeEnum pattern;
    public int lineValue = 10;
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

    public void initMissingColors(int nbColor) {
        while (colors.size() < nbColor) {
            colors.add(new Color());
        }
    }

    public void clearColors(){
        colors.clear();
    }

    public void addColor(Color color) {
        this.colors.add(new Color(color));
    }

    public void addCut(CutTypeEnum cut) {
        if(cuts.contains(cut) == false) {
            cuts.add(cut);
        }
    }

    protected boolean isCut(Point3D point) {
        return Cutter.cut(point, cuts);
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

    public void setPattern(PatternTypeEnum pattern) {
        this.pattern = pattern;
    }
    abstract protected Color getColor(Point3D localIntersection);
    abstract public void hit(Line3D ray, List<Intersection> intersections) throws Exception;
}
