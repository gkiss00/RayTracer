package server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import server.model.math.Point3D;
import server.model.math.Vector3D;

public class CameraDTO {
    private Point3D pointOfVue;
    private Vector3D direction;
    private Vector3D up;
    private double angle;

    public CameraDTO () {
        this.pointOfVue = new Point3D(0, 0, 0);
        this.direction = new Vector3D(1, 0, 0);
        this.up = new Vector3D(0, 0, 1);
        this.angle = 90;
    }

    public Point3D getPointOfVue() {
        return pointOfVue;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public Vector3D getUp() {
        return up;
    }

    public double getAngle() {
        return angle;
    }
}
