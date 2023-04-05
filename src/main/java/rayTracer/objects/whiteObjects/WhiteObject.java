package rayTracer.objects.whiteObjects;

import rayTracer.enums.CapacityTypeEnum;
import rayTracer.enums.ObjectTypeEnum;
import rayTracer.objects.Obj;

public abstract class WhiteObject extends Obj {
    public WhiteObject() {
        super();
        capacity = CapacityTypeEnum.EMPTY;
        type = ObjectTypeEnum.WHITE;
    }
}
