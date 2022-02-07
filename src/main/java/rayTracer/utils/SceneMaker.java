package rayTracer.utils;

import rayTracer.enums.CutTypeEnum;
import rayTracer.enums.PatternTypeEnum;
import rayTracer.lights.Light;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.*;
import rayTracer.objects.composedObjects.ClosedCylinder;
import rayTracer.objects.fractals.CubeFractal;
import rayTracer.objects.planeSurfaces.Disk;
import rayTracer.objects.planeSurfaces.Plane;
import rayTracer.objects.planeSurfaces.Square;
import rayTracer.objects.quarticSurfaces.GoursatSurface;
import rayTracer.objects.simpleObjects.*;
import rayTracer.visual.Camera;

import java.util.List;

public class SceneMaker {
    public static Camera getPoolTable(List<BaseObject> objects, List<Light> lights) {
        //Table
        double tableSize = 100;
        Square table = new Square(tableSize, new Color(10.0 / 255, 108.0 / 255, 3.0 / 255));
        table.updateMatrices(0, 0, 0, 1, 1, 1, -tableSize / 2, -tableSize / 2, 0);
        objects.add(table);

        //Balls
        double ballRadius = 10;
        Sphere ball1 = new Sphere(ballRadius, PatternTypeEnum.UNIFORM, new Color(1, 0, 0));
        ball1.setTexture("./src/main/resources/textures/poolBallSkins/Ball1.jpg");
        ball1.updateMatrices(0, 0, 270, 1, 1, 1, 0, 0, ballRadius);
        objects.add(ball1);

//        Sphere ball2 = new Sphere(ballRadius);
//        ball2.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball2);
//
//        Sphere ball3 = new Sphere(ballRadius);
//        ball3.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball3);
//
//        Sphere ball4 = new Sphere(ballRadius);
//        ball4.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball4);
//
//        Sphere ball5 = new Sphere(ballRadius);
//        ball5.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball5);
//
//        Sphere ball6 = new Sphere(ballRadius);
//        ball6.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball6);
//
//        Sphere ball7 = new Sphere(ballRadius);
//        ball7.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball7);
//
//        Sphere ball8 = new Sphere(ballRadius);
//        ball8.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball8);
//
//        Sphere ball9 = new Sphere(ballRadius);
//        ball9.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball9);
//
//        Sphere ball10 = new Sphere(ballRadius);
//        ball10.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball10);
//
//        Sphere ball11 = new Sphere(ballRadius);
//        ball11.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball11);
//
//        Sphere ball12 = new Sphere(ballRadius);
//        ball12.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball12);
//
//        Sphere ball13 = new Sphere(ballRadius);
//        ball13.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball13);
//
//        Sphere ball14 = new Sphere(ballRadius);
//        ball14.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball14);
//
//        Sphere ball15 = new Sphere(ballRadius);
//        ball15.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, ballRadius);
//        rayTracer.objects.add(ball15);

        return new Camera(new Point3D(-200, 0, 60), new Vector3D(1, 0, -0.25), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleSphere(List<BaseObject> objects, List<Light> lights) {
        Sphere sphere = new Sphere(30);
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(sphere);

        return new Camera(new Point3D(-100, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimplePlane(List<BaseObject> objects, List<Light> lights) {
        Plane plane = new Plane(new Color(0, 0, 1));
        plane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -20);
        plane.setNormalMap("./src/main/resources/textureAndNormal/water.normal.jpg");
        plane.setTexture("./src/main/resources/textureAndNormal/water.jpg");
        plane.setNormal();
        plane.setReflectionRatio(0.8);
        objects.add(plane);

        Sphere moon = new Sphere(20);
        moon.updateMatrices(0, 0, 0, 1, 1, 1, 100, 0, 100);
        moon.setTexture("./src/main/resources/textures/planets/moon.jpeg");
        objects.add(moon);

        Sphere milkyWay = new Sphere(210);
        milkyWay.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        milkyWay.setTexture("./src/main/resources/textures/planets/milkyWay.jpeg");
        objects.add(milkyWay);

        lights.add(new Light(new Point3D(0, 0, 0)));
        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCube(List<BaseObject> objects, List<Light> lights) {
        Cube cube = new Cube(30);
        cube.updateMatrices(0, -30, 45, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        //cube.setReflectionRatio(0.5);
        objects.add(cube);

        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -30);
        plane1.setNormal();
        plane1.setReflectionRatio(0.9);
        objects.add(plane1);

        Light light = new Light(new Point3D(-100, 0, 0));
        lights.add(light);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleDisk(List<BaseObject> objects, List<Light> lights) {
        Disk disk1 = new Disk(20, 60, PatternTypeEnum.UNIFORM, new Color(0.1, 0.54, 0.36, 1), new Color(0.54, 0.25, 0.87, 1));
        disk1.updateMatrices(0, -45, 0, 1, 1, 1, 0, 0, 0);
        disk1.setNormal();
        objects.add(disk1);

        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -30);
        plane1.setNormal();
        plane1.setReflectionRatio(0.9);
        objects.add(plane1);

        Light light = new Light(new Point3D(-100, 0, 0));
        lights.add(light);

        return new Camera(new Point3D(-300, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleSquare(List<BaseObject> objects, List<Light> lights) {
        Square square = new Square(120, PatternTypeEnum.UNIFORM, new Color(0.1, 0.54, 0.36, 1), new Color(0.54, 0.25, 0.87, 1));
        square.updateMatrices(0, 45, 0, 1, 1, 1, 0, 0, 0);
        square.setNormal();
        objects.add(square);

        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -30);
        plane1.setNormal();
        plane1.setReflectionRatio(0.9);
        objects.add(plane1);

        Light light = new Light(new Point3D(-100, 0, 0));
        lights.add(light);

        return new Camera(new Point3D(-300, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleTorus(List<BaseObject> objects, List<Light> lights) {

        Plane plane = new Plane(PatternTypeEnum.GRID, new Color(0, 0, 0.3), new Color(1, 1, 1));
        plane.updateMatrices(0, 90, 45, 1, 1, 1, 0, 0, 0);
        plane.setNormal();
        objects.add(plane);

        Plane plane2 = new Plane(PatternTypeEnum.GRID, new Color(0, 0, 0.3), new Color(1, 1, 1));
        plane2.updateMatrices(0, 90, -45, 1, 1, 1, 0, 0, 0);
        plane2.setNormal();
        objects.add(plane2);

        Torus torus1 = new Torus(20, 5, PatternTypeEnum.UNIFORM, new Color(0, 0, 0.01, 0.85));
        torus1.updateMatrices(0, -30, 0, 1, 1, 1, -100, 0, 0);
        objects.add(torus1);

        Light light = new Light(new Point3D(-100, 100, 100));
        //rayTracer.lights.add(light);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleMobiusTape(List<BaseObject> objects, List<Light> lights) {

        MobiusTape mt = new MobiusTape(5);
        mt.updateMatrices(0, -30, 0, 1, 1, 1, 0, 0, 0);
        objects.add(mt);

        Light light = new Light(new Point3D(-100, 100, 100));
        lights.add(light);

        //top
        //return new Camera(new Point3D(0, 0, 3), new Vector3D(0, 0, -1), new Vector3D(1, 0, 0), 90);
        //left
        //return new Camera(new Point3D(0, -3, 0), new Vector3D(0, 1, 0), new Vector3D(0, 0, 1), 90);
        //right
        //return new Camera(new Point3D(0, 2.5, 0), new Vector3D(0, -1, 0), new Vector3D(0, 0, 1), 90);
        //front
        return new Camera(new Point3D(-2, 0, 0.0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
        //back
        //return new Camera(new Point3D(2.5, 0, 0), new Vector3D(-1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleClosedCylinder(List<BaseObject> objects, List<Light> lights) {
        ClosedCylinder cubeFractal = new ClosedCylinder(5, 20);
        cubeFractal.updateMatrices(30, -30, 0, 1, 1, 1, 0, 0, 0);
        //cubeFractal.addCut(CutTypeEnum.FRONT);
        cubeFractal.setNormals();
        //cube.setReflectionRatio(0.5);
        objects.add(cubeFractal);

        Light light = new Light(new Point3D(-100, 0, 0));
        lights.add(light);

        return new Camera(new Point3D(-50, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCubeFractal(List<BaseObject> objects, List<Light> lights) {
        CubeFractal cubeFractal = new CubeFractal(1, 90);
        cubeFractal.updateMatrices(0, -40, 45, 1, 1, 1, 0, 0, 0);
        cubeFractal.setNormals();
        //cube.setReflectionRatio(0.5);
        objects.add(cubeFractal);

        Light light = new Light(new Point3D(-100, 0, 0));
        lights.add(light);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCubicSurface(List<BaseObject> objects, List<Light> lights) {
        CubicSurface cubicSurface = new CubicSurface();
        cubicSurface.setX3(0);
        cubicSurface.setY3(0);
        cubicSurface.setZ3(0);
        cubicSurface.setX2Y(0);
        cubicSurface.setX2Z(0);
        cubicSurface.setY2X(0);
        cubicSurface.setY2Z(0);
        cubicSurface.setZ2X(0);
        cubicSurface.setZ2Y(0);
        cubicSurface.setXYZ(0);
        cubicSurface.setX2(1);
        cubicSurface.setY2(1);
        cubicSurface.setZ2(1);
        cubicSurface.setXY(0);
        cubicSurface.setXZ(0);
        cubicSurface.setYZ(0);
        cubicSurface.setX(0);
        cubicSurface.setY(0);
        cubicSurface.setZ(0);
        cubicSurface.setK(-600);
        cubicSurface.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(cubicSurface);

        Light light = new Light(new Point3D(-100, 0, 0));
        lights.add(light);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleGoursatSurface(List<BaseObject> objects, List<Light> lights) {
        GoursatSurface goursatSurface = new GoursatSurface(0, 0, -1);
        goursatSurface.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(goursatSurface);

        Light light = new Light(new Point3D(-1000, 1000, 1000));
        lights.add(light);

        return new Camera(new Point3D(-2, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getShadows1(List<BaseObject> objects, List<Light> lights) {
        Plane horizontalPlane = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        horizontalPlane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -30);
        horizontalPlane.setNormal();
        objects.add(horizontalPlane);

        Plane verticalPlane = new Plane();
        verticalPlane.updateMatrices(0, -90, 0, 1, 1, 1, 50, 0, 0);
        verticalPlane.setNormal();
        objects.add(verticalPlane);

        Sphere sphere = new Sphere(30);
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 30);
        objects.add(sphere);

//        rayTracer.lights.add(new Light(new Point3D(-50, 40, 15)));
        lights.add(new Light(new Point3D(-50, 0, 15)));
//        rayTracer.lights.add(new Light(new Point3D(-50, -40, 15)));

        return new Camera(new Point3D(-300, 0, 30), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getShadows2(List<BaseObject> objects, List<Light> lights) {
        Plane horizontalPlane = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        horizontalPlane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -30);
        horizontalPlane.setNormal();
        objects.add(horizontalPlane);

        Plane verticalPlane = new Plane(new Color(0.5, 0.5, 0.5));
        verticalPlane.updateMatrices(0, -90, 0, 1, 1, 1, 50, 0, 0);
        verticalPlane.setNormal();
        objects.add(verticalPlane);

        Sphere sphere = new Sphere(30, new Color(0, 0,1, 0.5));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 30);
        objects.add(sphere);

//        rayTracer.lights.add(new Light(new Point3D(-50, 40, 15)));
        lights.add(new Light(new Point3D(-50, 0, 15)));
//        rayTracer.lights.add(new Light(new Point3D(-50, -40, 15)));

        return new Camera(new Point3D(-300, 0, 30), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getMirrorBox(List<BaseObject> objects, List<Light> lights) {
        Cube cube = new Cube(100, new Color(120.0 / 255, 120.0 / 255, 120.0 / 255));
        cube.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        cube.setReflectionRatio(0.9);
        objects.add(cube);

        Sphere sphere = new Sphere(8, new Color(1, 0, 0));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        sphere.setReflectionRatio(0.75);
        objects.add(sphere);

        lights.add(new Light(new Point3D(-45, 45, 45)));

        return new Camera(new Point3D(-45, 0, 20), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getTextureScene(List<BaseObject> objects, List<Light> lights) {

        //Sphere
        Sphere ball1 = new Sphere(30);
        ball1.setTexture("./src/main/resources/textures/poolBallSkins/Ball1.jpg");
        ball1.updateMatrices(0, 0, 270, 1, 1, 1, 200, -80, 160);
        objects.add(ball1);

        Sphere ball2 = new Sphere(30);
        ball2.setTexture("./src/main/resources/textures/poolBallSkins/Ball2.jpg");
        ball2.updateMatrices(0, 0, 270, 1, 1, 1, 200, 0, 160);
        objects.add(ball2);

        Sphere ball3 = new Sphere(30);
        ball3.setTexture("./src/main/resources/textures/poolBallSkins/Ball3.jpg");
        ball3.updateMatrices(0, 0, 270, 1, 1, 1, 200, 80, 160);
        objects.add(ball3);

        Sphere ball4 = new Sphere(30);
        ball4.setTexture("./src/main/resources/textures/poolBallSkins/Ball4.jpg");
        ball4.updateMatrices(0, 0, 270, 1, 1, 1, 200, -80, 80);
        objects.add(ball4);

        Sphere ball5 = new Sphere(30);
        ball5.setTexture("./src/main/resources/textures/poolBallSkins/Ball5.jpg");
        ball5.updateMatrices(0, 0, 270, 1, 1, 1, 200, 0, 80);
        objects.add(ball5);

        Sphere ball6 = new Sphere(30);
        ball6.setTexture("./src/main/resources/textures/poolBallSkins/Ball6.jpg");
        ball6.updateMatrices(0, 0, 270, 1, 1, 1, 200, 80, 80);
        objects.add(ball6);

        Sphere ball7 = new Sphere(30);
        ball7.setTexture("./src/main/resources/textures/poolBallSkins/Ball7.jpg");
        ball7.updateMatrices(0, 0, 270, 1, 1, 1, 200, -80, 0);
        objects.add(ball7);

        Sphere ball8 = new Sphere(30);
        ball8.setTexture("./src/main/resources/textures/poolBallSkins/Ball8.jpg");
        ball8.updateMatrices(0, 0, 270, 1, 1, 1, 200, 0, 0);
        objects.add(ball8);

        Sphere ball9 = new Sphere(30);
        ball9.setTexture("./src/main/resources/textures/poolBallSkins/Ball9.jpg");
        ball9.updateMatrices(0, 0, 270, 1, 1, 1, 200, 80, 0);
        objects.add(ball9);

        Sphere ball10 = new Sphere(30);
        ball10.setTexture("./src/main/resources/textures/poolBallSkins/Ball10.jpg");
        ball10.updateMatrices(0, 0, 270, 1, 1, 1, 200, -80, -80);
        objects.add(ball10);

        Sphere ball11 = new Sphere(30);
        ball11.setTexture("./src/main/resources/textures/poolBallSkins/Ball11.jpg");
        ball11.updateMatrices(0, 0, 270, 1, 1, 1, 200, 0, -80);
        objects.add(ball11);

        Sphere ball12 = new Sphere(30);
        ball12.setTexture("./src/main/resources/textures/poolBallSkins/Ball12.jpg");
        ball12.updateMatrices(0, 0, 270, 1, 1, 1, 200, 80, -80);
        objects.add(ball12);

        Sphere ball13 = new Sphere(30);
        ball13.setTexture("./src/main/resources/textures/poolBallSkins/Ball13.jpg");
        ball13.updateMatrices(0, 0, 270, 1, 1, 1, 200, -80, -160);
        objects.add(ball13);

        Sphere ball14 = new Sphere(30);
        ball14.setTexture("./src/main/resources/textures/poolBallSkins/Ball14.jpg");
        ball14.updateMatrices(0, 0, 270, 1, 1, 1, 200, 0, -160);
        objects.add(ball14);

        Sphere ball15 = new Sphere(30);
        ball15.setTexture("./src/main/resources/textures/poolBallSkins/Ball15.jpg");
        ball15.updateMatrices(0, 0, 270, 1, 1, 1, 200, 80, -160);
        objects.add(ball15);

        //Cylinder
        Cylinder cylinder1 = new Cylinder(30);
        cylinder1.setTexture("./src/main/resources/textures/poolBallSkins/Ball1.jpg");
        cylinder1.updateMatrices(0, 0, 270, 1, 1, 1, 250, 40, 0);
        objects.add(cylinder1);

        Cylinder cylinder2 = new Cylinder(30);
        cylinder2.setTexture("./src/main/resources/textures/poolBallSkins/Ball1.jpg");
        cylinder2.updateMatrices(0, 0, 270, 1, 1, 1, 250, -40, 0);
        objects.add(cylinder2);

        //Plane
        Plane plane = new Plane(new Color(1, 0, 0));
        plane.setTexture("./src/main/resources/textures/poolBallSkins/Ball2.jpg");
        plane.setTextureWidth(100);
        plane.updateMatrices(0, -90, 0, 1, 1, 1, 300, 0, 0);
        plane.setNormal();
        objects.add(plane);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getAll(List<BaseObject> objects, List<Light> lights) {
        // Spheres
        Sphere sphere1 = new Sphere(40, PatternTypeEnum.GRID, new Color(0, 0, 0), new Color(1, 1, 0));
        sphere1.updateMatrices(0, 45, 0, 1, 1, 1, 500, -150, 100);
        Sphere sphere2 = new Sphere(40, PatternTypeEnum.GRADIENT, new Color(0, 1, 0), new Color(0, 0, 1), new Color(1, 0, 0));
        sphere2.updateMatrices(0, 0, 0, 1, 1, 1, 500, 0, 100);
        Sphere sphere3 = new Sphere(40, new Color(0, 0, 1));
        sphere3.updateMatrices(0, 0, 0, 1, 1, 2, 500, 150, 100);
        objects.add(sphere1);
        objects.add(sphere2);
        objects.add(sphere3);

        // Plane
        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 200, 0, -20);
        plane1.setNormal();
        plane1.setReflectionRatio(0.9);
        objects.add(plane1);

        // Cylinder
        Cylinder cylinder1 = new Cylinder(30, PatternTypeEnum.UNIFORM, new Color(1, 0, 0), new Color(0, 0, 0));
        cylinder1.updateMatrices(45, 0, 0, 1, 1, 1, 650, -75, 0);
        cylinder1.setReflectionRatio(0.8);
        Cylinder cylinder2 = new Cylinder(30, PatternTypeEnum.HORIZONTAL_LINED, new Color(0.75, 1, 0.2), new Color(0.28, .34, 0.58));
        cylinder2.updateMatrices(90, 0, 0, 1, 1, 1, 650, 75, 50);
        objects.add(cylinder1);
        objects.add(cylinder2);

        // Cone
        Cone cone1 = new Cone(30, PatternTypeEnum.HORIZONTAL_LINED, new Color(0.5, 0, 0.7, 0.5), new Color(0.7, 0.2, 0.6, 0.4));
        cone1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        cone1.setReflectionRatio(0.5);
        objects.add(cone1);

        //Disk
        Disk disk1 = new Disk(20, 60, PatternTypeEnum.GRID, new Color(0.1, 0.54, 0.36, 1), new Color(0.54, 0.25, 0.87, 1));
        disk1.updateMatrices(0, 0, 0, 1, 1, 1, -50, 0, 0);
        disk1.setNormal();
        objects.add(disk1);

        // Square
        Square square1 = new Square(70, PatternTypeEnum.GRADIENT, new Color(0.5, 0.5, 0.5), new Color(0.2, 0.5, 0.1, 0));
        square1.updateMatrices(90, 0, 0, 1, 1, 1, -120, 70, -20);
        square1.setNormal();
        square1.setReflectionRatio(0);
        objects.add(square1);

        // Torus
        Torus torus1 = new Torus(50, 10);
        torus1.updateMatrices(90, 0, 0, 1, 1, 1, 0, -100, 30);
        objects.add(torus1);


        Light light1 = new Light(new Point3D(-300, 0, 100), new Color(1, 1, 1));
        lights.add(light1);

        return new Camera(new Point3D(-300, 0, 120), new Vector3D(1, 0, -0.25), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getMoon(List<BaseObject> objects, List<Light> lights) {
        Sphere moon = new Sphere(30);
        moon.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        moon.setTexture("./src/main/resources/textures/planets/moon.jpeg");
        objects.add(moon);

        Sphere milkyWay = new Sphere(210);
        milkyWay.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        milkyWay.setTexture("./src/main/resources/textures/planets/milkyWay.jpeg");
        objects.add(milkyWay);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSolarSystem(List<BaseObject> objects, List<Light> lights) {
        double sunRadius = 150;
        Sphere sun = new Sphere(sunRadius);
        sun.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        sun.setTexture("./src/main/resources/textures/planets/sun.jpeg");
        objects.add(sun);

        double sunMercurySpace = 15;
        double mercuryRadius = 5;
        double distanceFromSun = sunRadius + sunMercurySpace;
        Sphere mercury = new Sphere(mercuryRadius);
        mercury.updateMatrices(0, 0, 0, 1, 1, 1, 0, distanceFromSun, 0);
        mercury.setTexture("./src/main/resources/textures/planets/mercury.jpeg");
        objects.add(mercury);

        Disk mercuryCircle = new Disk(distanceFromSun - 1, distanceFromSun + 1, PatternTypeEnum.GRADIENT, new Color(1, 1, 1), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1));
        mercuryCircle.updateMatrices(0, -5, 0, 1, 1, 1, 0, 0, 0);
        objects.add(mercuryCircle);

        double mercuryVenusSpace = 30;
        double venusRadius = 10;
        distanceFromSun = sunRadius + sunMercurySpace + mercuryRadius + mercuryVenusSpace;
        Sphere venus = new Sphere(venusRadius);
        venus.updateMatrices(0, 0, 0, 1, 1, 1, 0, distanceFromSun, 0);
        venus.setTexture("./src/main/resources/textures/planets/venus.jpeg");
        objects.add(venus);

        Disk venusCircle = new Disk(distanceFromSun - 1, distanceFromSun + 1, PatternTypeEnum.GRADIENT, new Color(1, 1, 1), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1));
        venusCircle.updateMatrices(0, -5, 0, 1, 1, 1, 0, 0, 0);
        objects.add(venusCircle);

        double venusEarthSpace = 30;
        double earthRadius = 10;
        distanceFromSun = sunRadius + sunMercurySpace + mercuryRadius + mercuryVenusSpace + venusRadius + venusEarthSpace;
        Sphere earth = new Sphere(earthRadius);
        earth.updateMatrices(0, 0, 0, 1, 1, 1, 0, distanceFromSun, 0);
        earth.setTexture("./src/main/resources/textures/planets/earthDay.jpeg");
        objects.add(earth);

        Disk earthCircle = new Disk(distanceFromSun - 1, distanceFromSun + 1, PatternTypeEnum.GRADIENT, new Color(1, 1, 1), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1));
        earthCircle.updateMatrices(0, -5, 0, 1, 1, 1, 0, 0, 0);
        objects.add(earthCircle);

        double earthMarsSpace = 30;
        double marsRadius = 7;
        distanceFromSun = sunRadius + sunMercurySpace + mercuryRadius + mercuryVenusSpace + venusRadius + venusEarthSpace + earthRadius + earthMarsSpace;
        Sphere mars = new Sphere(marsRadius);
        mars.updateMatrices(0, 0, 0, 1, 1, 1, 0, distanceFromSun, 0);
        mars.setTexture("./src/main/resources/textures/planets/mars.jpeg");
        objects.add(mars);

        Disk marsCircle = new Disk(distanceFromSun - 1, distanceFromSun + 1, PatternTypeEnum.GRADIENT, new Color(1, 1, 1), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1));
        marsCircle.updateMatrices(0, -5, 0, 1, 1, 1, 0, 0, 0);
        objects.add(marsCircle);

        double marsJupiterSpace = 80;
        double jupiterRadius = 20;
        distanceFromSun = sunRadius + sunMercurySpace + mercuryRadius + mercuryVenusSpace + venusRadius + venusEarthSpace + earthRadius + earthMarsSpace + marsRadius + marsJupiterSpace;
        Sphere jupiter = new Sphere(jupiterRadius);
        jupiter.updateMatrices(20, 0, 0, 1, 1, 1, 0, distanceFromSun, 0);
        jupiter.setTexture("./src/main/resources/textures/planets/jupiter.jpeg");
        objects.add(jupiter);

        Disk jupiterCircle = new Disk(distanceFromSun - 1, distanceFromSun + 1, PatternTypeEnum.GRADIENT, new Color(1, 1, 1), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1));
        jupiterCircle.updateMatrices(0, -5, 0, 1, 1, 1, 0, 0, 0);
        objects.add(jupiterCircle);

        double jupiterSaturnSpace = 60;
        double saturnRadius = 17;
        distanceFromSun = sunRadius + sunMercurySpace + mercuryRadius + mercuryVenusSpace + venusRadius + venusEarthSpace + earthRadius + earthMarsSpace + marsRadius + marsJupiterSpace + jupiterRadius + jupiterSaturnSpace;
        Sphere saturn = new Sphere(saturnRadius);
        saturn.updateMatrices(-30, 0, 0, 1, 1, 1, 0, distanceFromSun, 0);
        saturn.setTexture("./src/main/resources/textures/planets/saturn.jpeg");
        objects.add(saturn);

        Disk saturnCircle = new Disk(distanceFromSun - 1, distanceFromSun + 1, PatternTypeEnum.GRADIENT, new Color(1, 1, 1), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1));
        saturnCircle.updateMatrices(0, -5, 0, 1, 1, 1, 0, 0, 0);
        objects.add(saturnCircle);

        double saturnUranusSpace = 60;
        double uranusRadius = 15;
        distanceFromSun = sunRadius + sunMercurySpace + mercuryRadius + mercuryVenusSpace + venusRadius + venusEarthSpace + earthRadius + earthMarsSpace + marsRadius + marsJupiterSpace + jupiterRadius + jupiterSaturnSpace + saturnRadius + saturnUranusSpace;
        Sphere uranus = new Sphere(uranusRadius);
        uranus.updateMatrices(0, 0, 0, 1, 1, 1, 0, distanceFromSun, 0);
        uranus.setTexture("./src/main/resources/textures/planets/uranus.jpeg");
        objects.add(uranus);

        Disk uranusCircle = new Disk(distanceFromSun - 1, distanceFromSun + 1, PatternTypeEnum.GRADIENT, new Color(1, 1, 1), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1));
        uranusCircle.updateMatrices(0, -5, 0, 1, 1, 1, 0, 0, 0);
        objects.add(uranusCircle);

        double uranusNeptuneSpace = 60;
        double neptuneRadius = 13;
        distanceFromSun = sunRadius + sunMercurySpace + mercuryRadius + mercuryVenusSpace + venusRadius + venusEarthSpace + earthRadius + earthMarsSpace + marsRadius + marsJupiterSpace + jupiterRadius + jupiterSaturnSpace + saturnRadius + saturnUranusSpace + uranusRadius + uranusNeptuneSpace;
        Sphere neptune = new Sphere(neptuneRadius);
        neptune.updateMatrices(0, 0, 0, 1, 1, 1, 0, distanceFromSun, 0);
        neptune.setTexture("./src/main/resources/textures/planets/neptune.jpeg");
        objects.add(neptune);

        Disk neptuneCircle = new Disk(distanceFromSun - 1, distanceFromSun + 1, PatternTypeEnum.GRADIENT, new Color(1, 1, 1), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1, 0), new Color(1, 1, 1));
        neptuneCircle.updateMatrices(0, -5, 0, 1, 1, 1, 0, 0, 0);
        neptuneCircle.setPattern(PatternTypeEnum.GRADIENT);
        objects.add(neptuneCircle);

        Sphere milkyWay = new Sphere(1200);
        milkyWay.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        milkyWay.setTexture("./src/main/resources/textures/planets/milkyWay.jpeg");
        objects.add(milkyWay);

        return new Camera(new Point3D(-600, 350, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getBibou(List<BaseObject> objects, List<Light> lights) {

        Color green = new Color(9.0 / 255, 106.0 / 55, 9.0 / 255);

        //B1
        ClosedCylinder closedCylinderB1 = new ClosedCylinder(1.5, 46, green);
        closedCylinderB1.updateMatrices(0, 0, 0, 1, 1, 1, 0, -55, 0);
        closedCylinderB1.setNormals();
        objects.add(closedCylinderB1);

        Torus torusB1Small = new Torus(8, 1.5, green);
        torusB1Small.updateMatrices(0, 90, 0, 1, 1.4, 1, 0, -55, 13.5);
        torusB1Small.addCut(CutTypeEnum.LEFT);
        objects.add(torusB1Small);

        Torus torusB1Big = new Torus(13.5, 1.5, green);
        torusB1Big.updateMatrices(0, 90, 0, 1, 1.4, 1, 0, -55, -8);
        torusB1Big.addCut(CutTypeEnum.LEFT);
        objects.add(torusB1Big);

        //I
        Sphere sphereI = new Sphere(1.5);
        sphereI.updateMatrices(0, 0, 0, 1, 1, 11.5 * 2 / 1.5, 0, -25, 0);
        objects.add(sphereI);

        //B2
        ClosedCylinder closedCylinderB2 = new ClosedCylinder(1.5, 46);
        closedCylinderB2.updateMatrices(0, 0, 0, 1, 1, 1, 0, -15, 0);
        closedCylinderB2.setNormals();
        objects.add(closedCylinderB2);

        Torus torusB2Small = new Torus(8, 1.5);
        torusB2Small.updateMatrices(0, 90, 0, 1, 1.4, 1, 0, -15, 13.5);
        torusB2Small.addCut(CutTypeEnum.LEFT);
        objects.add(torusB2Small);

        Torus torusB2Big = new Torus(13.5, 1.5);
        torusB2Big.updateMatrices(0, 90, 0, 1, 1.4, 1, 0, -15, -8);
        torusB2Big.addCut(CutTypeEnum.LEFT);
        objects.add(torusB2Big);

        //O
        Torus torusO = new Torus(10, 1.5);
        torusO.updateMatrices(0, -90, 0, 1, 1, 2, 0, 20, 0);
        objects.add(torusO);

        //U
        Torus torusU = new Torus(10, 1.5);
        torusU.updateMatrices(0, -90, 0, 1, 1, 4, 0, 45, 23);
        torusU.addCut(CutTypeEnum.BACK);
        objects.add(torusU);

        Plane plane1 = new Plane();
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -23);
        plane1.setNormalMap("./src/main/resources/textureAndNormal/water.normal.jpg");
        plane1.setTexture("./src/main/resources/textureAndNormal/water.jpg");
        plane1.lineValue = 100;
        plane1.setNormal();
        plane1.setReflectionRatio(0.8);
        objects.add(plane1);


        return new Camera(new Point3D(-150, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }
}
