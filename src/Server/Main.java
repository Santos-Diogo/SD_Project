package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import Protocol.Exec.Request;

public class Main 
{

    private MemoryManager memoryManager;  // Instância de MemoryManager para gerir a memória.
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

}
