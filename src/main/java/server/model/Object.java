package server.model;

import server.model.enums.ObjectTypeEnum;
import server.model.math.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Object {
    private ObjectTypeEnum type;
    private List<Double> values;
    private Point3D coordinates;
    private Point3D scaling;
    private Point3D rotation;

    public Object() {
        this.type = ObjectTypeEnum.SPHERE;
        this.values = new ArrayList<>();
        this.coordinates = new Point3D(0, 0, 0);
        this.scaling = new Point3D(1, 1, 1);
        this.rotation = new Point3D(0, 0, 0);
    }

    public ObjectTypeEnum getType() {
        return type;
    }

    public List<Double> getValues() {
        return values;
    }

    public Point3D getCoordinates() {
        return coordinates;
    }

    public Point3D getScaling() {
        return scaling;
    }

    public Point3D getRotation() {
        return rotation;
    }

    public void update(Object o) {
        this.type = o.type;
        this.values = o.values;
        this.coordinates = o.coordinates;
        this.scaling = o.scaling;
        this.rotation = o.rotation;
    }
}
