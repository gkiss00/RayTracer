package rayTracer.objects;

import lombok.ToString;
import rayTracer.enums.CapacityTypeEnum;
import rayTracer.enums.ObjectTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.objects.blackObjects.BlackObject;
import rayTracer.utils.Intersection;
import rayTracer.utils.Transform;

import java.util.ArrayList;
import java.util.List;

@ToString
public abstract class Obj {
    protected  static int _id = 0;
    protected int id = 0;
    protected final static double EPSILON = 0.0001;
    protected Transform transform;
    protected CapacityTypeEnum capacity = CapacityTypeEnum.EMPTY;
    protected ObjectTypeEnum type = ObjectTypeEnum.BASE;
    public double density = 1;
    protected List<Obj> blackObjects = new ArrayList<>();
    public double[] t;

    /* * * * * * * * * * * * * * * * * * * * *

     *             CONSTRUCTORS              *

     * * * * * * * * * * * * * * * * * * * * */

    public Obj() {
        this.id = ++_id;
        try {
            this.transform = new Transform();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                UTILS                  *

     * * * * * * * * * * * * * * * * * * * * */

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

    public void addBlackObject(BlackObject obj) {
        blackObjects.add(obj);
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

    /* * * * * * * * * * * * * * * * * * * * *

     *                EQUALS                 *

     * * * * * * * * * * * * * * * * * * * * */

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof BaseObject))
            return false;
        BaseObject object = (BaseObject) o;
        return this.id == object.id;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *               ABSTRACT                *

     * * * * * * * * * * * * * * * * * * * * */

    abstract public void hit(Line3D ray, List<Intersection> intersections) throws Exception;
}
