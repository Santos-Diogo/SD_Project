package Server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import Protocol.Exec.Request;

public class Main 
{
    private MemoryManager memoryManager;  // Instância de MemoryManager para gerir a memória.
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    public static void main (String [] args)
    {
        
    }
}
