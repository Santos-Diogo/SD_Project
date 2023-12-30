package Server.ScalonatorServer.Communication.Worker;

import ThreadTools.ThreadControl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Server.ScalonatorServer.State;
import Server.Shared.Defines;

public class WorkerComManager implements Runnable
{
    ThreadControl tc;
    State state;

    WorkerComManager (ThreadControl tc, State state)
    {
        this.tc= tc;
        this.state= state;
    }

    public void run ()
    {
        try
        {
            ServerSocket server_socket= new ServerSocket(Defines.scalonator_worker_port);
            while (this.tc.getRunning())
            {
                try
                {
                    Socket s= server_socket.accept();
                    new Thread ();
                }
                catch (InterruptedException e) {}
            }
            server_socket.close();
        }
        catch (IOException e)
        {

        }
        finally
        {
        }
    }    
}
