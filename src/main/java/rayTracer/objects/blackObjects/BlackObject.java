package rayTracer.objects.blackObjects;


import rayTracer.enums.CapacityTypeEnum;
import rayTracer.enums.ObjectTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.objects.Obj;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.utils.Intersection;
import rayTracer.utils.Transform;

import java.util.List;


public abstract class BlackObject extends Obj {
    public BlackObject() {
        super();
        capacity = CapacityTypeEnum.FULL;
        type = ObjectTypeEnum.BLACK;
    }
}
