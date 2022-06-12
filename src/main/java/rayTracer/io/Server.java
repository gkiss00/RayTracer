package rayTracer.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int maxClient = 1;
    private static int nbClient = 0;
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    //***********************************************************************
    //***********************************************************************
    //UTILS
    //***********************************************************************
    //***********************************************************************

    //RETURN A SERVER SOCKET CHANNEL CONFIGURED NON BLOCKING
    private static ServerSocketChannel createServerSocketChannel(Selector selector, int port){
        try{
            //NEW SERVER SOCKET
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();

            //CONFIGURE NON BLOCKING
            serverSocketChannel.configureBlocking(false);
            //OPEN ON PORT <SELECTED>
            serverSocket.bind(new InetSocketAddress(port));
            //REGISTER TO THE SELECTOR
            serverSocketChannel.register(selector, serverSocketChannel.validOps()); //SelectionKey.OP_ACCEPT
            return (serverSocketChannel);
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return (null);
    }

    //***********************************************************************
    //***********************************************************************
    //START LISTENING FOR CONNECTION
    //***********************************************************************
    //***********************************************************************

    //START LISTENING FOR ANY CHANGE
    private static void startListening(Selector selector) throws Exception{
        System.out.println("Waiting for connexion");
        while(true){
            selector.select();
            //if someone try to connect
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            //foreach connexion
            while(it.hasNext()){
                //remove the key from the list
                SelectionKey sk = it.next();
                it.remove();
                //get the serverSocket
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel)sk.channel();
                //Accept the entry connexion
                Socket sock = serverSocketChannel.socket().accept();
                //START A WORKER WITH THE SOCKET...
                ++nbClient;
                Worker worker = new Worker(sock, nbClient);
                System.out.println("Client " + nbClient + ": accepted");
                executorService.submit(worker);
                if(nbClient == maxClient)
                    break;
            }
            if(nbClient == maxClient)
                break;
        }
        Worker.start();
    }

    //***********************************************************************
    //***********************************************************************
    //MAIN
    //***********************************************************************
    //***********************************************************************

    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            //CREATE A SERVER SOCKET FOR THE CLIENTS
            ServerSocketChannel serverSocketChannel = createServerSocketChannel(selector, 5000);
            startListening(selector);
        } catch (Exception e) {

        }
    }
}
