package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import Protocol.Exec.Request;
import Shared.Defines;

public class Main 
{
    private static State server_state;
    private static MemoryManager memoryManager;  // Instância de MemoryManager para gerir a memória.
    private static BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private static ServerSocket serverSocket;


    public static void main (String[] args)
    {
        try {
            serverSocket = new ServerSocket(Defines.serverport);
            server_state= new State();

            while(true)
            {
                Socket socket = serverSocket.accept();
                new Thread(new Handler(server_state, socket)).start();
            }   
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
