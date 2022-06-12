package rayTracer.io;

import rayTracer.lights.Light;
import rayTracer.objects.BaseObject;
import rayTracer.utils.SceneMaker;
import rayTracer.visual.Camera;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static Camera cam;
    private static List<BaseObject> objects = new ArrayList<>();
    private static List<Light> lights = new ArrayList<>();
    private static List<Thread> threads = new ArrayList<>();

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

        }
    }

    private static void start(rayTracer.config.Config config){
        for (int h = 0; h < config.height; ++h) {
            for (int w = min; w < max; ++ w) {
                // calculate pixel color
                // send it to the server
                printStream.println(w + " " + h + " COLOR"); // <X> <Y> <PIXEL_COLOR>
            }
        }
        printStream.println("DISCONNECT");
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
            socket = new Socket("localhost", 5000);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            waitForStart();
            start(config);
        }catch(Exception e){
            System.out.println("Exception : " + e.getMessage());
        }
    }
}
