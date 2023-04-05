package rayTracer.utils;

import rayTracer.enums.*;
import rayTracer.factories.ChessPieceFactory;
import rayTracer.noiser.GradientNoise;
import rayTracer.noiser.GradientNoise3D;
import rayTracer.noiser.WorleyNoise;
import rayTracer.noiser.WorleyNoise3D;
import rayTracer.objects.Obj;
import rayTracer.objects.baseObjects.composedObjects.Pipe;
import rayTracer.objects.baseObjects.composedObjects.objectMade.Assembly;
import rayTracer.objects.blackObjects.*;
import rayTracer.factories.PolygonFactory;
import rayTracer.lights.Light;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.baseObjects.*;
import rayTracer.objects.baseObjects.composedObjects.ClosedCone;
import rayTracer.objects.baseObjects.composedObjects.ClosedCylinder;
import rayTracer.objects.baseObjects.composedObjects.triangleMade.Polygon;
import rayTracer.objects.baseObjects.fractals.CubeFractal;
import rayTracer.objects.baseObjects.planeSurfaces.Disk;
import rayTracer.objects.baseObjects.planeSurfaces.Plane;
import rayTracer.objects.baseObjects.planeSurfaces.Square;
import rayTracer.objects.baseObjects.simpleObjects.*;
import rayTracer.visual.Camera;

import java.util.List;
import java.util.Random;

public class SceneMaker {

    /* * * * * * * * * * * * * * * * * * * * *

     *             SIMPLE OBJECTS            *

     * * * * * * * * * * * * * * * * * * * * */

