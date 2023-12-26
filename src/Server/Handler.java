package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    private State server_state;
    private DataInputStream in;
    private DataOutputStream out;
    
    
    public Handler (State server_state, Socket clientSocket) throws IOException
    {
        this.server_state= server_state;
        in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
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
        this.server_state.taskQueue.add(new Task(packet.n_job, packet.arg));
        

        return null;    //!!!
        /* if (not fucked)
            return new GoodResponse();
        else
            return new BadResponse(); */
    }


    private void handle (Protocol packet)
    {
        try 
        {
            switch (packet.type) 
            {
                case EXEC_RQ:
                    handleExec(Request.deserialize(in));
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
