package Server.ScalonatorServer.Communication.Client;

import ThreadTools.ThreadControl;

import java.util.ArrayList;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Server.ScalonatorServer.State.ClientInfo;

import Server.ScalonatorServer.State;

public class ClientComManager implements Runnable
{
    ThreadControl tc;
    State state;

    public ClientComManager (ThreadControl tc, State state)
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

                // add a transmitter and receiver for each new connection
                int info= this.state.registerMapClient();
                threads.add(new Thread(new ClientReceiver(tc, new DataInputStream(socket.getInputStream()), state.to_scalonator, info)));
                threads.add(new Thread(new ClientTransmitter(tc, new DataOutputStream(socket.getOutputStream()), this.state.getMapClient(info))));
            }
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        finally
        {
            // stop all threads
            for (Thread t: threads)
            {
                t.interrupt();
                try
                {
                    t.join();
                }
                catch (InterruptedException e) {}
            }
        }
    }
}
