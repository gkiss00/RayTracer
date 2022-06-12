package rayTracer.io;

import rayTracer.lights.Light;
import rayTracer.objects.BaseObject;
import rayTracer.utils.Calculator;
import rayTracer.utils.SceneMaker;
import rayTracer.visual.Camera;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Client {
    private static Camera cam;
    private static List<BaseObject> objects = new ArrayList<>();
    private static List<Light> lights = new ArrayList<>();
    private static List<Thread> threads = new ArrayList<>();
    private Semaphore mutex = new Semaphore(1);


    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static PrintStream printStream;
    private static int min;
    private static int max;

    //***********************************************************************
    //***********************************************************************
    //UTILS
    //***********************************************************************
    //***********************************************************************
    private static void closeConnection() {
        try {
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void start(rayTracer.config.Config config){
        for (int h = 0; h < config.height; ++h) {
            for (int w = min; w < max; ++ w) {
                // calculate pixel color
                // send it to the server
                printStream.println(w + " " + h + " #ff0000"); // <X> <Y> <PIXEL_COLOR>
            }
        }
        printStream.println("DISCONNECT");
        closeConnection();
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

    private static void startThreads(int nbThread, rayTracer.config.Config config) {
        for (int i = 0; i < nbThread; ++i) {
            Calculator calculator = new Calculator(config, null, nbThread, min + i, max, false);
            Thread thread = new Thread(calculator);
            threads.add(thread);
            thread.start();
        }
    }

    private static void startMultiThread(rayTracer.config.Config config){
        int nbThread = Runtime.getRuntime().availableProcessors();
        startThreads(nbThread, config);
        waitThreadsToEnd(nbThread);
        for (int h = 0; h < config.height; ++h) {
            for (int w = min; w < max; ++ w) {
                // calculate pixel color
                // send it to the server
                printStream.println(w + " " + h + " #ff0000"); // <X> <Y> <PIXEL_COLOR>
            }
        }
        printStream.println("DISCONNECT");
        closeConnection();
    }

    //***********************************************************************
    //***********************************************************************
    //CREATE CLIENT
    //***********************************************************************
    //***********************************************************************

    private static void waitForStart() throws Exception{
        String msg = bufferedReader.readLine(); // START <MIN> <MAX>
        //if server is done exit
        if (msg == null){
            System.out.println("Server closed");
            socket.close();
            System.exit(1);
        }
        String[] args = msg.split("\\s+");
        if (args.length != 3) {
            System.out.println("Bad message from the server");
        }
        min = Integer.parseInt(args[1]);
        max = Integer.parseInt(args[2]);
    }

    //***********************************************************************
    //***********************************************************************
    //START
    //***********************************************************************
    //***********************************************************************

    public static void main(String[] args){
        // Raytracer part
        rayTracer.config.Config config = new rayTracer.config.Config();
        cam = SceneMaker.getProjectedShadow(objects, lights);
        cam.update(config.height, config.width);
        config.objects = objects;
        config.lights = lights;
        config.cam = cam;

        // Server part
        try{
            //Connect to the server
            socket = new Socket("192.168.1.61", 5000);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            waitForStart();
            start(config);
        }catch(Exception e){
            System.out.println("Exception : " + e.getMessage());
        }
    }
}
