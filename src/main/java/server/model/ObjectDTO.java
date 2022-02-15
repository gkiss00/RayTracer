package server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import server.model.enums.ObjectTypeEnum;
import server.model.math.Point3D;

import java.util.ArrayList;
import java.util.List;

public class ObjectDTO {
    private ObjectTypeEnum type;
    private List<Double> values;
    private Point3D coordinates;
    private Point3D scaling;
    private Point3D rotation;
    private String[] colors;

    public ObjectDTO() {
        this.type = ObjectTypeEnum.SPHERE;
        this.values = new ArrayList<>();
        this.coordinates = new Point3D(0, 0, 0);
        this.scaling = new Point3D(1, 1, 1);
        this.rotation = new Point3D(0, 0, 0);
        this.colors = new String[0];
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

    public String[] getColors() {
        return colors;
    }
}
