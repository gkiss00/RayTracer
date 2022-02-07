package server.utils;

import rayTracer.objects.BaseObject;
import rayTracer.objects.planeSurfaces.Plane;
import rayTracer.objects.simpleObjects.Cylinder;
import rayTracer.objects.simpleObjects.Sphere;
import server.model.Object;

import java.util.ArrayList;
import java.util.List;

public class Converter {
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
}
