import enums.FilterTypeEnum;
import enums.PatternTypeEnum;
import lights.Light;
import math.Line3D;
import math.Point3D;
import math.Vector3D;
import objects.*;
import utils.Color;
import utils.Filter;
import utils.Intersection;
import utils.SceneMaker;
import visual.Camera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RayTracer {
    private static final double EPSILON = 0.00001;
    private static final int REFLECTION_MAX = 3;
    private static final FilterTypeEnum filter = FilterTypeEnum.NONE;
    private static final Color ambientLight = new Color(1, 1, 1, 1);
    private static int height = 900;
    private static int width = 900;
    private static int antiAliasing = 5;

    private static Camera cam;
    private static List<BaseObject> objects = new ArrayList<>();
    private static List<Light> lights = new ArrayList<>();

    private static void setup() {
        cam = new Camera(new Point3D(-400, 0, 120), new Vector3D(1, 0, -0.25), new Vector3D(0, 0, 1), 90);
        cam.update(height, width);

//        // Spheres
        Sphere sphere1 = new Sphere(40, PatternTypeEnum.GRID, new Color(0, 0, 0), new Color(1, 1, 0));
        sphere1.updateMatrices(0, 45, 0, 1, 1, 1, 500, -150, 100);
        Sphere sphere2 = new Sphere(40, PatternTypeEnum.GRADIENT, new Color(0, 1, 0), new Color(0, 0, 1), new Color(1, 0, 0));
        sphere2.updateMatrices(0, 0, 0, 1, 1, 1, 500, 0, 100);
        Sphere sphere3 = new Sphere(40, new Color(0, 0, 1));
        sphere3.updateMatrices(0, 0, 0, 1, 1, 2, 500, 150, 100);
        objects.add(sphere1);
        objects.add(sphere2);
        objects.add(sphere3);

//        Sphere sphere4 = new Sphere(50, PatternTypeEnum.UNIFORM, new Color(1, 0, 0));
//        sphere4.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
//        objects.add(sphere4);

        // Plane
        Plane plane1 = new Plane(PatternTypeEnum.GRID, new Color(1, 1, 1), new Color(0, 0, 0));
        plane1.updateMatrices(0, 0, 0, 1, 1, 1, 200, 0, -20);
        plane1.setNormal();
        plane1.setReflectionRatio(0.9);
        objects.add(plane1);

//        Random rand = new Random();
//        for (int i = 0; i < 5; ++i) {
//            Sphere sp = new Sphere(10);
//            sp.updateMatrices(0, 0, 0, 1, 1, 1, rand.nextInt(800) - 400, rand.nextInt(800) - 400, 50);
//            if(rand.nextInt(10) == 1)
//                sp.setReflectionRatio(.9);
//            objects.add(sp);
//        }

//        Sphere sp = new Sphere(50, new Color(0.5, 0.5, 0.5));
//        sp.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 80);
//        sp.setReflectionRatio(0.9);
//        objects.add(sp);

//        // Cylinder
        Cylinder cylinder1 = new Cylinder(30, PatternTypeEnum.UNIFORM, new Color(1, 0, 0), new Color(0, 0, 0));
        cylinder1.updateMatrices(45, 0, 0, 1, 1, 1, 650, -75, 0);
        cylinder1.setReflectionRatio(0.8);
        Cylinder cylinder2 = new Cylinder(30, PatternTypeEnum.HORIZONTAL_LINED, new Color(0.75, 1, 0.2), new Color(0.28, .34, 0.58));
        cylinder2.updateMatrices(90, 0, 0, 1, 1, 1, 650, 75, 50);
        objects.add(cylinder1);
        objects.add(cylinder2);
//
//        // Cone
        Cone cone1 = new Cone(30, PatternTypeEnum.HORIZONTAL_LINED, new Color(0.5, 0, 0.7, 0.5), new Color(0.7, 0.2, 0.6, 0.4));
        cone1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
        cone1.setReflectionRatio(0.5);
        objects.add(cone1);
//
//        //Disk
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

        //QuadraticSurface
//        QuadraticSurface quadraticSurface1 = new QuadraticSurface(1.0 / 9, -1.0 / 16, 0, 0, 0, 0, 0, 0, -1.0 / 9, 0, new Color(1, 0, 0));
//        quadraticSurface1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
//        quadraticSurface1.setReflectionRatio(0.5);
//        objects.add(quadraticSurface1);

        // Torus
//        Torus torus1 = new Torus(100, 30, new Color(0, 0, 1, 1));
//        torus1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 30);
//        objects.add(torus1);

        //MobiusTape
//        MobiusTape mobiusTape = new MobiusTape(1, 50, new Color(1, 0, 0));
//        mobiusTape.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
//        objects.add(mobiusTape);

        //Cubic Surface
//        CubicSurface cubicSurface1 = new CubicSurface(new Color(1, 0, 0));
//        cubicSurface1.setX3(16);
//        cubicSurface1.setY3(16);
//        cubicSurface1.setZ3(1);
//        cubicSurface1.setX2Y(-48);
//        cubicSurface1.setX2Z(24);
//        cubicSurface1.setY2X(-48);
//        cubicSurface1.setY2Z(24);
//        cubicSurface1.setXYZ(1);
//        cubicSurface1.setX2(1.0 / 9);
//        cubicSurface1.setY2(-1.0 / 16);
//        cubicSurface1.setZ2(-1);
//        cubicSurface1.setXZ(10);
//        cubicSurface1.setYZ(10);
//        cubicSurface1.setZ(-1.0 / 9);
//        cubicSurface1.setK(-1);
//        cubicSurface1.updateMatrices(0, 0, 0, 1, 1, 1, 0, 0, 0);
//        cubicSurface1.setReflectionRatio(0.5);
//        objects.add(cubicSurface1);

//        Light light1 = new Light(new Point3D(-300, 0, 100), new Color(1, 1, 1));
//        lights.add(light1);
    }

    private static void sortIntersections(List<Intersection> intersections) {
        int size = intersections.size();
        for (int i = 0; i < size - 1; ++i) {
            boolean swapped = false;
            for (int j = 0; j < size - i - 1; ++j) {
                if (intersections.get(j).getDistanceFromCamera() > intersections.get(j + 1).getDistanceFromCamera()) {
                    Intersection tmp = intersections.get(j);
                    intersections.set(j, intersections.get(j + 1));
                    intersections.set(j + 1, tmp);
                    swapped = true;
                }
            }
            if(!swapped)
                break;
        }
    }

    private static void getIntersections(Line3D ray, List<Intersection> intersections) {
        for (int i = 0; i < objects.size(); ++i) {
            try {
                objects.get(i).hit(ray, intersections);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Color applyAmbientLight(Intersection intersection, Color color) {
        Color tmp = new Color(color);
        Line3D ray = new Line3D(cam.getPointOfVue(), intersection.getPointOfIntersection());
        double angle = Vector3D.angleBetween(ray.getVector(), intersection.getNormal());
        if (angle >= 90) {
            double intensity = 1.0 - (angle - 90) / 90;
            tmp.add(ambientLight.reduceOf(Math.abs(Math.cos(Math.toRadians(angle - 90))) / 1.1).reduceOf(0.75));
        }
        return tmp;
    }

    private static Color applyLights(Intersection intersection, Color color) {
        Color tmp = new Color(color);
        for (int i = 0; i < lights.size(); ++i) {
            Line3D ray = new Line3D(lights.get(i).getPoint(), intersection.getPointOfIntersection());
            ray.normalize();
//            Color shadowColor = new Color(lights.get(i).getColor());
//            shadowColor.setAlpha(0);
            double distanceFromLightToIntersection = Point3D.distanceBetween(intersection.getPointOfIntersection(), ray.getPoint());
            List<Intersection> intersections = new ArrayList<>();
            getIntersections(ray, intersections);
            sortIntersections(intersections);
            boolean bright = true;
            for(int j = 0; j < intersections.size(); ++j) {
                double distanceFromLightToNewIntersection = Point3D.distanceBetween(intersections.get(j).getPointOfIntersection(), ray.getPoint());
                if(distanceFromLightToNewIntersection < distanceFromLightToIntersection - EPSILON) {
                    // get alpha color
                    // alpha = alpha blending alpha
//                    shadowColor = Color.alphaBlending(shadowColor, intersections.get(j).getColor());
//                    // if alpha = 1 => bright = false => break
//                    if(shadowColor.getAlpha() >= 1.0 - EPSILON) {
//                        System.out.println("Break");
//                        bright = false;
//                        break;
//                    }
                    // get new color (average current color and color)
                    bright = false;
                    break;
                }
            }
            if(bright) {
                double angle = Vector3D.angleBetween(ray.getVector(), intersection.getNormal());
                if (angle >= 90) {
                    // double intensity = 1.0 - (angle - 90) / 90;
                    // add reduceOf alpha and change color
                    //shadowColor.unit();
//                    if(shadowColor.getAlpha() != 0)
//                        System.out.println(shadowColor.getAlpha() + " " + shadowColor);
//                    tmp.add(shadowColor.reduceOf(Math.abs(Math.cos(Math.toRadians(angle - 90))) / 1.1).reduceOf(0.55).reduceOf(1.0 - shadowColor.getAlpha()));
//                    if(shadowColor.getAlpha() != 0) {
//                        System.out.println(tmp);
//                        tmp.unit();
//                        System.out.println(tmp);
//                    }
                    tmp.add(lights.get(i).getColor().reduceOf(Math.abs(Math.cos(Math.toRadians(angle - 90))) / 1.1).reduceOf(0.55));
                }
            }

        }
        return tmp;
    }

    private static Color getPixelColor(Line3D ray, int reflectionDeepness) {
        List<Intersection> intersections = new ArrayList<>();
        getIntersections(ray, intersections);
        sortIntersections(intersections);
        Color res = new Color(0, 0, 0, 0);
        int size = intersections.size();
        for(int i = 0; i < size; ++i) {
            Color tmp = intersections.get(i).getColor();
            if(intersections.get(i).getReflectionRatio() != 0 && reflectionDeepness < REFLECTION_MAX) {
                Line3D reflectedRay = new Line3D(intersections.get(i).getPointOfIntersection(), Vector3D.reflectedRay(ray.getVector(), intersections.get(i).getNormal()));
                tmp = Color.colorReflection(tmp, getPixelColor(reflectedRay, reflectionDeepness + 1), intersections.get(i).getReflectionRatio());
            }
            tmp = applyLights(intersections.get(i), tmp);
            tmp = applyAmbientLight(intersections.get(i), tmp);
            res = Color.alphaBlending(res, tmp);
            if(res.getAlpha() > 1.0 - EPSILON) {
                break;
            }
        }
        return res;
    }

    private static void run() {
        File image = new File("Image.png");
        BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Color pixelColor = new Color(0, 0, 0);
                for (int h = 0; h < antiAliasing; ++h) {
                    for (int w = 0; w < antiAliasing; ++w) {
                        double heightRatio = ((y - (height / 2) + 0.5) / height) + ((1.0 / height / antiAliasing) * h);
                        double widthRatio = ((x - (width / 2) + 0.5) / width) + ((1.0 / width / antiAliasing) * w);
                        Line3D ray = new Line3D(cam.getPointOfVue(), cam.getPoint(heightRatio, widthRatio));
                        ray.normalize();
                        pixelColor.add(getPixelColor(ray, 0));
                    }
                }
                pixelColor.divide(antiAliasing * antiAliasing);
                Filter.applyFilter(pixelColor, filter);
                buffer.setRGB(x, y, pixelColor.toInt());
            }
            //System.out.print((double)y / height * 100 + "%\r");
        }
        try {
            ImageIO.write(buffer, "PNG", image);
            Random rand = new Random();
            File savedImage = new File("/Users/kissgautier/Desktop/RayTracerSavedPictures/" + "savedImage_" + rand.nextInt(Integer.MAX_VALUE) + ".png");
            ImageIO.write(buffer, "PNG", savedImage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        // setup();
        //System.out.println(ProcessHandle.current().pid());
        cam = SceneMaker.getSimpleCubeFractal(objects, lights);
        //Scanner scan = new Scanner(System.in);
        //scan.next();
        cam.update(height, width);
        long start = System.nanoTime();
        run();
        long end = System.nanoTime();
        System.out.println("Time taken: " + (double)((double)(end - start) / 1000000000D));
    }
}
