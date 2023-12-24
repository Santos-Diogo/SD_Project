package Server;

import java.util.concurrent.BlockingQueue;
import sd23.*;
import Protocol.Exec.Request;
import Protocol.Status.StatusREP;
import Protocol.Status.StatusREQ;

public class Handler 
{

    private MemoryManager memoryManager;
    private BlockingQueue<Task> taskQueue;
    
    
    public Handler (MemoryManager memoryManager, BlockingQueue<Task> taskQueue)
    {
        this.memoryManager = memoryManager;
        this.taskQueue = taskQueue;
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
}
