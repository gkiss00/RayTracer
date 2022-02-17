package server.model;

import server.model.math.Point3D;

public class LightDTO {
    private Point3D point;
    private String color;

    public LightDTO() {
        this.point = new Point3D(0, 0, 0);
        this.color = "#ffffff";
    }

    public Point3D getPoint() {
        return point;
    }

    public String getColor() {
        return color;
    }
}
