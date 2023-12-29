package Server.WorkerServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import Shared.Defines;
import ThreadTools.ThreadControl;

public class Main 
{
    public static void main (String[] args)
    {
        // get the scalonator's ip adress
        String scalonator_adr= args [1];

        // get own memory
        long mem= Long.valueOf(args[2]);

        try
        {
            // connect to the scalonator
            Socket socket= new Socket(scalonator_adr, Server.Shared.Defines.scalonator_worker_port);
            DataInputStream input= new DataInputStream(socket.getInputStream());
            DataOutputStream output= new DataOutputStream(socket.getOutputStream());

            // create state, reciever, worker and transmitter
            ThreadControl tc= new ThreadControl();
            State state= new State(mem);
            new Thread (new Receiver(tc, state, input)).start();
            new Thread (new WorkerManager(tc, state)).start();
            new Thread (new Transmitter (tc, output, state)).start();

            // @TODO
            // when command== quit
            // tc.setRunning(false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }    
}
