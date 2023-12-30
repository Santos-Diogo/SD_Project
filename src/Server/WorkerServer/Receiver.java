package Server.WorkerServer;

import java.io.DataInputStream;
import java.io.IOException;

import Protocol.Protocol;
import Protocol.Exec.Request;
import Server.Packet.Packet;
import ThreadTools.ThreadControl;

public class Receiver implements Runnable
{
    private ThreadControl tc;
    private State state;
    private DataInputStream input;

    public Receiver (ThreadControl tc, State state, DataInputStream input)
    {
        this.tc= tc;
        this.state= state;
        this.input= input;
    }

    public void run ()
    {
        while (this.tc.getRunning())
        {
            try
            {
                Protocol.deserialize(input);
                Request request = Request.deserialize(input);
                new Thread(new Worker(state, (Packet.deserialize(this.input, request)))).start();
            }
            catch (IOException e) 
            {
                System.err.println("Server disconnected, please press enter to quit");
                tc.setRunning(false);
                break;
            }
        }
    }
}
