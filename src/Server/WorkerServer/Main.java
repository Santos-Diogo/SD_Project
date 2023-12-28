package Server.WorkerServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Shared.Defines;
import ThreadTools.ThreadControl;

public class Main 
{
    static State state;
    static Socket socket;
    public static void main (String[] args)
    {
        // GET FROM INPUT
        InetAddress scalonator_adr;
        try
        {
            // connect to the scalonator
            socket= new Socket(scalonator_adr, Server.Shared.Defines.scalonator_worker_port);
            DataInputStream input= new DataInputStream(socket.getInputStream());
            DataOutputStream output= new DataOutputStream(socket.getOutputStream());

            // create reciever, executor and transmitter
            ThreadControl tc= new ThreadControl();
            new Thread (new Transmitter (tc, output, state)).start();
            new Thread (new Receiver(tc, state, input)).start();
            new Thread (new WorkerManager()).start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }    
}
