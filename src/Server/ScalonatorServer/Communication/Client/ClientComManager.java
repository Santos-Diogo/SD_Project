package Server.ScalonatorServer.Communication.Client;

import ThreadTools.ThreadControl;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Server.ScalonatorServer.State;
import Server.Shared.Defines;


public class ClientComManager implements Runnable
{
    ThreadControl tc;
    State state;

    ClientComManager (ThreadControl tc, State state)
    {
        this.tc= new ThreadControl();
        this.state= state;
    }

    public void run ()
    {
        ServerSocket server_socket;
        List<Thread> threads= new ArrayList<>();

        try
        {
            server_socket= new ServerSocket(Shared.Defines.client_scalonator_port);
            
            while (this.tc.getRunning())
            {
                // listen to connections
                Socket socket= server_socket.accept();

                // add a thread for each new connection
                Thread t= new Thread(new ClientCom (tc, socket, state));
                t.start();
                threads.add(t);
            }
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        // stop all threads
    }
}
