package rayTracer;

import rayTracer.objects.Obj;
import rayTracer.objects.blackObjects.BlackObject;
import rayTracer.lights.Light;
import rayTracer.objects.baseObjects.*;
import rayTracer.utils.*;
import rayTracer.visual.Camera;
import server.model.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RayTracer {

    private static Camera cam;
    private final static List<Obj> objects = new ArrayList<>();
    private final static List<Obj> blackObjects = new ArrayList<>();
    private final static List<Light> lights = new ArrayList<>();
    private final static List<Thread> threads = new ArrayList<>();

    //***********************************************************************
    //***********************************************************************
    // BACKEND SERVER
    //***********************************************************************
    //***********************************************************************
    public static void runFromSever(Config config, List<BaseObject> baseObjects, List<Camera> cameras, List<Light> newLights) {

    }

    //***********************************************************************
    //***********************************************************************
    // NORMAL
    //***********************************************************************
    //***********************************************************************

    private static void saveImage(BufferedImage buffer) {
        File image = new File("Image.png");
        try {
            ImageIO.write(buffer, "PNG", image);
            /*Random rand = new Random();
            File savedImage = new File("/Users/kissgautier/Desktop/RayTracerSavedPictures/" + "savedImage_" + rand.nextInt(Integer.MAX_VALUE) + "" + rand.nextInt(Integer.MAX_VALUE) + ".png");
            ImageIO.write(buffer, "PNG", savedImage);*/
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void waitThreadsToEnd(int nbThread) {
        for (int i = 0; i < nbThread; ++i) {
            try {
                threads.get(i).join();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void startThreads(int nbThread, rayTracer.config.Config config, BufferedImage buffer) {
        for (int i = 0; i < nbThread; ++i) {
            Calculator calculator = new Calculator(config, buffer, nbThread, i, config.width);
            Thread thread = new Thread(calculator);
            threads.add(thread);
            thread.start();
        }
    }

    private static void runViaThread(rayTracer.config.Config config) {
        BufferedImage buffer = new BufferedImage(config.width, config.height, BufferedImage.TYPE_INT_RGB);

        int nbThread = Runtime.getRuntime().availableProcessors();
        startThreads(nbThread, config, buffer);
        waitThreadsToEnd(nbThread);

        saveImage(buffer);
    }

    public static void main(String[] args) {
        rayTracer.config.Config config = new rayTracer.config.Config();

        cam = SceneMaker.getSimpleBlackSphereOnCube(objects, lights, blackObjects);
        cam.update(config.height, config.width);

        config.objects = objects;
        config.backObjects = blackObjects;
        config.lights = lights;
        config.cam = cam;
        long start = System.nanoTime();
        runViaThread(config);
        long end = System.nanoTime();
        System.out.println("Time taken: " + ((double)(end - start) / 1000000000D));
    }
}
