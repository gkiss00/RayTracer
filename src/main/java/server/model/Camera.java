package server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import server.model.math.Point3D;
import server.model.math.Vector3D;

public class Camera {
    @JsonIgnore
    private static int _id = 0;
    private int id;
    private Point3D pointOfVue;
    private Vector3D direction;
    private Vector3D up;
    private double angle;

    public Camera () {
        this.id = ++_id;
        this.pointOfVue = new Point3D(0, 0, 0);
        this.direction = new Vector3D(1, 0, 0);
        this.up = new Vector3D(0, 0, 1);
        this.angle = 90;
    }

    public Camera (CameraDTO cameraDTO) {
        this.id = ++_id;
        this.pointOfVue = new Point3D(cameraDTO.getPointOfVue());
        this.direction = new Vector3D(cameraDTO.getDirection());
        this.up = new Vector3D(cameraDTO.getUp());
        this.angle = cameraDTO.getAngle();
    }

    public int getId() {
        return id;
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

    public void update(CameraDTO cam) {
        this.pointOfVue = new Point3D(cam.getPointOfVue());
        this.direction = new Vector3D(cam.getDirection());
        this.up = new Vector3D(cam.getUp());
        this.angle = cam.getAngle();
    }
}
