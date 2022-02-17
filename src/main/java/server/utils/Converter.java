package server.utils;

import rayTracer.objects.BaseObject;
import rayTracer.objects.planeSurfaces.Plane;
import rayTracer.objects.simpleObjects.Cube;
import rayTracer.objects.simpleObjects.Cylinder;
import rayTracer.objects.simpleObjects.Sphere;
import server.model.Camera;
import server.model.Object;
import server.model.enums.PatternTypeEnum;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Converter {

    private static Map<server.model.enums.PatternTypeEnum, rayTracer.enums.PatternTypeEnum> patternMap = new HashMap<server.model.enums.PatternTypeEnum, rayTracer.enums.PatternTypeEnum>() {{
        put(server.model.enums.PatternTypeEnum.UNIFORM, rayTracer.enums.PatternTypeEnum.UNIFORM);
        put(server.model.enums.PatternTypeEnum.VERTICAL_LINED, rayTracer.enums.PatternTypeEnum.VERTICAL_LINED);
        put(server.model.enums.PatternTypeEnum.HORIZONTAL_LINED, rayTracer.enums.PatternTypeEnum.HORIZONTAL_LINED);
        put(server.model.enums.PatternTypeEnum.GRID, rayTracer.enums.PatternTypeEnum.GRID);
        put(server.model.enums.PatternTypeEnum.GRADIENT, rayTracer.enums.PatternTypeEnum.GRADIENT);
        put(server.model.enums.PatternTypeEnum.TEXTURE, rayTracer.enums.PatternTypeEnum.TEXTURE);
    }};

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
            BaseObject tmp = null;
            switch (objects.get(i).getType()) {
                case SPHERE:
                    tmp = getSphere(objects.get(i));
                    //baseObjects.add(getSphere(objects.get(i)));
                    break;
                case PLANE:
                    tmp = getPlane(objects.get(i));
                    //baseObjects.add(getPlane(objects.get(i)));
                    break;
                case CYLINDER:
                    tmp = getCylinder(objects.get(i));
                    //baseObjects.add(getCylinder(objects.get(i)));
                    break;
                case CUBE:
                    tmp = getCube(objects.get(i));
                    //baseObjects.add(getCube(objects.get(i)));
                    break;
            }
            setAttributes(tmp, objects.get(i));
            baseObjects.add(tmp);
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

    private static BaseObject setAttributes(BaseObject baseObject, Object object) {

        // COLORS
        baseObject.clearColors();
        for (int i = 0; i < object.getColors().length; ++i) {
            java.awt.Color tmpColor = java.awt.Color.decode(object.getColors()[i]);
            System.out.println(tmpColor.toString());
            baseObject.addColor(new rayTracer.utils.Color((double)tmpColor.getRed() / 255, (double)tmpColor.getGreen() / 255, (double)tmpColor.getBlue() / 255));
        }

        // PATTERN
        baseObject.setPattern(patternMap.get(object.getPattern()));

        // FILL MISSING COLORS
        baseObject.initMissingColors(3);

        // REFLEXION
        baseObject.setReflectionRatio((double)object.getReflexion() / 100);

        return baseObject;
    }
}
