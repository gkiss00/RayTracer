package rayTracer.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Worker implements Runnable{
    private static boolean go = false;
    private final int id;
    private final int min = 0;
    private final int max = 100;
    private final Socket socket;
    private BufferedReader bufferedReader;
    private PrintStream printStream;

    public Worker(Socket socket, int id) {
        this.id = id;
        this.socket = socket;
        try{
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void start() {
        go = true;
    }

    private void closeConnection(){
        try {
            socket.close();
        } catch (Exception e) {

        }
    }

    private void waitForStart() {
        while(!go) {

        }
        printStream.println("START " + min + " " + max);
    }

    @Override
    public void run() {
        waitForStart();
        while(true){
            try {
                String msg = bufferedReader.readLine();
                System.out.println(msg);
                if(msg == null || msg.equals("DISCONNECT")) {
                    closeConnection();
                    break;
                }
            } catch (Exception exception) {

            }
        }
    }
}
