package Server.ScalonatorServer.Communication.Worker;

import ThreadTools.ThreadControl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Server.Packet.Packet;
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
                    // read memory
                    DataInputStream socket_input= new DataInputStream(s.getInputStream());
                    DataOutputStream socket_output= new DataOutputStream(s.getOutputStream());
                    int mem= socket_input.readInt();

                    //start transmitter and receiver
                    this.state.registerMapWorker(mem);
                    this.state.

                    new Thread(new WorkerReceiver(tc, socket_input, state)).start();
                    new Thread(new WorkerTransmitter(tc, , socket_output)).start();
                }
            }
            server_socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }    
}
