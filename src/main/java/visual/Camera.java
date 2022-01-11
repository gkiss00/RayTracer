package visual;

import math.*;

public class Camera {
    private Point3D pointOfVue;
    private Vector3D direction;
    private Vector3D up;
    private double angle;
    private int height;
    private int width;

    private Vector3D U;
    private Vector3D V;
    private Point3D screenCenter;

    public Camera(Point3D pointOfVue, Vector3D direction, Vector3D up, double angle) {
        this.pointOfVue = new Point3D(pointOfVue);
        this.direction = new Vector3D(direction);
        this.up = new Vector3D(up);
        this.angle = angle;
    }

    public Point3D getPointOfVue() {
        return pointOfVue;
    }

    public void update(int height, int width) {
        this.height = height;
        this.width = width;

        direction.normalize();
        U = Vector3D.crossProduct(up, direction);
        U.normalize();
        V = Vector3D.crossProduct(U, direction);
        V.normalize();

        U.times(width);
        V.times(height);

        double L = width / Math.tan(Math.toRadians(angle / 2));
        screenCenter = new Point3D(pointOfVue);
        Vector3D tmp = new Vector3D(direction);
        tmp.times(L);
        screenCenter.add(tmp);
    }

    public Point3D getPoint(int h, int w) {
        return getPoint((double)(h - ((double)height / 2)) / height, (double)(w - ((double)width / 2)) / width);
    }

    public Point3D getPoint(double h, double w) {
        Vector3D UTmp = new Vector3D(U);
        UTmp.times(w);
        Vector3D VTmp = new Vector3D(V);
        VTmp.times(h);
        Point3D res = new Point3D(screenCenter);
        res.add(UTmp);
        res.add(VTmp);
        return res;
    }
}
