package rayTracer.objects;

import rayTracer.enums.CapacityTypeEnum;
import rayTracer.enums.ObjectTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Intersection;
import rayTracer.utils.Transform;

import java.util.List;

public abstract class Obj {
    protected  static int _id = 0;
    protected int id = 0;
    protected final static double EPSILON = 0.0001;
    protected Transform transform;
    protected CapacityTypeEnum capacity = CapacityTypeEnum.EMPTY;
    protected ObjectTypeEnum type = ObjectTypeEnum.BASE;
    public double density = 1;

    public Obj() {
        this.id = ++_id;
        try {
            this.transform = new Transform();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }
    public CapacityTypeEnum getCapacity() {
        return capacity;
    }

    public ObjectTypeEnum getType() {
        return type;
    }

    public double getDensity() {
        return this.density;
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

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof BaseObject))
            return false;
        BaseObject object = (BaseObject) o;
        return this.id == object.id;
    }

    abstract public void hit(Line3D ray, List<Intersection> intersections) throws Exception;
}
