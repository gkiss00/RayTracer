package rayTracer.io;

import rayTracer.utils.Color;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Worker implements Runnable{
    private final int id;
    private final int min;
    private final int max;
    BufferedImage bufferedImage;
    private final Socket socket;
    private BufferedReader bufferedReader;
    private PrintStream printStream;

    public Worker(Socket socket, int id, BufferedImage buffer, int min, int max) {
        this.id = id;
        this.bufferedImage = buffer;
        this.min = min;
        this.max = max;
        this.socket = socket;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void closeConnection(){
        try {
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        printStream.println("START " + min + " " + max);
        while(true){
            try {
                String msg = bufferedReader.readLine();
                System.out.println(msg);
                if(msg == null || msg.equals("DISCONNECT")) {
                    closeConnection();
                    break;
                } else {
                    String[] data = msg.split("\\s+");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    bufferedImage.setRGB(x, y, new Color(data[2]).toInt());
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
