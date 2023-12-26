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
import Server.Task.Task;
import Server.Task.TaskMaker;

public class Handler implements Runnable
{
    private State server_state;
    private TaskMaker task_maker;
    private BlockingQueue<Response> task_result;
    private Socket clientSocket;
    private DataInputStream in;
    
    
    public Handler (State server_state, Socket clientSocket)
    {
        // set the server state, get a taskMaker and get the task result queue
        this.server_state= server_state;
        this.task_maker= new TaskMaker();
        this.task_result= this.server_state.registerSubmitter(this.task_maker.submitter);
        
        this.clientSocket= clientSocket;
    }


    // Método para tratar requisições de status. Retorna uma resposta com o estado atual do serviço.
    // Não aceita nada por parametro pois o StatusREQ (para já pelo menos) não tem qualquer conteúdo
    private StatusREP handleStatusRequest() 
    {
        long availableMemory = this.server_state.getAvailableMemory();
        int pendingTasks = this.server_state.taskQueue.size();
        return new StatusREP(availableMemory, pendingTasks);
    }

    private Response handleExec (Request packet)
    {
        // send a task request
        this.server_state.taskQueue.add(task_maker.newTask(packet.arg));
        
        // get a task result
        try
        {
            return this.task_result.take();
        }
        catch (InterruptedException e)
        {
            System.out.println("Interrupted in handleExec return");
            return null;
        }
    }


    private void handle (Protocol packet)
    {
        try 
        {
            switch (packet.type) 
            {
                case EXEC_RQ:
                    handleExec(Request.deserialize(this.in));
                    break;
                case STATUS_RQ:
                    handleStatusRequest();
                    break;
                default:
                    break;
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error deserializing packet");
        }
    }


    public void run()
    {
        try 
        {
            in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            while(true)
            {
                handle(Protocol.deserialize(in));
            }
        }
        catch (IOException e) 
        {
            System.err.println("fds");
        }
    }
}
