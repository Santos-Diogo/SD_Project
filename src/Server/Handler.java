package Server;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import sd23.*;
import Protocol.Exec.Request;
import Protocol.Status.StatusREP;
import Protocol.Status.StatusREQ;

public class Handler implements Runnable
{

    private MemoryManager memoryManager;
    private BlockingQueue<Task> taskQueue;
    private Socket clientSocket; 
    
    
    public Handler (MemoryManager memoryManager, BlockingQueue<Task> taskQueue, Socket clientSocket)
    {
        this.memoryManager = memoryManager;
        this.taskQueue = taskQueue;
        this.clientSocket = clientSocket;
    }


    // Método para tratar requisições de status. Retorna uma resposta com o estado atual do serviço.
    public StatusREP handleStatusRequest(StatusREQ request) {
        long availableMemory = memoryManager.getAvailableMemory();
        int pendingTasks = taskQueue.size();
        return new StatusREP(availableMemory, pendingTasks);
    }

    public void handleExec (Request packet)
    {
        try 
        {
            // execute task and get output
            byte[] output = JobFunction.execute(packet.arg);
        }
        catch (JobFunctionException e)
        {
            System.err.println("job failed: code="+e.getCode()+" message="+e.getMessage());
            // send error to client (TODO)
        }
    }

    public void run()
    {
        
    }
}
