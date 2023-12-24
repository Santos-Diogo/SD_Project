package Server;

import java.util.concurrent.BlockingQueue;
import sd23.*;
import Protocol.Exec.BadResponse;
import Protocol.Exec.Request;
import Protocol.Exec.Response;
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
    public StatusREP handleStatusRequest(StatusREQ request) 
    {
        long availableMemory = memoryManager.getAvailableMemory();
        int pendingTasks = taskQueue.size();
        return new StatusREP(availableMemory, pendingTasks);
    }

    public Response handleExec (Request packet)
    {
        this.taskQueue.add(new Task(packet.n_job, packet.arg));
        

        return null;    //!!!
        /* if (not fucked)
            return new GoodResponse ();
        else
            return new BadResponse(null); */
    }
}
