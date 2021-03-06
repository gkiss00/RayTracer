package rayTracer.utils;

import rayTracer.config.Config;
import rayTracer.enums.CapacityTypeEnum;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Calculator implements Runnable{
    private static int _id = 0;
    private final int id;
    private final int totalThread;
    private final Config config;
    private final BufferedImage buffer;

    public Calculator(Config config, BufferedImage buffer, int totalThread) {
        this.config = config;
        this.buffer = buffer;
        this.totalThread = totalThread;
        this.id = _id++;
    }
    @Override
    public void run() {
        for (int y = id; y < config.height; y += totalThread) {
            for (int x = 0; x < config.width; ++x) {
                Color pixelColor = new Color(0, 0, 0);
                for (int h = 0; h < config.ANTI_ALIASING; ++h) {
                    for (int w = 0; w < config.ANTI_ALIASING; ++w) {
                        double heightRatio = ((y - (config.height / 2) + 0.5) / config.height) + ((1.0 / config.height / config.ANTI_ALIASING) * h);
                        double widthRatio = ((x - (config.width / 2) + 0.5) / config.width) + ((1.0 / config.width / config.ANTI_ALIASING) * w);
                        Line3D ray = new Line3D(config.cam.getPointOfVue(), config.cam.getPoint(heightRatio, widthRatio));
                        ray.normalize();
                        pixelColor.add(getPixelColor(ray, 0));
                    }
                }
                pixelColor.divide(config.ANTI_ALIASING * config.ANTI_ALIASING);
                Filter.applyFilter(pixelColor, config.filter);
                buffer.setRGB(x, y, pixelColor.toInt());
            }
        }
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

    private void getIntersections(Line3D ray, List<Intersection> intersections) {
        for (int i = 0; i < config.objects.size(); ++i) {
            try {
                config.objects.get(i).hit(ray, intersections);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Color applyAmbientLight(Intersection intersection, Color color) {
        Color tmp = new Color(color);
        Line3D ray = new Line3D(config.cam.getPointOfVue(), intersection.getPointOfIntersection());
        double angle = Vector3D.angleBetween(ray.getVector(), intersection.getNormal());
        if (angle >= 90) {
            double intensity = 1.0 - (angle - 90) / 90;
            tmp.add(config.ambientLight.reduceOf(Math.abs(Math.cos(Math.toRadians(angle - 90))) / 1.1).reduceOf(0.75));
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
            getIntersections(ray, intersections);
            sortIntersections(intersections);

            // IF LIGHT MET WITH INTERSECTION => BRIGHT FALSE
            double alpha = 0.0;
            for(int j = 0; j < intersections.size(); ++j) {
                Intersection currentIntersection = intersections.get(j);
                double distanceFromLightToNewIntersection = Point3D.distanceBetween(currentIntersection.getPointOfIntersection(), ray.getPoint());
                if(distanceFromLightToNewIntersection < distanceFromLightToIntersection - config.EPSILON) {
                    // OBJECT INTERSECTED IS FULL
                    if(currentIntersection.getColor().getAlpha() != 1 && currentIntersection.getObject().getCapacity() == CapacityTypeEnum.FULL) {
                        double dist = Point3D.distanceBetween(currentIntersection.getPointOfIntersection(), intersections.get(j + 1).getPointOfIntersection());
                        double ratio = Math.min(1, dist / config.MAX_DIST);
                        double colorAlpha = currentIntersection.getColor().getAlpha();
                        double newAlpha = (1 - colorAlpha) * ratio + colorAlpha;
                        alpha = alpha + newAlpha;
                        ++i;
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

    private Color getPixelColor(Line3D ray, int reflectionDeepness) {
        // FIND INTERSECTIONS
        List<Intersection> intersections = new ArrayList<>();
        getIntersections(ray, intersections);
        sortIntersections(intersections);

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
            }
            // TRANSPARENCY FULL OBJECT
            if(intersection.getColor().getAlpha() != 1 && intersection.getObject().getCapacity() == CapacityTypeEnum.FULL) {
                double dist = Point3D.distanceBetween(intersection.getPointOfIntersection(), intersections.get(i + 1).getPointOfIntersection());
                double ratio = Math.min(1, dist / config.MAX_DIST);
                double alpha = (1 - intersection.getColor().getAlpha()) * ratio + intersection.getColor().getAlpha();
                tmp = new Color(
                        intersection.getColor().getRed(),
                        intersection.getColor().getGreen(),
                        intersection.getColor().getBlue(),
                        alpha
                );
                ++i;
            }
            // LIGHTS
            tmp = applyLights(intersection, tmp);
            tmp = applyAmbientLight(intersection, tmp);
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
}
