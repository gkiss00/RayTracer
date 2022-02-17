package server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import server.model.math.Point3D;

public class Light {
    @JsonIgnore
    private static int _id = 0;
    private int id;
    private Point3D point;
    private String color;

    public Light() {
        this.id = ++_id;
        this.point = new Point3D(0, 0, 0);
        this.color = "#ffffff";
    }

    public Light(LightDTO lightDTO) {
        this.id = ++_id;
        this.point = new Point3D(lightDTO.getPoint());
        this.color = lightDTO.getColor();
    }

    public int getId() {
        return id;
    }

    public Point3D getPoint() {
        return point;
    }

    public String getColor() {
        return color;
    }

    public void update(LightDTO lightDTO) {
        this.point = new Point3D(lightDTO.getPoint());
        this.color = lightDTO.getColor();
    }
}