    public static Camera getSimpleSphere(List<Obj> objects, List<Light> lights) {
        Sphere sphere = new Sphere(30);
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(sphere);

        return new Camera(new Point3D(-100, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCube(List<Obj> objects, List<Light> lights, List<Obj> blacks) {
        Cube cube = new Cube(30);
        cube.updateMatrices(20, 20, 35, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        objects.add(cube);

        return new Camera(new Point3D(-100, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimplePlane(List<Obj> objects, List<Light> lights) {
        Plane plane = new Plane(new Color(1, 0, 0));
        plane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        plane.setNormal();
        objects.add(plane);

        return new Camera(new Point3D(-200, 20, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleSquare(List<Obj> objects, List<Light> lights) {
        Square square = new Square(120);
        square.updateMatrices(0, -90, 0, 1, 1, 1, 0, 0, 0);
        square.setNormal();
        objects.add(square);

        return new Camera(new Point3D(-300, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleDisk(List<Obj> objects, List<Light> lights) {
        Disk disk = new Disk(20, 60);
        disk.updateMatrices(0, -45, 0, 1, 1, 1, 0, 0, 0);
        disk.setNormal();
        objects.add(disk);

        return new Camera(new Point3D(-300, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCylinder(List<Obj> objects, List<Light> lights) {
        Cylinder cylinder = new Cylinder(20);
        cylinder.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(cylinder);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleClosedCylinder(List<Obj> objects, List<Light> lights) {
        ClosedCylinder closedCylinder = new ClosedCylinder(5, 20);
        closedCylinder.updateMatrices(30, -30, 0, 1, 1, 1, 0, 0, 0);
        closedCylinder.setNormals();
        objects.add(closedCylinder);

        return new Camera(new Point3D(-50, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCone(List<Obj> objects, List<Light> lights) {
        Cone cone = new Cone(20);
        cone.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(cone);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleClosedCone(List<Obj> objects, List<Light> lights) {
        ClosedCone closedCone = new ClosedCone(20, 20);
        closedCone.updateMatrices(30, -30, 0, 1, 1, 1, 0, 0, 0);
        closedCone.setNormals();
        objects.add(closedCone);

        return new Camera(new Point3D(-50, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleTorus(List<Obj> objects, List<Light> lights, List<Obj> blackObjects, double alpha) {
        Torus torus = new Torus(30,  7.5);
        torus.updateMatrices(0, -20, alpha, 1, 1, 1, 0, 0, 0);
        torus.setTexture("./src/main/resources/textures/planets/venus.jpeg");
        objects.add(torus);

        return new Camera(new Point3D(-120, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimplePipe(List<Obj> objects, List<Light> lights) {

        Pipe pipe = new Pipe(30, 25, 40);
        pipe.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        pipe.setNormals();
        objects.add(pipe);

        lights.add(new Light(new Point3D(-100, 100, 100)));

        return new Camera(new Point3D(-150, 0, 150), new Vector3D(1, 0, -1), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleMobiusTapePolygon(List<Obj> objects, List<Light> lights) {
        try {
            Polygon mobiusTape = PolygonFactory.createPolygon(PolygonTypeEnum.MOBIUS_TAPE, 40, 10, 1000);
            mobiusTape.updateMatrices(0, -20, 0, 1, 1, 1, 0, 0, 0);
            mobiusTape.setPattern(PatternTypeEnum.GRADIENT);
            mobiusTape.clearColors();
            mobiusTape.addColor(new Color(1, 0.1, 0.1));
            mobiusTape.addColor(new Color());
            mobiusTape.addColor(new Color());
            mobiusTape.addColor(new Color());
            mobiusTape.addColor(new Color());
            mobiusTape.addColor(new Color(1, 0.1, 0.1));
            objects.add(mobiusTape);
        } catch(Exception e) {

        }

        return new Camera(new Point3D(-120, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleStar(List<Obj> objects, List<Light> lights) {
        Polygon star = PolygonFactory.createPolygon(PolygonTypeEnum.STAR, 5, 20, 60, 20);
        if( star != null) {
            star.updateMatrices(0, 90, 0, 1, 1, 1, 0, 0, 0);
            objects.add(star);
        }

        return new Camera(new Point3D(-300, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleTetrahedron(List<Obj> objects, List<Light> lights) {
        Polygon tetrahedron = PolygonFactory.createPolygon(PolygonTypeEnum.TETRAHEDRON, 60);
        if(tetrahedron != null) {
            tetrahedron.updateMatrices(0, 0, 180, 1, 1, 1, 0, 0, 0);
            objects.add(tetrahedron);
        }

        return new Camera(new Point3D(-300, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleDiamond(List<Obj> objects, List<Light> lights) {
        Polygon diamond = PolygonFactory.createPolygon(PolygonTypeEnum.DIAMOND, 9, 40, 75, 35, 75);
        if(diamond != null) {
            diamond.updateMatrices(0, -20, 0, 1, 1, 1, 0, 0, 0);
            diamond.setReflectionRatio(0.5);
            objects.add(diamond);
        }

        return new Camera(new Point3D(-300, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCubeFractal(List<Obj> objects, List<Light> lights, List<Obj> blackObjects) {
        CubeFractal cubeFractal = new CubeFractal(4, 90);
        cubeFractal.updateMatrices(0, -40, 45, 1, 1, 1, 0, 0, 0);
        cubeFractal.setNormals();
        objects.add(cubeFractal);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             SIMPLE OBJECTS            *
     *                  WITH                 *
     *                PATTERN                *

     * * * * * * * * * * * * * * * * * * * * */

    public static Camera getSimpleSphereWithPattern(List<Obj> objects, List<Light> lights) {
        Sphere sphere = new Sphere(30, PatternTypeEnum.TEXTURE, new Color(), new Color());
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        sphere.setTexture("./src/main/resources/textures/rgba/fire.png");
        objects.add(sphere);

        return new Camera(new Point3D(-100, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCubeWithPattern(List<Obj> objects, List<Light> lights) {
        Cube cube = new Cube(30, PatternTypeEnum.GRADIENT, new Color(), new Color());
        //cube.setTexture("./src/main/resources/textures/rgba/squareBorder2.png");
        cube.updateMatrices(20, 20, 35, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        objects.add(cube);

        return new Camera(new Point3D(-100, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimplePlaneWithPattern(List<Obj> objects, List<Light> lights) {
        Plane plane = new Plane(PatternTypeEnum.GRID, new Color(), new Color());
        plane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        plane.setNormal();
        objects.add(plane);

        return new Camera(new Point3D(-200, 20, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleDiskWithPattern(List<Obj> objects, List<Light> lights) {
        Disk disk = new Disk(20, 60, PatternTypeEnum.GRID, new Color(), new Color());
        disk.updateMatrices(0, -45, 0, 1, 1, 1, 0, 0, 0);
        disk.setNormal();
        objects.add(disk);

        return new Camera(new Point3D(-300, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCylinderWithPattern(List<Obj> objects, List<Light> lights) {
        Cylinder cylinder = new Cylinder(20, PatternTypeEnum.GRID, new Color(), new Color());
        cylinder.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(cylinder);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleConeWithPattern(List<Obj> objects, List<Light> lights) {
        Cone cone = new Cone(20, PatternTypeEnum.GRID, new Color(), new Color());
        cone.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(cone);

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleTorusWithPattern(List<Obj> objects, List<Light> lights) {
        Torus torus = new Torus(25,  5, PatternTypeEnum.TEXTURE);
        torus.updateMatrices(10, 20, 20, 1, 1, 1, 0, 0, 0);
        torus.setTexture("./src/main/resources/textures/random/numbers.png");
        objects.add(torus);

        return new Camera(new Point3D(-120, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleMobiusTapePolygonWithPattern(List<Obj> objects, List<Light> lights) {
        try {
            Polygon mobiusTape = PolygonFactory.createPolygon(PolygonTypeEnum.MOBIUS_TAPE, 40, 20, 1000);
            mobiusTape.updateMatrices(0, -20, 0, 1, 1, 1, 0, 0, 0);
            mobiusTape.setTexture("./src/main/resources/textures/random/numbers.png");
            objects.add(mobiusTape);
        } catch(Exception e) {

        }

        return new Camera(new Point3D(-120, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *              FULL OBJECTS             *

     * * * * * * * * * * * * * * * * * * * * */

    public static Camera getFullCube(List<Obj> objects, List<Light> lights) {
        Cube cube = new Cube(30, new Color(0.1, 0.1, 1, 0));
        cube.updateMatrices(30, -15, -45, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        cube.setCapacity(CapacityTypeEnum.FULL);
        objects.add(cube);

        cube = new Cube(30, new Color(0.1, 0.1, 1, 0));
        cube.updateMatrices(30, -15, -45, 1, 1, 1, 0, -20, 0);
        cube.setNormals();
        cube.setCapacity(CapacityTypeEnum.FULL);
        objects.add(cube);

        Cylinder cylinder = new Cylinder(8);
        cylinder.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(cylinder);

        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -60);
        plane1.setNormal();
        objects.add(plane1);

        Plane plane2 = new Plane(new Color(0.9, 0.1, 0));
        plane2.updateMatrices(0, 90, 0, 1, 1, 1, 100, 0, 0);
        plane2.setNormal();
        objects.add(plane2);

        Light light = new Light(new Point3D(-60, 60, 0));
        //lights.add(light);

        return new Camera(new Point3D(-120, 0, 0), new Vector3D(1, -0.2, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getFullSphere(List<Obj> objects, List<Light> lights) {
        Sphere sphere = new Sphere(30, new Color(0.1, 0.1, 1, 0));
        sphere.updateMatrices(30, -15, -45, 1, 1, 1, 0, 0, 0);
        sphere.setCapacity(CapacityTypeEnum.FULL);
        objects.add(sphere);

        sphere = new Sphere(30, new Color(0.1, 0.1, 1, 0));
        sphere.updateMatrices(30, -15, -45, 1, 1, 1, 0, -20, 0);
        sphere.setCapacity(CapacityTypeEnum.FULL);
        objects.add(sphere);

        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -60);
        plane1.setNormal();
        objects.add(plane1);

        Plane plane2 = new Plane(new Color(0.9, 0.1, 0));
        plane2.updateMatrices(0, 90, 0, 1, 1, 1, 100, 0, 0);
        plane2.setNormal();
        objects.add(plane2);

        Light light = new Light(new Point3D(-60, 60, 0));
        lights.add(light);

        return new Camera(new Point3D(-120, 0, 0), new Vector3D(1, -0.2, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleFullTorus(List<Obj> objects, List<Light> lights) {
        Torus torus = new Torus(25,  5, new Color(0.1, 0.1, 1, 0));
        torus.updateMatrices(0, 90, -45, 1, 1, 1, 0, 0, 0);
        torus.setCapacity(CapacityTypeEnum.FULL);
        objects.add(torus);

        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -60);
        plane1.setNormal();
        objects.add(plane1);

        Plane plane2 = new Plane(new Color(0.9, 0.1, 0));
        plane2.updateMatrices(0, 90, 0, 1, 1, 1, 100, 0, 0);
        plane2.setNormal();
        objects.add(plane2);

        Light light = new Light(new Point3D(-60, 60, 0));
        lights.add(light);

        return new Camera(new Point3D(-120, 0, 0), new Vector3D(1, -0.2, 0), new Vector3D(0, 0, 1), 90);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *           REFRACTION OBJECTS          *

     * * * * * * * * * * * * * * * * * * * * */

    public static Camera getRefractedSphere(List<Obj> objects, List<Light> lights, List<Obj> blacks) {
        Random rand = new Random();
        Sphere sphere = new Sphere(30, new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 0.9));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 30, 0, 0);
        //sphere.setCapacity(CapacityTypeEnum.FULL);
        sphere.setReflectionRatio(0.6);
        objects.add(sphere);

        Torus torus = new Torus(30, 10, new Color(1, 0, 0, 0.4));
        torus.updateMatrices(0, 90, 0, 1, 1, 1, 0, 0, 0);
        torus.setCapacity(CapacityTypeEnum.FULL);
        //sphere.setReflectionRatio(1);
        torus.density = 2;
        objects.add(torus);

        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -60);
        plane1.setNormal();
        objects.add(plane1);

        Plane plane2 = new Plane(new Color(0.9, 0.1, 0));
        plane2.updateMatrices(0, 90, 0, 1, 1, 1, 100, 0, 0);
        plane2.setNormal();
        plane2.setTexture("./src/main/resources/textures/random/numbers.png");
        objects.add(plane2);

        Light light = new Light(new Point3D(-60, 60, 0));
        //lights.add(light);

        return new Camera(new Point3D(-120, 0, 0), new Vector3D(1, -0.2, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getRefractedSpheres(List<Obj> objects, List<Light> lights) {
        Cube c1 = new Cube(50);
        c1.updateMatrices(0, 0, 0, 1, 1, 1, 0, -75, 25);
        c1.setNormals();
        c1.setTexture("./src/main/resources/textures/random/numbers.png");
        objects.add(c1);

        c1 = new Cube(50);
        c1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 25);
        c1.setNormals();
        c1.setTexture("./src/main/resources/textures/random/numbers.png");
        objects.add(c1);

        c1 = new Cube(50);
        c1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 75, 25);
        c1.setNormals();
        c1.setTexture("./src/main/resources/textures/random/numbers.png");
        objects.add(c1);

        Sphere sp1 = new Sphere(20, new Color(1, 1, 1, 1));
        sp1.updateMatrices(0, 90, 0, 1, 1, 1, 0, -75, 70);
        sp1.setCapacity(CapacityTypeEnum.FULL);
        sp1.density = 2;
        objects.add(sp1);

        sp1 = new Sphere(20, new Color(1, 1, 1, 1));
        sp1.updateMatrices(0, 90, 0, 1, 1, 1, 0, 0, 70);
        sp1.setCapacity(CapacityTypeEnum.FULL);
        sp1.density = 5;
        objects.add(sp1);

        sp1 = new Sphere(20, new Color(1, 1, 1, 1));
        sp1.updateMatrices(0, 90, 0, 1, 1, 1, 0, 75, 70);
        sp1.setCapacity(CapacityTypeEnum.FULL);
        sp1.density = 10;
        objects.add(sp1);

        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -60);
        plane1.setNormal();
        plane1.lineValue = 100;
        plane1.columnValue = 100;
        objects.add(plane1);

        Light light = new Light(new Point3D(-1000, 1000, 1000));
        //lights.add(light);

        return new Camera(new Point3D(-300, 0, 50), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                 TEST                  *

     * * * * * * * * * * * * * * * * * * * * */

    public static Camera getSphereInTorus(List<Obj> objects, List<Light> lights) {
        Sphere sp = new Sphere(5, new Color(0.25, 0.365, 0.7));
        sp.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        sp.setReflectionRatio(1);
        objects.add(sp);

        Torus torus = new Torus(20, 5, new Color(0.25, 0.365, 0.7));
        torus.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        objects.add(torus);

        Light light = new Light(new Point3D(-100, 100, 0));
        //lights.add(light);

        return new Camera(new Point3D(-30, 0, 10), new Vector3D(1, 0, -0.3), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleTetrahedronFractal(List<Obj> objects, List<Light> lights, List<Obj> blackObjects, double alpha) {
        Polygon fractal = PolygonFactory.createPolygon(PolygonTypeEnum.TETRAHEDRON_FRACTAL, 60, 3);
        if(fractal != null) {
            fractal.updateMatrices(0, 0, alpha, 1, 1, 1, 0, 0, 0);
            fractal.clearColors();
            //fractal.addColor(new Color(1, 0, 0));
            fractal.addColor(new Color(0, 1, 0));
            fractal.addColor(new Color(0, 0, 1));
            fractal.setPattern(PatternTypeEnum.GRADIENT);
            objects.add(fractal);
        }

        lights.add(new Light(new Point3D(100, 100, 100)));

        return new Camera(new Point3D(-150, 0, 20), new Vector3D(1, 0, -(20.0 / 150.0)), new Vector3D(0, 0, 1), 90);
    }

    public static Camera test(List<Obj> objects, List<Light> lights, List<Obj> blacks) {
        Random rand = new Random();
        Sphere sphere = new Sphere(30, new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 0.5));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 40, 0);
        sphere.setCapacity(CapacityTypeEnum.FULL);
        sphere.density = 0.5;
        objects.add(sphere);

        sphere = new Sphere(30);
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, -40, 0);
        sphere.setReflectionRatio(0.6);
        objects.add(sphere);

        Polygon polygon = PolygonFactory.createPolygon(PolygonTypeEnum.DIAMOND, 3, 30, 50, 15, 50);
        polygon.updateMatrices(0, -30, 0, 1, 1, 1, 50, 0, -30);
        polygon.setCapacity(CapacityTypeEnum.FULL);
        polygon.clearColors();
        polygon.addColor(new Color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble(), 0.5));
        polygon.density = 2.5;
        //objects.add(polygon);

        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -70);
        plane1.setNormal();
        objects.add(plane1);

        return new Camera(new Point3D(-120, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleMobiusTape(List<Obj> objects, List<Light> lights) {

        MobiusTape mt = new MobiusTape(5);
        mt.updateMatrices(0, 0, 180, 1, 1, 1, 0, 0, 0);
        objects.add(mt);

        lights.add(new Light(new Point3D(-100, 100, 100)));

        //top
        //return new Camera(new Point3D(0, 0, 3), new Vector3D(0, 0, -1), new Vector3D(1, 0, 0), 90);
        //left
        //return new Camera(new Point3D(0, -3, 0), new Vector3D(0, 1, 0), new Vector3D(0, 0, 1), 90);
        //right
        //return new Camera(new Point3D(0, 2.5, 0), new Vector3D(0, -1, 0), new Vector3D(0, 0, 1), 90);
        //front
        return new Camera(new Point3D(-3, 0, 1.5), new Vector3D(1, 0, -0.5), new Vector3D(0, 0, 1), 90);
        //back
        //return new Camera(new Point3D(2.5, 0, 0), new Vector3D(-1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleCubicSurface(List<Obj> objects, List<Light> lights) {
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

    /* * * * * * * * * * * * * * * * * * * * *

     *             NOISE OBJECTS             *

     * * * * * * * * * * * * * * * * * * * * */

    public static Camera getSimpleCubeNoise(List<Obj> objects, List<Light> lights, List<Obj> blacks) {
        GradientNoise3D gradientNoise3D1 = new GradientNoise3D(5);
        gradientNoise3D1.setAmplitude(4);

        GradientNoise3D gradientNoise3D2 = new GradientNoise3D(16);
        gradientNoise3D2.setAmplitude(1);

        GradientNoise3D gradientNoise3D3 = new GradientNoise3D(32);
        gradientNoise3D3.setAmplitude(0.1);

        GradientNoise3D gradientNoise3D4 = new GradientNoise3D(64);
        gradientNoise3D4.setAmplitude(10);

        WorleyNoise3D worleyNoise3 =  new WorleyNoise3D(10, 3);

        gradientNoise3D1.setNoise(gradientNoise3D2);
        gradientNoise3D2.setNoise(gradientNoise3D3);
        //gradientNoise3D3.setNoise(gradientNoise3D4);

        Cube cube = new Cube(50, PatternTypeEnum.NOISE, new Color(1, 0.1, 0.15), new Color());
        cube.updateMatrices(0, -35, 30, 1, 1, 1, 0,0, 0);
        cube.setNormals();
        cube.setCapacity(CapacityTypeEnum.FULL);
        cube.noise = gradientNoise3D1;
        objects.add(cube);

        return new Camera(new Point3D(-100, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleSquareNoise(List<Obj> objects, List<Light> lights, List<Obj> blacks) {

        GradientNoise noise = new GradientNoise(2);
        noise.setAmplitude(4);

        Square square = new Square(50, new Color());
        square.updateMatrices(0, 90, 0, 1, 1, 1, 0, 0, 0);
        square.setNormal();
        square.noise = new WorleyNoise(10, 1);
        objects.add(square);

        return new Camera(new Point3D(-100, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleSphereNoise(List<Obj> objects, List<Light> lights, List<Obj> blacks) {
        GradientNoise3D gradientNoise3D1 = new GradientNoise3D(16);
        gradientNoise3D1.setAmplitude(5);

        GradientNoise3D gradientNoise3D2 = new GradientNoise3D(8);
        gradientNoise3D2.setAmplitude(10);

        GradientNoise3D gradientNoise3D3 = new GradientNoise3D(4);
        gradientNoise3D3.setAmplitude(1);

        GradientNoise3D gradientNoise3D4 = new GradientNoise3D(2);
        gradientNoise3D4.setAmplitude(1);

        GradientNoise3D gradientNoise3D5 = new GradientNoise3D(128);
        gradientNoise3D5.setAmplitude(2);

        gradientNoise3D1.setNoise(gradientNoise3D2);
        gradientNoise3D2.setNoise(gradientNoise3D3);
        //gradientNoise3D3.setNoise(gradientNoise3D4);
        //gradientNoise3D4.setNoise(gradientNoise3D5);

        Sphere sphere = new Sphere(25, PatternTypeEnum.NOISE,
                new Color(1, 0.12, 0.25)
        );
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        sphere.noise = gradientNoise3D1;
        objects.add(sphere);

        return new Camera(new Point3D(-100, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimplePlaneNoise(List<Obj> objects, List<Light> lights, List<Obj> blacks) {
        GradientNoise gradientNoiser = new GradientNoise(10);
        gradientNoiser.setAmplitude(4);
        gradientNoiser.setRange(NoiseRangeEnum.INFINITY);

        Plane plane = new Plane();
        plane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        plane.setNormal();
        plane.noise = gradientNoiser;
        objects.add(plane);

        return new Camera(new Point3D(-4, 0, 5), new Vector3D(1, 0, -1), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getNoiseScene(List<Obj> objects, List<Light> lights, List<Obj> blacks) {

        WorleyNoise worleyNoise1 =  new WorleyNoise(10, 1);
        WorleyNoise3D worleyNoise2 =  new WorleyNoise3D(10, 2);
        WorleyNoise3D worleyNoise3 =  new WorleyNoise3D(10, 3);

        WorleyNoise3D worleyNoise3D = new WorleyNoise3D(10, 2);

        GradientNoise gradientNoise1 = new GradientNoise(5);
        gradientNoise1.setAmplitude(4);

        GradientNoise3D gradientNoise3D1 = new GradientNoise3D(5);
        gradientNoise3D1.setAmplitude(4);

        GradientNoise3D gradientNoise3D2 = new GradientNoise3D(16);
        gradientNoise3D2.setAmplitude(1);

        GradientNoise3D gradientNoise3D3 = new GradientNoise3D(32);
        gradientNoise3D3.setAmplitude(1);

        GradientNoise3D gradientNoise3D4 = new GradientNoise3D(64);
        gradientNoise3D4.setAmplitude(10);

        gradientNoise3D1.setNoise(gradientNoise3D2);
        //gradientNoise3D2.setNoise(gradientNoise3D3);
        gradientNoise3D3.setNoise(gradientNoise3D4);

        Sphere sphere = new Sphere(25, PatternTypeEnum.NOISE, new Color());
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        sphere.noise = gradientNoise3D1;
        objects.add(sphere);

        sphere = new Sphere(25, PatternTypeEnum.NOISE, new Color());
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, -60, 0);
        sphere.noise = worleyNoise1;
        objects.add(sphere);

        sphere = new Sphere(25, PatternTypeEnum.NOISE, new Color());
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 60, 0);
        sphere.noise = gradientNoise1;
        objects.add(sphere);

        sphere = new Sphere(25, PatternTypeEnum.NOISE, new Color());
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 60);
        sphere.noise = worleyNoise3D;
        objects.add(sphere);

        sphere = new Sphere(25, PatternTypeEnum.NOISE, new Color());
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, -60, 60);
        sphere.noise = worleyNoise2;
        objects.add(sphere);

        sphere = new Sphere(25, PatternTypeEnum.NOISE, new Color());
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 60, 60);
        sphere.noise = worleyNoise3;
        objects.add(sphere);

        Plane plane = new Plane();
        plane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -25);
        plane.setNormal();
        plane.setReflectionRatio(0.75);
        objects.add(plane);

        return new Camera(new Point3D(-200, 0, 100), new Vector3D(1, 0, -0.5), new Vector3D(0, 0, 1), 90);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             BLACK OBJECTS             *

     * * * * * * * * * * * * * * * * * * * * */

    public static Camera getSimpleBlackSphereOnSphere(List<Obj> objects, List<Light> lights, List<Obj> blackObjects) {
        Sphere sphere = new Sphere(30);
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        sphere.setCapacity(CapacityTypeEnum.FULL);
        objects.add(sphere);

        BlackSphere blackSphere = new BlackSphere(30);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, -30, 0, 30);
        sphere.addBlackObject(blackSphere);

        lights.add(new Light(new Point3D(-100, 100, 100)));

        return new Camera(new Point3D(-100, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleBlackTorusOnTorus(List<Obj> objects, List<Light> lights, List<Obj> blackObjects) {
        Torus torus = new Torus(30, 5);
        torus.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        torus.setCapacity(CapacityTypeEnum.FULL);
        objects.add(torus);

        BlackTorus blackTorus = new BlackTorus(37, 5);
        blackTorus.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        blackObjects.add(blackTorus);

        blackTorus = new BlackTorus(23, 5);
        blackTorus.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        blackObjects.add(blackTorus);

        blackTorus = new BlackTorus(30, 5);
        blackTorus.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 7);
        blackObjects.add(blackTorus);

        blackTorus = new BlackTorus(30, 5);
        blackTorus.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -7);
        blackObjects.add(blackTorus);

        lights.add(new Light(new Point3D(-100, 100, 100)));

        return new Camera(new Point3D(-100, 0, 50), new Vector3D(1, 0, -0.5), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getHoledCube(List<Obj> objects, List<Light> lights, List<Obj> blackObjects) {
        Cube cube = new Cube(50);
        cube.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        cube.setCapacity(CapacityTypeEnum.FULL);
        objects.add(cube);

        BlackCylinder blackCylinder = new BlackCylinder(12.5);
        blackCylinder.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        blackObjects.add(blackCylinder);

        blackCylinder = new BlackCylinder(12.5);
        blackCylinder.updateMatrices(0, 90, 0, 1, 1, 1, 0, 0, 0);
        blackObjects.add(blackCylinder);

        blackCylinder = new BlackCylinder(12.5);
        blackCylinder.updateMatrices(90, 0, 0, 1, 1, 1, 0, 0, 0);
        blackObjects.add(blackCylinder);

        return new Camera(new Point3D(-100, 50, 50), new Vector3D(1, -0.5, -0.5), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getSimpleBlackSphereOnCube(List<Obj> objects, List<Light> lights, List<Obj> blackObjects) {
        GradientNoise3D gradientNoise3D1 = new GradientNoise3D(5);
        gradientNoise3D1.setAmplitude(4);

        GradientNoise3D gradientNoise3D2 = new GradientNoise3D(16);
        gradientNoise3D2.setAmplitude(1);

        GradientNoise3D gradientNoise3D3 = new GradientNoise3D(32);
        gradientNoise3D3.setAmplitude(0.1);

        GradientNoise3D gradientNoise3D4 = new GradientNoise3D(64);
        gradientNoise3D4.setAmplitude(10);
        gradientNoise3D1.setNoise(gradientNoise3D2);
        gradientNoise3D2.setNoise(gradientNoise3D3);
        //gradientNoise3D3.setNoise(gradientNoise3D4);

        Cube cube = new Cube(50, PatternTypeEnum.NOISE, new Color());
        cube.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        cube.noise = gradientNoise3D1;
        cube.setCapacity(CapacityTypeEnum.FULL);
        objects.add(cube);

        BlackSphere blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, -25, 25, 25);
        blackObjects.add(blackSphere);

        blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, -25, 25, -25);
        blackObjects.add(blackSphere);

        blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, -25, -25, 25);
        blackObjects.add(blackSphere);

        blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, -25, -25, -25);
        blackObjects.add(blackSphere);

        blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, -25, 0, 0);
        blackObjects.add(blackSphere);

        blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 25);
        blackObjects.add(blackSphere);

        blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, 25, 25, 25);
        blackObjects.add(blackSphere);

        blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, 25, -25, 25);
        blackObjects.add(blackSphere);

        blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, -25, 25, -25);
        blackObjects.add(blackSphere);

        blackSphere = new BlackSphere(10);
        blackSphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 25, 0);
        blackObjects.add(blackSphere);

        BlackCylinder cylinder = new BlackCylinder(5);
        cylinder.updateMatrices(45, 0, 0, 1, 1, 1, -25, 0, 0);
        blackObjects.add(cylinder);

        cylinder = new BlackCylinder(5);
        cylinder.updateMatrices(-45, 0, 0, 1, 1, 1, -25, 0, 0);
        blackObjects.add(cylinder);

        BlackTorus blackTorus = new BlackTorus(20, 5);
        blackTorus.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 25);
        blackObjects.add(blackTorus);

        BlackCube blackCube = new BlackCube(10);
        blackCube.updateMatrices(0, 0, 0, 1, 1, 1, 25, 25, 0);
        blackCube.setNormals();
        blackObjects.add(blackCube);

        blackCube = new BlackCube(10);
        blackCube.updateMatrices(0, 0, 0, 1, 1, 1, -25, 25, 0);
        blackCube.setNormals();
        blackObjects.add(blackCube);

        blackCube = new BlackCube(10);
        blackCube.updateMatrices(0, 0, 0, 1, 1, 1, 0, 25, -25);
        blackCube.setNormals();
        blackObjects.add(blackCube);


        //lights.add(new Light(new Point3D(-100, 100, 100)));

        return new Camera(new Point3D(-100, 50, 50), new Vector3D(1, -0.5, -0.50), new Vector3D(0, 0, 1), 90);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             SHADOW IMAGES             *

     * * * * * * * * * * * * * * * * * * * * */

    public static Camera getSimpleShadow(List<Obj> objects, List<Light> lights) {
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

        lights.add(new Light(new Point3D(-50, 0, 15)));

        return new Camera(new Point3D(-300, 0, 30), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getMultipleShadow(List<Obj> objects, List<Light> lights) {
        Plane horizontalPlane = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        horizontalPlane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -30);
        horizontalPlane.setNormal();
        objects.add(horizontalPlane);

        Plane verticalPlane = new Plane(new Color(0.5, 0.5, 1));
        verticalPlane.updateMatrices(0, -90, 0, 1, 1, 1, 50, 0, 0);
        verticalPlane.setNormal();
        objects.add(verticalPlane);

        Sphere sphere = new Sphere(30, new Color(0, 0,1, 1));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 30);
        objects.add(sphere);

        lights.add(new Light(new Point3D(-50, 40, 15)));
        lights.add(new Light(new Point3D(-50, 0, 15)));
        lights.add(new Light(new Point3D(-50, -40, 15)));

        return new Camera(new Point3D(-300, 0, 30), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getMitigateShadow(List<Obj> objects, List<Light> lights) {
        Plane horizontalPlane = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        horizontalPlane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -30);
        horizontalPlane.setNormal();
        objects.add(horizontalPlane);

        Plane verticalPlane = new Plane(new Color(0.5, 0.5, 0.5));
        verticalPlane.updateMatrices(0, -90, 0, 1, 1, 1, 50, 0, 0);
        verticalPlane.setNormal();
        objects.add(verticalPlane);

        Sphere sphere = new Sphere(30, new Color(0, 0,1, 0.5));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, -40, 30);
        objects.add(sphere);

        Sphere translucentSphere = new Sphere(30, PatternTypeEnum.GRID, new Color(0, 0,1, 0.), new Color(0, 0,1, 1));
        translucentSphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 40, 30);
        objects.add(translucentSphere);

        lights.add(new Light(new Point3D(-75, 0, 15)));

        return new Camera(new Point3D(-300, 0, 30), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getProjectedShadow(List<Obj> objects, List<Light> lights) {
        Plane verticalPlane = new Plane();
        verticalPlane.updateMatrices(0, -90, 0, 1, 1, 1, 250, 0, 0);
        verticalPlane.setNormal();
        objects.add(verticalPlane);

        Square square = new Square(150);
        square.updateMatrices(0, -90, 0, 1, 1, 1, -150, 0, 0);
        square.setTexture("./src/main/resources/textures/rgba/rgba.png");
        square.setNormal();
        objects.add(square);

        lights.add(new Light(new Point3D(-500, 0, 0)));

        return new Camera(new Point3D(-300, 100, 0), new Vector3D(1, -0.30, 0), new Vector3D(0, 0, 1), 90);
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *             COMPLEX IMAGES            *

     * * * * * * * * * * * * * * * * * * * * */

    public static Camera getFieldOfCube(List<Obj> objects, List<Light> lights, List<Obj> blacks) {
        GradientNoise gradientNoise = new GradientNoise(3);
        gradientNoise.setAmplitude(10);
        Random rand = new Random();
        double cubeSize = 10;
        double size = 10;
        for(int i = 0; i < size; ++i) {
            for(int j = 0; j < size; ++j) {
                double x = (double)i / size;
                double y = (double)j / size;
                double scale = Math.abs(gradientNoise.getValue(x, y));
                Cube cube = new Cube(cubeSize, new Color(1, 1, 1, 0.1));
                cube.updateMatrices(0, 0, 0, 1, 1, 1 * scale, i * cubeSize, j * cubeSize, 0);
                cube.setNormals();
                objects.add(cube);
            }
        }


        return new Camera(new Point3D(-100, -100, 100), new Vector3D(1, 1, -1), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getRings(List<Obj> objects, List<Light> lights) {
        Cube cube = new Cube(100, new Color(0.6, 0.8, 0.5, 0.1));
        cube.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        cube.setReflectionRatio(0.75);
        objects.add(cube);

        Torus torus1 = new Torus(20, 5, PatternTypeEnum.UNIFORM, new Color("#DB7093"));
        torus1.updateMatrices(0, 110, 0, 1, 1, 1, 0, -10, 0);
        //torus1.setCapacity(CapacityTypeEnum.FULL);
        objects.add(torus1);

        Torus torus2 = new Torus(20, 5, PatternTypeEnum.UNIFORM, new Color("#0B0B45"));
        torus2.updateMatrices(0, 20, 0, 1, 1, 1, 0, 10, 0);
        //torus2.setCapacity(CapacityTypeEnum.FULL);
        objects.add(torus2);

        lights.add(new Light(new Point3D(-100, 100, 100)));
        //lights.add(new Light(new Point3D(-100, -100, -100)));
        //lights.add(new Light(new Point3D(-100, 0, 0)));
        //lights.add(new Light(new Point3D(0, 0, 0)));

        return new Camera(new Point3D(-200, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getMirrorBox(List<Obj> objects, List<Light> lights, List<Obj> blacks) {
        Cube cube = new Cube(100, new Color(120.0 / 255, 120.0 / 255, 120.0 / 255));
        cube.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        cube.setReflectionRatio(0.9);
        objects.add(cube);

        Sphere sphere = new Sphere(8, new Color(1, 0, 0));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        sphere.setReflectionRatio(0.75);
        objects.add(sphere);

        return new Camera(new Point3D(-45, 0, 20), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getMirrorBox2(List<Obj> objects, List<Light> lights, List<Obj> blacks) {
        Cube cube = new Cube(100, new Color(120.0 / 255, 120.0 / 255, 120.0 / 255));
        cube.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        cube.setNormals();
        cube.setReflectionRatio(0.9);
        objects.add(cube);

        Sphere sphere = new Sphere(8, new Color(1, 0, 0));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 15, -15, 15);
        sphere.setReflectionRatio(0.25);
        objects.add(sphere);

        sphere = new Sphere(8, new Color(1, 0, 0));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 15, 15, 15);
        sphere.setReflectionRatio(0.50);
        objects.add(sphere);

        sphere = new Sphere(8, new Color(1, 0, 0));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 15, -15, -15);
        sphere.setReflectionRatio(0.75);
        objects.add(sphere);

        sphere = new Sphere(8, new Color(1, 0, 0));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 15, 15, -15);
        sphere.setReflectionRatio(0.99);
        objects.add(sphere);

        return new Camera(new Point3D(-45, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getTripleTorus(List<Obj> objects, List<Light> lights) {
        Square ground = new Square(240, PatternTypeEnum.GRID, new Color(0, 0, 0), new Color(1, 1, 1));
        ground.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -40);
        ground.setNormal();
        ground.setReflectionRatio(0.5);
        objects.add(ground);

        Torus torus1 = new Torus(25, 4, new Color(1, 1, 0));
        torus1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        torus1.setReflectionRatio(0.5);
        objects.add(torus1);

        Torus torus2 = new Torus(25, 4, new Color(1, 0, 1));
        torus2.updateMatrices(0, 90, 0, 1, 1, 1, 0, 0, 0);
        torus2.setReflectionRatio(0.5);
        objects.add(torus2);

        Torus torus3 = new Torus(25, 4, new Color(0, 1, 1));
        torus3.updateMatrices(0, 90, 90, 1, 1, 1, 0, 0, 0);
        torus3.setReflectionRatio(0.5);
        objects.add(torus3);

        Sphere sphere = new Sphere(8, new Color(0, 0, 0.1));
        sphere.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        sphere.setReflectionRatio(0.5);
        objects.add(sphere);

        lights.add(new Light(new Point3D(-200, 200, 200)));

        return new Camera(new Point3D(-80, 80, 80), new Vector3D(1, -1, -1), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getAll(List<Obj> objects, List<Light> lights, List<Obj> blacks) {

        GradientNoise gradientNoise1 = new GradientNoise(4);
        gradientNoise1.setAmplitude(4);

        GradientNoise gradientNoise2 = new GradientNoise(8);
        gradientNoise1.setAmplitude(2);

        gradientNoise1.setNoise(gradientNoise2);
        // SIMPLE OBJECTS

        // Sphere
        Sphere sphere = new Sphere(40, PatternTypeEnum.GRID, new Color(0.977074, 0.524043, 0.985372), new Color(0.494861, 0.479416, 0.412559));
        sphere.updateMatrices(25, 210, 100, 1, 1, 1, 0, 0, 0);
        objects.add(sphere);

        sphere = new Sphere(60, new Color(0.013436, 0.119706, 0.325974));
        sphere.updateMatrices(0, 0, 0, 1, 1, 0.75, 300, 200, 0);
        sphere.setCapacity(CapacityTypeEnum.FULL);
        objects.add(sphere);

        sphere = new Sphere(60);
        sphere.updateMatrices(0, 0, 0, 1, 1, 1.5, 300, 200, 200);
        sphere.setCapacity(CapacityTypeEnum.FULL);
        sphere.setTexture("./src/main/resources/textures/planets/earthNight.jpeg");
        objects.add(sphere);

        // Cone
        Cone cone = new Cone(30, new Color(0.007845, 0.038946, 0.221346));
        cone.updateMatrices(0, 0, 0, 1, 1, 1, 300, -200, 0);
        cone.setCapacity(CapacityTypeEnum.FULL);
        objects.add(cone);

        BlackCylinder blackCylinder = new BlackCylinder(100);
        blackCylinder.updateMatrices(0, 90, 0, 1, 1, 1, 0, 0, 200);
        cone.addBlackObject(blackCylinder);

        // Cube
        Cube cube = new Cube(50, new Color(0.5478, 0.367, 1, 0.1));
        cube.updateMatrices(78, 159, 12, 1, 1, 1, -200, -100, 0);
        cube.setNormals();
        cube.setCapacity(CapacityTypeEnum.FULL);
        objects.add(cube);

        // Cylinder
        Cylinder cylinder = new Cylinder(40, PatternTypeEnum.HORIZONTAL_LINED, new Color(0.270390, 0.819424, 0.743335), new Color(0.383270, 0.560625, 0.790191));
        cylinder.updateMatrices(0, 0, 0, 1, 1, 1, 300, 200, 0);
        objects.add(cylinder);

        // Torus
        Torus torus = new Torus(70, 15, PatternTypeEnum.GRADIENT, new Color("#5EB1BF"), new Color("#CDEDF6"), new Color("#AB81CD"), new Color("#E2ADF2"));
        torus.updateMatrices(90, 0, 0, 1, 1, 1, -200, -150, 0);
        objects.add(torus);

        // PLANE SURFACES

        // Disk
        Disk disk = new Disk(50, 70, new Color(0.578585, 0.618446, 0.976605));
        disk.updateMatrices(-30, -20, 0, 1, 1, 1, 0, 0, 0);
        disk.setNormal();
        objects.add(disk);

        // Plane
        Plane plane = new Plane(new Color("#17183B"));
        plane.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -100);
        plane.setNormal();
        objects.add(plane);

        // Square
        Square square = new Square(120, PatternTypeEnum.NOISE, new Color(0.622888, 0.487868, 0.943172), new Color());
        square.updateMatrices(0, 0, 0, 1, 1, 1, -163, 0, -99);
        square.setNormal();
        square.noise = gradientNoise1;
        square.setReflectionRatio(0.25);
        objects.add(square);

        // FRACTALS

        // Cube fractal
        CubeFractal cubeFractal = new CubeFractal(3, 120, new Color(0.608065, 0.108303, 0.947895));
        cubeFractal.updateMatrices(0, 0, 30, 1, 1, 1, 300, 340, -39);
        cubeFractal.setNormals();
        objects.add(cubeFractal);

        // COMPOSED OBJECTS

        // Star
        Polygon star = PolygonFactory.createPolygon(PolygonTypeEnum.STAR, 5, 40, 90, 30);
        star.updateMatrices(0, -90, 0, 1, 1, 1, 300, -200, 200);
        objects.add(star);

        star.clearColors();
        star.addColor(new Color("#2274A5"));


        lights.add(new Light(new Point3D(-450, 20, 100), new Color(1, 1, 1)));

        return new Camera(new Point3D(-600, 0, 150), new Vector3D(1, 0, -0.25), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getMoon(List<Obj> objects, List<Light> lights) {
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

    public static Camera getSolarSystem(List<Obj> objects, List<Light> lights) {
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
        mercuryCircle.setNormal();
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
        venusCircle.setNormal();
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
        earthCircle.setNormal();
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
        marsCircle.setNormal();
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
        jupiterCircle.setNormal();
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
        saturnCircle.setNormal();
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
        uranusCircle.setNormal();
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
        neptuneCircle.setNormal();
        neptuneCircle.setPattern(PatternTypeEnum.GRADIENT);
        objects.add(neptuneCircle);

        Sphere milkyWay = new Sphere(1200);
        milkyWay.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        milkyWay.setTexture("./src/main/resources/textures/planets/milkyWay.jpeg");
        objects.add(milkyWay);

        return new Camera(new Point3D(-600, 350, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getBibou(List<Obj> objects, List<Light> lights) {

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

    public static Camera getChestGame(List<Obj> objects, List<Light> lights, List<Obj> blacks) {

        Square board = new Square(160, PatternTypeEnum.GRID, new Color(0, 0, 0), new Color(1, 1, 1));
        board.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, -17.6);
        board.setNormal();
        //board.setReflectionRatio(0.2);
        board.lineValue = 8;
        board.columnValue = 8;
        objects.add(board);

        // WHITE PAWNS
        int x = -50;
        int y = -70;
        for (int i = 0; i < 8; ++i) {
            Sphere sp1 = new Sphere(5, new Color(1, 1, 1));
            sp1.updateMatrices(0, 0, 0, 1, 1, 1, x, y, 3);
            sp1.setReflectionRatio(0.2);
            objects.add(sp1);

            Torus torus1 = new Torus(3.5, 2, new Color(1, 1, 1));
            torus1.updateMatrices(0, 0, 0, 1, 1, 1, x, y, 0);
            torus1.setReflectionRatio(0.2);
            objects.add(torus1);

            /*OpenCylinder cylinder = new OpenCylinder(3.5, 15, new Color(1, 1, 1));
            cylinder.updateMatrices(0, 0, 0, 1, 1, 1, x, y, -7.5);
            cylinder.setReflectionRatio(0.2);
            objects.add(cylinder);

             */
            QuadraticSurface s = new QuadraticSurface(1, 1, -0.075, 0, 0, 0, 0, 0, 0, -5, new Color(1, 1, 1));
            s.updateMatrices(0, 0, 0, 1, 1, 1, x, y, -3);
            s.maxHeight = 3;
            s.minHeight = -16.5;
            objects.add(s);

            Torus torus2 = new Torus(3.5, 2, new Color(1, 1, 1));
            torus2.updateMatrices(0, 0, 0, 1, 1, 1, x, y, -15);
            torus2.setReflectionRatio(0.2);
            objects.add(torus2);

            Torus torus3 = new Torus(4, 2.2, new Color(1, 1, 1));
            torus3.updateMatrices(0, 0, 0, 1, 1, 1, x, y, -16.5);
            torus3.addCut(CutTypeEnum.BOTTOM);
            torus3.setReflectionRatio(0.2);
            objects.add(torus3);

            y += 20;
        }

        // BLACK PAWNS
        x = 50;
        y = -70;
        for (int i = 0; i < 8; ++i) {
            Sphere sp1 = new Sphere(5, new Color(0.1, 0.1, 0.2));
            sp1.updateMatrices(0, 0, 0, 1, 1, 1, x, y, 3);
            objects.add(sp1);

            Torus torus1 = new Torus(3.5, 2, new Color(0.1, 0.1, 0.2));
            torus1.updateMatrices(0, 0, 0, 1, 1, 1, x, y, 0);
            objects.add(torus1);

            QuadraticSurface s = new QuadraticSurface(1, 1, -0.075, 0, 0, 0, 0, 0, 0, -5, new Color(0.1, 0.1, 0.2));
            s.updateMatrices(0, 0, 0, 1, 1, 1, x, y, -3);
            s.maxHeight = 3;
            s.minHeight = -16.5;
            objects.add(s);

            Torus torus2 = new Torus(3.5, 2, new Color(0.1, 0.1, 0.2));
            torus2.updateMatrices(0, 0, 0, 1, 1, 1, x, y, -15);
            objects.add(torus2);

            Torus torus3 = new Torus(4, 2.2, new Color(0.1, 0.1, 0.2));
            torus3.updateMatrices(0, 0, 0, 1, 1, 1, x, y, -16.5);
            torus3.addCut(CutTypeEnum.BOTTOM);
            objects.add(torus3);

            y += 20;
        }



        lights.add(new Light(new Point3D(-100, 100, 100)));

        return new Camera(new Point3D(-150, 0, 150), new Vector3D(1, 0, -1), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getPawn(List<Obj> objects, List<Light> lights, List<Obj> blacks) {

        Assembly assembly = ChessPieceFactory.createPiece(ChessPieceEnum.PAWN, 0.0);
        assembly.updateMatrices(0, -10, 0, 1, 1, 1, 0, 0, 0);
        objects.add(assembly);

        assembly = ChessPieceFactory.createPiece(ChessPieceEnum.PAWN, 0.0);
        assembly.updateMatrices(0, -10, 0, 1, 1, 1, 20, -30, 0);
        objects.add(assembly);

        assembly = ChessPieceFactory.createPiece(ChessPieceEnum.PAWN, 0.0);
        assembly.updateMatrices(0, -10, 0, 1, 1, 1, 20, 30, 0);
        objects.add(assembly);

        lights.add(new Light(new Point3D(-100, 100, 100), new Color(0.2, 0.2, 1)));

        return new Camera(new Point3D(-70, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getHyperboloid(List<Obj> objects, List<Light> lights, List<Obj> blacks) {

        Hyperboloid s = new Hyperboloid(0.5, 0.5, 1, 5);
        s.updateMatrices(90, 0, 0, 1, 1, 1, 0, 0, 0);
        s.isLimited = true;
        s.upperLimit = 3;
        s.lowerLimit = -16.5;
        objects.add(s);

        lights.add(new Light(new Point3D(-100, 100, 100), new Color(0.2, 0.2, 1)));

        return new Camera(new Point3D(-70, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }

    public static Camera getTower(List<Obj> objects, List<Light> lights, List<Obj> blacks) {

        int x = 0;
        int y = 0;

        // TOP
        Pipe pipe = new Pipe(10, 8, 5);
        pipe.setCapacity(CapacityTypeEnum.FULL);
        pipe.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 10);
        pipe.setNormals();
        objects.add(pipe);

        BlackCube blackCube = new BlackCube(5);
        blackCube.updateMatrices(0, 0, 0, 5, 1, 2, 0, 0, 10);
        blackCube.setNormals();
        blacks.add(blackCube);

        blackCube = new BlackCube(5);
        blackCube.updateMatrices(0, 0, 0, 1, 5, 2, 0, 0, 0);
        blackCube.setNormals();
        blacks.add(blackCube);

        // CENTER
        ClosedCylinder closedCylinder = new ClosedCylinder(9, 15);
        closedCylinder.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        closedCylinder.setNormals();
        objects.add(closedCylinder);

        // BOTTOM

        //lights.add(new Light(new Point3D(-100, 100, 100), new Color(0.2, 0.2, 1)));

        return new Camera(new Point3D(-60, 0, 20), new Vector3D(1, 0, -0.30), new Vector3D(0, 0, 1), 90);
    }

    public static Camera test2(List<Obj> objects, List<Light> lights, List<Obj> blacks) {

        Sphere sphere1 = new Sphere(75);
        sphere1.updateMatrices(0, 0, 0, 1, 1, 1, 450, 0, 0);
        objects.add(sphere1);

        sphere1 = new Sphere(120);
        sphere1.updateMatrices(0, 0, 0, 1, 1, 1, 1200, 300, 0);
        objects.add(sphere1);

        sphere1 = new Sphere(10);
        sphere1.updateMatrices(0, 0, 0, 1, 1, 1, 100, -30, 0);
        objects.add(sphere1);

        Plane plane = new Plane(PatternTypeEnum.GRID, new Color(), new Color());
        plane.updateMatrices(0, 0, 0, 1, 1, 1,0, 0, -100);
        plane.setNormal();
        objects.add(plane);

        return new Camera(new Point3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1), 90);
    }
}