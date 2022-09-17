package rayTracer.utils;

import rayTracer.config.Config;
import rayTracer.enums.CapacityTypeEnum;
import rayTracer.enums.ObjectTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.Obj;
import rayTracer.objects.baseObjects.BaseObject;
import rayTracer.objects.blackObjects.BlackObject;

import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Calculator implements Runnable{
    private final int totalThread;
    private final Config config;
    private final BufferedImage buffer;
    private final int min;
    private final int max;
    private final boolean shouldUpdateBuffer;
    private final boolean shouldUpdateServer;
    private final Semaphore mutex;
    private final PrintStream printStream;

    // FOR NORMAL RAYTRACING
    public Calculator(Config config, BufferedImage buffer, int totalThread, int min, int max) {
        this.config = config;
        this.buffer = buffer;
        this.totalThread = totalThread;
        this.min = min;
        this.max = max;
        this.shouldUpdateBuffer = true;
        this.shouldUpdateServer = false;
        this.mutex = null;
        this.printStream = null;
    }

    // FOR CLIENT
    public Calculator(Config config, int min, int max, int totalThread, Semaphore mutex, PrintStream printStream) {
        this.config = config;
        this.buffer = null;
        this.totalThread = totalThread;
        this.min = min;
        this.max = max;
        this.shouldUpdateBuffer = false;
        this.shouldUpdateServer = true;
        this.mutex = mutex;
        this.printStream = printStream;
    }

    private void senColorToServer(int x, int y, Color color) {
        try {
            mutex.acquire();
            printStream.println(x + " " + y + " " + color.toHex());
            mutex.release();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void run() {
        for (int y = 0; y < config.height; ++y) {
            for (int x = min; x < max; x += totalThread) {
                Color pixelColor = new Color(0, 0, 0);
                addPixelColor(x, y, pixelColor);
                pixelColor.divide(config.ANTI_ALIASING * config.ANTI_ALIASING);
                Filter.applyFilter(pixelColor, config.filter);
                if(shouldUpdateBuffer)
                    buffer.setRGB(x, y, pixelColor.toInt());
                if(shouldUpdateServer)
                    senColorToServer(x, y, pixelColor);
            }
        }
    }

    public void addPixelColor(int x, int y, Color pixelColor) {
        for (int h = 0; h < config.ANTI_ALIASING; ++h) {
            for (int w = 0; w < config.ANTI_ALIASING; ++w) {
                double heightRatio = ((y - ((double)config.height / 2) + 0.5) / config.height) + ((1.0 / config.height / config.ANTI_ALIASING) * h);
                double widthRatio = ((x - ((double)config.width / 2) + 0.5) / config.width) + ((1.0 / config.width / config.ANTI_ALIASING) * w);
                Line3D ray = new Line3D(config.cam.getPointOfVue(), config.cam.getPoint(heightRatio, widthRatio));
                ray.normalize();
                pixelColor.add(getPixelColor(ray, 0));
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                LIGHTS                 *

     * * * * * * * * * * * * * * * * * * * * */

    private Color applyAmbientLight(Intersection intersection, Color color) {
        Color tmp = new Color(color);
        Line3D ray = new Line3D(config.cam.getPointOfVue(), intersection.getPointOfIntersection());
        double angle = Vector3D.angleBetween(ray.getVector(), intersection.getNormal());
        if (angle >= 135) {
            // 1.0 -> 0 of ambient 180 135
            double rest = (angle - 135) * 2;
            tmp.add(config.ambientLight.reduceOf(Math.cos(Math.toRadians(rest))).reduceOf(0.75));
        } else if(angle >= 90) {
            // 0 -> 1 of shadow 135 -> 90
            double rest = (angle - 90) * 2;
            tmp = tmp.reduceOf(1.0 - Math.sin(Math.toRadians(rest)));
        }
        return tmp;
    }

    private Color applyLights(Intersection intersection, Color color) {
        Color tmp = new Color(color);
        for (int i = 0; i < config.lights.size(); ++i) {

            // COMPUTE THE LIGHT INTERSECTION RAY
            Line3D ray = new Line3D(config.lights.get(i).getPoint(), intersection.getPointOfIntersection());
            ray.normalize();
            double distanceFromLightToIntersection = Point3D.distanceBetween(intersection.getPointOfIntersection(), ray.getPoint());

            // GET THE NEW INTERSECTIONS
            List<Intersection> intersections = new ArrayList<>();
            IntersectionManager.getIntersections(ray, config.objects, intersections);
            List<Intersection> blackIntersections = new ArrayList<>();
            IntersectionManager.getIntersections(ray, config.backObjects, blackIntersections);
            IntersectionManager.preProcessIntersections(intersections, blackIntersections);

            // IF LIGHT MET WITH INTERSECTION => BRIGHT FALSE
            double alpha = 0.0;
            for(int j = 0; j < intersections.size(); ++j) {
                Intersection currentIntersection = intersections.get(j);
                double distanceFromLightToNewIntersection = Point3D.distanceBetween(currentIntersection.getPointOfIntersection(), ray.getPoint());
                if(distanceFromLightToNewIntersection < distanceFromLightToIntersection - config.EPSILON) {
                    // OBJECT INTERSECTED IS FULL
                    if(currentIntersection.getColor().getAlpha() != 1 && currentIntersection.getObject().getCapacity() == CapacityTypeEnum.FULL) {
                        Intersection nextIntersectionWithSameObject = findNextIntersectionOfSameObject(currentIntersection, intersections, i + 1);
                        if (nextIntersectionWithSameObject != null) {
                            double dist = Point3D.distanceBetween(currentIntersection.getPointOfIntersection(), intersections.get(j + 1).getPointOfIntersection());
                            double ratio = Math.min(1, dist / config.MAX_DIST);
                            double colorAlpha = currentIntersection.getColor().getAlpha();
                            double newAlpha = (1 - colorAlpha) * ratio + colorAlpha;
                            alpha = alpha + newAlpha;
                        }else {
                            Intersection previousIntersection = intersections.get(i - 1);
                            if (currentIntersection.getObject() != previousIntersection.getObject()) {
                                double dist = Point3D.distanceBetween(currentIntersection.getPointOfIntersection(), previousIntersection.getPointOfIntersection());
                                double ratio = Math.min(1, dist / config.MAX_DIST);
                                double colorAlpha = currentIntersection.getColor().getAlpha();
                                double newAlpha = (1 - colorAlpha) * ratio + colorAlpha;
                                alpha = alpha + newAlpha;
                            }
                        }
                    } else {
                        // OBJECT INTERSECTED IS EMPTY
                        alpha = alpha + (currentIntersection.getColor().getAlpha() * (1.0 - alpha));
                    }
                }
            }

            // IF BRIGHT
            if(alpha == 0) {
                double angle = Vector3D.angleBetween(ray.getVector(), intersection.getNormal());
                if (angle >= 90) {
                    tmp.add(config.lights.get(i).getColor().reduceOf(Math.abs(Math.cos(Math.toRadians(angle - 90))) / 1.1).reduceOf(0.55));
                }
            } else {
                alpha = Math.max(alpha, 1.0);
                tmp = tmp.reduceOf(config.SHADOW_DEEPNESS * alpha);
            }

        }
        return tmp;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                COLOR                  *

     * * * * * * * * * * * * * * * * * * * * */

    private Color getPixelColor(Line3D ray, int reflectionDeepness) {
        // FIND INTERSECTIONS
        List<Intersection> intersections = new ArrayList<>();
        IntersectionManager.getIntersections(ray, config.objects, intersections);
        List<Intersection> blackIntersections = new ArrayList<>();
        IntersectionManager.getIntersections(ray, config.backObjects, blackIntersections);
        IntersectionManager.preProcessIntersections(intersections, blackIntersections);

        // COMPUTE COLOR
        Color res = new Color(0, 0, 0, 0);
        int size = intersections.size();
        for(int i = 0; i < size; ++i) {
            Intersection intersection = intersections.get(i);
            Color tmp = intersection.getColor();
            // REFLECTION
            if(intersection.getReflectionRatio() != 0 && reflectionDeepness < config.REFLECTION_MAX) {
                Line3D reflectedRay = new Line3D(intersection.getPointOfIntersection(), Vector3D.reflectedRay(ray.getVector(), intersection.getNormal()));
                tmp = Color.colorReflection(tmp, getPixelColor(reflectedRay, reflectionDeepness + 1), intersection.getReflectionRatio());
            } else if (intersection.getObject().getCapacity() == CapacityTypeEnum.FULL && intersection.getObject().getDensity() != 1) {
                Vector3D refracted1 = Vector3D.refractedRay(ray.getVector(), intersection.getNormal(), 1, intersection.getObject().getDensity());
                Line3D newRay1 = new Line3D(intersection.getPointOfIntersection(), refracted1);
                List<Intersection> xxx = new ArrayList<>();
                IntersectionManager.getIntersections(newRay1, config.objects, xxx);
                Vector3D refracted2 = Vector3D.refractedRay(newRay1.getVector(), xxx.get(0).getNormal(), intersection.getObject().getDensity(), 1);
                Line3D newRay2 = new Line3D(xxx.get(0).getPointOfIntersection(), refracted2);
                tmp =getPixelColor(newRay2, reflectionDeepness + 1);
            }
            // TRANSPARENCY FULL OBJECT
            if(intersection.getColor().getAlpha() != 1 && intersection.getObject().getCapacity() == CapacityTypeEnum.FULL && intersection.getObject().getDensity() == 1) {
                Intersection nextIntersectionWithSameObject = findNextIntersectionOfSameObject(intersection, intersections, i + 1);
                if (nextIntersectionWithSameObject != null) {
                    double dist = Point3D.distanceBetween(intersection.getPointOfIntersection(), intersections.get(i + 1).getPointOfIntersection());
                    double ratio = Math.min(1, dist / config.MAX_DIST);
                    double alpha = (1 - intersection.getColor().getAlpha()) * ratio + intersection.getColor().getAlpha();
                    tmp = new Color(
                            intersection.getColor().getRed(),
                            intersection.getColor().getGreen(),
                            intersection.getColor().getBlue(),
                            alpha
                    );
                } else {
                    Intersection previousIntersection = intersections.get(i - 1);
                    if (intersection.getObject() != previousIntersection.getObject()) {
                        double dist = Point3D.distanceBetween(intersection.getPointOfIntersection(), previousIntersection.getPointOfIntersection());
                        double ratio = Math.min(1, dist / config.MAX_DIST);
                        double alpha = (1 - intersection.getColor().getAlpha()) * ratio + intersection.getColor().getAlpha();
                        tmp = new Color(
                                intersection.getColor().getRed(),
                                intersection.getColor().getGreen(),
                                intersection.getColor().getBlue(),
                                alpha
                        );
                    }
                }
            }
            // LIGHTS
            tmp = applyLights(intersection, tmp);
            tmp = applyAmbientLight(intersection, tmp); //TODO fix this shit with reflection
            if(res.getAlpha() == 0)
                res = new Color(tmp);
            else
                res = Color.alphaBlending(res, tmp);
            if(res.getAlpha() > 1.0 - config.EPSILON) {
                break;
            }
        }
        return res;
    }

    /* * * * * * * * * * * * * * * * * * * * *

     *                UTILS                  *

     * * * * * * * * * * * * * * * * * * * * */

    private Intersection findNextIntersectionOfSameObject(Intersection intersection, List<Intersection> intersections, int i) {
        for (;i < intersections.size(); ++i) {
            if (intersection.getObject().equals(intersections.get(i).getObject()))
                return intersections.get(i);
        }
        return null;
    }
}
