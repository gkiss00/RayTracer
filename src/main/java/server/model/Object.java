package server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import server.model.enums.ObjectTypeEnum;
import server.model.enums.PatternTypeEnum;
import server.model.math.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Object {
    @JsonIgnore
    private static int _id = 0;
    private int id;
    private ObjectTypeEnum type;
    private List<Double> values;
    private Point3D coordinates;
    private Point3D scaling;
    private Point3D rotation;
    private PatternTypeEnum pattern;
    private String[] colors;
    private int reflexion;

    public Object() {
        this.id = ++_id;
        this.type = ObjectTypeEnum.SPHERE;
        this.values = new ArrayList<>();
        this.coordinates = new Point3D(0, 0, 0);
        this.scaling = new Point3D(1, 1, 1);
        this.rotation = new Point3D(0, 0, 0);
        this.pattern = PatternTypeEnum.UNIFORM;
        this.colors = new String[0];
        this.reflexion = 0;
    }

    public Object(ObjectDTO objectDTO) {
        this.id = ++_id;
        this.type = objectDTO.getType();
        this.values = new ArrayList<>(objectDTO.getValues());
        this.coordinates = new Point3D(objectDTO.getCoordinates());
        this.scaling = new Point3D(objectDTO.getScaling());
        this.rotation = new Point3D(objectDTO.getRotation());
        this.pattern = objectDTO.getPattern();
        this.colors = new String[objectDTO.getColors().length];
        System.arraycopy(objectDTO.getColors(), 0, this.colors,0, objectDTO.getColors().length);
        this.reflexion = objectDTO.getReflexion();
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

    public PatternTypeEnum getPattern() {
        return pattern;
    }

    public String[] getColors() {
        return colors;
    }

    public int getReflexion() {
        return reflexion;
    }

    public int getId() {
        return id;
    }

    public void update(ObjectDTO objectDTO) {
        this.type = objectDTO.getType();
        this.values = new ArrayList<>(objectDTO.getValues());
        this.coordinates = new Point3D(objectDTO.getCoordinates());
        this.scaling = new Point3D(objectDTO.getScaling());
        this.rotation = new Point3D(objectDTO.getRotation());
        this.pattern = objectDTO.getPattern();
        this.colors = new String[objectDTO.getColors().length];
        System.arraycopy(objectDTO.getColors(), 0, this.colors,0, objectDTO.getColors().length);
        this.reflexion = objectDTO.getReflexion();
    }
}
