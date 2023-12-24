package Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import sd23.*;
import Protocol.Protocol;
import Protocol.Exec.Request;
import Protocol.Status.StatusREP;
import Protocol.Status.StatusREQ;

public class Handler implements Runnable
{

    private MemoryManager memoryManager;
    private BlockingQueue<Task> taskQueue;
    private Socket clientSocket;
    private DataInputStream in;
    
    
    public Handler (MemoryManager memoryManager, BlockingQueue<Task> taskQueue, Socket clientSocket)
    {
        this.memoryManager = memoryManager;
        this.taskQueue = taskQueue;
        this.clientSocket = clientSocket;
    }


    // Método para tratar requisições de status. Retorna uma resposta com o estado atual do serviço.
    // Não aceita nada por parametro pois o StatusREQ (para já pelo menos) não tem qualquer conteúdo
    private StatusREP handleStatusRequest() {
        long availableMemory = memoryManager.getAvailableMemory();
        int pendingTasks = taskQueue.size();
        return new StatusREP(availableMemory, pendingTasks);
    }

    private void handleExec (Request packet)
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


    private void handle (Protocol packet)
    {
        try {
            switch (packet.type) {
                case EXEC_RQ:
                    handleExec(Request.deserialize(this.in));
                    break;
                case STATUS_RQ:
                    handleStatusRequest();
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.err.println("Error deserializing packet");
        }
    }


    public void run()
    {
        try {
            in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            while(true)
            {
                handle(Protocol.deserialize(in));
            }
        } catch (IOException e) {
            System.err.println("fds");
        }
    }
}
