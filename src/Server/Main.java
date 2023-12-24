package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Protocol.Exec.Request;

public class Main 
{

    private static MemoryManager memoryManager;  // Instância de MemoryManager para gerir a memória.
    private static BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private static ServerSocket serversocket;


    public static void main (String[] args)
    {
        try {
            serversocket = new ServerSocket(Shared.Defines.serverport);

            while(true)
            {
                Socket socket = serversocket.accept();
                new Thread(new Handler(memoryManager, taskQueue, socket)).start();
            }
        } catch (IOException e) {
            System.err.println("nem a puta da socket consegues abrir oh filho");
        }
    }

}
