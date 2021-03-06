package rayTracer;

import com.fasterxml.jackson.databind.ObjectMapper;
import rayTracer.enums.CapacityTypeEnum;
import rayTracer.enums.FilterTypeEnum;
import rayTracer.lights.Light;
import rayTracer.math.Line3D;
import rayTracer.math.Point3D;
import rayTracer.math.Vector3D;
import rayTracer.objects.*;
import rayTracer.utils.*;
import rayTracer.visual.Camera;
import server.model.Config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RayTracer {
    public static void run(Config config, List<BaseObject> baseObjects, List<Camera> cameras, List<Light> newLights) {
        //SET CONFIG
        /*height = config.getHeight();
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

         */
    }

    private static Camera cam;
    private static List<BaseObject> objects = new ArrayList<>();
    private static List<Light> lights = new ArrayList<>();
    private static List<Thread> threads = new ArrayList<>();

    private static void runViaThread(rayTracer.config.Config config) {
        File image = new File("Image.png");
        BufferedImage buffer = new BufferedImage(config.width, config.height, BufferedImage.TYPE_INT_RGB);

        int nbThread = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < nbThread; ++i) {
            Calculator calculator = new Calculator(config, buffer, nbThread);
            Thread thread = new Thread(calculator);
            threads.add(thread);
            thread.start();
        }

        for (int i = 0; i < nbThread; ++i) {
            try {
                threads.get(i).join();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        try {
            ImageIO.write(buffer, "PNG", image);
            /*Random rand = new Random();
            File savedImage = new File("/Users/kissgautier/Desktop/RayTracerSavedPictures/" + "savedImage_" + rand.nextInt(Integer.MAX_VALUE) + "" + rand.nextInt(Integer.MAX_VALUE) + ".png");
            ImageIO.write(buffer, "PNG", savedImage);*/
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        System.out.println(width);
        rayTracer.config.Config config = new rayTracer.config.Config();

        cam = SceneMaker.getSimpleMobiusTapePolygon(objects, lights);
        cam.update(config.height, config.width);

        config.objects = objects;
        config.lights = lights;
        config.cam = cam;
        long start = System.nanoTime();
        runViaThread(config);
        //run();
        long end = System.nanoTime();
        System.out.println("Time taken: " + (double)((double)(end - start) / 1000000000D));
    }
}
