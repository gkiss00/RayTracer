package rayTracer;

import com.fasterxml.jackson.databind.ObjectMapper;
import rayTracer.enums.FilterTypeEnum;
import rayTracer.lights.Light;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.*;
import rayTracer.utils.Color;
import rayTracer.utils.Filter;
import rayTracer.utils.Intersection;
import rayTracer.utils.SceneMaker;
import rayTracer.visual.Camera;
import server.model.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RayTracer {
    private static final double EPSILON = 0.00001;
    private static int REFLECTION_MAX = 3;
    private static FilterTypeEnum filter = FilterTypeEnum.NONE;
    private static final Color ambientLight = new Color(1, 1, 1, 1);
    private static int height = 900;
    private static int width = 900;
    private static int ANTI_ALIASING = 1;

    private static Camera cam;
    private static List<BaseObject> objects = new ArrayList<>();
    private static List<Light> lights = new ArrayList<>();

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
//            Color shadowColor = new Color(rayTracer.lights.get(i).getColor());
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
                for (int h = 0; h < ANTI_ALIASING; ++h) {
                    for (int w = 0; w < ANTI_ALIASING; ++w) {
                        double heightRatio = ((y - (height / 2) + 0.5) / height) + ((1.0 / height / ANTI_ALIASING) * h);
                        double widthRatio = ((x - (width / 2) + 0.5) / width) + ((1.0 / width / ANTI_ALIASING) * w);
                        Line3D ray = new Line3D(cam.getPointOfVue(), cam.getPoint(heightRatio, widthRatio));
                        ray.normalize();
                        pixelColor.add(getPixelColor(ray, 0));
                    }
                }
                pixelColor.divide(ANTI_ALIASING * ANTI_ALIASING);
                Filter.applyFilter(pixelColor, filter);
                buffer.setRGB(x, y, pixelColor.toInt());
            }
            //System.out.print((double)y / height * 100 + "%\r");
        }
        try {
            ImageIO.write(buffer, "PNG", image);
//            Random rand = new Random();
//            File savedImage = new File("/Users/kissgautier/Desktop/RayTracerSavedPictures/" + "savedImage_" + rand.nextInt(Integer.MAX_VALUE) + " " + rand.nextInt(Integer.MAX_VALUE) + ".png");
//            ImageIO.write(buffer, "PNG", savedImage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void run(Config config, List<BaseObject> baseObjects, List<Camera> cameras, List<Light> newLights) {
        //SET CONFIG
        height = config.getHeight();
        width = config.getWidth();
        ANTI_ALIASING = config.getAntiAliasing();
        filter = config.getFinalFilter();
        REFLECTION_MAX = config.getMaxReflexion();

        // SET CAMERA
        cam = cameras.get(0);
        cam.update(height, width);

        // SET OBJECTS
        objects.clear();
        objects.addAll(baseObjects);

        // SET LIGHTS
        lights.clear();
        lights.addAll(newLights);

        try {

        } catch (Exception e) {

        }
        long start = System.nanoTime();
        run();
        long end = System.nanoTime();
        System.out.println("Time taken: " + (double)((double)(end - start) / 1000000000D));
    }

    public static void main(String[] args) {
        cam = SceneMaker.getSimplePlane(objects, lights);
        cam.update(height, width);
        long start = System.nanoTime();
        run();
        long end = System.nanoTime();
        System.out.println("Time taken: " + (double)((double)(end - start) / 1000000000D));
    }
}
