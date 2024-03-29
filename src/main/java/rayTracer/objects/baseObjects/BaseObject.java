package rayTracer.objects.baseObjects;

import lombok.ToString;
import rayTracer.enums.CapacityTypeEnum;
import rayTracer.enums.CutTypeEnum;
import rayTracer.enums.NoiseDimensionEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.math.Point3D;
import rayTracer.noiser.Noise;
import rayTracer.objects.Obj;
import rayTracer.utils.Color;
import rayTracer.utils.Cutter;

import java.util.ArrayList;
import java.util.List;

@ToString
public abstract class BaseObject extends Obj {
    public List<Color> colors = new ArrayList<>();
    protected List<CutTypeEnum> cuts = new ArrayList<>();
    protected PatternTypeEnum pattern;
    public int lineValue = 10;
    public int columnValue = 10;
    protected double reflectionRatio = 0;
    public Noise noise;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public BaseObject() {
        super();
        this.colors.add(new Color());
        init();
    }

    public BaseObject(Color color) {
        super();
        this.colors.add(new Color(color));
        init();
    }

    public BaseObject(Color... colors) {
        super();
        for (int i = 0; i < colors.length; ++i)
            this.colors.add(new Color(colors[i]));
        init();
    }

    private void init() {
        this.pattern = PatternTypeEnum.UNIFORM;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                COLORS                 *

     * * * * * * * * * * * * * * * * * * * * */

    public void clearColors(){
        colors.clear();
    }

    public void addColor(Color color) {
        this.colors.add(new Color(color));
    }

    public void initMissingColors(int nbColor) {
        while (colors.size() < nbColor) {
            colors.add(new Color());
        }
    }

    public Color getColorAt(Point3D realIntersection) {
        return null;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                 CUTS                  *

     * * * * * * * * * * * * * * * * * * * * */

    public void addCut(CutTypeEnum cut) {
        if(!cuts.contains(cut)) {
            cuts.add(cut);
        }
    }

    protected boolean isCut(Point3D point) {
        return Cutter.cut(point, cuts);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *           REFLECTION RATIO            *

     * * * * * * * * * * * * * * * * * * * * */

    public double getReflectionRatio() {
        return this.reflectionRatio;
    }

    public void setReflectionRatio(double reflectionRatio) {
        this.reflectionRatio = reflectionRatio < 0 ? 0 : reflectionRatio > 1 ? 1 : reflectionRatio;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                PATTERN                *

     * * * * * * * * * * * * * * * * * * * * */

    public PatternTypeEnum getPattern() {
        return pattern;
    }

    public void setPattern(PatternTypeEnum pattern) {
        this.pattern = pattern;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *          NOISE AND CAPACITY           *

     * * * * * * * * * * * * * * * * * * * * */

    public NoiseDimensionEnum getNoiseDimension() {
        return noise.getDimension();
    }

    public void setCapacity(CapacityTypeEnum capacity) {
        this.capacity = capacity;
    }


    /* * * * * * * * * * * * * * * * * * * * *

     *               ABSTRACT                *

     * * * * * * * * * * * * * * * * * * * * */

    abstract protected Color getColor(Point3D localIntersection);
}
