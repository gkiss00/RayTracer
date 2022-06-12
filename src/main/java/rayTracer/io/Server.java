package rayTracer.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static int nbClient = 0;
    private static final int maxClient = 1;
    private static String localIpV4Address;
    private static ServerSocket serverSocket;
    private static final List<Thread> threadList = new ArrayList<>();
    private static final int HEIGHT = 900;
    private static final int WIDTH = 900;
    private static File image;
    private static BufferedImage buffer;
    private static final int sizePerClient = WIDTH / maxClient;

    private static void saveImage() {
        try {
            ImageIO.write(buffer, "PNG", image);
            /*Random rand = new Random();
            File savedImage = new File("/Users/kissgautier/Desktop/RayTracerSavedPictures/" + "savedImage_" + rand.nextInt(Integer.MAX_VALUE) + "" + rand.nextInt(Integer.MAX_VALUE) + ".png");
            ImageIO.write(buffer, "PNG", savedImage);*/
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //***********************************************************************
    //***********************************************************************
    //START JOB
    //***********************************************************************
    //***********************************************************************

    private static void endJob() {
        for (int i = 0; i < threadList.size(); ++i) {
            try {
                threadList.get(i).join();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static void startJob() {
        for (int i = 0; i < threadList.size(); ++i) {
            threadList.get(i).start();
        }
    }

    //***********************************************************************
    //***********************************************************************
    //START LISTENING FOR CONNECTION
    //***********************************************************************
    //***********************************************************************

    //START LISTENING FOR ANY CHANGE
    private static void startListening() throws Exception{
        System.out.println("Waiting for connexion on: " + localIpV4Address);
        while(nbClient != maxClient){
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client " + nbClient + ": accepted");
            int clientMin = nbClient * sizePerClient;
            int clientMax = clientMin + sizePerClient;
            if(nbClient == clientMax - 1)
                clientMax = WIDTH;
            Worker worker = new Worker(clientSocket, nbClient, buffer, clientMin, clientMax);
            Thread thread = new Thread(worker);
            threadList.add(thread);
            ++nbClient;
        }
        serverSocket.close();
    }

    //***********************************************************************
    //***********************************************************************
    //MAIN
    //***********************************************************************
    //***********************************************************************

    public static void main(String[] args) {
        try {
            image = new File("Image.png");
            buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            localIpV4Address = Inet4Address.getLocalHost().getHostAddress();
            InetAddress serverAddress = InetAddress.getByName(localIpV4Address);
            serverSocket = new ServerSocket(5000, 50, serverAddress);
            startListening();
            long start = System.nanoTime();
            startJob();
            endJob();
            long end = System.nanoTime();
            System.out.println("Time taken: " + ((double)(end - start) / 1000000000D));
            saveImage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
