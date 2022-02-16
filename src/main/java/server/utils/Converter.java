package server.utils;

import rayTracer.objects.BaseObject;
import rayTracer.objects.planeSurfaces.Plane;
import rayTracer.objects.simpleObjects.Cube;
import rayTracer.objects.simpleObjects.Cylinder;
import rayTracer.objects.simpleObjects.Sphere;
import server.model.Camera;
import server.model.Object;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                CAMERA                 *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    public static List<rayTracer.visual.Camera> getCameras(List<Camera> cameras) {
        List<rayTracer.visual.Camera> newCameras = new ArrayList<>();

        for (int i = 0; i < cameras.size(); ++i) {
            newCameras.add(new rayTracer.visual.Camera(
                    new rayTracer.math.Point3D(
                            cameras.get(i).getPointOfVue().getX(),
                            cameras.get(i).getPointOfVue().getY(),
                            cameras.get(i).getPointOfVue().getZ()
                    ),
                    new rayTracer.math.Vector3D(
                            cameras.get(i).getDirection().getX(),
                            cameras.get(i).getDirection().getY(),
                            cameras.get(i).getDirection().getZ()
                    ),
                    new rayTracer.math.Vector3D(
                            cameras.get(i).getUp().getX(),
                            cameras.get(i).getUp().getY(),
                            cameras.get(i).getUp().getZ()
                    ),
                    cameras.get(i).getAngle()
            ));
        }

        return newCameras;
    }

    /* * * * * * * * * * * * * * * * * * * * *
     *                                       *
     *                OBJECT                 *
     *                                       *
     * * * * * * * * * * * * * * * * * * * * */

    public static List<BaseObject> getObjects(List<Object> objects) {
        List<BaseObject> baseObjects = new ArrayList<>();

        for (int i = 0; i < objects.size(); ++i) {
            switch (objects.get(i).getType()) {
                case SPHERE:
                    baseObjects.add(getSphere(objects.get(i)));
                    break;
                case PLANE:
                    baseObjects.add(getPlane(objects.get(i)));
                    break;
                case CYLINDER:
                    baseObjects.add(getCylinder(objects.get(i)));
                    break;
                case CUBE:
                    baseObjects.add(getCube(objects.get(i)));
                    break;
            }
        }

        return baseObjects;
    }

    private static BaseObject getSphere(Object obj) {
        Sphere sphere = new Sphere(obj.getValues().get(0));
        sphere.updateMatrices(
                obj.getRotation().getX(),
                obj.getRotation().getY(),
                obj.getRotation().getZ(),
                obj.getScaling().getX(),
                obj.getScaling().getY(),
                obj.getScaling().getZ(),
                obj.getCoordinates().getX(),
                obj.getCoordinates().getY(),
                obj.getCoordinates().getZ()
        );

        return sphere;
    }

    private static BaseObject getPlane(Object obj) {
        Plane plane = new Plane();
        plane.updateMatrices(
                obj.getRotation().getX(),
                obj.getRotation().getY(),
                obj.getRotation().getZ(),
                obj.getScaling().getX(),
                obj.getScaling().getY(),
                obj.getScaling().getZ(),
                obj.getCoordinates().getX(),
                obj.getCoordinates().getY(),
                obj.getCoordinates().getZ()
        );
        plane.setNormal();

        return plane;
    }

    private static BaseObject getCylinder(Object obj) {
        Cylinder cylinder = new Cylinder(obj.getValues().get(0));
        cylinder.updateMatrices(
                obj.getRotation().getX(),
                obj.getRotation().getY(),
                obj.getRotation().getZ(),
                obj.getScaling().getX(),
                obj.getScaling().getY(),
                obj.getScaling().getZ(),
                obj.getCoordinates().getX(),
                obj.getCoordinates().getY(),
                obj.getCoordinates().getZ()
        );

        return cylinder;
    }

    private static BaseObject getCube(Object obj) {
        Cube cube = new Cube(obj.getValues().get(0));
        cube.updateMatrices(
                obj.getRotation().getX(),
                obj.getRotation().getY(),
                obj.getRotation().getZ(),
                obj.getScaling().getX(),
                obj.getScaling().getY(),
                obj.getScaling().getZ(),
                obj.getCoordinates().getX(),
                obj.getCoordinates().getY(),
                obj.getCoordinates().getZ()
        );
        cube.setNormals();

        return cube;
    }
}
