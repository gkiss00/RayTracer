package server.model;

import server.model.math.Point3D;
import server.model.math.Vector3D;

public class Camera {
    private Point3D pointOfVue;
    private Vector3D direction;
    private Vector3D up;
    private double angle;

    public Camera () {
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

    public void setPointOfVue(Point3D pointOfVue) {
        this.pointOfVue = pointOfVue;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    public void setUp(Vector3D up) {
        this.up = up;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void update(Camera cam) {
        this.pointOfVue = new Point3D(cam.pointOfVue);
        this.direction = new Vector3D(cam.direction);
        this.up = new Vector3D(cam.up);
        this.angle = cam.angle;
    }
}
