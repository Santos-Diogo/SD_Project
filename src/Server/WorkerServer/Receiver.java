package Server.WorkerServer;

import java.io.DataInputStream;
import java.io.IOException;

import ThreadTools.ThreadControl;
import Server.Shared.Packet.Packet;

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
                Packet p= Packet.deserialize(this.input);
                this.state.inputQueue.put(p);
            }
            catch (InterruptedException e) {}
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
}
