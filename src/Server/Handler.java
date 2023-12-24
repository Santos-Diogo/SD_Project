package Server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import sd23.*;
import Protocol.Exec.BadResponse;
import Protocol.Protocol;
import Protocol.Exec.Request;
import Protocol.Exec.Response;
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
    public StatusREP handleStatusRequest(StatusREQ request) 
    {
    // Não aceita nada por parametro pois o StatusREQ (para já pelo menos) não tem qualquer conteúdo
    private StatusREP handleStatusRequest() {
        long availableMemory = memoryManager.getAvailableMemory();
        int pendingTasks = taskQueue.size();
        return new StatusREP(availableMemory, pendingTasks);
    }

    private void handleExec (Request packet)
    public Response handleExec (Request packet)
    {
        this.taskQueue.add(new Task(packet.n_job, packet.arg));
        

        return null;    //!!!
        /* if (not fucked)
            return new GoodResponse ();
        else
            return new BadResponse(null); */
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
